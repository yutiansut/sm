package cn.mdni.business.constants;

/**
 * @Description: 客户合同状态枚举
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
public enum  CustomerContractEnum {
    /**
     * 所有状态
     * 订单流转状态
     * 选材状态
     * 变更状态
     */
    STAY_TURN_DETERMINE("待转大定","0"),
    SUPERVISOR_STAY_ASSIGNED("督导组长待分配","1"),
    DESIGN_STAY_ASSIGNED("设计待分配","1"),
    APPLY_REFUND("申请退回","12"),
    STAY_DESIGN("待设计","3"),
    STAY_SIGN("待签约","5"),
    STAY_SEND_SINGLE_AGAIN("待重新派单","1"),
    STAY_CONSTRUCTION("待施工","6"),
    ON_CONSTRUCTION("施工中","8"),
    PROJECT_COMPLETE("竣工","9"),
    ORDER_CLOSE("订单关闭","21"),

    NOT_SELECT_MATERIAL("未选材",""),
    MATERIAL_SELECTION("选材中",""),
    ASSIGN_AUDIT("指派审计",""),
    UNDER_AUDIT("审计审核中",""),
    WAIT_TRANSFER("等待转单",""),
    PEND_AUDIT_RETRACT("待审计撤回",""),
    AUDIT_RETRACT("审计撤回",""),
    TRANSFER_COMPLETE("转单完成",""),
    NOT_PASS("审计未通过",""),

    CHANGING("变更中",""),
    CHANGE_AUDIT("变更审核中",""),
    CHANGE_AUDIT_SUCCESS("变更审核通过",""),
    CHANGE_AUDIT_NOT_PASS("变更审核未通过","");

    private String label;

    private String statusNum;//订单状态号(crm对外接口用)

    CustomerContractEnum(String label,String statusNum) {
        this.label = label;
        this.statusNum = statusNum;
    }

    public String getLabel() {
        return label;
    }

    public String getStatusNum() {
        return statusNum;
    }
}
