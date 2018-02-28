package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.UUIDEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材用量</dd>
 * <dd>@date：2017/11/6  15:10</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Document(collection = "sm_sku_dosage")
public class SmSkuDosage extends UUIDEntity {

    /**
     * 项目主材id
     */
    private String projectMaterialId;

    /**
     * 功能区名称
     */
    private String domainName;

    /**
     * sku门店采购价
     */
    private BigDecimal storePurchasePrice;

    /**
     * sku门店销售价
     */
    private BigDecimal storeSalePrice;
    /**
     * sku升级项价
     */
    private BigDecimal storeUpgradeDifferencePrice;
    /**
     * sku增项价
     */
    private BigDecimal storeIncreasePrice;
    /**
     * sku减项价
     */
    private BigDecimal storeReducePrice;

    /**
     * 预算用量 85 m2/850 片
     */
    private BigDecimal budgetDosage;

    /**
     * ㎡"转为"片
     */
    private String convertUnit;

    /**
     * 损耗系数
     */
    private BigDecimal lossFactor;

    /**
     * 850
     */
    private BigDecimal noLossDosage;

    /**
     * 含损耗用量
     */
    private BigDecimal lossDosage;


    /**
     * 工程占比
     */
    private BigDecimal projectProportion;

    /**
     * 单价/基装增项总价占比/工程总价占比
     */
    private String dosagePricingMode;

    /**
     * 备注
     */
    private String dosageRemark;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date updateTime;
    /**
     * skuCode
     */
    private String skuCode;
    /**
     * 价格起效日期
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date date;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格类型
     */
    private String PriceType;
    /**
     * 选材类型（套餐标配、增项、减项）
     */
    private String categoryCode;
    /**
     * 主材，基装增项
     */
    private String categoryDetailCode;

    /**
     * select / change 选材、变更
     */
    private String pageType;
    /**
     * 项目编号
     */
    private String contractCode;
    /**
     * 变更版本号
     */
    private String changeVersionNo;
    /**
     * mongo存入的时候原始选材用量的标识
     */
    private String skuMaterialFlag;
    private String changeNo;

    private String changeFlag;

    /**
     * 原预算用量--临时代替子类中属性,封装在此处
     */
    private BigDecimal originalDosage;

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDetailCode() {
        return categoryDetailCode;
    }

    public void setCategoryDetailCode(String categoryDetailCode) {
        this.categoryDetailCode = categoryDetailCode;
    }

    public BigDecimal getStoreUpgradeDifferencePrice() {
        return storeUpgradeDifferencePrice;
    }

    public void setStoreUpgradeDifferencePrice(BigDecimal storeUpgradeDifferencePrice) {
        this.storeUpgradeDifferencePrice = storeUpgradeDifferencePrice;
    }

    public BigDecimal getStoreIncreasePrice() {
        return storeIncreasePrice;
    }

    public void setStoreIncreasePrice(BigDecimal storeIncreasePrice) {
        this.storeIncreasePrice = storeIncreasePrice;
    }

    public BigDecimal getStoreReducePrice() {
        return storeReducePrice;
    }

    public void setStoreReducePrice(BigDecimal storeReducePrice) {
        this.storeReducePrice = storeReducePrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceType() {
        return PriceType;
    }

    public void setPriceType(String priceType) {
        PriceType = priceType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getProjectMaterialId() {
        return projectMaterialId;
    }

    public void setProjectMaterialId(String projectMaterialId) {
        this.projectMaterialId = projectMaterialId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public BigDecimal getStorePurchasePrice() {
        return storePurchasePrice;
    }

    public void setStorePurchasePrice(BigDecimal storePurchasePrice) {
        this.storePurchasePrice = storePurchasePrice;
    }

    public BigDecimal getStoreSalePrice() {
        return storeSalePrice;
    }

    public void setStoreSalePrice(BigDecimal storeSalePrice) {
        this.storeSalePrice = storeSalePrice;
    }

    public BigDecimal getBudgetDosage() {
        return budgetDosage;
    }

    public void setBudgetDosage(BigDecimal budgetDosage) {
        this.budgetDosage = budgetDosage;
    }

    public String getConvertUnit() {
        return convertUnit;
    }

    public void setConvertUnit(String convertUnit) {
        this.convertUnit = convertUnit;
    }

    public BigDecimal getLossFactor() {
        return lossFactor;
    }

    public void setLossFactor(BigDecimal lossFactor) {
        this.lossFactor = lossFactor;
    }

    public BigDecimal getNoLossDosage() {
        return noLossDosage;
    }

    public void setNoLossDosage(BigDecimal noLossDosage) {
        this.noLossDosage = noLossDosage;
    }

    public BigDecimal getLossDosage() {
        return lossDosage;
    }

    public void setLossDosage(BigDecimal lossDosage) {
        this.lossDosage = lossDosage;
    }

    public BigDecimal getProjectProportion() {
        return projectProportion;
    }

    public void setProjectProportion(BigDecimal projectProportion) {
        this.projectProportion = projectProportion;
    }

    public String getDosagePricingMode() {
        return dosagePricingMode;
    }

    public void setDosagePricingMode(String dosagePricingMode) {
        this.dosagePricingMode = dosagePricingMode;
    }

    public String getDosageRemark() {
        return dosageRemark;
    }

    public void setDosageRemark(String dosageRemark) {
        this.dosageRemark = dosageRemark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getChangeVersionNo() {
        return changeVersionNo;
    }

    public void setChangeVersionNo(String changeVersionNo) {
        this.changeVersionNo = changeVersionNo;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getSkuMaterialFlag() {
        return skuMaterialFlag;
    }

    public void setSkuMaterialFlag(String skuMaterialFlag) {
        this.skuMaterialFlag = skuMaterialFlag;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public BigDecimal getOriginalDosage() {
        return originalDosage;
    }

    public void setOriginalDosage(BigDecimal originalDosage) {
        this.originalDosage = originalDosage;
    }
}