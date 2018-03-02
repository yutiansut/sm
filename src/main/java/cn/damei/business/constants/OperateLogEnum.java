package cn.damei.business.constants;

public enum OperateLogEnum {
    /**
     * 操作类型
     */
    CREATE_TIME("项目创建"),SUPERVISOR_STAY_ASSIGNED("状态变为督导组长待分配"),DESIGN_STAY_ASSIGNED("督导组长分配设计组"),
    ASSIGN_DESIGNER("设计组长分配设计师"),APPLY_REFUND("申请退回"),SUPERVISE_RECALL("督导收回"),
    VOLUME_ROOM_COMPLETE("量房完成"),MAP_COMPLETE("出图完成"),SIGN_COMPLETE("签约完成"),
    PUSH_TO_MPS("推送到产业工人"),
    SEND_SINGLE_AGAIN_COMPLETE("重新派单完成"),PROJECT_START("开工"),PROJECT_COMPLETE("竣工"),
    SYNC_PM_SUPERVISOR("同步项目经理或监理"),MPS_REJECT_PROJECT("产业工人拒绝接单"),SYNC_CONSTRUCTION_TIME("同步开工竣工基装时间"),
    PERFECT_CUSTOMER_INFO("完善客户信息"),
    SUBMIT_AUDIT("提交审计"),ASSIGN_AUDIT("指派审计"),
    AUDIT_PASS("审计通过"),AUDIT_FAILED("审计未通过"),
    UNDER_AUDIT("审核中"),
    AUDIT_APPLY_RETRACT("审计申请撤回"),AUDIT_MANAGER_RETRACT("审计经理撤回"),
    TRANSFER_COMPLETE("转单完成"),SUBMIT_CHANGE_AUDIT("提交变更审核"),CHANGE_AUDIT_PASS("变更审核通过"),
    CHANGE_AUDIT_FAILED("变更审核未通过"),ORDER_CLOSE("订单关闭");

    private String label;

    OperateLogEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
