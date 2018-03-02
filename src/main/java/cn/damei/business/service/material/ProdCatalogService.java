package cn.damei.business.service.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.dao.material.ProdCatalogDao;
import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.ProjectMaterial;
import cn.damei.business.entity.material.ProdCatalog;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.base.service.CrudService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProdCatalogService extends CrudService<ProdCatalogDao, ProdCatalog> {

    @Autowired
    private ProdCatalogDao prodCatalogDao;
    @Autowired
    private ProjectMaterialService projectMaterialService;
    //变更选材service
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;

    /**
     * 查询所有可用的 一级和其二级子分类
     * 同时: 1.去查询每个二级分类对应的 项目主材sku集合
     *        2.每个项目主材sku下面对应的 sku用量信息集合
     * @param url 商品一级分类url
     * @param contractCode  合同编码
     * @param pageType  页面类型,选材:查选材表; 变更:查临时表
     * @param categoryCode  选材类型（套餐标配、增项、减项等）枚举
     * @return
     */
    public List<ProdCatalog> findTwoStageWithProMaterialData(String url, String contractCode, String categoryCode,
                                    String pageType) {
        //通过 一级分类url 查询所有分类
        Map<String, Object> catalogParams = new HashMap<String,Object>();
        MapUtils.putNotNull(catalogParams, "url", url);
        List<ProdCatalog> catalogList = prodCatalogDao.findAll(catalogParams);

        //按条件查询 项目主材sku及sku用量
        Map<String, Object> materialParams = new HashMap<String,Object>();
        MapUtils.putNotNull(materialParams, "contractCode", contractCode);
        MapUtils.putNotNull(materialParams, "categoryCode", categoryCode);

        List<ProjectMaterial> projectMaterialList = null;
        List<ProjectChangeMaterial> ProjectChangeMaterialList = null;
        if(Constants.PAGE_TYPE_SELECT.equals(pageType)){
            //选材
            //查询该合同编码下的 所有 项目主材sku 及 其下面的所有 sku用量信息
            projectMaterialList = projectMaterialService.findWithSubListByMaterialParams(materialParams);
            for(ProdCatalog cat: catalogList){
                if(cat != null && cat.getParent() != null && cat.getParent().getId().equals(0L)){
                    //是一级分类 去查找其子分类并将 二级分类对应的项目主材sku及sku用量
                    cat.setSubCatalogList(findSbuCatalogsWithSku(cat, catalogList, projectMaterialList, ProjectMaterial.class));
                }
            }
        }else if(Constants.PAGE_TYPE_CHANGE.equals(pageType)){
            //变更 -- 查询临时表 sm_project_change_material
            //查询该合同编码下的 所有 项目主材sku 及 其下面的所有 sku用量信息
            ProjectChangeMaterialList = projectChangeMaterialService.findWithSubListByMaterialParams(materialParams);
            for(ProdCatalog cat: catalogList){
                if(cat != null && cat.getParent() != null && cat.getParent().getId().equals(0L)){
                    //是一级分类 去查找其子分类并将 二级分类对应的项目主材sku及sku用量
                    cat.setSubCatalogList(findSbuCatalogsWithSku(cat, catalogList, ProjectChangeMaterialList, ProjectChangeMaterial.class));
                }
            }
        }

        return catalogList;
    }

    /**
     * 获取 一层子分类, 并将二级分类对应的项目主材sku及sku用量
     * @param catalog
     * @param catalogList
     * @return
     */
    private <T> List<ProdCatalog> findSbuCatalogsWithSku(ProdCatalog catalog, List<ProdCatalog> catalogList,
                                                     List<T> projectMaterialList, Class clazz) {
        ArrayList<ProdCatalog> subCatalogList = new ArrayList<>(0);

        if(catalog == null || catalogList == null || catalogList.size() == 0){
            return subCatalogList;
        }
        //目标对象分类的url
        String url = catalog.getUrl();

        //二级分类对应的项目sku集合
        List<ProjectChangeMaterial> newMaterialList = null;
        for(ProdCatalog cat: catalogList){
            //判断当前url 是否包含当前url
            if(cat != null && StringUtils.isNotBlank(cat.getUrl())
                    && cat.getUrl() != url  && cat.getUrl().indexOf(url) != -1){
                newMaterialList = new ArrayList<ProjectChangeMaterial>();
                //是对应的二级分类, 去查找对应的 项目sku
                if(projectMaterialList != null && projectMaterialList.size() > 0){
                    for(T projectMaterial : projectMaterialList){
                        if( projectMaterial != null && cat.getUrl().equals(
                                ((ProjectMaterial)projectMaterial).getProductCategoryUrl()) ){
                            //是当前二级分类的 项目sku
                            if(projectMaterial.getClass().equals(clazz)){
                                //选材对象,需要转为选材变更对象
                                ProjectChangeMaterial projectChangeMaterial = new ProjectChangeMaterial();
                                try {
                                    BeanUtils.copyProperties(projectChangeMaterial, projectMaterial);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                newMaterialList.add(projectChangeMaterial);
                            }else{
                                newMaterialList.add((ProjectChangeMaterial)projectMaterial);
                            }

                        }
                    }
                }
                //添加到该二级分类中
                cat.setProjectMaterialList(newMaterialList);
                subCatalogList.add(cat);
            }
        }
        return subCatalogList;
    }

    /**
     * 查询一级分类并且含有二级分类的集合
     * @return
     */
    public List<ProdCatalog> findFirstHasChildren() {
        //查询所有
        List<ProdCatalog> catalogs = prodCatalogDao.findAll();
        List<ProdCatalog> result = new ArrayList<>();
        if(catalogs != null && catalogs.size() > 0){
            for (ProdCatalog catalog : catalogs){
                if(catalog != null && hasChildren(catalog, catalogs)){
                    //含有二级分类
                    result.add(catalog);
                }
            }
        }
        return result;
    }

    /**
     * 判断当前url是否含有二级分类
     * @param catalog
     * @return
     */
    private boolean hasChildren(ProdCatalog catalog, List<ProdCatalog> catalogs) {
        for (ProdCatalog cat : catalogs){
            //该url不等于目标url,且包含目标url,认为有子类(至少有一个)
            if(!cat.getUrl().equals(catalog.getUrl()) && cat.getUrl().contains(catalog.getUrl())){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据url查询名字
     * @param changeCategoryUrl
     */
    public String getNameByUrl(String changeCategoryUrl) {
        return this.entityDao.getNameByUrl(changeCategoryUrl);
    }
}
