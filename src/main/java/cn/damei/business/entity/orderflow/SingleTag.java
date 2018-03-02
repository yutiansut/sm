package cn.damei.business.entity.orderflow;


import cn.damei.business.constants.CustomerContractEnum;
import cn.damei.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SingleTag extends IdEntity {

    /**
     * 标签tagid
     */
    private String tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date operateTime;

    /**
     * 描述
     */
    private String describtion;

    /**
     * 项目数量
     */
    private String itemCount;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 客户id reference customer(id)
     */
    private Long customerId;

    /**
     * 设计师
     */
    private String designer;

    /**
     * 设计师
     */
    private String designerMobile;

    /**
     * 第二联系人
     */
    private String secondContact;

    /**
     * 第二联系人电话
     */
    private String secondContactMobile;

    /**
     * 房屋面积
     */
    private BigDecimal buildArea;

    /**
     * 计价面积
     */
    private BigDecimal valuateArea;
    /**
     * 工程地址-省份
     */
    private String addressProvince;

    /**
     * 工程地址-市州
     */
    private String addressCity;

    /**
     * 工程地址-区县
     */
    private String addressArea;

    /**
     * 串单信息
     */
    private String singleOrderInfo;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户级别
     */
    private String customerTag;

    /**
     * 项目状态
     */
    private CustomerContractEnum contractStatus;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;

    /**
     * 客服姓名
     */
    private String serviceName;

    /**
     * 客服电话
     */
    private String serviceMobile;

    /**
     * 设计师部门名称
     */
    private String designerDepName;
    /**
     * 计划量房时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date planHouseTime;
    /**
     * 预约量房时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date bookHouseTime;
    /**
     * 订单流转状态
     */
    private String orderFlowStatus;
    /**
     * 付款状态
     */
    private String paymentStatus;

    /**
     * 签订时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date completeTime;

    /**
     * 门店
     * @return
     */
    private String storeCode;
    /**
     * 门店
     * @return
     */
    private String stageName;
    /**
     * 客户装修地址
     * @return
     */
    private String houseAddr;

    public String getHouseAddr() {
        return houseAddr;
    }

    public void setHouseAddr(String houseAddr) {
        this.houseAddr = houseAddr;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getDesignerMobile() {
        return designerMobile;
    }

    public void setDesignerMobile(String designerMobile) {
        this.designerMobile = designerMobile;
    }

    public String getSecondContact() {
        return secondContact;
    }

    public void setSecondContact(String secondContact) {
        this.secondContact = secondContact;
    }

    public String getSecondContactMobile() {
        return secondContactMobile;
    }

    public void setSecondContactMobile(String secondContactMobile) {
        this.secondContactMobile = secondContactMobile;
    }

    public BigDecimal getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }

    public BigDecimal getValuateArea() {
        return valuateArea;
    }

    public void setValuateArea(BigDecimal valuateArea) {
        this.valuateArea = valuateArea;
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

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getSingleOrderInfo() {
        return singleOrderInfo;
    }

    public void setSingleOrderInfo(String singleOrderInfo) {
        this.singleOrderInfo = singleOrderInfo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerContractEnum getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(CustomerContractEnum contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getDesignerDepName() {
        return designerDepName;
    }

    public void setDesignerDepName(String designerDepName) {
        this.designerDepName = designerDepName;
    }

    public Date getPlanHouseTime() {
        return planHouseTime;
    }

    public void setPlanHouseTime(Date planHouseTime) {
        this.planHouseTime = planHouseTime;
    }

    public Date getBookHouseTime() {
        return bookHouseTime;
    }

    public void setBookHouseTime(Date bookHouseTime) {
        this.bookHouseTime = bookHouseTime;
    }

    public String getOrderFlowStatus() {
        return orderFlowStatus;
    }

    public void setOrderFlowStatus(String orderFlowStatus) {
        this.orderFlowStatus = orderFlowStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCustomerTag() {
        return customerTag;
    }

    public void setCustomerTag(String customerTag) {
        this.customerTag = customerTag;
    }
}