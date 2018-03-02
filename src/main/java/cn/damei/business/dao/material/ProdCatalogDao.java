package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProdCatalog;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProdCatalogDao extends CrudDao<ProdCatalog> {

    /**
     * 查询所有  可带参数
     * @return
     */
    List<ProdCatalog> findAll(Map<String, Object> params);

    String getNameByUrl(String changeCategoryUrl);
}
