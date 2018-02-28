package cn.mdni.business.entity.orderflow;


import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;

/**
 * @Description: 编号规则实体
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/17
 */
public class NumberingRule extends IdEntity {

    /**
     * 门店code
     */
    private String storeCode;

    /**
     * 编号类型 （项目编号，收据编号 等）
     */
    private String numberType;

    /**
     * 编号前缀
     */
    private String  prefix ;

    /**
     * 编号中部信息
     */
    private String midNumber;
    /**
     * 编号尾部信息
     */
    private Integer tailNumber;
    /**
     * 编号尾部 位数格式化类型
     */
    private String tailFormatType;


    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMidNumber() {
        return midNumber;
    }

    public void setMidNumber(String midNumber) {
        this.midNumber = midNumber;
    }

    public Integer getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(Integer tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getTailFormatType() {
        return tailFormatType;
    }

    public void setTailFormatType(String tailFormatType) {
        this.tailFormatType = tailFormatType;
    }
}
