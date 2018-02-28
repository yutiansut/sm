package cn.mdni.business.entity.material;

import cn.mdni.business.constants.ChangeLogEnum;
import cn.mdni.core.base.entity.IdEntity;

import java.util.Date;

/**
 * @Description: 操作轨迹
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/07.
 */
public class ChangeLog extends IdEntity {
    /**
     * 项目id
     */
    private String contractCode;
    /**
     * 变更版本号
     */
    private String changeVersionNo;
    /**
     * 变更状态
     */
    private ChangeLogEnum changeStatus;
    /**
     * 操作人
     */
    private String operatUser;
    /**
     * 操作时间
     */
    private Date operatTime;

    private String currentStatus;

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getChangeVersionNo() {
        return changeVersionNo;
    }

    public void setChangeVersionNo(String changeVersionNo) {
        this.changeVersionNo = changeVersionNo;
    }

    public String getOperatUser() {
        return operatUser;
    }

    public void setOperatUser(String operatUser) {
        this.operatUser = operatUser;
    }

    public Date getOperatTime() {
        return operatTime;
    }

    public void setOperatTime(Date operatTime) {
        this.operatTime = operatTime;
    }

    public ChangeLogEnum getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(ChangeLogEnum changeStatus) {
        this.changeStatus = changeStatus;
    }
}
