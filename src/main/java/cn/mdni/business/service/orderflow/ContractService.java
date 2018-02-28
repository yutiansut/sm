package cn.mdni.business.service.orderflow;

import cn.mdni.business.constants.*;
import cn.mdni.business.dao.material.CustomerContractDao;
import cn.mdni.business.dao.orderflow.ContractDao;
import cn.mdni.business.dao.orderflow.OrderBespeakContractDao;
import cn.mdni.business.dto.orderflow.*;
import cn.mdni.business.entity.orderflow.Customer;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.business.entity.operatelog.RequestOutapiLog;
import cn.mdni.business.entity.orderflow.OrderBespeakContract;
import cn.mdni.business.service.finance.FinanceProjectService;
import cn.mdni.business.service.finance.PayplanService;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.operatelog.OperateLogService;
import cn.mdni.business.service.operatelog.RequestOutapiLogService;
import cn.mdni.business.service.api.OutApiService;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.commons.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cn.mdni.business.constants.PropertyHolder.getMpsPushUrl;

/**
 * @Description: 订单Service
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/13.
 */
@Service
public class ContractService extends CrudService<ContractDao, CustomerContract> {


    private static String system_type = "1"; //系统类型 1 订单流转  2 选材系统
    @Autowired
    private CustomerContractDao customerContractDao;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private NumberingRuleService numberingRuleService;
    @Autowired
    private OrderBespeakContractDao orderBespeakContractDao;
    @Autowired
    private PayplanService payplanService;
    @Autowired
    private FinanceProjectService financeProjectService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private RequestOutapiLogService requestOutapiLogService;
    @Autowired
    private OutApiService outApiService;

    /**
     * 根据合同code查询
     *
     * @param contractCode 合同编码
     * @return
     */
    public CustomerContract getContractByContractCode(String contractCode) {
        return this.entityDao.getContractByContractCode(contractCode);
    }

