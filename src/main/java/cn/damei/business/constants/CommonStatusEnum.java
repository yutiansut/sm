package cn.damei.business.constants;

import javax.sound.sampled.EnumControl;

public enum CommonStatusEnum {

    ENABLE("启用"),
    DISABLE("禁用"),
    VALID("有效"),
    INVALID("无效"),
    NORMAL("正常"),
    DELETED("已删除");

    private String label;

    CommonStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
