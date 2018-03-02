package cn.damei.business.entity.material;


import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class Store extends IdEntity {

    /**
     * 门店名称
     */
    private String name;

    /**
     * 门店编码
     */
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}