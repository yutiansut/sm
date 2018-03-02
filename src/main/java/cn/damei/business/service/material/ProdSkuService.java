package cn.damei.business.service.material;


import cn.damei.business.dao.material.ProdSkuDao;
import cn.damei.business.entity.commodity.prodsku.ProdSku;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProdSkuService extends CrudService<ProdSkuDao,ProdSku> {
    public Long countSkuPriceTotal(Map<String, Object> params) {
        return entityDao.countSkuPriceTotal(params);
    }
    public List<ProdSku> findSkuPriceList(Map<String, Object> params) {
        return entityDao.findSkuPriceList(params);
    }
    public List<Map<String,String>> findStoreList() {
       return entityDao.findStoreList();
    }

    public List<Map<String,String>> findRegionSupplierByStoreCode(String code) {
        return entityDao.findRegionSupplierByStoreCode(code);
    }

    public  List<Map<String,String>> findSupplierByRegionId(Integer id) {
        return entityDao.findSupplierByRegionId(id);
    }
    public List<Map<String,String>>  findAllBrand() {
        return entityDao.findAllBrand();
    }
    public List<Map<String,String>> findCatalogFirstList() {
        return  entityDao.findCatalogFirstList();
    }
    public List<Map<String,String>> findCatalogListByUrl(String url) {
        return entityDao.findCatalogListByUrl(url);
    }

    public List<ProdSku> findSkuPriceListByType(Map<String, Object> params){
        return entityDao.findSkuPriceListByType(params);
    }
    public Long countfindSkuPriceListByTypeTotal(Map<String, Object> params){
        return entityDao.countfindSkuPriceListByTypeTotal(params);
    }


    public List<ProdSku> findByIdAndType(Map<String, Object> params){
        params.put("date",new Date());
        return  entityDao.findByIdAndType(params);
    }
}