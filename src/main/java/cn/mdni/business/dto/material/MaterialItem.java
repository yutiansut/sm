package cn.mdni.business.dto.material;

public  class MaterialItem{
    /**
     * 材料类型  增项 减项 升级项 标配项
     */
    private String materialsChoiceType;
    /**
     * 商品二级类目Code
     */
    private String materialsChoiceCategoryCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 单位
     */
    private String unit;
    /**
     * 规格
     */
    private String spec;
    /**
     * 位置
     */
    private String position;
    /**
     * 预算用量 (如果是平米转片的传片数)
     */
    private String budgetNumber;
    /**
     * 预算用量 (如果是平米转片的传面积 否则给空字符串)
     */
    private String area;
    /**
     * 损耗系数
     */
    private String lossRatio;
    /**
     * 含损耗数量 (如果是平米转片的传片数)
     */
    private String includeLossNumber;

    public String getMaterialsChoiceType() {
        return materialsChoiceType;
    }

    public void setMaterialsChoiceType(String materialsChoiceType) {
        this.materialsChoiceType = materialsChoiceType;
    }

    public String getMaterialsChoiceCategoryCode() {
        return materialsChoiceCategoryCode;
    }

    public void setMaterialsChoiceCategoryCode(String materialsChoiceCategoryCode) {
        this.materialsChoiceCategoryCode = materialsChoiceCategoryCode;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBudgetNumber() {
        return budgetNumber;
    }

    public void setBudgetNumber(String budgetNumber) {
        this.budgetNumber = budgetNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLossRatio() {
        return lossRatio;
    }

    public void setLossRatio(String lossRatio) {
        this.lossRatio = lossRatio;
    }

    public String getIncludeLossNumber() {
        return includeLossNumber;
    }

    public void setIncludeLossNumber(String includeLossNumber) {
        this.includeLossNumber = includeLossNumber;
    }
}
