package cn.damei.business.constants;

public enum OpreatModeEnum {

    SYSTEM("系统"),
    MANUAL("手动");

    private String label;

    OpreatModeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
