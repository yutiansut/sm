package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProjectIntem;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
