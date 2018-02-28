package cn.mdni.business.entity.material;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * <dl>
 * <dd>Description: 美得你智装  变更的项目主材 </dd>
 * <dd>@date：2017/11/7  11:14</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Document(collection = "sm_project_material")
public class ProjectChangeMaterial extends ProjectMaterial {
    /**
     *含损用量-原含损用量
     */
    private BigDecimal  num;
    /**
     *价格
     */
    private BigDecimal  price;
    /**
     *功能区
     */
    private String domainName;
    /**
     * 材料状态
     */
    private String materialsStatus;

    /**
     * 含损用量--不在表里
     */
    private Long lossDosage;
    /**
     * 原含损用量--不在表里
     */
    private Long originalDosage;

    public Long getOriginalDosage() {
        return originalDosage;
    }

    public void setOriginalDosage(Long originalDosage) {
        this.originalDosage = originalDosage;
    }

    public Long getLossDosage() {
        return lossDosage;
    }

    public void setLossDosage(Long lossDosage) {
        this.lossDosage = lossDosage;
    }
    /*public ProjectChangeMaterial(ProjectMaterial projectMaterial) {
        super(projectMaterial);
    }*/

    public ProjectChangeMaterial() {
    }

    public String getMaterialsStatus() {
        return materialsStatus;
    }

    public void setMaterialsStatus(String materialsStatus) {
        this.materialsStatus = materialsStatus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}