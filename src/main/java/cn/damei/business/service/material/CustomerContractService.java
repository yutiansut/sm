package cn.damei.business.service.material;

import cn.damei.business.constants.CustomerContractEnum;
import cn.damei.business.constants.OperateLogEnum;
import cn.damei.business.constants.PricingMethodEnum;
import cn.damei.business.constants.SelectMaterialTypeEnmu;
import cn.damei.business.dao.material.ProjectIntemDao;
import cn.damei.business.dao.material.ProjectMaterialDao;
import cn.damei.business.dao.material.SmSkuDosageDao;
import cn.damei.business.dao.material.CustomerContractDao;
import cn.damei.business.dao.material.MealInfoDao;
import cn.damei.business.dao.operatelog.OperateLogDao;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.ProjectIntem;
import cn.damei.business.entity.material.SmSkuChangeDosage;
import cn.damei.business.entity.material.MealInfo;
import cn.damei.business.entity.operatelog.OperateLog;
import cn.damei.business.service.api.OutApiService;
import cn.damei.business.service.operatelog.OperateLogService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.dto.BootstrapPage;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class CustomerContractService extends CrudService<CustomerContractDao,CustomerContract> {

    @Autowired
    private CustomerContractDao customerContractDao;
    @Autowired
    private SmOrderQuotaBillService smOrderQuotaBillService;
    @Autowired
    private ProportionMoneyService proportionMoneyService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private MealInfoService mealInfoService;
    @Autowired
    private MealInfoDao mealInfoDao;
    @Autowired
    private OperateLogDao operateLogDao;
    @Autowired
    private ProjectIntemDao projectIntemDao;
    @Autowired
    private ProjectMaterialDao projectMaterialDao;
    @Autowired
    private SmSkuDosageDao smSkuDosage;
    @Autowired
    private ContractOperateTimeSummaryService contractOperateTimeSummaryService;
    @Autowired
    private OutApiService outApiService;

    private static String system_type = "1";    //选材

    /**
     * 根据项目编号查询
     * @param contractCode
     * @return
     */
    public CustomerContract getByCode(String contractCode) {
        return customerContractDao.getByCode(contractCode);
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String contractCode, String contractStatus) {
        this.entityDao.updateStatus(contractCode,contractStatus);
    }

    /**
     * 修改项目
     * @param customerContract 项目实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateContract(CustomerContract customerContract){
        MealInfo mealInfo = this.mealInfoService.getById(customerContract.getMealId());
        if(mealInfo != null){
            customerContract.setMealPrice(mealInfo.getMealSalePrice());
        }
        this.entityDao.update(customerContract);
        OperateLog operateLog = this.buildOperateLog(customerContract, customerContract.getContractStatus(),null,null,null);
        this.operateLogService.insert(operateLog);
    }

    /**
     * 修改项目状态
     * @param id 项目id
     * @param status 项目状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateContractStatus(Long id,CustomerContractEnum status){
        CustomerContract customerContract = this.entityDao.getById(id);
        //如果审计驳回再次指派则状态为审计审核中
        if(CustomerContractEnum.ASSIGN_AUDIT.equals(status)){
            if(customerContract.getAuditor() != null){
                customerContract.setContractStatus(CustomerContractEnum.UNDER_AUDIT);
            }else {
                customerContract.setContractStatus(CustomerContractEnum.ASSIGN_AUDIT);
                //记录指派审计时间
                this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.ASSIGN_AUDIT);
            }
        }
        //通过修改订单状态为待签约以及存入合同金额和拆改金额
        if(CustomerContractEnum.WAIT_TRANSFER.equals(status)){
            //修改订单状态为  待设计
            customerContract.setOrderFlowStatus(CustomerContractEnum.STAY_SIGN);
            //修改选材状态为  等待转单
            customerContract.setContractStatus(CustomerContractEnum.WAIT_TRANSFER);
            //记录审计通过时间
            this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.WAIT_TRANSFER);
            //插入金额
            Map<String, BigDecimal> stringBigDecimalMap = this.smOrderQuotaBillService.materialRationing(customerContract.getContractCode());
            customerContract.setContractAmount(stringBigDecimalMap.get("total"));
            customerContract.setModifyAmount(stringBigDecimalMap.get("totalWork"));
            this.proportionMoneyService.insertByContractCode(customerContract.getContractCode());
        }
        //审计审核未通过
        if(CustomerContractEnum.NOT_PASS.equals(status)){
            customerContract.setContractStatus(CustomerContractEnum.NOT_PASS);
            //记录审计未通过时间
            this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.NOT_PASS);
        }
        //设计师申请审计撤回
        if(CustomerContractEnum.PEND_AUDIT_RETRACT.equals(status)){
            customerContract.setContractStatus(CustomerContractEnum.PEND_AUDIT_RETRACT);
        }
        //审计经理审计撤回
        if(CustomerContractEnum.AUDIT_RETRACT.equals(status)){
            customerContract.setContractStatus(CustomerContractEnum.AUDIT_RETRACT);
            this.proportionMoneyService.deleteByContractCode(customerContract.getContractCode());
            this.smOrderQuotaBillService.deleteByCode(customerContract.getContractCode());
        }
        this.entityDao.update(customerContract);
        OperateLog operateLog = buildOperateLog(customerContract, status,null,null,null);
        this.operateLogService.insert(operateLog);
    }

    /**
     * 指派审计员
     * @param id 项目id
     * @param auditorOrgCode 审计员工号
     * @param auditorName 审计员姓名
     * @param auditorPhone 审计员电话
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAudit(Long id,String auditorOrgCode,String auditorName,String auditorPhone){
        CustomerContract customerContract = this.entityDao.getById(id);
        customerContract.setAuditorOrgCode( auditorOrgCode );
        customerContract.setAuditor( auditorName );
        customerContract.setAuditorMobile( auditorPhone );
        customerContract.setContractStatus(CustomerContractEnum.UNDER_AUDIT);
        this.entityDao.update(customerContract);
        //记录指派审计员的时间
        this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.UNDER_AUDIT);
        OperateLog operateLog = this.buildOperateLog(customerContract, CustomerContractEnum.UNDER_AUDIT,auditorOrgCode,auditorName,auditorPhone);
        this.operateLogService.insert(operateLog);
    }
    /**
     * 开始设计
     *      1.
     *      2.去查询默认的定额,并将其存储到选材表中
     * @param customerContract 客户合同
     */
    @Transactional(rollbackFor = Exception.class)
    public Object startDesign(CustomerContract customerContract) {
        MealInfo mealInfo = mealInfoDao.getById(customerContract.getMealId());
        if(mealInfo != null){
            customerContract.setMealPrice(mealInfo.getMealSalePrice());
        }
        customerContractDao.update(customerContract);
        OperateLog operateLog = buildOperateLog(customerContract, customerContract.getContractStatus(),null,null,null);
        operateLogDao.insert(operateLog);

        //合同门店信息不为空时,去查询默认定额
        if(StringUtils.isNotBlank(customerContract.getStoreCode())){
            //去查看sm_project_intem定额的默认值,并做相应存储
            List<ProjectIntem> projectIntemList = projectIntemDao.findByStoreCodeAndIsDefault(customerContract.getStoreCode());
            if(projectIntemList != null && projectIntemList.size() > 0){
                ShiroUser loggedUser = WebUtils.getLoggedUser();
                String userName = loggedUser != null ? loggedUser.getName() + "(" + loggedUser.getOrgCode() + ")" : "";
                Date date = new Date();

                List<ProjectChangeMaterial> projectMaterials = new ArrayList<ProjectChangeMaterial>();
                List<SmSkuChangeDosage> smSkuChangeDosageList = new ArrayList<SmSkuChangeDosage>();

                //过滤掉其价格为空的,不存储!
                projectIntemList.stream()
                        .filter(intem -> intem.getProjectIntemPrice() != null)
                        .forEach(intem -> {
                    //新增主材定额
                    ProjectChangeMaterial material = new ProjectChangeMaterial();
                    String materialId = UUID.randomUUID().toString();
                    material.setId(materialId);
                    material.setCreateTime(date);
                    material.setCreateUser(userName);
                    material.setContractCode(customerContract.getContractCode());
                    material.setCategoryDetailCode(intem.getSubordinateCategory());
                    material.setMaterialUnit(intem.getProjectIntemUnit());
                    material.setProductName(intem.getProjectIntemName());
                    material.setQuotaDescribe(intem.getProjectIntemDetail());
                    material.setStoreSalePrice(intem.getProjectIntemPrice());

                    String subordinateCategory = intem.getSubordinateCategory();
                    if(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString().equals(subordinateCategory)
                            || SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString().equals(subordinateCategory)){
                        //基桩定额(增项)
                        material.setCategoryCode(SelectMaterialTypeEnmu.ADDITEM.toString());
                    }else if(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString().equals(subordinateCategory)
                            || SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString().equals(subordinateCategory)
                            || SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString().equals(subordinateCategory)){
                        //老房拆除基桩定额 / 老房拆除基桩增项综合费 / 老房拆除其他综合费
                        material.setCategoryCode(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString());
                    }else if(SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES.toString().equals(subordinateCategory)){
                        //其他综合费
                        material.setCategoryCode(SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE.toString());
                    }
                    projectMaterials.add(material);

                    //新增用量,默认都给 1
                    SmSkuChangeDosage skuDosage = new SmSkuChangeDosage();
                    skuDosage.setId(UUID.randomUUID().toString());
                    skuDosage.setCreateTime(date);
                    skuDosage.setCreateUser(userName);
                    skuDosage.setProjectMaterialId(materialId);
                    skuDosage.setDomainName(intem.getProjectIntemTypeName());
                    skuDosage.setStorePurchasePrice(intem.getProjectIntemCostPrice());
                    skuDosage.setStoreSalePrice(intem.getProjectIntemPrice());
                    skuDosage.setStoreReducePrice(intem.getProjectIntemPrice());
                    skuDosage.setStoreIncreasePrice(intem.getProjectIntemPrice());
                    skuDosage.setBudgetDosage(new BigDecimal(1));
                    skuDosage.setLossFactor(new BigDecimal(1));
                    skuDosage.setNoLossDosage(new BigDecimal(1));
                    skuDosage.setLossDosage(new BigDecimal(1));
                    if(!PricingMethodEnum.fixedUnitPrice.toString().equals(intem.getValuationMethod())){
                        skuDosage.setProjectProportion(intem.getProjectIntemPrice());
                        //除了固定单价,都给0
                        /*skuDosage.setBudgetDosage(null);
                        skuDosage.setNoLossDosage(BigDecimal.ZERO);
                        skuDosage.setLossDosage(BigDecimal.ZERO);*/
                    }
                    skuDosage.setDosagePricingMode(intem.getValuationMethod());

                    smSkuChangeDosageList.add(skuDosage);
                });
                if(projectMaterials.size() > 0){
                    //批量插入主材表
                    projectMaterialDao.insertByprojectChangeMaterial(projectMaterials);
                }
                if(smSkuChangeDosageList.size() > 0){
                    //批量插入用量
                    smSkuDosage.insertBySmSkuChangeDosage(smSkuChangeDosageList);
                }

            }
        }
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     * 构建操作日志:每一个状态对应一个操作类型，
     * 根据不同的状态构建不同的操作类型
     * @param customerContract 项目信息
     * @param status 当前状态
     */
    private OperateLog buildOperateLog(
            CustomerContract customerContract,CustomerContractEnum status,
            String appointOrgCode,String appointName,String appointPhone){
        OperateLog operateLog = new OperateLog();
        operateLog.setContractCode(customerContract.getContractCode());
        operateLog.setSystemType( system_type );
        if(CustomerContractEnum.MATERIAL_SELECTION.equals(status)){
            operateLog.setOperateType(OperateLogEnum.PERFECT_CUSTOMER_INFO);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.ASSIGN_AUDIT.equals(status)){
            operateLog.setOperateType(OperateLogEnum.SUBMIT_AUDIT);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.UNDER_AUDIT.equals(status)){
            operateLog.setOperateType(OperateLogEnum.ASSIGN_AUDIT);
            operateLog.setOperateDescription(status.getLabel());
            operateLog.setAppointOrgCode( appointOrgCode );
            operateLog.setAppointName( appointName );
            operateLog.setAppointPhone( appointPhone );
        }else if(CustomerContractEnum.WAIT_TRANSFER.equals(status)){
            operateLog.setOperateType(OperateLogEnum.AUDIT_PASS);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.PEND_AUDIT_RETRACT.equals(status)){
            operateLog.setOperateType(OperateLogEnum.AUDIT_APPLY_RETRACT);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.AUDIT_RETRACT.equals(status)){
            operateLog.setOperateType(OperateLogEnum.AUDIT_MANAGER_RETRACT);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.NOT_PASS.equals(status)){
            operateLog.setOperateType(OperateLogEnum.AUDIT_FAILED);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.TRANSFER_COMPLETE.equals(status)){
            operateLog.setOperateType(OperateLogEnum.TRANSFER_COMPLETE);
            operateLog.setOperateDescription(status.getLabel());
        }
        String operator = WebUtils.getLoggedUser().getName()+"("+WebUtils.getLoggedUser().getOrgCode()+")";
        operateLog.setOperator(operator);
        operateLog.setOperateTime(new Date());
        return operateLog;
    }

    /**
     * 获取项目列表 隐藏手机号中间四位
     * @param paramMap
     * @return
     */
    public BootstrapPage<CustomerContract> searchCustomerContractScrollPage(Map<String ,Object> paramMap) {
        BootstrapPage<CustomerContract> result=this.searchScrollPage(paramMap);
        result.setRows(this.replaceAllMobile(result.getRows()));
        return  result;
    }

    /**
     * 隐藏手机号中间四位
     * @param customerContractList
     * @return
     */
    public List<CustomerContract> replaceAllMobile(List<CustomerContract> customerContractList) {
        customerContractList.forEach(p->{
            String mobile = p.getCustomerMobile();
            String secondMobile = p.getSecondContactMobile();
            if( null != mobile && mobile.length() > 7 ){
                p.setCustomerMobile(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
            if( null != secondMobile && secondMobile.length() > 7 ){
                p.setSecondContactMobile(secondMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
        });
        return customerContractList;
    }
}
