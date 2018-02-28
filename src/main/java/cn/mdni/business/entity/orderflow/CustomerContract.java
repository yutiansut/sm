package cn.mdni.business.entity.orderflow;

import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 客户合同信息
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
public class CustomerContract extends IdEntity {
    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 项目合同uuid
     */
    private String contractUuid;

    /**
     * 客户id reference customer(id)
     */
    private Long customerId;

    /**
     * 客户装修地址
     */
    private String houseAddr;

    /**
     * 设计师
     */
    private String designer;

    private String designerMobile;

    /**
     * 监理
     */
    private String supervisor;

    private String supervisorMobile;

    /**
     * 项目经理
     */
    private String projectManager;

    /**
     * 项目经理电话
     */
    private String pmMobile;

    /**
     * 第二联系人
     */
    private String secondContact;

    /**
     * 第二联系人电话
     */
    private String secondContactMobile;

    /**
     * 选择套餐id
     */
    private Long mealId;

    /**
     * 套餐单价
     */
    private BigDecimal mealPrice;

    /**
     * 户型
     */
    private String layout;

    /**
     * 房屋面积
     */
    private BigDecimal buildArea;

    /**
     * 计价面积
     */
    private BigDecimal valuateArea;

    /**
     * 房屋状况
     */
    private String houseCondition;

    /**
     * 有无电梯
     */
    private Integer elevator;

    /**
     * 房屋类型
     */
    private String houseType;

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
     * 活动名称
     */
    private String activityName;

    /**
     * 是否有吊顶,石膏线,电视背景墙,以上三项皆无
     */
    private String otherInstallInfo;

    /**
     * 设计备注
     */
    private String designRemark;

    /**
     * 审计备注
     */
    private String auditRemark;

    /**
     * 原始合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 拆改费
     */
    private BigDecimal modifyAmount;

    /**
     * 交款时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date paymentTime;
    /**
     * 串单信息
     */
    private String singleOrderInfo;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String customerMobile;
    /**
     * 套餐名称
     */
    private String mealName;
    /**
     * 设计师编号
     */
    private String designerOrgCode;
    /**
     * 审计员
     */
    private String auditor;
    /**
     * 审计员电话
     */
    private String auditorMobile;
    /**
     * 审计员编号
     */
    private String auditorOrgCode;
    /**
     * 项目状态
     */
    private CustomerContractEnum contractStatus;
    /**
     * 财务状态
     */
    private String financeStatus;
    /**
     * 时间节点
     */
    private String changeStatus;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date operateTime;
    /**
     * 变更单号
     */
    private String currentChangeVersion;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 客服工号
     */
    private String serviceCode;
    /**
     * 客服姓名
     */
    private String serviceName;
    /**
     * 客服电话
     */
    private String serviceMobile;
    /**
     * 设计师工号
     */
    private String designerCode;
    /**
     * 设计师部门号
     */
    private String designerDepCode;
    /**
     * 设计师部门名称
     */
    private String designerDepName;
    /**
     * 计划量房时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date planHouseTime;
    /**
     * 预约量房时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date bookHouseTime;
    /**
     * 计划装修时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date planDecorateTime;
    /**
     * 订单流转状态
     */
    private CustomerContractEnum orderFlowStatus;
    /**
     * 付款状态
     */
    private String paymentStatus;
    /**
     * 折扣名称
     */
    private String discountName;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;
    /**
     * 量房是否完成
     */
    private String bookHouseComplete;
    /**
     * 量房完成时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date bookHouseCompleteTime;
    /**
     * 量房执行人
     */
    private String bookHouseExecutor;
    /**
     * 量房CAD路径
     */
    private String bookHouseCadurl;
    /**
     * 出图是否完成
     */
    private String outMapComplete;
    /**
     * 出图完成时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date outMapCompleteTime;
    /**
     * 出图执行人
     */
    private String outMapExecutor;
    /**
     * 出图CAD路径
     */
    private String outMapCadurl;
    /**
     * 是否期房
     */
    private Integer forwardDeliveryHousing;
    /**
     * 是否签约完成
     */
    private String completeSign;
    /**
     * 预定签约时间
     */
    private Date scheduleSignTime;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 套餐类型
     */
    private String packageType;
    /**
     * 是否
     */
    private String taxedAmount;
    /**
     * 拆除修复费
     */
    private BigDecimal dismantleRepairCost;
    /**
     * 远程费
     */
    private BigDecimal longRangeCost;
    /**
     * 搬运费
     */
    private BigDecimal carryCost;
    /**
     * 预算总金额
     */
    private BigDecimal totalBudgetAmount;
    /**
     * 预付款（定金）
     */
    private BigDecimal advancePayment;
    /**
     * 开工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date startConstructionTime;
    /**
     * 竣工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date completeConstructionTime;
    /**
     * 基装完成时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date basicConstructionTime;
    /**
     * 产业工人拒绝接单备注
     */
    private String mpsBackRemark;
    /**
     * 门店code
     */
    private String storeCode;
    /**
     * 是否签定金合同
     */
    private Integer signDeposit;
    /**
     * 是否有可退字样
     */
    private Integer returnWord;
    /**
     * 客户级别
     */
    private String customerLevel;
    /**
     * 交款阶段名称
     */
    private String stageName;
    /**
     * 省份标识
     */
    private String provinceCode;
    /**
     * 市标识
     */
    private String cityCode;
    /**
     * 区标识
     */
    private String areaCode;
    /**
     * 计划开工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private  Date contractStartTime;
    /**
     * 计划竣工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private  Date contractCompleteTime;

    /**
     * 签约完成时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date completeTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 客户标签
     */
    private String customerTag;
    /**
     * 变更时间类型--方便sql查询
     */
    private String changeTime;
    /**
     * 退回原因
     */
    private String returnReason;
    /**
     * 退回原因描述
     */
    private String returnReasonDescribe;

