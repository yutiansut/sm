package cn.mdni.business.dto.material;


/**
 * 变更项
 */
public class MaterialChangeItem
{
    /**
     * 变更项类型 1:增项 2:减项
     */
    private String changeType;
    /**
     * 材料类型  增项 减项 升级项 标配项
     */
    private String materialsChoiceType;
    /**
     * 商品类目
     */
    private String materialsChoiceCategoryCode;
    /**
     * 品牌
     */
    private String brand;
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
     * 用量（增加，减少）
     */
    private String changeNumber;
    /**
     * 型号
     */
    private String model;

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(String changeNumber) {
        this.changeNumber = changeNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
