package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectChangeMaterialDao extends CrudUUIDDao<ProjectChangeMaterial> {

    List<ProjectChangeMaterial> getAuditList(@Param("categoryUrl") String categoryUrl, @Param("contractCode") String contractCode);

    /**
     * 通过条件  查询所有:项目主材sku及其下面的所有sku用量信息
     * @return
     */
    List<ProjectChangeMaterial> findWithSubListByMaterialParams(Map<String, Object> materialParams);

    /**
     * 根据类目查询本次变更单的数据
     * @param changeCategoryUrl
     * @return
     */
    List<ProjectChangeMaterial> getBychangeCategoryUrl(@Param("changeCategoryUrl") String changeCategoryUrl,@Param("contractCode") String contractCode);

    /**
     * 更改主材的使用状态
     * @param contractCode
     * @param changeCategoryUrl
     * @param status
     */
    void updateMaterialStatus(@Param("contractCode")String contractCode, @Param("changeCategoryUrl")String changeCategoryUrl, @Param("status") String status);

    void delete(@Param("contractCode")String contractCode);
    List<ProjectChangeMaterial> getBychangeCategoryUrl(String changeCategoryUrl);

    /**
     * @Description: 大美智装 通过订单编号 获取列表
     * @date: 2017/11/15  15:17
     * @param contractCode 编号
     *
     */
    List<ProjectChangeMaterial> getListByCode(String contractCode);
    void batchInsert(List<ProjectChangeMaterial> list);

    List<Map<String,Object>> getPackagestandardByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getUpMaterialByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getAddItemByContractCode(@Param("contractCode")String contractCode,@Param("addItemType")String addItemType);

    List<Map<String,Object>> getProIntemByContractCode(@Param("contractCode") String contractCode,
                                                       @Param("categoryCode") String categoryCode,
                                                       @Param("categoryDetailCode") String categoryDetailCode);

    List<Map<String,Object>> getReduceitemByContractCode(@Param("contractCode")String contractCode,@Param("reduceItemType")String reduceItem);

    List<Map<String,Object>> getOthercateFeesByContractCode(@Param("contractCode")String contractCode);

    List<Map<String,Object>> getDismantlebaseinstallquotaByContractCode(@Param("contractCode")String contractCode,@Param("oldHouseType")String oldHouseType);

    List<ProjectChangeMaterial> getByContractCode(String contractCode);
    /**
     * @Description: 大美智装 查看类目有没有复尺
     * @date: 2017/12/14  16:08
     * @param categoryUrl
     *
     */
    Integer getMaterialIsCheckScale(@Param("categoryUrl") String categoryUrl);

    /**
     * 根据项目code和url查询
     * @param changeCategoryUrl
     * @param contractCode
     * @return
     */

    List<ProjectChangeMaterial> getMaterial(@Param("contractCode") String contractCode,@Param("changeCategoryUrl") String changeCategoryUrl);


    void deleteByIds(List<String> ids);

    void deleteByConAndUrl(@Param("contractCode") String contractCode, @Param("changeCategoryUrl") String changeCategoryUrl);
}
