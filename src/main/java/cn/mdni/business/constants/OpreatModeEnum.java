package cn.mdni.business.constants;

/**
 * @author liupengfei
 * @Description 操作模式，人工/系统
 * @Date Created in 2017/11/17 17:41
 */
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
