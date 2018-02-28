package cn.mdni.business.constants;

/**
 * @author Allen
 * @Description 变更单类型枚举
 * @Date Created in 2017/12/19 14:27
 */
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
