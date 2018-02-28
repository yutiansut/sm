package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: 施工项价格实体
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 14:30.
 */
public class ProjectIntemPrice extends IdEntity {
    /**
     * 门店id  产业工人的门店id
     */
    private Integer storeId;

    /**
     * 门店code
     */
    private String storeCode;

    /**
     * 施工项id
     */
    private Integer projectIntemId;

    /**
     * 价格
     */
    private BigDecimal projectIntemPrice;

    /**
     * 版本号
     */
    private Integer projectIntemVersion;

    /**
     * 生效日期
     */
    private Date effectDate;

    /**
     * 价格备注
     */
    private String projectIntemPriceRemarks;

    /**
     * 成本单价/成本占比
     */
    private BigDecimal projectIntemCostPrice;

    /**
     * 签名key
     */
    private String  key;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Integer getProjectIntemId() {
        return projectIntemId;
    }

    public void setProjectIntemId(Integer projectIntemId) {
        this.projectIntemId = projectIntemId;
    }

    public BigDecimal getProjectIntemPrice() {
        return projectIntemPrice;
    }

    public void setProjectIntemPrice(BigDecimal projectIntemPrice) {
        this.projectIntemPrice = projectIntemPrice;
    }

    public Integer getProjectIntemVersion() {
        return projectIntemVersion;
    }

    public void setProjectIntemVersion(Integer projectIntemVersion) {
        this.projectIntemVersion = projectIntemVersion;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getProjectIntemPriceRemarks() {
        return projectIntemPriceRemarks;
    }

    public void setProjectIntemPriceRemarks(String projectIntemPriceRemarks) {
        this.projectIntemPriceRemarks = projectIntemPriceRemarks;
    }

    public BigDecimal getProjectIntemCostPrice() {
        return projectIntemCostPrice;
    }

    public void setProjectIntemCostPrice(BigDecimal projectIntemCostPrice) {
        this.projectIntemCostPrice = projectIntemCostPrice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
