package cn.mdni.business.entity.commodity.prodsku;

import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;

/**
 * <dl>
 * <dd>Description: 美得你智装 sku实体</dd>
 * <dd>@date：2017/11/2  14:48</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class ProdSku extends IdEntity  {

    /**
     * sku编码
     */
    private String code;

    /**
     * sku名称
     */
    private String name;

    /**
     * 产品id
     */
    private Long productId;
    /**
     * 商品供货商
     */
    private Long supplierId;
    /**
     * 属性值
     */
    private String attribute1;

    private String attribute2;

    private String attribute3;

    /**
     * 属性名称
     */
    private String label1;

    private String label2;

    private String label3;

    /**
     *型号
     */
    private  String productModel;
    /**
     *规格
     */
    private  String productSpec;
    /**
     * 品牌
     */
    private  String productBrandName;
    /**
     * 品牌id
     */
    private  Long productBrandId;

    /**
     * 类别
     */
    private  String productCatalogName;

    /**
     * 流程状态
     */
    private String processStatus;

    /**
     * 库存数
     */
    private Long stock;
    /**
     * 价格标记有没有填写神吗价格
     */
    private String priceFlag;
    /**
     * 升级项价、增项 减项
     */
    private BigDecimal price;
    /**
     * 图片路径
     */
    private String imagePath;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 类别
     */
    private String  productCategoryUrl;

    public String getProductCategoryUrl() {
        return productCategoryUrl;
    }

    public void setProductCategoryUrl(String productCategoryUrl) {
        this.productCategoryUrl = productCategoryUrl;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public String getPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(String priceFlag) {
        this.priceFlag = priceFlag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }

    public String getProductBrandName() {
        return productBrandName;
    }

    public void setProductBrandName(String productBrandName) {
        this.productBrandName = productBrandName;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }

    public Long getProductBrandId() {
        return productBrandId;
    }

    public void setProductBrandId(Long productBrandId) {
        this.productBrandId = productBrandId;
    }
}