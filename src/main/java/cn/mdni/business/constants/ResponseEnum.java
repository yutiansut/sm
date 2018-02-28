package cn.mdni.business.constants;

/**
 * 响应 code message 枚举
 * Created by Allen on 2017/11/9.
 */

public  enum  ResponseEnum {

    SUCCESS("成功","1"),
    DEFEAT("失败","0"),
    PARAM_LACK("参数缺失","401"),
    PARAM_ERROR("请求参数错误","402"),
    SIGN_ERROR("签名出错","406"),
    ERROR("系统异常","500");



    // 成员变量
    private String message;
    private String code;

    // 构造方法
    private ResponseEnum(String message,String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
