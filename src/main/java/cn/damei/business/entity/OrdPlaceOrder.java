package cn.damei.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class OrdPlaceOrder {


    private BigDecimal removingRepairFee;//拆除修复费

    private BigDecimal remotingFee;//远程费

    private BigDecimal carryFee;//搬运费

    private BigDecimal budgetAmount;//预算总金额

    private BigDecimal imprest;//预付款定金

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date planMeasureTime;//预约量房时间

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

	public BigDecimal getCarryFee() {
		return carryFee;
	}

	public void setCarryFee(BigDecimal carryFee) {
		this.carryFee = carryFee;
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
}