    /**
     * 合同工期
     */
    private Integer contractSignTrem;

    /**
     * 串单id
     */
    private Long singleId;

    private static final long serialVersionUID = 1L;

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Date contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public Date getContractCompleteTime() {
        return contractCompleteTime;
    }

    public void setContractCompleteTime(Date contractCompleteTime) {
        this.contractCompleteTime = contractCompleteTime;
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

    public String getHouseAddr() {
        return houseAddr;
    }

    public void setHouseAddr(String houseAddr) {
        this.houseAddr = houseAddr;
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

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisorMobile() {
        return supervisorMobile;
    }

    public void setSupervisorMobile(String supervisorMobile) {
        this.supervisorMobile = supervisorMobile;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getPmMobile() {
        return pmMobile;
    }

    public void setPmMobile(String pmMobile) {
        this.pmMobile = pmMobile;
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

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public BigDecimal getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(BigDecimal mealPrice) {
        this.mealPrice = mealPrice;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
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

    public String getHouseCondition() {
        return houseCondition;
    }

    public void setHouseCondition(String houseCondition) {
        this.houseCondition = houseCondition;
    }

    public Integer getElevator() {
        return elevator;
    }

    public void setElevator(Integer elevator) {
        this.elevator = elevator;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getOtherInstallInfo() {
        return otherInstallInfo;
    }

    public void setOtherInstallInfo(String otherInstallInfo) {
        this.otherInstallInfo = otherInstallInfo;
    }

    public String getDesignRemark() {
        return designRemark;
    }

    public void setDesignRemark(String designRemark) {
        this.designRemark = designRemark;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getModifyAmount() {
        return modifyAmount;
    }

    public void setModifyAmount(BigDecimal modifyAmount) {
        this.modifyAmount = modifyAmount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
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

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getDesignerOrgCode() {
        return designerOrgCode;
    }

    public void setDesignerOrgCode(String designerOrgCode) {
        this.designerOrgCode = designerOrgCode;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditorMobile() {
        return auditorMobile;
    }

    public void setAuditorMobile(String auditorMobile) {
        this.auditorMobile = auditorMobile;
    }

    public String getAuditorOrgCode() {
        return auditorOrgCode;
    }

    public void setAuditorOrgCode(String auditorOrgCode) {
        this.auditorOrgCode = auditorOrgCode;
    }

    public CustomerContractEnum getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(CustomerContractEnum contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(String financeStatus) {
        this.financeStatus = financeStatus;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getCurrentChangeVersion() {
        return currentChangeVersion;
    }

    public void setCurrentChangeVersion(String currentChangeVersion) {
        this.currentChangeVersion = currentChangeVersion;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
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

    public String getDesignerCode() {
        return designerCode;
    }

    public void setDesignerCode(String designerCode) {
        this.designerCode = designerCode;
    }

    public String getDesignerDepCode() {
        return designerDepCode;
    }

    public void setDesignerDepCode(String designerDepCode) {
        this.designerDepCode = designerDepCode;
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

    public Date getPlanDecorateTime() {
        return planDecorateTime;
    }

    public void setPlanDecorateTime(Date planDecorateTime) {
        this.planDecorateTime = planDecorateTime;
    }

    public CustomerContractEnum getOrderFlowStatus() {
        return orderFlowStatus;
    }

    public void setOrderFlowStatus(CustomerContractEnum orderFlowStatus) {
        this.orderFlowStatus = orderFlowStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBookHouseComplete() {
        return bookHouseComplete;
    }

    public void setBookHouseComplete(String bookHouseComplete) {
        this.bookHouseComplete = bookHouseComplete;
    }

    public Date getBookHouseCompleteTime() {
        return bookHouseCompleteTime;
    }

    public void setBookHouseCompleteTime(Date bookHouseCompleteTime) {
        this.bookHouseCompleteTime = bookHouseCompleteTime;
    }

    public String getBookHouseExecutor() {
        return bookHouseExecutor;
    }

    public void setBookHouseExecutor(String bookHouseExecutor) {
        this.bookHouseExecutor = bookHouseExecutor;
    }

    public String getBookHouseCadurl() {
        return bookHouseCadurl;
    }

    public void setBookHouseCadurl(String bookHouseCadurl) {
        this.bookHouseCadurl = bookHouseCadurl;
    }

    public String getOutMapComplete() {
        return outMapComplete;
    }

    public void setOutMapComplete(String outMapComplete) {
        this.outMapComplete = outMapComplete;
    }

    public Date getOutMapCompleteTime() {
        return outMapCompleteTime;
    }

    public void setOutMapCompleteTime(Date outMapCompleteTime) {
        this.outMapCompleteTime = outMapCompleteTime;
    }

    public String getOutMapExecutor() {
        return outMapExecutor;
    }

    public void setOutMapExecutor(String outMapExecutor) {
        this.outMapExecutor = outMapExecutor;
    }

    public String getOutMapCadurl() {
        return outMapCadurl;
    }

    public void setOutMapCadurl(String outMapCadurl) {
        this.outMapCadurl = outMapCadurl;
    }

    public Integer getForwardDeliveryHousing() {
        return forwardDeliveryHousing;
    }

    public void setForwardDeliveryHousing(Integer forwardDeliveryHousing) {
        this.forwardDeliveryHousing = forwardDeliveryHousing;
    }

    public String getCompleteSign() {
        return completeSign;
    }

    public void setCompleteSign(String completeSign) {
        this.completeSign = completeSign;
    }

    public Date getScheduleSignTime() {
        return scheduleSignTime;
    }

    public void setScheduleSignTime(Date scheduleSignTime) {
        this.scheduleSignTime = scheduleSignTime;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getTaxedAmount() {
        return taxedAmount;
    }

    public void setTaxedAmount(String taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public BigDecimal getDismantleRepairCost() {
        return dismantleRepairCost;
    }

    public void setDismantleRepairCost(BigDecimal dismantleRepairCost) {
        this.dismantleRepairCost = dismantleRepairCost;
    }

    public BigDecimal getLongRangeCost() {
        return longRangeCost;
    }

    public void setLongRangeCost(BigDecimal longRangeCost) {
        this.longRangeCost = longRangeCost;
    }

    public BigDecimal getCarryCost() {
        return carryCost;
    }

    public void setCarryCost(BigDecimal carryCost) {
        this.carryCost = carryCost;
    }

    public BigDecimal getTotalBudgetAmount() {
        return totalBudgetAmount;
    }

    public void setTotalBudgetAmount(BigDecimal totalBudgetAmount) {
        this.totalBudgetAmount = totalBudgetAmount;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Date getStartConstructionTime() {
        return startConstructionTime;
    }

    public void setStartConstructionTime(Date startConstructionTime) {
        this.startConstructionTime = startConstructionTime;
    }

    public Date getCompleteConstructionTime() {
        return completeConstructionTime;
    }

    public void setCompleteConstructionTime(Date completeConstructionTime) {
        this.completeConstructionTime = completeConstructionTime;
    }

    public Integer getSignDeposit() {
        return signDeposit;
    }

    public void setSignDeposit(Integer signDeposit) {
        this.signDeposit = signDeposit;
    }

    public Integer getReturnWord() {
        return returnWord;
    }

    public void setReturnWord(Integer returnWord) {
        this.returnWord = returnWord;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getBasicConstructionTime() {
        return basicConstructionTime;
    }

    public void setBasicConstructionTime(Date basicConstructionTime) {
        this.basicConstructionTime = basicConstructionTime;
    }

    public String getMpsBackRemark() {
        return mpsBackRemark;
    }

    public void setMpsBackRemark(String mpsBackRemark) {
        this.mpsBackRemark = mpsBackRemark;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomerTag() {
        return customerTag;
    }

    public void setCustomerTag(String customerTag) {
        this.customerTag = customerTag;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getContractSignTrem() {
        return contractSignTrem;
    }

    public void setContractSignTrem(Integer contractSignTrem) {
        this.contractSignTrem = contractSignTrem;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getReturnReasonDescribe() {
        return returnReasonDescribe;
    }

    public void setReturnReasonDescribe(String returnReasonDescribe) {
        this.returnReasonDescribe = returnReasonDescribe;
    }

    public Long getSingleId() {
        return singleId;
    }

    public void setSingleId(Long singleId) {
        this.singleId = singleId;
    }

    /**
     * 获得施工款合同款金额(不含拆改费)
     * @return
     */
    public BigDecimal getContractMoneyAmount(){
        //处理控制，不影响数据库列对应的字段
        BigDecimal totalContractMoneyAmount = contractAmount == null ? BigDecimal.ZERO : contractAmount;
        BigDecimal modifyMoneyAmount = modifyAmount == null? BigDecimal.ZERO : modifyAmount;
        BigDecimal contractMoneyAmount = totalContractMoneyAmount.subtract(modifyMoneyAmount);
        //合同金额最小为0
        return contractMoneyAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : contractMoneyAmount;
    }
}