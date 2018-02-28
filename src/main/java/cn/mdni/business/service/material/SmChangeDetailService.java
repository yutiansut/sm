package cn.mdni.business.service.material;

import cn.mdni.business.constants.*;
import cn.mdni.business.dao.material.ProjectChangeMaterialDao;
import cn.mdni.business.dao.material.SmChangeAmountDao;
import cn.mdni.business.dao.material.SmChangeDetailDao;
import cn.mdni.business.dao.material.SmSkuChangeDosageDao;
import cn.mdni.business.dao.material.OtherAddReduceAmountDao;
import cn.mdni.business.entity.material.*;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.service.api.OutApiService;
import cn.mdni.business.service.finance.ProjectChangeMoneyService;
import cn.mdni.commons.bean.BeanUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更详细 Service</dd>
 * <dd>@date：2017/11/16  17:19</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class SmChangeDetailService extends CrudService<SmChangeDetailDao, SmChangeDetail> {

    private  final String STRING_ZERO = "0";
    private  final String STRING_ONE = "1";
    private final BigDecimal MULTIPLICAND = new BigDecimal(-1);
    private final BigDecimal OTHER = new BigDecimal(0);

    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;
    @Autowired
    private ProjectMaterialService projectMaterialService;
    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;
    @Autowired
    private SmSkuDosageService smSkuDosageService;
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;
    @Autowired
    private SmChangeAmountDao smChangeAmountDao;
    @Autowired
    private OtherAddReduceAmountDao otherAddReduceAmountDao;
    @Autowired
    private OtherAddRecuceAmountService otherAddRecuceAmountService;
    @Autowired
    private ProjectChangeMoneyService projectChangeMoneyService;
    @Autowired
    private OtherAddReduceAmountDao otherAddRecuceAmountDao;
    @Autowired
    private SmChangeDetailDao smChangeDetailDao;
    @Autowired
    private ProjectChangeMaterialDao projectChangeMaterialDao;
    @Autowired
    private ProdCatalogService prodCatalogService;
    @Autowired
    private ContractOperateTimeSummaryService contractOperateTimeSummaryService;
    @Autowired
    private SmSkuChangeDosageDao smSkuChangeDosageDao;
    @Autowired
    private IndentPrepareOrderService indentPrepareOrderService;
    @Autowired
    private OutApiService outApiService;


    /**
     * 查询所有变更单
     *
     * @param params
     * @return
     */
    public List<SmChangeDetail> findChangeOrderList(Map<String, Object> params) {
        return this.entityDao.findChangeOrderList(params);
    }

    /**
     * 根据id修改变更单状态（ 变更单 撤回 ）
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, String changeLogId, String contractCode, String changeCategoryUrl) {
        //根据id查询变更单
        SmChangeDetail smChangeDetail = this.entityDao.getById(id);
        smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.CHANGEORDERRECALL.toString());
        smChangeDetail.setId(id);
        this.entityDao.update(smChangeDetail);
        //根据变更流水查询该项目下的变更单的状态
        //该批次下所有变更单未通过的合计
        Long count = this.entityDao.getStatusByChangeLogId(changeLogId);


        //查询原用量大于0的值
//        List<SmSkuChangeDosage> smSkuChangeDosageList = smSkuChangeDosageService.getByOriDosGTZero(contractCode, changeCategoryUrl);
//        for (SmSkuChangeDosage smSkuChangeDosage : smSkuChangeDosageList) {
//            //把原用量大于0的记录，现用量改为和原用量相同的值
//            smSkuChangeDosageService.updateBudDosByOriDos(smSkuChangeDosage.getOriginalDosage(), smSkuChangeDosage.getId());
//        }
//        //原用量等于0的记录删除
//        smSkuChangeDosageService.deleteOriDosIsZero(contractCode, changeCategoryUrl);
//        //根据项目code查询主材和用量
//        Map<String,Object> map = Maps.newHashMap();
//        map.put("contractCode",contractCode);
//        map.put("productCategoryUrl",changeCategoryUrl);
//        List<ProjectChangeMaterial> projectChangeMaterial = projectChangeMaterialDao.findWithSubListByMaterialParams(map);
//        List<String> ids = new ArrayList<>();
//        for (ProjectChangeMaterial projectChangeMaterial1 : projectChangeMaterial) {
//            List<SmSkuDosage> skuChangeDosageList = projectChangeMaterial1.getSkuDosageList();
//            if ((skuChangeDosageList != null && skuChangeDosageList.size() == 0) || skuChangeDosageList == null) {
//                ids.add(projectChangeMaterial1.getId());
//            }
//        }
//        if (ids != null && ids.size() > 0) {
//            //根据id删除主材
//            projectChangeMaterialService.deleteByIds(ids);
//        }


        if (StringUtils.isNotBlank(changeCategoryUrl) && !changeCategoryUrl.equals("null")) {
            //删除临时用量表数据
            smSkuChangeDosageService.deleteByConAndUrl(contractCode, changeCategoryUrl);
            //删除临时主材表
            projectChangeMaterialService.deleteByConAndUrl(contractCode, changeCategoryUrl);
            //查询用量表数据
            List<SmSkuDosage> smSkuDosageList = smSkuDosageService.findByConAndUrl(contractCode, changeCategoryUrl);
            //查询主材表
            List<ProjectMaterial> projectMaterialList = projectMaterialService.findByConAndUrl(contractCode, changeCategoryUrl);


            List<SmSkuChangeDosage> smSkuChangeDosages = new ArrayList<>();
            List<ProjectChangeMaterial> projectChangeMaterials = new ArrayList<>();

            for (SmSkuDosage smSkuDosage : smSkuDosageList) {
                Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(smSkuDosage, false);
                SmSkuChangeDosage entity1 = BeanUtils.mapTransBean(stringObjectMap, SmSkuChangeDosage.class);
                entity1.setOriginalDosage(entity1.getLossDosage());
                smSkuChangeDosages.add(entity1);
            }

            for (ProjectMaterial projectMaterial : projectMaterialList) {
                Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(projectMaterial, false);
                ProjectChangeMaterial entity1 = BeanUtils.mapTransBean(stringObjectMap, ProjectChangeMaterial.class);
                projectChangeMaterials.add(entity1);
            }

            if (smSkuChangeDosages != null && smSkuChangeDosages.size() > 0) {
                //插入
                smSkuChangeDosageDao.batchInsert(smSkuChangeDosages);
            }
            if (projectChangeMaterials != null  && projectChangeMaterials.size() > 0) {
                projectChangeMaterialDao.batchInsert(projectChangeMaterials);
            }


        }else{
            otherAddRecuceAmountService.deleteByCodeAndFlag(contractCode,"1");
        }
        //所有变更单都通过或撤回,更改项目状态为转单完成
        if (count.equals(0L)) {
            //根据类目查询临时主材表里的数据(用量大于0的),原用量和现用量都为0的不存
            List<ProjectChangeMaterial> projectChangeMaterialList = projectChangeMaterialService.getByContractCode(contractCode);

            //根据类目查询临时用量表（现用量大于0的）
            List<SmSkuChangeDosage> smSkuChangeDosageList2 = smSkuChangeDosageService.getByContractCode(contractCode);

            //删除用量表的数据
            smSkuDosageService.deleteByContractCode(contractCode);
            //删除主材表的数据
            projectMaterialService.delete(contractCode);
            //把临时表的数据插入主材主表
            projectMaterialService.insertByprojectChangeMaterial(projectChangeMaterialList);
            //插入用量
            smSkuDosageService.insertBySmSkuChangeDosage(smSkuChangeDosageList2);

            //删除临时用量表的数据
            smSkuChangeDosageService.deleteByContractCode(contractCode);
            //删除临时主材表的数据
            projectChangeMaterialService.delete(contractCode);
            //其他金额增减
            otherAddRecuceAmountService.updateByContractCodeAndStatus(contractCode,"1");
            //更改变更的状态
            customerContractService.updateStatus(contractCode, CustomerContractEnum.TRANSFER_COMPLETE.toString());
        }
    }

    /**
     * 审核通过时修改变更为转单完成
     *
     * @param id
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateChangeOrderStatus(Long id, String changeNo, String changeLogId, String contractCode, String changeCategoryUrl, String status) {

        String changVersionNo = changeNo.substring(0, changeNo.length() - 3);

        BigDecimal priceTotal = new BigDecimal(0);
        if (StringUtils.isNotBlank(changeCategoryUrl)) {
            //保存变更款
            List<ProjectChangeMaterial> auditList = projectChangeMaterialService.getAuditList(changeCategoryUrl, contractCode);
            List<ProjectChangeMaterial> collect = auditList.stream().filter(a -> !a.getCategoryCode().equals(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString())).collect(Collectors.toList());
            for (ProjectChangeMaterial projectChangeMaterial : collect) {
                BigDecimal price = projectChangeMaterial.getPrice();//单价
                BigDecimal num = projectChangeMaterial.getNum();//数量
                BigDecimal multiply = price.multiply(num);//单项价
                if (projectChangeMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
                    priceTotal = priceTotal.subtract(multiply);
                }else {
                    priceTotal = priceTotal.add(multiply);//总价
                }
            }
        }else{
            List<OtherAddReduceAmount> changeByContractCode = otherAddRecuceAmountService.getChangeByContractCode(contractCode);
            for (OtherAddReduceAmount otherAddReduceAmount : changeByContractCode) {
                BigDecimal quota = otherAddReduceAmount.getQuota();//价格
                String addReduceType = otherAddReduceAmount.getAddReduceType();
                if ("1".equals(addReduceType)) {//增加的金额
                    priceTotal = priceTotal.add(quota);
                }else{//减少的金额
                    priceTotal = priceTotal.subtract(quota);
                }
            }
        }
        projectChangeMoneyService.insertNewChangeMoney(contractCode,changeNo,priceTotal, ChangeTypeEnum.MATERIAL);


        //根据类目url查询类目名字
        String cataLogName = prodCatalogService.getNameByUrl(changeCategoryUrl);

        //一、更改变更单状态
        this.entityDao.updateChangeOrderStatus(id, status);
        //状态通过时
        if (status.equals(ChangeDetailAuditEnum.EXAMINATIONPASSED.toString())) {

            //二、是 主材/其他金额 的存到芒果
            if (StringUtils.isNotBlank(changeCategoryUrl)) {
                //把临时表的项标记为已使用(暂时不使用这个状态)
                //projectChangeMaterialService.updateMaterialStatus(contractCode, changeCategoryUrl, "1");

                //根据类目查询临时用量表（现用量大于0的）
                List<SmSkuChangeDosage> smSkuChangeDosageList = smSkuChangeDosageService.getBychangeCategoryUrl(contractCode, changeCategoryUrl);
                for (SmSkuChangeDosage smSkuChangeDosage : smSkuChangeDosageList) {
                    smSkuChangeDosage.setId(null);
                    smSkuChangeDosage.setContractCode(contractCode);
                    smSkuChangeDosage.setChangeVersionNo(changVersionNo);
                    smSkuChangeDosage.setChangeNo(changeNo);
                    if (smSkuChangeDosage.getOriginalDosage().compareTo(smSkuChangeDosage.getLossDosage()) == 0) {
                        smSkuChangeDosage.setChangeFlag("0");
                    }else{
                        smSkuChangeDosage.setChangeFlag("1");
                    }
                }
                Map<String, List<SmSkuChangeDosage>> collect = smSkuChangeDosageList.stream().collect(Collectors.groupingBy(SmSkuChangeDosage::getProjectMaterialId));
                //根据类目查询临时主材表里的数据(用量大于0的),原用量和现用量都为0的不存
                List<ProjectChangeMaterial> projectChangeMaterialList = projectChangeMaterialService.getBychangeCategoryUrl(changeCategoryUrl, contractCode);
                List<ProjectChangeMaterial> addProjectChangeMaterialList = new ArrayList<>();
                for (ProjectChangeMaterial projectChangeMaterial : projectChangeMaterialList) {
                    projectChangeMaterial.setChangeVersionNo(changVersionNo);
                    projectChangeMaterial.setChangeNo(changeNo);
                    projectChangeMaterial.setCataLogName(cataLogName);
                    List<SmSkuChangeDosage> smSkuChangeDosageList1 = collect.get(projectChangeMaterial.getId());
                    //过滤掉没用量的
                    if(smSkuChangeDosageList1!=null && smSkuChangeDosageList1.size()>0){
                        BigDecimal originalDosage = smSkuChangeDosageList1.stream().filter(b -> b.getOriginalDosage() != null).map(a -> a.getOriginalDosage()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                        BigDecimal lossDosage = smSkuChangeDosageList1.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getLossDosage()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                        if (originalDosage.compareTo(lossDosage) == 0) {
                            projectChangeMaterial.setChangeFlag("0");
                        } else {
                            projectChangeMaterial.setChangeFlag("1");
                        }
                        String id1 = projectChangeMaterial.getId();
                        String s = UUID.randomUUID().toString();
                        smSkuChangeDosageList.stream().filter(a -> a.getProjectMaterialId().equals(id1)).forEach(b -> b.setProjectMaterialId(s));
                        projectChangeMaterial.setId(s);
                        addProjectChangeMaterialList.add(projectChangeMaterial);
                    }
                }
                List<SmSkuChangeDosage> collect1 = smSkuChangeDosageList.stream().filter(a -> a.getProjectMaterialId() != null).collect(Collectors.toList());
                if(collect1!=null && collect1.size()>0) {
                    mongoCustomerContractService.insertBatch(smSkuChangeDosageList);
                }
                if(addProjectChangeMaterialList!=null && addProjectChangeMaterialList.size()>0) {
                    //根据类目把临时主材表的数据保存到芒果
                    mongoCustomerContractService.insertBatch(addProjectChangeMaterialList);
                }

            } else {
                //查询其他金额增减
                List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.getChangeByContractCode(contractCode);
                for (OtherAddReduceAmount otherAddReduceAmount : otherAddReduceAmountList) {
                    otherAddReduceAmount.setChangeVersionNo(changVersionNo);
                    otherAddReduceAmount.setChangeNo(changeNo);
                }
                //把其他金额保存到芒果
                mongoCustomerContractService.insertBatch(otherAddReduceAmountList);
            }


            //三、查询该批次下所有变更单未通过的总数
            Long count = this.entityDao.getStatusByChangeLogId(changeLogId);
            //所有变更单已通过或撤回
            if (count.equals(0L)) {
                contractOperateTimeSummaryService.buildContractOperateTimeSummary(contractCode,CustomerContractEnum.CHANGE_AUDIT_SUCCESS);

                //查询其他金额增减表 变更标识为1的总数（查询本次变更单是其他金额的个数）
                Long otherAmountCount = otherAddRecuceAmountService.getOthermoneyAddOrReduceByChangeFlag(contractCode);
                if (otherAmountCount.equals(0L)) {//本次变更没有其他金额增减
                    //查询其他金额增减
                    List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountDao.getChangeByContractCode(contractCode,"0");
                    //把其他金额保存到芒果
                    otherAddReduceAmountList.stream().forEach(a->a.setId(null));
                    mongoCustomerContractService.insertBatch(otherAddReduceAmountList);
                } else {//本次变更有其他金额增减，那么就将该项目下的其他金额变更标识更新为0
                    otherAddRecuceAmountService.updateByContractCodeAndStatus(contractCode, "1");
                }
                //根据类目查询临时主材表里的数据(用量大于0的),原用量和现用量都为0的不存
                List<ProjectChangeMaterial> projectChangeMaterialList = projectChangeMaterialService.getByContractCode(contractCode);

                //根据类目查询临时用量表（现用量大于0的）
                List<SmSkuChangeDosage> smSkuChangeDosageList = smSkuChangeDosageService.getByContractCode(contractCode);

                //删除用量表的数据
                smSkuDosageService.deleteByContractCode(contractCode);
                //删除主材表的数据
                projectMaterialService.delete(contractCode);
                //把临时表的数据插入主材主表
                projectMaterialService.insertByprojectChangeMaterial(projectChangeMaterialList);
                //插入用量
                smSkuDosageService.insertBySmSkuChangeDosage(smSkuChangeDosageList);
                //删除临时用量表的数据
                smSkuChangeDosageService.deleteByContractCode(contractCode);
                //删除临时主材表的数据
                projectChangeMaterialService.delete(contractCode);
                //构造操作时间汇总
                contractOperateTimeSummaryService.buildContractOperateTimeSummary(contractCode,CustomerContractEnum.TRANSFER_COMPLETE);

                //更改项目的状态为转单完成
                customerContractService.updateStatus(contractCode, CustomerContractEnum.TRANSFER_COMPLETE.toString());

                //李照新增推送变更单 异步到产业工人
                outApiService.pushMaterialChangeToMps(contractCode,changVersionNo);

                //调用转 预备单 方法
                try{
                    indentPrepareOrderService.asynCreatePrepareOrder(contractCode, Constants.PAGE_TYPE_CHANGE);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

        }

    }


    /**
     * @param changeVersionNo 批次号
     * @Description: 美得你智装 根据批次号生成单次变更号
     * @date: 2017/11/21  16:17
     * @author: Ryze
     */
    public String getChangeNo(String changeVersionNo) {
        String str= entityDao.getChangeNoThree(changeVersionNo);
        StringBuilder changeNo = new StringBuilder(changeVersionNo);
       if(StringUtils.isBlank(str)){
               changeNo = changeNo.append("001");
       }else {
           long l = 10L;
           long l1 = 100L;
           Long changeNoThree=Long.parseLong(str)+1L;
           if (changeNoThree < l) {
               changeNo.append("00").append( changeNoThree);
           } else if (changeNoThree < l1) {
              changeNo.append( "0").append(changeNoThree);
           } else {
             changeNo.append(changeNoThree);
           }
       }
        return changeNo.toString();
    }
    /**
     * @param changeVersionNo 批次号
     * @Description: 美得你智装 根据批次号生成一批变更号
     * @date: 2017/11/21  16:17
     * @author: Ryze
     */
    public List<String> getChangeNos(String changeVersionNo,Long size) {
        String str= entityDao.getChangeNoThree(changeVersionNo);
        ArrayList<String> strings = new ArrayList<>(size.intValue());
        //数据库没有的话
        if(StringUtils.isBlank(str)){
            for (long i=1;i<size+1L;i++){
                StringBuilder changeNo = new StringBuilder(changeVersionNo);
                changeNo.append("00").append(i);
                strings.add(changeNo.toString());
            }
         //数据库有的话
        }else {
            long l = 10L;
            long l1 = 100L;
            Long changeNoThree=Long.parseLong(str);
            for (long i=1;i<size+1L;i++){
                StringBuilder changeNo = new StringBuilder(changeVersionNo);
                long numb = changeNoThree + i;
                if (numb < l) {
                    changeNo = changeNo.append("00").append(numb);
                } else if (numb < l1) {
                    changeNo = changeNo.append("0").append(numb);
                } else {
                    changeNo = changeNo.append(numb);
                }
                strings.add(changeNo.toString());
            }
        }
        return strings;
    }
    /**
     * @param id              log_id
     * @param changeVersionNo 批次号
     * @param contractCode    合同号
     * @Description: 美得你智装 提交变更审核 拆弹
     * @date: 2017/11/21  16:51
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public String  submitChangeReview(Long id, String changeVersionNo, String contractCode) {
        String returnString=STRING_ZERO;
        //1 根据 contractCode 查询  sm_project_change_material 列表
        List<ProjectChangeMaterial> listByCode = projectChangeMaterialService.getListByCode(contractCode);
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String name = loggedUser.getName();
        String jobNum = loggedUser.getOrgCode();
        if (listByCode != null && listByCode.size()>0) {
            //2 根据获取类别集合分组
            Map<String, List<ProjectChangeMaterial>> collect = listByCode.stream().collect(Collectors.groupingBy(ProjectChangeMaterial::getProductCategoryUrl));
            //3 生成 变更单 确认 变更单状态
            List<SmChangeDetail> smChangeDetails=new ArrayList<>();
            for (Map.Entry<String, List<ProjectChangeMaterial>> entries : collect.entrySet()) {
                //类别url
                String key = entries.getKey();
                //集合
                List<ProjectChangeMaterial> value = entries.getValue();
                ProjectChangeMaterial projectChangeMaterial = value.get(0);
                SmChangeDetail smChangeDetail = new SmChangeDetail();
                smChangeDetail.setContractCode(contractCode);
                smChangeDetail.setChangeLogId(id);
                smChangeDetail.setChangeCategoryUrl(key);
                smChangeDetail.setChangeCategoryName(projectChangeMaterial.getCataLogName());
                smChangeDetail.setCreateTime(new Date());
                smChangeDetail.setCreateUser(name + "(" + jobNum + ")");
                String currentStatus = getCurrentStatus(value);
                smChangeDetail.setCurrentStatus(currentStatus);
                if(currentStatus.equals(ChangeDetailAuditEnum.MATERIALDEPARTMENTAUDIT.toString())){
                    smChangeDetail.setPassMaterialsDepartment("1");
                }else{
                    smChangeDetail.setPassMaterialsDepartment("0");
                }
                smChangeDetails.add(smChangeDetail);
            }
            List<String> changeNos = getChangeNos(changeVersionNo,new Long(collect.size()));
            //设置变更单号
            for (int i=0;i<smChangeDetails.size();i++){
                smChangeDetails.get(i).setChangeNo(changeNos.get(i));
            }
            //返回主键
            entityDao.batchInsertList(smChangeDetails);
            List<SmChangeAmount> smChangeAmounts = new ArrayList<>();
            //计算金额
            for (SmChangeDetail detail:smChangeDetails) {
                List<SmChangeAmount> smChangeAmounts1 = calculateTheAmount(collect.get(detail.getChangeCategoryUrl()), detail.getId());
                smChangeAmounts.addAll(smChangeAmounts1);
            }
            smChangeAmountDao.batchInsertList(smChangeAmounts);
            returnString=STRING_ONE;
            }
            //4 其他金额增减 生成变更单 确认状态 计算金额
            List<OtherAddReduceAmount> changeByContractCode1 = otherAddReduceAmountDao.getChangeByContractCode(contractCode, "1");
            //没有变化的
            if(changeByContractCode1!=null&& changeByContractCode1.size()>0){
                List<OtherAddReduceAmount> changeByContractCode = otherAddReduceAmountDao.getChangeByContractCode(contractCode, "0");
                //变更前金额
                BigDecimal bigDecimal = changeByContractCode.stream().map(a -> {
                    String b = a.getAddReduceType();
                    if (STRING_ZERO.equals(b)) {
                        return a.getQuota().multiply(MULTIPLICAND);
                    } else {
                        return a.getQuota();
                    }
                }).reduce(BigDecimal::add).orElse(OTHER);
                //变更的金额
                BigDecimal bigDecimal1 = changeByContractCode1.stream().map(a -> {
                    BigDecimal c = a.getQuota();
                    String b = a.getAddReduceType();
                    if (STRING_ZERO.equals(b)) {
                        return c.multiply(MULTIPLICAND);
                    } else {
                        return c;
                    }
                }).reduce(BigDecimal::add).orElse(OTHER);
                SmChangeDetail smChangeDetail = new SmChangeDetail();
                smChangeDetail.setContractCode(contractCode);
                smChangeDetail.setChangeLogId(id);
                smChangeDetail.setChangeCategoryUrl(null);
                smChangeDetail.setChangeNo(getChangeNo(changeVersionNo));
                smChangeDetail.setChangeCategoryName(null);
                smChangeDetail.setCreateTime(new Date());
                smChangeDetail.setCreateUser(name + "(" + jobNum + ")");
                //如果 有减的金额去设计总监 否则 审计
                OtherAddReduceAmount otherAddReduceAmount = changeByContractCode1.stream().filter(a -> STRING_ZERO.equals(a.getAddReduceType())).findAny().orElse(null);
                if (otherAddReduceAmount!=null) {
                    smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.DESIGNDIRECTORINTHEAUDIT.toString());
                } else {
                    smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITREVIEW.toString());
                }
                entityDao.insert(smChangeDetail);
                SmChangeAmount smChangeAmount = new SmChangeAmount();
                smChangeAmount.setChangeDetailId(smChangeDetail.getId());
                smChangeAmount.setCategoryCode(SelectMaterialTypeEnmu.OTHERMONEYADDORREDUCE.toString());
                smChangeAmount.setCategoryDetailCode(SelectMaterialTypeEnmu.OTHERMONEYADDORREDUCE.toString());
                smChangeAmount.setOriginalAmount(bigDecimal);
                smChangeAmount.setCashAmount(bigDecimal.add(bigDecimal1));
                smChangeAmount.setCreateUser(name + "(" + jobNum + ")");
                smChangeAmount.setCreateTime(new Date());
                smChangeAmountDao.insert(smChangeAmount);
                returnString=STRING_ONE;
        }
                return returnString;
    }

    /**
     * @param value 集合变更单
     * @param id    id smChangeAmountDao
     * @Description: 美得你智装 计算金额
     * @date: 2017/11/22  11:05
     * @author: Ryze
     */
    private List<SmChangeAmount> calculateTheAmount(List<ProjectChangeMaterial> value, Long id) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String name = loggedUser.getName();
        String jobNum = loggedUser.getOrgCode();
        ArrayList<SmChangeAmount> smChangeAmounts = new ArrayList<>();
        Map<String, Map<String, List<ProjectChangeMaterial>>> collect = value.stream().collect(Collectors.groupingBy(ProjectChangeMaterial::getCategoryCode, Collectors.groupingBy(ProjectChangeMaterial::getCategoryDetailCode)));
        for (Map.Entry<String, Map<String, List<ProjectChangeMaterial>>> map : collect.entrySet()) {
            String key = map.getKey();
            Map<String, List<ProjectChangeMaterial>> value1 = map.getValue();
            for (Map.Entry<String, List<ProjectChangeMaterial>> map1 : value1.entrySet()) {
                String key1 = map1.getKey();
                List<ProjectChangeMaterial> value2 = map1.getValue();
                SmChangeAmount smChangeAmount = new SmChangeAmount();
                smChangeAmount.setChangeDetailId(id);
                smChangeAmount.setCategoryCode(key);
                smChangeAmount.setCategoryDetailCode(key1);
                smChangeAmount.setCashAmount(value2.stream().map(a -> {
                    BigDecimal b = a.getPrice().multiply(new BigDecimal(a.getLossDosage()));
                    return b;
                }).reduce(BigDecimal::add).orElse(OTHER));
                smChangeAmount.setOriginalAmount(value2.stream().map(a -> {
                    BigDecimal b = a.getPrice().multiply(new BigDecimal(a.getOriginalDosage()));
                    return b;
                }).reduce(BigDecimal::add).orElse(OTHER));
                smChangeAmount.setCreateTime(new Date());
                smChangeAmount.setCreateUser(name + "(" + jobNum + ")");
                smChangeAmounts.add(smChangeAmount);
            }
        }
        return smChangeAmounts;
    }

    /**
     * @param value
     * @Description: 美得你智装根据材料变更详情  返回状态
     * @date: 2017/11/21  18:46
     * @author: Ryze
     */
    private String getCurrentStatus(List<ProjectChangeMaterial> value) {
        String status = ChangeDetailAuditEnum.AUDITREVIEW.toString();
        //过滤掉 减项
        value=value.stream().filter(a->!SelectMaterialTypeEnmu.REDUCEITEM.toString().equals(a.getCategoryCode())).collect(Collectors.toList());
        for (ProjectChangeMaterial projectChangeMaterial : value) {
            // 现在-原来=num
            if (projectChangeMaterial.getNum().compareTo(new BigDecimal(0)) == -1) {
                status = ChangeDetailAuditEnum.MATERIALDEPARTMENTAUDIT.toString();
                break;
            }
        }
        return status;
    }


    /**
     * 根据项目code和变更版本号查询芒果,计算原用量和现用量
     * @param contractCode
     * @param changeNo
     * @return
     */
    public List<ProjectChangeMaterial> findProMatrlByContrCode(String contractCode, String changeNo) {
        List<ProjectChangeMaterial> projectChangeMaterials = new ArrayList<>();
        //查询主材
        List<ProjectChangeMaterial> proMatrlByContrList = mongoCustomerContractService.findProMatrlByContrCode(contractCode, changeNo);
        //查询用量
        List<SmSkuChangeDosage> smSkuChangeDosageList = mongoCustomerContractService.findDosageByContrCode(contractCode, changeNo);
        for (ProjectChangeMaterial projectMaterial : proMatrlByContrList) {
            String id = projectMaterial.getId();
            if(id!=null) {
                //获取用量列表
                List<SmSkuChangeDosage> collect = smSkuChangeDosageList.stream().filter(a -> id.equals(a.getProjectMaterialId())).collect(Collectors.toList());
                if (collect != null && collect.size() > 0) {
                    SmSkuChangeDosage smSkuChangeDosage = collect.get(0);
                    BigDecimal original = collect.stream().map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal lossDosage = collect.stream().map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal subtract = lossDosage.subtract(original);
                    //数量无变化
                    if (subtract.compareTo(BigDecimal.ZERO) == 0) {
                    }else{
                        projectMaterial.setNum(subtract);
                        BigDecimal storeUpgradeDifferencePrice = smSkuChangeDosage.getStoreUpgradeDifferencePrice();
                        BigDecimal storeIncreasePrice = smSkuChangeDosage.getStoreIncreasePrice();
                        BigDecimal storeReducePrice = smSkuChangeDosage.getStoreReducePrice();
                        BigDecimal storeSalePrice = smSkuChangeDosage.getStoreSalePrice();
                        if (storeUpgradeDifferencePrice != null) {
                            projectMaterial.setPrice(storeUpgradeDifferencePrice);
                        } else if (storeIncreasePrice != null) {
                            projectMaterial.setPrice(storeIncreasePrice);
                        } else if (storeReducePrice != null) {
                            projectMaterial.setPrice(storeReducePrice);
                        } else if (storeSalePrice != null) {
                            projectMaterial.setPrice(storeSalePrice);
                        } else {
                            projectMaterial.setPrice(BigDecimal.ZERO);
                        }
                        projectMaterial.setLossDosage(lossDosage.longValue());
                        projectMaterial.setOriginalDosage(original.longValue());
                        projectMaterial.setDomainName(smSkuChangeDosage.getDomainName());
                        projectChangeMaterials.add(projectMaterial);
                    }
                }
            }
        }
        return projectChangeMaterials;
    }

    /**
     * 根据项目号查询变更单
     *
     * @param contractCode
     * @return
     */
    public List<SmChangeDetail> findChangeDetail(String contractCode, String changeVersionNo) {
        return this.entityDao.findChangeDetail(contractCode, changeVersionNo);
    }

    /**
     *  变更单管理 列表
     * @param params 关键字
     * @return
     */
    public BootstrapPage<SmChangeDetail> searchChangeOrderListPage(Map<String, Object> params) {
        List<SmChangeDetail> pageData = Collections.emptyList();
        Long count = this.entityDao.searchChangeOrderListTotal(params);
        if (count > 0) {
            pageData = entityDao.findChangeOrderList(params);
        }
        //替换客户手机号中间四位
        pageData = this.replaceAllMobile(pageData);
        return new BootstrapPage(count, pageData);
    }



    public void updateDownloadStatus(String changeNo) {
        this.entityDao.updateDownloadStatus(changeNo);
    }

    /**
     * 根据变更单号查询变更单
     * @param changeNo
     * @return
     */
    public SmChangeDetail findDownloadTimesByChangeNo(String changeNo) {
        return this.entityDao.findDownloadTimesByChangeNo(changeNo);
    }

    /**
     * 查询变更单列表
     * @param params
     */
    public BootstrapPage<SmChangeDetail> changeMaterialList(Map<String, Object> params) {
        List<SmChangeDetail> pageData = Collections.emptyList();
        Long count = this.entityDao.listCount(params);
        if (count > 0) {
            pageData = entityDao.changeMaterialList(params);
        }
        //替换客户手机号中间四位
        pageData = this.replaceAllMobile(pageData);
        return new BootstrapPage(count, pageData);
    }

    /**
     * 根据id查询变更单
     * @param ids
     * @return
     */
    public List<SmChangeDetail> getByIds(Long[] ids) {
        return this.entityDao.getByIds(ids);
    }

    /**
     * 提交变更 单
     *      一.changeCategoryUrl 为空时,为其他金额增减:
     *          1.至少有一项是减少金额, 变更单状态改为: 设计总监审核;
     *          2.否则 改为:审计审核;
     *      二.changeCategoryUrl 不为空,为主材变更:
     *          1.除减项外的主材,至少有一个sku用量总和减少, 变更单状态改为: 材料部审核中;
     *          2.否则 改为:审计审核;
     * @param smChangeDetail
     * @auth Paul
     * @return dto
     */
    @Transactional(rollbackFor = Exception.class)
    public Object submitChangeDetail(SmChangeDetail smChangeDetail) {
        if(StringUtils.isBlank(smChangeDetail.getChangeCategoryUrl())){
            //为其他金额增减 查询状态为1的
            Map<String,Object> otherMap = new HashMap<String,Object>();
            otherMap.put("contractCode", smChangeDetail.getContractCode());
            otherMap.put("changeFlag", "1");
            List<OtherAddReduceAmount> otherMoneyList = otherAddRecuceAmountDao.findByContractCodeList(otherMap);
            if(otherMoneyList != null && otherMoneyList.size() > 0){
                List<OtherAddReduceAmount> reduceList = otherMoneyList.stream()
                        .filter(other -> "0".equals(other.getAddReduceType()))
                        .collect(Collectors.toList());
                if(reduceList != null && reduceList.size() > 0){
                    //至少有一项 金额较少了
                    //设计总监审核
                    smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.DESIGNDIRECTORINTHEAUDIT.toString());
                }else{
                    //审计审核
                    smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITREVIEW.toString());
                }
            }else{
                //没有发生变更的
                //审计审核
                smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITREVIEW.toString());
            }
            smChangeDetail.setChangeCategoryUrl(null);
        }else{
            //主材
            Map<String,Object> materialMap = new HashMap<String,Object>();
            materialMap.put("contractCode", smChangeDetail.getContractCode());
            materialMap.put("productCategoryUrl", smChangeDetail.getChangeCategoryUrl());
            List<ProjectChangeMaterial> materialList = projectChangeMaterialDao.findWithSubListByMaterialParams(materialMap);
            if(materialList != null && materialList.size() > 0){
                //去除掉减项后遍历 统计每个sku用量总和
                materialList.stream().filter(material -> !SelectMaterialTypeEnmu.REDUCEITEM.toString().equals(material.getCategoryCode()))
                        .forEach(material -> {
                            List<SmSkuDosage> skuDosageList = material.getSkuDosageList();
                            //去除掉减项后遍历 统计每个sku用量总和
                            BigDecimal lossDosageAmount = BigDecimal.ZERO;
                            BigDecimal originalDosageAmount = BigDecimal.ZERO;
                            if(skuDosageList != null && skuDosageList.size() > 0){
                                for(SmSkuDosage sku : skuDosageList){
                                    lossDosageAmount = lossDosageAmount.add(sku.getLossDosage());
                                    originalDosageAmount = originalDosageAmount.add(sku.getOriginalDosage());
                                }
                            }
                            //判断现用量是否和原用量相等
                            if(lossDosageAmount.compareTo(originalDosageAmount) < 0){
                                //有用量减少的 材料部审核中
                                smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.MATERIALDEPARTMENTAUDIT.toString());
                                //去材料部审核
                                smChangeDetail.setPassMaterialsDepartment("1");
                            }
                        });
                //上面遍历完之后,如果有状态,说明是去了材料部,否则就去审计
                if(StringUtils.isBlank(smChangeDetail.getCurrentStatus())){
                    //没有减少的
                    //审计审核
                    smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITREVIEW.toString());
                }
            }else{
                //没有减少的
                //审计审核
                smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITREVIEW.toString());
            }
        }

        //更新状态
        smChangeDetailDao.update(smChangeDetail);
        return StatusDto.buildSuccessStatusDto("变更单提交成功!");
    }

    /**
     * 隐藏手机号中间四位
     * @param customerContractList
     * @return
     */
    public List<SmChangeDetail> replaceAllMobile(List<SmChangeDetail> customerContractList) {
        customerContractList.forEach(p->{
            String mobile = p.getMobile();
            String secondMobile = p.getSecondContactMobile();
            if( null != mobile && mobile.length() > 7 ){
                p.setMobile(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
            if( null != secondMobile && secondMobile.length() > 7 ){
                p.setSecondContactMobile(secondMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
        });
        return customerContractList;
    }
    /**
     * @Description: 美得你智装 根据变更号查出 备注
     * @date: 2017/12/21  16:08
     * @param changeNo
     * @author: Ryze
     */
    public Map<String,String> getRemakk(String changeNo) {
        SmChangeDetail byChangeNo = entityDao.getByChangeNo(changeNo);
        String rocordByChangeNo = entityDao.getRocordByChangeNo(changeNo);
        HashMap<String, String> objectObjectHashMap = Maps.newHashMap();
        if(byChangeNo!=null) {
            String materialRemarks = byChangeNo.getMaterialRemarks();
            String designDirectorRemarks = byChangeNo.getDesignDirectorRemarks();
            if(StringUtils.isBlank(materialRemarks)){
                materialRemarks=null;
            }
            if(StringUtils.isBlank(designDirectorRemarks)){
                designDirectorRemarks=null;
            }
            objectObjectHashMap.put("remarks1", materialRemarks);

            objectObjectHashMap.put("remarks2", designDirectorRemarks);
        }
        objectObjectHashMap.put("remarks3",rocordByChangeNo);
        return objectObjectHashMap;
    }
}
