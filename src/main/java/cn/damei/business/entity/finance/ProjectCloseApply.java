package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectCloseApply extends IdEntity{

    /**
     * 项目编号
     */
    protected String contractNo;

    /**
     * 合同唯一uuid
     */
    protected String contractUuid;

    /**
     * 申请人工号
     */
    protected String applyerNo;

    /**
     * 申请人姓名
     */
    protected String applyer;

    /**
     * 退单申请类型，如客服申请、设计师申请等
     */
    protected String applyerType;

    /**
     * 申请时间
     */

    private Date applyerTime;

    /**
     * 审批人
     */
    protected String checker;

    /**
     * 项目关闭原因
     */
    protected String closeReason;

    /**
     * 应退
     */
    protected BigDecimal expectRefundAmount;

    /**
     * 应扣
     */
    protected BigDecimal expectDeductAmount;

    /**
     * 执行状况，已执行、未执行等
     */
    protected String executeStatus;

    /**
     * 执行人
     */
    protected String executor;

    /**
     * 执行时间
     */
    protected Date executTime;

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

    public String getApplyerNo() {
        return applyerNo;
    }

    public void setApplyerNo(String applyerNo) {
        this.applyerNo = applyerNo;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getApplyerType() {
        return applyerType;
    }

    public void setApplyerType(String applyerType) {
        this.applyerType = applyerType;
    }

    public Date getApplyerTime() {
        return applyerTime;
    }

    public void setApplyerTime(Date applyerTime) {
        this.applyerTime = applyerTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public BigDecimal getExpectRefundAmount() {
        return expectRefundAmount;
    }

    public void setExpectRefundAmount(BigDecimal expectRefundAmount) {
        this.expectRefundAmount = expectRefundAmount;
    }

    public BigDecimal getExpectDeductAmount() {
        return expectDeductAmount;
    }

    public void setExpectDeductAmount(BigDecimal expectDeductAmount) {
        this.expectDeductAmount = expectDeductAmount;
    }

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Date getExecutTime() {
        return executTime;
    }

    public void setExecutTime(Date executTime) {
        this.executTime = executTime;
    }
}
