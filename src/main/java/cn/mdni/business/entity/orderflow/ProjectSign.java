package cn.mdni.business.entity.orderflow;

import cn.mdni.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 签约实体
 * @Company: 美得你智装科技有限公司
 * @Author: Chaos
 * @Date: 2017/11/17
 */
public class ProjectSign extends IdEntity {
    /**
     * 项目编号
     */
    private String contractCode;
    /**
     * 是否签约完成
     */
    private String complete;
    /**
     * 完成时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date completeTime;
    /**
     * 签约执行人
     */
    private String signExecutor;
    /**
     * 合同签订工期
     */
    private String contractSignTrem;
    /**
     * 合同签订开工时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date contractStartTime;
    /**
     * 合同签订竣工日期
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date contractCompleteTime;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date operateTime;
    /**
     * 原始合同金额
     */
    private BigDecimal contractAmount;
    /**
     * 拆改费
     */
    private BigDecimal modifyAmount;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getSignExecutor() {
        return signExecutor;
    }

    public void setSignExecutor(String signExecutor) {
        this.signExecutor = signExecutor;
    }

    public String getContractSignTrem() {
        return contractSignTrem;
    }

    public void setContractSignTrem(String contractSignTrem) {
        this.contractSignTrem = contractSignTrem;
    }

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Date contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public Date getContractCompleteTime() {
        return contractCompleteTime;
    }

    public void setContractCompleteTime(Date contractCompleteTime) {
        this.contractCompleteTime = contractCompleteTime;
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

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getModifyAmount() {
        return modifyAmount;
    }

    public void setModifyAmount(BigDecimal modifyAmount) {
        this.modifyAmount = modifyAmount;
    }
}
