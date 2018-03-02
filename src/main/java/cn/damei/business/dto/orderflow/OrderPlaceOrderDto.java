package cn.damei.business.dto.orderflow;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPlaceOrderDto {

    /**
     * 是否签约完成
     */
    private Integer isSighContract;
    /**
     * 预定合同 签约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date reservedSighTime;

    /**
     * 执行人
     */
    private String executor;

    /**
     * 套餐类别
     */
    private String comboType;
    /**
     * 拆除修复费
     */
    private BigDecimal removingRepairFee;
    /**
     * 远程费
     */
    private BigDecimal remotingFee;
    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;
    /**
     * 预付款
     */
    private BigDecimal imprest;
    /**
     * 计划量房时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date planMeasureTime;

    /**
     * 计划装修时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date renovationTime;

    /**
     * 套餐类别
     */
    private Integer isContractFile;

    /**
     * 搬运费
     */
    private BigDecimal carryFee;

    public Integer getIsSighContract() {
        return isSighContract;
    }

    public void setIsSighContract(Integer isSighContract) {
        this.isSighContract = isSighContract;
    }

    public Date getReservedSighTime() {
        return reservedSighTime;
    }

    public void setReservedSighTime(Date reservedSighTime) {
        this.reservedSighTime = reservedSighTime;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getComboType() {
        return comboType;
    }

    public void setComboType(String comboType) {
        this.comboType = comboType;
    }

    public BigDecimal getRemovingRepairFee() {
        return removingRepairFee;
    }

    public void setRemovingRepairFee(BigDecimal removingRepairFee) {
        this.removingRepairFee = removingRepairFee;
    }

    public BigDecimal getRemotingFee() {
        return remotingFee;
    }

    public void setRemotingFee(BigDecimal remotingFee) {
        this.remotingFee = remotingFee;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getImprest() {
        return imprest;
    }

    public void setImprest(BigDecimal imprest) {
        this.imprest = imprest;
    }

    public Date getPlanMeasureTime() {
        return planMeasureTime;
    }

    public void setPlanMeasureTime(Date planMeasureTime) {
        this.planMeasureTime = planMeasureTime;
    }

    public Integer getIsContractFile() {
        return isContractFile;
    }

    public void setIsContractFile(Integer isContractFile) {
        this.isContractFile = isContractFile;
    }

    public BigDecimal getCarryFee() {
        return carryFee;
    }

    public void setCarryFee(BigDecimal carryFee) {
        this.carryFee = carryFee;
    }

    public Date getRenovationTime() {
        return renovationTime;
    }

    public void setRenovationTime(Date renovationTime) {
        this.renovationTime = renovationTime;
    }
}
