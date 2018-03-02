package cn.damei.business.dao.material;

import cn.damei.business.entity.material.StoreRelationMapping;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRelationMappingDao extends CrudDao<StoreRelationMapping> {
    StoreRelationMapping getByMpsStoreId(Long mpsStoreId);
}
