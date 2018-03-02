package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.util.Date;

public class FinaOperatLog extends IdEntity{

    /**
     * 操作类型
     */
    private String operatType;


    /**
     * 项目编号
     */
    private String contractCode;


    /**
     * 项目唯一表示uuid
     */
    private String contractUuid;


    /**
     * 操作的目标对象名字
     */
    private String targetKey;


    /**
     * 操作的目标对象主键值
     */
    private String targetVal;


    /**
     * 操作描述
     */
    private String operatMsg;


    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private Date operatTime;


    public String getOperatType() {
        return operatType;
    }

    public void setOperatType(String operatType) {
        this.operatType = operatType;
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

    public String getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }

    public String getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(String targetVal) {
        this.targetVal = targetVal;
    }

    public String getOperatMsg() {
        return operatMsg;
    }

    public void setOperatMsg(String operatMsg) {
        this.operatMsg = operatMsg;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperatTime() {
        return operatTime;
    }

    public void setOperatTime(Date operatTime) {
        this.operatTime = operatTime;
    }


    /**
     * 财务操作类型
     */
    public enum FinanceOperateTypeEnum {

        PAY_DEPOSIT("交定金"),
        PAY_MODIFY("交拆改费"),
        PAY_CONSTRUCT("交工程款"),
        PAY_RCW("交款红冲"),
        PAY_RETURN("退款"),
        PAY_BACKRETURN("退单"),
        STAGE_TRANSFORM("财务阶段流转"),
        OUTER_INTERFACE_CALL("外部接口调用"),
        CREATE_CHANGE("新增变更"),
        CREATE_REPARATION("新增赔款"),
        CANCEL_REPARATION("撤销赔款"),
        ALTER_CUSTOMER_INFO("修改客户信息"),
        CANCEL_ORDER("退单退款");


        private String label;

        FinanceOperateTypeEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }


    /**
     * 财务操作目标数据表的表名
     */
    public enum FinanceTableName{
        fina_paymoney_record,
        fina_project_account,
        fina_project_payplan_stage,
        fina_projectchange_money,
        fina_refund_record,
        customer,
        fina_reparation_money;
    }
}
