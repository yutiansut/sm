package cn.mdni.business.constants;

import javax.sound.sampled.EnumControl;

/**
 * @author liupengfei
 * @Description 通用的状态枚举
 * @Date Created in 2017/11/17 14:27
 */
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
