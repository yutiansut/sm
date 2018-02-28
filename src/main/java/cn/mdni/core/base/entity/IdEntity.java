package cn.mdni.core.base.entity;

import java.io.Serializable;

/**
 * @Description: 实体基类
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:24 2017/10/30.
 */
public class IdEntity implements Serializable {

    private static final long serialVersionUID = -2716222356509348153L;
    protected Long id;
    public static final String ID_FIELD_NAME = "id";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
