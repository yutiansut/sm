package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectchangeMoney extends IdEntity{

    /**
     * 变更号
     */
    private String changeNo;


    /**
     * 项目单号
     */
    private String contractCode;


    /**
     * 项目uuid
     */
    private String contractUuid;


    /**
     * 变更金额
     */
    private BigDecimal changeAmount;


    /**
     * 变更创建模式，人工录入还是系统接口调用
     */
    private String changeMode;


    /**
     * 变更创建时的id
     */
    private String createStageId;


    /**
     * 创建人
     */
    private String creator;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 影响的阶段id
     */
    private String effectStageId;


    /**
     * 影响的阶段名称
     */
    private String effectStageName;


    /**
     * 是否清算
     */
    private Boolean cleared;


    /**
     * 清算所在阶段的id
     */
    private String clearStageId;


    /**
     * 清算所在阶段的名称
     */
    private String clearStageName;

    /**
     * 清算时间
     */
    private Date clearedTime;


    /**
     * 打印次数
     */
    private Integer printCount;


    /**
     * 变更状态
     */
    private String changeStatus;

    /**
     * 变更状态
     */
    private String changeType;


    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getChangeMode() {
        return changeMode;
    }

    public void setChangeMode(String changeMode) {
        this.changeMode = changeMode;
    }

    public String getCreateStageId() {
        return createStageId;
    }

    public void setCreateStageId(String createStageId) {
        this.createStageId = createStageId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEffectStageId() {
        return effectStageId;
    }

    public void setEffectStageId(String effectStageId) {
        this.effectStageId = effectStageId;
    }

    public String getEffectStageName() {
        return effectStageName;
    }

    public void setEffectStageName(String effectStageName) {
        this.effectStageName = effectStageName;
    }

    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    public String getClearStageId() {
        return clearStageId;
    }

    public void setClearStageId(String clearStageId) {
        this.clearStageId = clearStageId;
    }

    public Date getClearedTime() {
        return clearedTime;
    }

    public void setClearedTime(Date clearedTime) {
        this.clearedTime = clearedTime;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getClearStageName() {
        return clearStageName;
    }

    public void setClearStageName(String clearStageName) {
        this.clearStageName = clearStageName;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
