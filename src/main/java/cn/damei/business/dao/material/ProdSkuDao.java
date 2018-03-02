package cn.damei.business.dao.material;


import cn.damei.business.entity.commodity.prodsku.ProdSku;
import cn.damei.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ProdSkuDao  extends CrudDao<ProdSku> {
    Long countSkuPriceTotal(Map<String, Object> params);
    List<ProdSku> findSkuPriceList(Map<String, Object> params);
    List<ProdSku> findSkuPriceListByType(Map<String, Object> params);
    Long countfindSkuPriceListByTypeTotal(Map<String, Object> params);
    List<Map<String,String>> findStoreList();
    List<Map<String,String>> findRegionSupplierByStoreCode(String code);
    List<Map<String,String>> findSupplierByRegionId(Integer id);
    List<Map<String,String>> findAllBrand();
    List<Map<String,String>> findCatalogFirstList();
    List<Map<String,String>> findCatalogListByUrl(String url);

    List<ProdSku> findSkuListByCodeList(List<String> codeList);


    ProdSku getByIdAndType(@Param("id") Long id,@Param("type") String type, @Param("date") Date date);
    List<ProdSku> findByIdAndType(Map<String, Object> params);

}