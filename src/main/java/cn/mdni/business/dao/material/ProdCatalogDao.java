package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ProdCatalog;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类dao
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/11/5 15:22.
 */
@Repository
public interface ProdCatalogDao extends CrudDao<ProdCatalog> {

    /**
     * 查询所有  可带参数
     * @return
     */
    List<ProdCatalog> findAll(Map<String, Object> params);

    String getNameByUrl(String changeCategoryUrl);
}
