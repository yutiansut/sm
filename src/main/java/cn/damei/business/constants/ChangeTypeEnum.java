package cn.damei.business.constants;

public enum ChangeTypeEnum {

    BASIC("基装变更"),
    MATERIAL("主材变更");

    private String label;

    ChangeTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
