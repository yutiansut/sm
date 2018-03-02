package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymoneyRecord extends IdEntity{

    /**
     * 交款记录id
     */
    private String payId;
    /**
     *项目编号
     */
    private String contractCode;
    /**
     *项目唯一uuid
     */
    private String contractUuid;
    /**
     *交款阶段uuid编码
     */
    private String stageCode;
    /**
     *交款阶段类型
     */
    private String payType;
    /**
     *交款人姓名
     */
    private String payerName;
    /**
     *交款人手机号
     */
    private String payerMobile;
    /**
     *交款时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date payTime;
    /**
     *应收
     */
    private BigDecimal expectReceived;
    /**
     *实收
     */
    private BigDecimal actualReceived;
    /**
     *收据号
     */
    private String receiptNum;
    /**
     *是否红冲
     */
    private Boolean ifRcw;
    /**
     *
     */
    private String ifRcwName;
    /**
     *被红冲的记录id
     */
    private String rcwPayid;
    /**
     *创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;
    /**
     *更新时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date updateTime;
    /**
     *数据创建人
     */
    private String creator;
    /**
     *正常、退单
     */
    private String payStatus;
    /**
     *备注
     */
    private String remark;
    /**
     *打印的次数
     */
    private Integer printCount;
    /**
     *收银员
     */
    private String cashier;
    /**
     *支付方式编号
     */
    private String paymethodCode;
    /**
     *支付方式名称
     */
    private String paymethodName;
    /**
     *
     */
    private Long paymethodAttrId;
    private String paymethodAttrFullname;
    /**
     *交易费率
     */
    private Double costfeeRate;
    /**
     *交易手续费金额
     */
    private BigDecimal costfeeAmount;
    /**
     *是否可以被抵扣
     */
    private Boolean ifAbleDeduct;
    /**
     *合同开工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date contractStartTime;
    /**
     *订单状态
     */
    private String orderFlowStatus;
    /**
     *房屋面积
     */
    private BigDecimal buildArea;
    /**
     *交款阶段
     */
    private String itemName;
    private Integer templateStageId;
    private String accountSubject;
    /**
     * 人工指定的模版节点编号
     */
    private String templateStageCode;
    /**
     * 人工指定的款项类型
     */
    private String payManualFlag;
    /**
     * 关联的第扣款、退款等记录的id
     */
    private String relateRecordId;

    private String configContent;

    /*********************
     * 数据库里没有对应字段，辅助查询
     * **********************/
    /**
     *客户姓名
     */
    private String customerName;
    /**
     *客户电话
     */
    private String customerMobile;
    /**
     *工程地址
     */
    private String addressProvince;
    private String addressCity;
    private String addressArea;
    /**
     *客户装修地址
     */
    private String houseAddr;

    /**
     *财务状态
     */
    private String financeStatus;
    /**
     *已签定金合同
     */
    private Boolean stageFinished;
    /**
     *客服姓名
     */
    private String serviceName;
    /**
     *客服电话
     */
    private String serviceMobile;
    /**
     *第二联系人
     */
    private String secondContact;
    private String secondContactMobile;
    /**
     *设计师
     */
    private String designer;

    private BigDecimal balance;
    private String mealName;
    private BigDecimal branch;
    private BigDecimal actual;
    /**
     *支付方式id
     */
    private Long paymethodId;
    /**
     * 使用的抵扣金id
     */
    private Long usedDeductMoneyId;

    public String getHouseAddr() {
        return houseAddr;
    }

    public BigDecimal getBranch() {
        return branch;
    }

    public void setBranch(BigDecimal branch) {
        this.branch = branch;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public void setHouseAddr(String houseAddr) {
        this.houseAddr = houseAddr;
    }

    public String getSecondContactMobile() {
        return secondContactMobile;
    }

    public void setSecondContactMobile(String secondContactMobile) {
        this.secondContactMobile = secondContactMobile;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIfRcwName() {
        return ifRcwName;
    }

    public void setIfRcwName(String ifRcwName) {
        this.ifRcwName = ifRcwName;
    }

    public String getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(String accountSubject) {
        this.accountSubject = accountSubject;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }

    public void setServiceMobile(String serviceMobile) {
        this.serviceMobile = serviceMobile;
    }

    public String getSecondContact() {
        return secondContact;
    }

    public void setSecondContact(String secondContact) {
        this.secondContact = secondContact;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getPayManualFlag() {
        return payManualFlag;
    }

    public void setPayManualFlag(String payManualFlag) {
        this.payManualFlag = payManualFlag;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getExpectReceived() {
        return expectReceived;
    }

    public void setExpectReceived(BigDecimal expectReceived) {
        this.expectReceived = expectReceived;
    }

    public BigDecimal getActualReceived() {
        return actualReceived;
    }

    public void setActualReceived(BigDecimal actualReceived) {
        this.actualReceived = actualReceived;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Boolean getIfRcw() {
        return ifRcw;
    }

    public void setIfRcw(Boolean ifRcw) {
        this.ifRcw = ifRcw;
    }

    public String getRcwPayid() {
        return rcwPayid;
    }

    public void setRcwPayid(String rcwPayid) {
        this.rcwPayid = rcwPayid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getPaymethodCode() {
        return paymethodCode;
    }

    public void setPaymethodCode(String paymethodCode) {
        this.paymethodCode = paymethodCode;
    }

    public String getPaymethodName() {
        return paymethodName;
    }

    public void setPaymethodName(String paymethodName) {
        this.paymethodName = paymethodName;
    }

    public Long getPaymethodAttrId() {
        return paymethodAttrId;
    }

    public void setPaymethodAttrId(Long paymethodAttrId) {
        this.paymethodAttrId = paymethodAttrId;
    }

    public String getPaymethodAttrFullname() {
        return paymethodAttrFullname;
    }

    public void setPaymethodAttrFullname(String paymethodAttrFullname) {
        this.paymethodAttrFullname = paymethodAttrFullname;
    }

    public Double getCostfeeRate() {
        return costfeeRate;
    }

    public void setCostfeeRate(Double costfeeRate) {
        this.costfeeRate = costfeeRate;
    }

    public BigDecimal getCostfeeAmount() {
        return costfeeAmount;
    }

    public void setCostfeeAmount(BigDecimal costfeeAmount) {
        this.costfeeAmount = costfeeAmount;
    }

    public Boolean getIfAbleDeduct() {
        return ifAbleDeduct;
    }

    public void setIfAbleDeduct(Boolean ifAbleDeduct) {
        this.ifAbleDeduct = ifAbleDeduct;
    }

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Date contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public String getOrderFlowStatus() {
        return orderFlowStatus;
    }

    public void setOrderFlowStatus(String orderFlowStatus) {
        this.orderFlowStatus = orderFlowStatus;
    }

    public BigDecimal getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getTemplateStageId() {
        return templateStageId;
    }

    public void setTemplateStageId(Integer templateStageId) {
        this.templateStageId = templateStageId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(String financeStatus) {
        this.financeStatus = financeStatus;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public Boolean getStageFinished() {
        return stageFinished;
    }

    public void setStageFinished(Boolean stageFinished) {
        this.stageFinished = stageFinished;
    }

    public String getTemplateStageCode() {
        return templateStageCode;
    }

    public void setTemplateStageCode(String templateStageCode) {
        this.templateStageCode = templateStageCode;
    }

    public Long getPaymethodId() {
        return paymethodId;
    }

    public void setPaymethodId(Long paymethodId) {
        this.paymethodId = paymethodId;
    }

    public String getRelateRecordId() {
        return relateRecordId;
    }

    public void setRelateRecordId(String relateRecordId) {
        this.relateRecordId = relateRecordId;
    }

    public Long getUsedDeductMoneyId() {
        return usedDeductMoneyId;
    }

    public void setUsedDeductMoneyId(Long usedDeductMoneyId) {
        this.usedDeductMoneyId = usedDeductMoneyId;
    }
}
