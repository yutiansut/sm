package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectPayplanStage extends IdEntity{

    private String stageCode;

    /**
     * 项目编号
     */
    private String contractCode;

    /**
     * 项目uuid
     */
    private String contractUuid;

    /**
     * 该规划节点的顺序值
     */
    private Double stageidx;


    /**
     * 交款规划节点模版id
     */
    private Integer stageTemplateId;


    /**
     * 交款规划阶段节点模板人工设定的编号
     */
    private String stageTemplateCode;


    /**
     * 该阶段应收
     */
    private BigDecimal expectReceived;

    /**
     * 该阶段实收
     */
    private BigDecimal actualTotalReceived;


    /**
     * 该阶段前累计未交金额
     */
    private BigDecimal agoUnpayAmount;

    /**
     * 该阶段开始时间
     */
    private Date startTime;

    /**
     * 该收款阶段是否结束
     */
    private Boolean stageFinished;

    /**
     * 该阶段结束时间
     */
    private Date finishTime;

    /**
     * 结束方式，是人工还是系统自动
     */
    private String finishMode;

    /**
     * 结束该收款阶段的人
     */
    private String finishOperator;

    /**
     * 是否是当前阶段
     */
    private Boolean curentFlag;

    /**
     * 上一个阶段的uuid
     */
    private String prevStageCode;

    /**
     * 下一个阶段的uuid
     */
    private String nextStageCode;


    /**
     *  交款阶段类型
     */
    private String stageType;

    /**
     *  交款阶段名称
     */
    private String stageName;

    /**
     * 交款规划模板编号
     */
    private String payPlanTemplateCode;


    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
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

    public Double getStageidx() {
        return stageidx;
    }

    public void setStageidx(Double stageidx) {
        this.stageidx = stageidx;
    }

    public Integer getStageTemplateId() {
        return stageTemplateId;
    }

    public void setStageTemplateId(Integer stageTemplateId) {
        this.stageTemplateId = stageTemplateId;
    }

    public BigDecimal getExpectReceived() {
        return expectReceived;
    }

    public void setExpectReceived(BigDecimal expectReceived) {
        this.expectReceived = expectReceived;
    }

    public BigDecimal getActualTotalReceived() {
        return actualTotalReceived;
    }

    public void setActualTotalReceived(BigDecimal actualTotalReceived) {
        this.actualTotalReceived = actualTotalReceived;
    }

    public BigDecimal getAgoUnpayAmount() {
        return agoUnpayAmount;
    }

    public void setAgoUnpayAmount(BigDecimal agoUnpayAmount) {
        this.agoUnpayAmount = agoUnpayAmount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getStageFinished() {
        return stageFinished;
    }

    public void setStageFinished(Boolean stageFinished) {
        this.stageFinished = stageFinished;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getFinishMode() {
        return finishMode;
    }

    public void setFinishMode(String finishMode) {
        this.finishMode = finishMode;
    }

    public String getFinishOperator() {
        return finishOperator;
    }

    public void setFinishOperator(String finishOperator) {
        this.finishOperator = finishOperator;
    }

    public Boolean getCurentFlag() {
        return curentFlag;
    }

    public void setCurentFlag(Boolean curentFlag) {
        this.curentFlag = curentFlag;
    }

    public String getPrevStageCode() {
        return prevStageCode;
    }

    public void setPrevStageCode(String prevStageCode) {
        this.prevStageCode = prevStageCode;
    }

    public String getNextStageCode() {
        return nextStageCode;
    }

    public void setNextStageCode(String nextStageCode) {
        this.nextStageCode = nextStageCode;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getPayPlanTemplateCode() {
        return payPlanTemplateCode;
    }

    public void setPayPlanTemplateCode(String payPlanTemplateCode) {
        this.payPlanTemplateCode = payPlanTemplateCode;
    }

    public String getStageTemplateCode() {
        return stageTemplateCode;
    }

    public void setStageTemplateCode(String stageTemplateCode) {
        this.stageTemplateCode = stageTemplateCode;
    }
}
