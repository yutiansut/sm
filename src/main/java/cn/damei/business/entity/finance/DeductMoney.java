package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class DeductMoney extends IdEntity{

    /**
     * 项目编号
     */
    private String contractNo;

    /**
     * 项目uuid
     */
    private String contractUuid;

    /**
     * 交款记录的id
     */
    private String payId;

    /**
     * 交款的收据号
     */
    private String receiptNo;

    /**
     * 抵扣款的类型
     */
    private String deductType;

    /**
     * 最多可抵扣的金额
     */
    private BigDecimal maxDeductAmount;

    /**
     * 已经抵扣了的金额
     */
    private BigDecimal deductedAmount;

    /**
     * 最近一次抵扣时间
     */
    private Date latestDeductTime;

    /**
     * 抵扣款状态：可用、用完、无效
     */
    private String deductStatus;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 创建人
     */
    private String creator;


    /**
     * 已抵扣的次数
     */
    private Integer deductTimes;


    /****************************** 辅助字段，数据表里没有对应的列*****************************/
    /**
     * 可用来抵扣的金额
     */
    private BigDecimal ableDeductAmount;


    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public BigDecimal getMaxDeductAmount() {
        return maxDeductAmount;
    }

    public void setMaxDeductAmount(BigDecimal maxDeductAmount) {
        this.maxDeductAmount = maxDeductAmount;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Date getLatestDeductTime() {
        return latestDeductTime;
    }

    public void setLatestDeductTime(Date latestDeductTime) {
        this.latestDeductTime = latestDeductTime;
    }

    public String getDeductStatus() {
        return deductStatus;
    }

    public void setDeductStatus(String deductStatus) {
        this.deductStatus = deductStatus;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public BigDecimal getAbleDeductAmount() {
        return this.maxDeductAmount.subtract(this.deductedAmount);
    }

    public void setAbleDeductAmount(BigDecimal ableDeductAmount) {
        this.ableDeductAmount = ableDeductAmount;
    }


    public Integer getDeductTimes() {
        return deductTimes;
    }

    public void setDeductTimes(Integer deductTimes) {
        this.deductTimes = deductTimes;
    }

    /**
     * 交款方式枚举类
     */
    public enum DeductMoneyTypeEnum{

        DEPOSIT_DEDUCT("抵定金");

        private String label;

        DeductMoneyTypeEnum(String label){
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }


    /**
     * 抵扣金状态枚举类
     */
    public enum DeductMoneyStatusEnum{

        ABLE_DEDUCT("抵定金"),
        USE_UP("用尽"),
        INVALID("无效");

        private String label;

        DeductMoneyStatusEnum(String label){
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }
}
