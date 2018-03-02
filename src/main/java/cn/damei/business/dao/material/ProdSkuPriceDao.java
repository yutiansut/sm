package cn.damei.business.dao.material;

import cn.damei.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProdSkuPriceDao extends CrudDao<ProdSkuPrice> {

    /**
     *
     * @date 2017/11/3 17:34
     * @description 根据sku id 和价格类型获取所有价格
     * @param   params skuId sku的id priceType 价格类型
     * @return
     */
    List<ProdSkuPrice> findByTypeAndSkuId(Map<String, Object> params);
}