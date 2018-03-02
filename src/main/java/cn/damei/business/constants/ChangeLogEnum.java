package cn.damei.business.constants;

public enum ChangeLogEnum {
    /**
     * 变更状态
     */
    CHANGING("变更中"),
    CHANGE_AUDIT("变更审核中"),
    CHANGE_AUDIT_SUCCESS("变更审核通过"),
    CHANGE_AUDIT_NOT_PASS("变更审核未通过");
    private String label;

    ChangeLogEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
