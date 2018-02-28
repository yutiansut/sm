package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ProjectIntem;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 施工项信息dao
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 14:50.
 */
@Repository
public interface ProjectIntemDao extends CrudDao<ProjectIntem> {

    List<Map<String,Object>> getProIntemByContractCode(@Param("contractCode") String contractCode,
                                                          @Param("categoryCode") String categoryCode,
                                                          @Param("categoryDetailCode") String categoryDetailCode);

    /**
     * 通过门店code 查询定额,并且关联定额价格表 取出对应的一个价格
     * @param storeCode
     * @return
     */
    List<ProjectIntem> findByStoreCodeAndIsDefault(String storeCode);
}
