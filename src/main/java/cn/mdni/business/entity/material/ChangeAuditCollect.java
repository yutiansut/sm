package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.util.Date;

/**
 * 变更审计汇总
 * Created by Allen on 2018/1/8.
 */
public class ChangeAuditCollect extends IdEntity {
    /**
     * 审计员
     */
    private String auditUser;
    /**
     * 审核单数
     */
    private String auditCount;
    /**
     * 起始时间
     */
    private String startDate;
    /**
     * 截止时间
     */
    private String endDate;
    /**
     * 项目编号
     */
    private String contractCode;
    /**
     * 变更单号
     */
    private String changeNo;
    /**
     * 审计时间
     */
    private Date auditTime;

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(String auditCount) {
        this.auditCount = auditCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
}
