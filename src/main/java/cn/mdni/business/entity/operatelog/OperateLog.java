package cn.mdni.business.entity.operatelog;

import cn.mdni.business.constants.OperateLogEnum;
import cn.mdni.core.base.entity.IdEntity;

import java.util.Date;

/**
 * @Description: 操作日志
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/14.
 */
public class OperateLog extends IdEntity{
    /**
     * 项目编号
     */
    private String contractCode;
    /**
     * 系统类型
     */
    private String systemType;
    /**
     * 操作类型(时间节点)
     */
    private OperateLogEnum operateType;
    /**
     * 操作描述
     */
    private String operateDescription;
    /**
     * 操作人
     */
    private String operator;

    /**
     * 指定人工号
     */
    private String appointOrgCode;
    /**
     * 指定人姓名
     */
    private String appointName;
    /**
     * 指定人手机号
     */
    private String appointPhone;

    /**
     * 操作时间
     */
    private Date operateTime;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public OperateLogEnum getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateLogEnum operateType) {
        this.operateType = operateType;
    }

    public String getOperateDescription() {
        return operateDescription;
    }

    public void setOperateDescription(String operateDescription) {
        this.operateDescription = operateDescription;
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

    public String getAppointOrgCode() {
        return appointOrgCode;
    }

    public void setAppointOrgCode(String appointOrgCode) {
        this.appointOrgCode = appointOrgCode;
    }

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getAppointPhone() {
        return appointPhone;
    }

    public void setAppointPhone(String appointPhone) {
        this.appointPhone = appointPhone;
    }
}
