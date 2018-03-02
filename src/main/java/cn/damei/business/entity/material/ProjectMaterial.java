package cn.damei.business.entity.material;

import cn.mdni.commons.date.DateUtils;
import cn.damei.core.base.entity.UUIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document(collection = "sm_project_material")
public class ProjectMaterial extends UUIDEntity {
    /**
     * mongo专用mysql数据库id
     */
    private String dbId;

    /**
     * 商品类目url
     */
    private String productCategoryUrl;

    /**
     * 项目编码
     */
    private String contractCode;

    /**
     * 选材类型（套餐标配、增项、减项）
     */
    private String categoryCode;

    /**
     * 主材，基装增项
     */
    private String categoryDetailCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 设计备注
     */
    private String designRemark;

    /**
     * 审计备注
     */
    private String auditRemark;

    /**
     * 规格
     */
    private String skuSqec;

    /**
     * 型号
     */
    private String skuModel;

    /**
     * 商品名称
     */
    private String productName;
    /**
     * 单位名称
     */
    private String materialUnit;

    /**
     * sku销售价
     */
    private BigDecimal storeSalePrice;

    /**
     * sku商品图片
     */
    private String skuImagePath;

    /**
     * 定额描述:
     * 1、12厚红砖墙拆除，含垃圾装袋，从业主室内运到物业指定建筑垃圾堆放处(地面100m内,超过贴补300元/户)，
     * 不含外运，如需外运，费用另计2、若无电梯，清运费另计
     */
    private String quotaDescribe;

    /**
     * 是否复尺
     */
    private Long checkScale;

    /**
     * 颜色：白色
     */
    private String attribute1;

    /**
     * 属性值2
     */
    private String attribute2;

    /**
     * 属性值3
     */
    private String attribute3;

    /**
     * 品牌名称
     */
    private String brand;

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商code
     */
    private String supplierCode;
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
     * sku用量集合
     */
    private List<SmSkuDosage> skuDosageList;

    private List<SmSkuChangeDosage> skuChangeDosageList;

    private String cataLogName;

    /**
     * 变更使用的用量
     */
    private BigDecimal nowLossDosageSum;
    private BigDecimal oldLossDosageSum;

    /**
     * 变更适用的合计
     */
    private BigDecimal nowPriceTotal;
    private BigDecimal oldPriceTotal;

    private String markedWords;//变更单详情左侧提示语



    /**
     * select / change 选材、变更
     */
    private String pageType;
    /**
     * 变更版本号
     */
    private String changeVersionNo;
    /**
     * mongo存入的时候原始选材的标识
     */
    private String materialFlag;

    private String changeNo;


    private String changeFlag;


    public ProjectMaterial() {
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getCataLogName() {
        return cataLogName;
    }

    public void setCataLogName(String cataLogName) {
        this.cataLogName = cataLogName;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getProductCategoryUrl() {
        return productCategoryUrl;
    }

    public void setProductCategoryUrl(String productCategoryUrl) {
        this.productCategoryUrl = productCategoryUrl;
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getDesignRemark() {
        return designRemark;
    }

    public void setDesignRemark(String designRemark) {
        this.designRemark = designRemark;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getSkuSqec() {
        return skuSqec;
    }

    public void setSkuSqec(String skuSqec) {
        this.skuSqec = skuSqec;
    }

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuotaDescribe() {
        return quotaDescribe;
    }

    public void setQuotaDescribe(String quotaDescribe) {
        this.quotaDescribe = quotaDescribe;
    }

    public Long getCheckScale() {
        return checkScale;
    }

    public void setCheckScale(Long checkScale) {
        this.checkScale = checkScale;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
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

    public List<SmSkuDosage> getSkuDosageList() {
        return skuDosageList;
    }

    public void setSkuDosageList(List<SmSkuDosage> skuDosageList) {
        this.skuDosageList = skuDosageList;
    }

    public String getSkuImagePath() {
        return skuImagePath;
    }

    public void setSkuImagePath(String skuImagePath) {
        this.skuImagePath = skuImagePath;
    }

    public BigDecimal getStoreSalePrice() {
        return storeSalePrice;
    }

    public void setStoreSalePrice(BigDecimal storeSalePrice) {
        this.storeSalePrice = storeSalePrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getChangeVersionNo() {
        return changeVersionNo;
    }

    public void setChangeVersionNo(String changeVersionNo) {
        this.changeVersionNo = changeVersionNo;
    }

    public BigDecimal getNowLossDosageSum() {
        return nowLossDosageSum;
    }

    public void setNowLossDosageSum(BigDecimal nowLossDosageSum) {
        this.nowLossDosageSum = nowLossDosageSum;
    }

    public BigDecimal getOldLossDosageSum() {
        return oldLossDosageSum;
    }

    public void setOldLossDosageSum(BigDecimal oldLossDosageSum) {
        this.oldLossDosageSum = oldLossDosageSum;
    }

    public BigDecimal getNowPriceTotal() {
        return nowPriceTotal;
    }

    public void setNowPriceTotal(BigDecimal nowPriceTotal) {
        this.nowPriceTotal = nowPriceTotal;
    }

    public BigDecimal getOldPriceTotal() {
        return oldPriceTotal;
    }

    public void setOldPriceTotal(BigDecimal oldPriceTotal) {
        this.oldPriceTotal = oldPriceTotal;
    }

    public List<SmSkuChangeDosage> getSkuChangeDosageList() {
        return skuChangeDosageList;
    }

    public void setSkuChangeDosageList(List<SmSkuChangeDosage> skuChangeDosageList) {
        this.skuChangeDosageList = skuChangeDosageList;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getMarkedWords() {
        return markedWords;
    }

    public void setMarkedWords(String markedWords) {
        this.markedWords = markedWords;
    }

    public String getMaterialFlag() {
        return materialFlag;
    }

    public void setMaterialFlag(String materialFlag) {
        this.materialFlag = materialFlag;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
}