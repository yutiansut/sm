package cn.mdni.business.entity.finance;

import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liupengfei
 * @Description 退款记录实体类
 * @Date Created in 2017/11/27 15:00
 */
public class RefundRecord extends IdEntity{

    /**
     * 退款编号
     */
    protected String refundNo;

    /**
     * 合同单号
     */
    protected String contractCode;

    /**
     * 合同唯一uuid
     */
    protected String contractUuid;

    /**
     * 在哪个阶段发生的赔款
     */
    protected String createStageId;

    /**
     * 退款类型
     */
    protected String refundType;

    /**
     * 总应退金额
     */
    protected BigDecimal refundExpectAmount;

    /**
     * 总实退金额
     */

    private BigDecimal refundActualAmount;

    /**
     * 定金应退
     */
    protected BigDecimal refundDepositAmount;

    /**
     * 拆改费应退
     */
    protected BigDecimal refundModifyAmount;

    /**
     * 施工款应退
     */
    protected BigDecimal refundConstructAmount;

    /**
     * 扣除设计费
     */
    protected BigDecimal deductDesignAmount;

    /**
     * 扣除其他费
     */
    protected BigDecimal deductOtherAmount;

    /**
     * 退款收钱人名称
     */
    protected String refundReceiverName;

    /**
     * 退款收钱人手机号
     */
    protected String refundReceiverMobile;

    /**
     * 退款收钱人给的银行卡号
     */
    protected String refundReceiverAccount;

    /**
     * 退款操作人
     */
    protected String operator;

    /**
     * 退款操作时间
     */
    protected Date operateTime;

    /**
     * 退款原因
     */
    protected String refundReson;


    /**
     * 退款备注，如退款时所处的情况描述
     */
    protected String refundMemo;


    /**
     * 人工填的退款时间
     */
    protected String refundTime;


    /****************************辅助字段***************************/
    protected String depositRefundDetailStr;


    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
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

    public String getCreateStageId() {
        return createStageId;
    }

    public void setCreateStageId(String createStageId) {
        this.createStageId = createStageId;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public BigDecimal getRefundExpectAmount() {
        return refundExpectAmount;
    }

    public void setRefundExpectAmount(BigDecimal refundExpectAmount) {
        this.refundExpectAmount = refundExpectAmount;
    }

    public BigDecimal getRefundActualAmount() {
        return refundActualAmount;
    }

    public void setRefundActualAmount(BigDecimal refundActualAmount) {
        this.refundActualAmount = refundActualAmount;
    }

    public BigDecimal getRefundDepositAmount() {
        return refundDepositAmount;
    }

    public void setRefundDepositAmount(BigDecimal refundDepositAmount) {
        this.refundDepositAmount = refundDepositAmount;
    }

    public BigDecimal getRefundModifyAmount() {
        return refundModifyAmount;
    }

    public void setRefundModifyAmount(BigDecimal refundModifyAmount) {
        this.refundModifyAmount = refundModifyAmount;
    }

    public BigDecimal getRefundConstructAmount() {
        return refundConstructAmount;
    }

    public void setRefundConstructAmount(BigDecimal refundConstructAmount) {
        this.refundConstructAmount = refundConstructAmount;
    }

    public BigDecimal getDeductDesignAmount() {
        return deductDesignAmount;
    }

    public void setDeductDesignAmount(BigDecimal deductDesignAmount) {
        this.deductDesignAmount = deductDesignAmount;
    }

    public BigDecimal getDeductOtherAmount() {
        return deductOtherAmount;
    }

    public void setDeductOtherAmount(BigDecimal deductOtherAmount) {
        this.deductOtherAmount = deductOtherAmount;
    }

    public String getRefundReceiverName() {
        return refundReceiverName;
    }

    public void setRefundReceiverName(String refundReceiverName) {
        this.refundReceiverName = refundReceiverName;
    }

    public String getRefundReceiverMobile() {
        return refundReceiverMobile;
    }

    public void setRefundReceiverMobile(String refundReceiverMobile) {
        this.refundReceiverMobile = refundReceiverMobile;
    }

    public String getRefundReceiverAccount() {
        return refundReceiverAccount;
    }

    public void setRefundReceiverAccount(String refundReceiverAccount) {
        this.refundReceiverAccount = refundReceiverAccount;
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

    public String getRefundReson() {
        return refundReson;
    }

    public void setRefundReson(String refundReson) {
        this.refundReson = refundReson;
    }

    public String getRefundMemo() {
        return refundMemo;
    }

    public void setRefundMemo(String refundMemo) {
        this.refundMemo = refundMemo;
    }

    public String getDepositRefundDetailStr() {
        return depositRefundDetailStr;
    }

    public void setDepositRefundDetailStr(String depositRefundDetailStr) {
        this.depositRefundDetailStr = depositRefundDetailStr;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    /**
     * 退款类型枚举类
     */
    public enum RefundTypeEnum{
        COMMONREFUND("普通退款"),
        ORDERCLOSEREFUND("退单退款");

        private String label;

        RefundTypeEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
