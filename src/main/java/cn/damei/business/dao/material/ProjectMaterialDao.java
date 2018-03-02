package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.ProjectMaterial;
import cn.damei.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectMaterialDao extends CrudUUIDDao<ProjectMaterial> {

    /**
     * 通过条件  查询所有:项目主材sku及其下面的所有sku用量信息
     * @return
     */
    List<ProjectMaterial> findWithSubListByMaterialParams(Map<String, Object> materialParams);

    /**
     * 保存定额
     * @param projectMaterial
     * @return
     */
    Long insertProjectMaterial(ProjectMaterial projectMaterial);


    List<Map<String,Object>> getPackagestandardByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getUpMaterialByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getAddItemByContractCode(@Param("contractCode")String contractCode,@Param("addItemType")String addItemType);

    List<Map<String,Object>> getReduceitemByContractCode(@Param("contractCode")String contractCode,@Param("reduceItemType")String reduceItem);

    List<Map<String,Object>> getOthercateFeesByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getDismantlebaseinstallquotaByContractCode(@Param("contractCode")String contractCode,@Param("oldHouseType")String oldHouseType);

    void delete(@Param("contractCode")String contractCode);

    void insertByprojectChangeMaterial(List<ProjectChangeMaterial> projectChangeMaterialList);
    /**
     * 根据项目code查询
     * @param contractCode
     * @return
     */
    List<ProjectMaterial> getByContractCode(@Param("contractCode")String contractCode);

    /**
     * 根据项目code和类目url查询
     * @param contractCode
     * @param changeCategoryUrl
     * @return
     */
    List<ProjectMaterial> getByConCodeAndChUrl(@Param("contractCode")String contractCode,@Param("changeCategoryUrl")String changeCategoryUrl);

    List<ProjectMaterial> findByConAndUrl(@Param("contractCode")String contractCode,@Param("changeCategoryUrl")String changeCategoryUrl);
}
