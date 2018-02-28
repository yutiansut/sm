package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.StoreRelationMapping;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * @Description: 选材门店与产业工人门店关系映射dao
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/7 17:30.
 */
@Repository
public interface StoreRelationMappingDao extends CrudDao<StoreRelationMapping> {
    StoreRelationMapping getByMpsStoreId(Long mpsStoreId);
}
