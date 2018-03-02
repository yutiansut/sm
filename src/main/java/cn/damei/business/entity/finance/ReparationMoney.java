package cn.damei.business.entity.finance;

import cn.mdni.commons.date.DateUtils;
import cn.damei.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class ReparationMoney extends IdEntity{

    /**
     * 退款号
     */
    private String reparationNo;

    /**
     * 项目单号
     */
    private String contractCode;

    /**
     * 项目唯一uuid
     */
    private String contractUuid;

    /**
     * 赔款金额
     */
    private BigDecimal reparationAmount;


    /**
     * 赔款模式
     */
    private String reparationMode;


     /**
     * 赔款创建的阶段
     */
    private String createStageId;


    /**
     * 创建人
     */
    private String creator;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;


    /**
     * 影响的收款阶段
     */
    private String effectStageId;


    /**
     * 影响的阶段名称
     */
    private String effectStageName;


    /**
     * 赔款是否清算
     */
    private Boolean cleared;

    /**
     * 清算时所在的阶段
     */
    private String clearStageId;


    /**
     * 清算所在阶段的名称
     */
    private String clearStageName;


    /**
     * 清算时间
     */
    private Date clearTime;

    /**
     * 退款原因
     */
    private String reparationReason;

    /**
     * 赔款状态
     */
    private String reparationStatus;


    public String getReparationNo() {
        return reparationNo;
    }

    public void setReparationNo(String reparationNo) {
        this.reparationNo = reparationNo;
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

    public BigDecimal getReparationAmount() {
        return reparationAmount;
    }

    public void setReparationAmount(BigDecimal reparationAmount) {
        this.reparationAmount = reparationAmount;
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

    public Date getClearTime() {
        return clearTime;
    }

    public void setClearTime(Date clearTime) {
        this.clearTime = clearTime;
    }

    public String getReparationReason() {
        return reparationReason;
    }

    public void setReparationReason(String reparationReason) {
        this.reparationReason = reparationReason;
    }

    public String getReparationStatus() {
        return reparationStatus;
    }

    public void setReparationStatus(String reparationStatus) {
        this.reparationStatus = reparationStatus;
    }

    public String getClearStageName() {
        return clearStageName;
    }

    public void setClearStageName(String clearStageName) {
        this.clearStageName = clearStageName;
    }

    public String getReparationMode() {
        return reparationMode;
    }

    public void setReparationMode(String reparationMode) {
        this.reparationMode = reparationMode;
    }
}
