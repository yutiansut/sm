package cn.mdni.business.dao.material;

import cn.mdni.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你智装 SKU 价格dao</dd>
 * <dd>@date：2017/11/2  16:27</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface ProdSkuPriceDao extends CrudDao<ProdSkuPrice> {

    /**
     * @author Ryze
     * @date 2017/11/3 17:34
     * @description 根据sku id 和价格类型获取所有价格
     * @param   params skuId sku的id priceType 价格类型
     * @return
     */
    List<ProdSkuPrice> findByTypeAndSkuId(Map<String, Object> params);
}