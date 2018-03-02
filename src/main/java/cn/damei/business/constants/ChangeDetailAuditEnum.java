package cn.damei.business.constants;
public enum ChangeDetailAuditEnum {
    MATERIALDEPARTMENTAUDIT("材料部审核中"),DESIGNDIRECTORINTHEAUDIT("设计总监审核中"),
    AUDITREVIEW("审计审核中"),EXAMINATIONPASSED("审核通过"),AUDITFAILED("审核未通过"),
    CHANGEORDERRECALL("变更单撤回"), SENTBACK("被打回");

    private String label;

    ChangeDetailAuditEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
