package cn.mdni.business.entity.orderflow;

import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 预签约信息
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/22 11:30.
 */
public class OrderBespeakContract extends IdEntity {
    /**
     * 项目编码
     */
    private String  contractCode;

    /**
     * 是否完成签约 ‘0’否；‘1’是
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
     * 备注
     */
    private String remark;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
