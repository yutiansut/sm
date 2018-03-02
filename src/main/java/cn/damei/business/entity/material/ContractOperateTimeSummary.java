package cn.damei.business.entity.material;

import cn.damei.core.base.entity.IdEntity;

import java.util.Date;

public class ContractOperateTimeSummary extends IdEntity {
    /**
     * 项目编码
     */
    private String contractCode;
    /**
     * 选材提交审计时间
     */
    private Date materialSubmitAuditTime;
    /**
     * 选材指派审计时间
     */
    private Date materialAssignAuditTime;
    /**
     * 选材审计通过时间
     */
    private Date materialAuditPassTime;
    /**
     * 选材审计未通过时间
     */
    private Date materialAuditRefuseTime;
    /**
     * 选材签约时间
     */
    private Date materialSignTime;
    /**
     * 发起变更时间
     */
    private Date startChangeTime;
    /**
     * 变更提交审计时间
     */
    private Date changeSubmitAuditTime;
    /**
     * 变更审计通过时间
     */
    private Date changeAuditPassTime;
    /**
     * 变更审计未通过时间
     */
    private Date changeAuditRefuseTime;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getMaterialSubmitAuditTime() {
        return materialSubmitAuditTime;
    }

    public void setMaterialSubmitAuditTime(Date materialSubmitAuditTime) {
        this.materialSubmitAuditTime = materialSubmitAuditTime;
    }

    public Date getMaterialAssignAuditTime() {
        return materialAssignAuditTime;
    }

    public void setMaterialAssignAuditTime(Date materialAssignAuditTime) {
        this.materialAssignAuditTime = materialAssignAuditTime;
    }

    public Date getMaterialAuditPassTime() {
        return materialAuditPassTime;
    }

    public void setMaterialAuditPassTime(Date materialAuditPassTime) {
        this.materialAuditPassTime = materialAuditPassTime;
    }

    public Date getMaterialAuditRefuseTime() {
        return materialAuditRefuseTime;
    }

    public void setMaterialAuditRefuseTime(Date materialAuditRefuseTime) {
        this.materialAuditRefuseTime = materialAuditRefuseTime;
    }

    public Date getMaterialSignTime() {
        return materialSignTime;
    }

    public void setMaterialSignTime(Date materialSignTime) {
        this.materialSignTime = materialSignTime;
    }

    public Date getStartChangeTime() {
        return startChangeTime;
    }

    public void setStartChangeTime(Date startChangeTime) {
        this.startChangeTime = startChangeTime;
    }

    public Date getChangeSubmitAuditTime() {
        return changeSubmitAuditTime;
    }

    public void setChangeSubmitAuditTime(Date changeSubmitAuditTime) {
        this.changeSubmitAuditTime = changeSubmitAuditTime;
    }

    public Date getChangeAuditPassTime() {
        return changeAuditPassTime;
    }

    public void setChangeAuditPassTime(Date changeAuditPassTime) {
        this.changeAuditPassTime = changeAuditPassTime;
    }

    public Date getChangeAuditRefuseTime() {
        return changeAuditRefuseTime;
    }

    public void setChangeAuditRefuseTime(Date changeAuditRefuseTime) {
        this.changeAuditRefuseTime = changeAuditRefuseTime;
    }
}
