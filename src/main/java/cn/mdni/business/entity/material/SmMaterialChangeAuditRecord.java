package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.util.Date;

/**
 * @author 
 */
public class SmMaterialChangeAuditRecord extends IdEntity {
    /**
     * id
     */
    private Integer id;

    /**
     * 变更单id
     */
    private String changeNo;

    /**
     * 审核人
     */
    private String auditUser;

    /**
     * 审核部门
     */
    private String auditDep;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核结果
     */
    private String auditResult;

    /**
     * 审核备注
     */
    private String auditRemark;


    /**
     * 判断通过未通过
     */
    private String type;

    private String auditUserType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private static final long serialVersionUID = 1L;


    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditDep() {
        return auditDep;
    }

    public void setAuditDep(String auditDep) {
        this.auditDep = auditDep;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getAuditUserType() {
        return auditUserType;
    }

    public void setAuditUserType(String auditUserType) {
        this.auditUserType = auditUserType;
    }
}