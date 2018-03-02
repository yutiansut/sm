package cn.damei.business.service.api;


import cn.damei.business.constants.CustomerContractEnum;
import cn.damei.business.constants.PropertyHolder;
import cn.damei.business.constants.SignatureUtils;
import cn.damei.business.dao.finance.FinaCustomerContractDao;
import cn.damei.business.dao.orderflow.ContractDao;
import cn.damei.business.entity.OrdPlaceOrder;
import cn.damei.business.entity.Order;
import cn.damei.business.entity.SimpleCustomerContract;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.FinaProjectAccount;
import cn.damei.business.entity.finance.ProjectCloseApply;
import cn.damei.business.service.finance.ProjectCloseApplyService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.commons.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class CrmApiService {

    @Autowired
    private FinaCustomerContractDao finaCustomerContractDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ProjectCloseApplyService projectCloseApplyService;
    /**
     * 根据门店code和项目Uuid查询订单简要列表
     * @param contractUuidStr,storeCode
     * @return
     */
    public List<SimpleCustomerContract> querySimpleCustomerContractList(String contractUuidStr) {
        String[] contractUuidArr = contractUuidStr.split(",");
        return finaCustomerContractDao.querySimpleCustomerContractList(contractUuidArr);
    }

    /**
     * 根据项目Uuid查询项目详情
     * @param contractUuid
     * @return
     */
    public Map<String,Object> queryCustomerContractDetail(String contractUuid) {

        //查询项目详情
        CustomerContract customerContract = contractDao.getContractByContractUuid(contractUuid);
        Map<String, Object> orderDtlMap = null;
        if(customerContract != null){
            //处理查到的对象
            orderDtlMap = queryOrderDtlInfo(customerContract);
        }

        return orderDtlMap;
    }

    /**
     * 处理查到的对象
     * 函数功能描述:批量查询订单的定金情况
     * @param contractUuidStr
     * @return
     */
    public List<Map<String, String>> queryFinanceDepositDetail(String contractUuidStr) {
        String[] contractUuidArr = contractUuidStr.split(",");
        return finaCustomerContractDao.queryFinanceDepositDetail(contractUuidArr);
    }


    /**
     *
     * @param customerContract
     * @return
     */
    private Map<String, Object> queryOrderDtlInfo(CustomerContract customerContract) {
        Map<String, Object> orderDtlMap = new HashMap<String, Object>();
        Order order = new Order();
        OrdPlaceOrder placeOrder = new OrdPlaceOrder();
        if(customerContract != null){
            order.setOrderId(customerContract.getContractUuid());
            order.setOrderNo(customerContract.getContractCode());
            order.setFloorArea(customerContract.getBuildArea());
            order.setIsNew(Integer.valueOf(customerContract.getHouseCondition()));
            order.setIsLift(customerContract.getElevator());
            order.setIsFDH(customerContract.getForwardDeliveryHousing());
            order.setProvince(customerContract.getAddressProvince());
            order.setCity(customerContract.getAddressCity());
            order.setDistrict(customerContract.getAddressArea());
            order.setAllotState(Integer.parseInt(customerContract.getOrderFlowStatus().getStatusNum()));
            order.setAddress(customerContract.getHouseAddr());
            order.setActivity(customerContract.getActivityName());
            order.setDiscount(customerContract.getDiscountName());
            //计划装修时间
            Calendar cal = Calendar.getInstance();
            cal.setTime(customerContract.getPlanDecorateTime());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            order.setPlanDecorateYear(year);
            order.setPlanDecorateMonth(month);

            order.setServiceName(customerContract.getServiceName());
            order.setServiceMobile(customerContract.getServiceMobile());
            order.setStylistName(customerContract.getDesigner());
            order.setStylistMobile(customerContract.getDesignerMobile());
            order.setSupervisorName(customerContract.getSupervisor());
            order.setSupervisorMobile(customerContract.getSupervisorMobile());
            order.setContact(customerContract.getProjectManager());
            order.setComboType(customerContract.getMealName());
            order.setTagName(customerContract.getSingleOrderInfo());

            order.setContractStartTime(customerContract.getContractStartTime());
            order.setContractCompleteTime(customerContract.getContractCompleteTime());

            placeOrder.setPlanMeasureTime(customerContract.getBookHouseTime());
            placeOrder.setBudgetAmount(customerContract.getTotalBudgetAmount());
            placeOrder.setCarryFee(customerContract.getCarryCost());
            placeOrder.setImprest(customerContract.getAdvancePayment());
            placeOrder.setRemotingFee(customerContract.getLongRangeCost());
            placeOrder.setRemovingRepairFee(customerContract.getDismantleRepairCost());
        }
        orderDtlMap.put("order", order);
        orderDtlMap.put("placeOrder", placeOrder);

        return orderDtlMap;
    }

    /**
     * 新增退单申请记录
     * @param projectCloseApply
     * @return
     */
    public void saveProjectCloseApply(ProjectCloseApply projectCloseApply) {
        //执行状态：未执行
        projectCloseApply.setExecuteStatus("0");
        //执行时间
        projectCloseApply.setExecutTime(new Date());
        projectCloseApplyService.insert(projectCloseApply);
    }


    /**
     * 发送定金情况到crm，在交/退定金时触发
     * @param finaAccount
     */
    public void sendDepositInfoToCrm(FinaProjectAccount finaAccount,CustomerContract project) {
        //构造调接口入参
        if (finaAccount == null && project == null) {
            return;
        }
        Map<String, Object> reqParam = new HashMap<>();
        if (finaAccount != null) {
            reqParam.put("contractUuid", finaAccount.getContractUuid());
            //定金已交金额
            reqParam.put("depositTotalPayed", finaAccount.getDepositTotalPayed());
            //定金交款时间，由于不是真正的交款时间，而是交款出发调用的时间，所以有少量延迟
            reqParam.put("depositTime",new Date());
        }
        if (project != null) {
            reqParam.put("contractUuid", project.getContractUuid());
            //是否转大定
            reqParam.put("depositFinished", project.getSignDeposit());
            //定金是否可退字样
            reqParam.put("depositAbleBack", project.getReturnWord());
            //订单是否关闭
            String orderClosedFlag = "0";
            if(CustomerContractEnum.ORDER_CLOSE.equals(project.getOrderFlowStatus())){
                orderClosedFlag = "1";
            }
            reqParam.put("orderClosed",orderClosedFlag);
            //订单关闭时间，由于不是真正的关闭时间，而是交款出发调用的时间，所以有少量延迟
            if("1".equals(orderClosedFlag)){
                reqParam.put("orderCloseTime", new Date());
            }
        }


        //构造请求签名
        Map<String, Object> paramMap = new HashMap<>();
        MapUtils.putNotNull(paramMap, "reqJson", JsonUtils.pojoToJson(reqParam));
        String signKey = SignatureUtils.getKey(paramMap, "");
        MapUtils.putNotNull(paramMap, "key", signKey);

        //发起请求用的map
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestKey", JsonUtils.pojoToJson(paramMap));

        //发起请求
        try{
            HttpUtils.post(PropertyHolder.getCrmApiHost() + "/company/api/ord/syncdeposit", requestMap);
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
        return;
    }
}

