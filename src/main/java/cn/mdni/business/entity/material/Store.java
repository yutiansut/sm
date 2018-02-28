package cn.mdni.business.entity.material;


import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @describe 门店
 * @author Zh
 * @date 2017-11-2 11:30:09
 *
 */
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