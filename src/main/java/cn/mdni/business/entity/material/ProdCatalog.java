package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.util.List;

/**
 * @Description: 商品分类实体
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/11/5 15:18.
 */
public class ProdCatalog extends IdEntity {

    /**分类名称 */
    private String name;

    /**分类路径*/
    private String url;

    /**父分类*/
    private ProdCatalog parent;

    /**当前的子分类集合*/
    private List<ProdCatalog> subCatalogList;

    /**当前二级分类 对应的 项目主材sku集合*/
    private List<ProjectChangeMaterial> projectMaterialList;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProdCatalog getParent() {
        return parent;
    }

    public void setParent(ProdCatalog parent) {
        this.parent = parent;
    }

    public List<ProdCatalog> getSubCatalogList() {
        return subCatalogList;
    }

    public void setSubCatalogList(List<ProdCatalog> subCatalogList) {
        this.subCatalogList = subCatalogList;
    }

    public List<ProjectChangeMaterial> getProjectMaterialList() {
        return projectMaterialList;
    }

    public void setProjectMaterialList(List<ProjectChangeMaterial> projectMaterialList) {
        this.projectMaterialList = projectMaterialList;
    }
}