    /**
     * 修改量房信息
     *
     * @param customerContract
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBookHouse(CustomerContract customerContract) {
        this.entityDao.updateBookHouse(customerContract);
    }

    /**
     * 修改出图信息
     *
     * @param customerContract
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOutMap(CustomerContract customerContract) {
        this.entityDao.updateOutMap(customerContract);
    }

    /**
     * 推送项目信息 到 产业工人系统
     *
     * @param contractCode
     */
    public boolean pushProjectToMps(String contractCode) {
        boolean result = false;
        RequestOutapiLog requestOutapiLog = new RequestOutapiLog();
        OperateLog operateLog = new OperateLog();
        StringBuffer operateDesc = new StringBuffer();
        operateDesc.append("项目编号:").append(contractCode).append(",签约完成推送项目信息到产业工人;");
        try {
            Map<String, Object> map = this.entityDao.getPushMpsInfoByCode(contractCode);
            String signKey = SignatureUtils.getKey(map, "&");
            map.put("key", signKey);
            String response = HttpUtils.post(getMpsPushUrl(), map);
            requestOutapiLog.setPushContent(JsonUtils.pojoToJson(map));
            requestOutapiLog.setResponseContent(response);
            Map<String, Object> mapResponse = JsonUtils.fromJson(response, HashMap.class);
            if (null == mapResponse || !mapResponse.containsKey("code")) {
                result = false;
                operateDesc.append("响应出错");
            } else if (mapResponse.get("code").toString().equals("200")) {
                result = true;
                operateDesc.append("推送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            operateDesc.append("系统异常");
        }
        //获取操作人
        String operator = WebUtils.getCurrentUserNameWithOrgCode();
        requestOutapiLog.setPushType(OutApiEnum.PUSH_PROJECT.toString());
        requestOutapiLog.setNeedAgainPush(result ? 0 : 1);
        requestOutapiLogService.insertRequestLog(requestOutapiLog);
        //插入操作日志
        operateLogService.insertApiLog(contractCode, system_type, operateDesc.toString(), OperateLogEnum.PUSH_TO_MPS, operator);
        return result;
    }

    /**
     * 同步开竣工时间 基装完成时间
     *
     * @param requestKey
     */
    @Transactional(rollbackFor = Exception.class)
    public Object syncStartWorkOrCompleted(String requestKey) {
        Object result = new Object();
        MpsSyncProjectInfoDto mpsSyncProjectInfoDto = new MpsSyncProjectInfoDto();
        CustomerContract customerContract = new CustomerContract();
        StringBuilder logDesc = new StringBuilder();
        OperateLogEnum logType = OperateLogEnum.SYNC_CONSTRUCTION_TIME;
        try {
            mpsSyncProjectInfoDto = JsonUtils.fromJson(requestKey, MpsSyncProjectInfoDto.class);
            //检查参数是否合法
            if (null == mpsSyncProjectInfoDto) {
                logDesc.append("参数异常:").append(requestKey);
                operateLogService.insertApiLog("", system_type, logDesc.toString(), logType, "");
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.PARAM_ERROR.getMessage());
            }
            //检查同步类型
            if (null == mpsSyncProjectInfoDto.getType()) {
                logDesc.append("参数异常:").append(requestKey);
                operateLogService.insertApiLog("", system_type, logDesc.toString(), logType, "");
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.PARAM_LACK.getMessage());
            }

            customerContract = customerContractDao.getByCode(mpsSyncProjectInfoDto.getOrderId());
            //检查项目信息
            if (null == customerContract) {
                //项目信息不存在 则向老订单系统推送
                return outApiService.makeOldOrderProject(requestKey, OutApiEnum.SYNC_START_COMPLETE);
            }
            //判断同步类型
            if (mpsSyncProjectInfoDto.getType().equals(typeEnum.START_CONSTRUCTION.getType())) {
                customerContract.setStartConstructionTime(mpsSyncProjectInfoDto.getTime());
                customerContract.setOrderFlowStatus(CustomerContractEnum.ON_CONSTRUCTION);
                //日志信息
                logDesc.append(typeEnum.START_CONSTRUCTION.getDesc()).append(requestKey);
                logType = OperateLogEnum.PROJECT_START;
            } else if (mpsSyncProjectInfoDto.getType().equals(typeEnum.BASIC_CONSTRUCTION.getType())) {
                customerContract.setBasicConstructionTime(mpsSyncProjectInfoDto.getTime());
                //日志信息
                logDesc.append(typeEnum.BASIC_CONSTRUCTION.getDesc()).append(requestKey);
            } else if (mpsSyncProjectInfoDto.getType().equals(typeEnum.COMPLETE_CONSTRUCTION.getType())) {
                customerContract.setCompleteConstructionTime(mpsSyncProjectInfoDto.getTime());
                customerContract.setOrderFlowStatus(CustomerContractEnum.PROJECT_COMPLETE);
                //日志信息
                logDesc.append(typeEnum.COMPLETE_CONSTRUCTION.getDesc()).append(requestKey);
                logType = OperateLogEnum.PROJECT_COMPLETE;
            }
            this.entityDao.syncProjectInfoFromMps(customerContract);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logDesc.append(typeEnum.COMPLETE_CONSTRUCTION.getDesc()).append(requestKey);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.ERROR.getMessage());
        }
        operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.toString(), logType, "");
        return result;
    }

    /**
     * 同步项目经理质检
     *
     * @param requestKey
     */
    @Transactional(rollbackFor = Exception.class)
    public Object syncProjectManagerOrSupervisor(String requestKey) {
        Object result = new Object();
        MpsSyncProjectInfoDto mpsSyncProjectInfoDto = new MpsSyncProjectInfoDto();
        CustomerContract customerContract = new CustomerContract();
        StringBuilder logDesc = new StringBuilder();
        OperateLogEnum logType = OperateLogEnum.SYNC_PM_SUPERVISOR;
        try {
            mpsSyncProjectInfoDto = JsonUtils.fromJson(requestKey, MpsSyncProjectInfoDto.class);
            //检查参数是否合法
            if (null == mpsSyncProjectInfoDto) {
                logDesc.append("参数异常：").append(requestKey);
                operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.toString(), logType, "");
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.PARAM_ERROR.getMessage());
            }
            //检查同步类型
            if (null == mpsSyncProjectInfoDto.getType()) {
                logDesc.append("参数异常：").append(requestKey);
                operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.toString(), logType, "");
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.PARAM_LACK.getMessage());
            }
            customerContract = customerContractDao.getByCode(mpsSyncProjectInfoDto.getOrderId());
            //检查项目信息
            if (null == customerContract) {
                //项目信息不存在 则向老订单系统推送
                return outApiService.makeOldOrderProject(requestKey, OutApiEnum.SYNC_PM_SUP);
            }
            //判断同步类型
            if (mpsSyncProjectInfoDto.getType().equals(typeEnum.PROJECT_MANAGER.getType())) {
                customerContract.setProjectManager(mpsSyncProjectInfoDto.getName());
                customerContract.setPmMobile(mpsSyncProjectInfoDto.getMobile());
                //日志信息
                logDesc.append(typeEnum.PROJECT_MANAGER.getDesc());
            } else if (mpsSyncProjectInfoDto.getType().equals(typeEnum.SUPERVISOR.getType())) {
                customerContract.setSupervisor(mpsSyncProjectInfoDto.getName());
                customerContract.setSupervisorMobile(mpsSyncProjectInfoDto.getMobile());
                //日志信息
                logDesc.append(typeEnum.SUPERVISOR.getDesc());
            }
            this.entityDao.syncProjectInfoFromMps(customerContract);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.ERROR.getMessage());
        }
        operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.append(requestKey).toString(), logType, "");
        return result;
    }

    /**
     * 产业工人拒绝接单
     *
     * @param requestKey
     */
    @Transactional(rollbackFor = Exception.class)
    public Object mpsRejectProject(String requestKey) {
        Object result = new Object();
        MpsSyncProjectInfoDto mpsSyncProjectInfoDto = new MpsSyncProjectInfoDto();
        CustomerContract customerContract = new CustomerContract();
        StringBuilder logDesc = new StringBuilder();
        OperateLogEnum logType = OperateLogEnum.MPS_REJECT_PROJECT;
        try {
            mpsSyncProjectInfoDto = JsonUtils.fromJson(requestKey, MpsSyncProjectInfoDto.class);
            //检查参数是否合法
            if (null == mpsSyncProjectInfoDto) {
                logDesc.append("参数异常：").append(requestKey);
                operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.append(requestKey).toString(), logType, "");
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.PARAM_ERROR.getMessage());
            }
            customerContract = customerContractDao.getByCode(mpsSyncProjectInfoDto.getOrderId());
            //判断项目信息
            if (null == customerContract) {
                //项目信息不存在 则向老订单系统推送
                return outApiService.makeOldOrderProject(requestKey, OutApiEnum.REJECT_PROJECT);
            }
            logDesc.append(typeEnum.BACK_PROJECT.getDesc());
            customerContract.setOrderFlowStatus(CustomerContractEnum.STAY_SEND_SINGLE_AGAIN);
            customerContract.setMpsBackRemark(mpsSyncProjectInfoDto.getRemarks());
            this.entityDao.syncProjectInfoFromMps(customerContract);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), ResponseEnum.ERROR.getMessage());
        }
        operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.append(requestKey).toString(), logType, "");
        return result;
    }


    /**
     * 通过项目合同的uuid查询项目信息
     *
     * @param contractUuid
     * @return
     */
    public CustomerContract getContractByUuid(String contractUuid) {
        return entityDao.getByUuid(contractUuid);
    }

    /**
     * 创建新项目
     *
     * @param reqJson crm 请求信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object createProject(String reqJson) throws Exception {
        ProjectDto projectDto = JsonUtils.fromJson(reqJson, ProjectDto.class);
        //获取项目信息
        CustomerContract customerContract = getCrmContractInfo(projectDto);
        StringBuilder logDesc = new StringBuilder();
        logDesc.append("生成订单");
        if (checkCustomerContract(customerContract)) {
            this.entityDao.insert(customerContract);
            //获取预备订单信息
            OrderBespeakContract orderBespeakContract = getCrmPlanContractInfo(projectDto);
            orderBespeakContract.setContractCode(customerContract.getContractCode());
            //初始化财务信息
            StatusDto finaStatusDto = financeProjectService.handleFinanceAfterProjectCreate(customerContract);
            if (!finaStatusDto.isSuccess()) {
                //初始化财务信息失败 抛出异常 回滚所有操作
                throw new Exception(finaStatusDto.getMessage());
            }
            orderBespeakContractDao.insert(orderBespeakContract);
            operateLogService.insertApiLog(customerContract.getContractCode(), system_type, logDesc.append(reqJson).toString(), OperateLogEnum.CREATE_TIME, "");
            Map<String, Object> responseMap = new HashMap<String, Object>();
            MapUtils.putNotNull(responseMap, "orderId", customerContract.getContractUuid());
            MapUtils.putNotNull(responseMap, "orderNo", customerContract.getContractCode());
            return StatusDto.buildDataSuccessStatusDto("success", responseMap);
        } else {
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(), "生成项目编号失败！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContractStatus(CustomerContract customerContract) {
        this.entityDao.updateContractStatus(customerContract);
    }

    /**
     * 分配退回
     *
     * @param customerContract
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRetreat(CustomerContract customerContract) {
        customerContract.setOrderFlowStatus(CustomerContractEnum.APPLY_REFUND);
        this.entityDao.updateContractRetreat(customerContract);
    }

    /**
     * 督导组长收回
     *
     * @param customerContract
     */
    @Transactional(rollbackFor = Exception.class)
    public void recovery(CustomerContract customerContract) {
        customerContract.setDesigner(null);
        customerContract.setDesignerDepName(null);
        customerContract.setDesignerDepCode(null);
        customerContract.setDesignerCode(null);
        customerContract.setDesignerMobile(null);
        customerContract.setDesignerOrgCode(null);
        customerContract.setOrderFlowStatus(CustomerContractEnum.SUPERVISOR_STAY_ASSIGNED);
        this.entityDao.updateRecovery(customerContract);
    }

    /**
     * 获取项目列表 隐藏手机号中间四位
     *
     * @param paramMap
     * @return
     */
    public BootstrapPage<CustomerContract> searchCustomerContractScrollPage(Map<String, Object> paramMap) {
        BootstrapPage<CustomerContract> result = this.searchScrollPage(paramMap);
        result.setRows(customerContractService.replaceAllMobile(result.getRows()));
        return result;
    }

    /**
     * 分配设计组
     *
     * @param customerContract
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDesignerGroup(CustomerContract customerContract) {
        this.entityDao.updateDesignerGroup(customerContract);
    }

    /**
     * 根据串单id查询串单信息
     *
     * @param contractCode 订单编号
     */
    public BootstrapPage<CustomerContract> findSingleDetailBySingleId(String contractCode) {
        CustomerContract customerContract = this.entityDao.getContractByContractCode(contractCode);
        BootstrapPage<CustomerContract> contractList = new BootstrapPage<CustomerContract>();
        List<CustomerContract> customerContractList = new ArrayList<>();
        if (customerContract.getSingleId() != null) {
            customerContractList = this.entityDao.findSingleDetailBySingleId(customerContract.getSingleId(), customerContract.getStoreCode());
            long size = customerContractList.size();
            contractList.setTotal(size);
            contractList.setRows( customerContractList );
        }
        return contractList;
    }

    /**
     * 获取项目信息
     *
     * @param projectDto crm 请求信息
     * @return
     */
    private CustomerContract getCrmContractInfo(ProjectDto projectDto) {

        CustomerContract customerContract = new CustomerContract();
        OrderDto orderDto = projectDto.getOrder();
        OrderPlaceOrderDto planOrderDto = projectDto.getPlaceOrder();
        CustomerDto customerDto = projectDto.getCustomer();

        Customer customer = this.getCustomerInfo(projectDto, orderDto.getAreaFlag());
        if (null == customer) {
            return customerContract;
        }
        customerContract.setStoreCode(orderDto.getAreaFlag());
        String contractCode = numberingRuleService.getNumber(orderDto.getAreaFlag(), NumberingTypeEnum.PROJECT_NUMBER);
        //获取订单初始 订单流转状态
        if (payplanService.ifProjectNeedDeposit(customerContract.getStoreCode())) {
            //有定金阶段 默认订单流转状态未待转大定 是否有定金合同  默认为无
            customerContract.setOrderFlowStatus(CustomerContractEnum.STAY_TURN_DETERMINE);
            customerContract.setSignDeposit(0);
        } else {
            customerContract.setOrderFlowStatus(CustomerContractEnum.SUPERVISOR_STAY_ASSIGNED);
            customerContract.setSignDeposit(1);
        }
        StringBuilder layout = new StringBuilder();
        layout.append(orderDto.getRoomNum());
        layout.append("室");
        layout.append(orderDto.getHallNum());
        layout.append("厅");
        layout.append(orderDto.getToiletNum());
        layout.append("卫");
        customerContract.setContractCode(contractCode);
        customerContract.setCustomerId(customer.getId());
        customerContract.setBuildArea(orderDto.getFloorArea());
        customerContract.setLayout(layout.toString());
        if (orderDto.getIsNew() != null) {
            customerContract.setHouseCondition(orderDto.getIsNew().toString());
        }
        customerContract.setElevator(orderDto.getIsLift());
        customerContract.setForwardDeliveryHousing(orderDto.getIsFDH());
        customerContract.setAddressProvince(orderDto.getProvince());
        customerContract.setAddressCity(orderDto.getCity());
        customerContract.setAddressArea(orderDto.getDistrict());
        customerContract.setHouseAddr(orderDto.getAddress());
        customerContract.setActivityName(orderDto.getActivity());
        customerContract.setDiscountName(orderDto.getDiscount());
        customerContract.setServiceCode(orderDto.getServiceUserId());
        customerContract.setServiceName(orderDto.getServiceName());
        customerContract.setServiceMobile(orderDto.getServiceMobile());
        customerContract.setPlanHouseTime(planOrderDto.getPlanMeasureTime());
        customerContract.setPlanDecorateTime(planOrderDto.getRenovationTime());
        customerContract.setContractUuid(UUID.randomUUID().toString());
        customerContract.setCreateTime(new Date());
        customerContract.setProvinceCode(orderDto.getProvinceCode());
        customerContract.setCityCode(orderDto.getCityCode());
        customerContract.setAreaCode(orderDto.getDistrictCode());
        return customerContract;
    }

    /**
     * 获取客户信息
     *
     * @param projectDto crm 请求信息
     * @return
     */
    private Customer getCustomerInfo(ProjectDto projectDto, String storeCode) {
        Customer customer = new Customer();
        try {
            customer = customerService.getByCode(projectDto.getCustomer().getCustomerNo());
            if (null == customer) {
                customer = new Customer();
                customer.setCode(projectDto.getCustomer().getCustomerNo());
                customer.setName(projectDto.getCustomer().getCustomerName());
                customer.setMobile(projectDto.getCustomer().getCustomerMobile());
                customer.setStoreCode(storeCode);
                customer.setIncomeSource(projectDto.getOrder().getPromoteWay());
                customer.setCustomerTag(projectDto.getCustomer().getCustomerTagName());
                customerService.insert(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    /**
     * 获取预订合同 签约信息
     *
     * @param projectDto crm 请求信息
     * @return
     */
    private OrderBespeakContract getCrmPlanContractInfo(ProjectDto projectDto) {


        OrderDto orderDto = projectDto.getOrder();
        OrderPlaceOrderDto planOrderDto = projectDto.getPlaceOrder();
        //预定合同签约信息
        OrderBespeakContract orderBespeakContract = new OrderBespeakContract();
        orderBespeakContract.setAdvancePayment(planOrderDto.getImprest());
        orderBespeakContract.setCarryCost(planOrderDto.getCarryFee());
        orderBespeakContract.setDismantleRepairCost(planOrderDto.getRemovingRepairFee());
        orderBespeakContract.setLongRangeCost(planOrderDto.getRemotingFee());
        orderBespeakContract.setTotalBudgetAmount(planOrderDto.getBudgetAmount());
        orderBespeakContract.setScheduleSignTime(planOrderDto.getReservedSighTime());
        orderBespeakContract.setExecutor(planOrderDto.getExecutor());
        orderBespeakContract.setPackageType(planOrderDto.getComboType());
        orderBespeakContract.setRemark(projectDto.getRemark().getRemarkContent());
        if (null != planOrderDto.getIsSighContract()) {
            orderBespeakContract.setCompleteSign(planOrderDto.getIsSighContract().toString());
        }
        return orderBespeakContract;
    }

    /**
     * 检查项目信息是否有效
     *
     * @param customerContract 要新建的项目信息
     * @return
     */
    private boolean checkCustomerContract(CustomerContract customerContract) {
        boolean result = true;
        if (!StringUtils.isNotBlank(customerContract.getContractCode())) {
            result = false;
        }
        if (!StringUtils.isNotBlank(customerContract.getServiceCode())) {
            result = false;
        }
        return result;
    }


    enum typeEnum {
        PROJECT_MANAGER("1", "同步项目经理"),
        SUPERVISOR("2", "同步监理"),
        START_CONSTRUCTION("1", "同步开工时间"),
        BASIC_CONSTRUCTION("2", "同步基装完成时间"),
        COMPLETE_CONSTRUCTION("3", "同步竣工时间"),
        BACK_PROJECT("4", "产业工人拒绝接单");


        private String type;
        private String desc;

        private typeEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }
}


