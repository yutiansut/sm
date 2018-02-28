package cn.mdni.business.service.material;


import cn.mdni.business.dao.material.ProdSkuDao;
import cn.mdni.business.entity.commodity.prodsku.ProdSku;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你智装 sku dao</dd>
 * <dd>@date：2017/11/2  14:53</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class ProdSkuService extends CrudService<ProdSkuDao,ProdSku> {
    /**
     * @author Ryze
     * @date 2017/11/2 17:32
     * @description 价格设置列表查询条数
     * @param
     * @return
     */
    public Long countSkuPriceTotal(Map<String, Object> params) {
        return entityDao.countSkuPriceTotal(params);
    }
    /**
     * @author Ryze
     * @date 2017/11/2 17:33
     * @description 价格设置列表查询
     * @param
     * @return
     */
    public List<ProdSku> findSkuPriceList(Map<String, Object> params) {
        return entityDao.findSkuPriceList(params);
    }
    /**
     * @author Ryze
     * @date 2017/11/2 18:45
     * @description 所有门店下拉框
     * @return
     */
    public List<Map<String,String>> findStoreList() {
       return entityDao.findStoreList();
    }
    /**
     * @author Ryze
     * @date 2017/11/3 10:31
     * @description 根据门店编号获取区域供应商下拉框
     * @param   code 门店编号
     * @return
     */

    public List<Map<String,String>> findRegionSupplierByStoreCode(String code) {
        return entityDao.findRegionSupplierByStoreCode(code);
    }

    /**
     * @author Ryze
     * @date 2017/11/3 10:34
     * @description 根据区域供应商id获取商品供应商下拉框
     * @param   id 区域供应商
     * @return
     */
    public  List<Map<String,String>> findSupplierByRegionId(Integer id) {
        return entityDao.findSupplierByRegionId(id);
    }
    /**
     * @author Ryze
     * @date 2017/11/3 11:32
     * @description  获取所有的品牌
     * @return 返回列表
     */
    public List<Map<String,String>>  findAllBrand() {
        return entityDao.findAllBrand();
    }
    /**
     * @author Ryze
     * @date 2017/11/3 11:46
     * @description 获取一级类目
     * @return
     */
    public List<Map<String,String>> findCatalogFirstList() {
        return  entityDao.findCatalogFirstList();
    }
    /**
     * @author Ryze
     * @date 2017/11/3 11:46
     * @description 根据url获取下级类目
     * @return
     */
    public List<Map<String,String>> findCatalogListByUrl(String url) {
        return entityDao.findCatalogListByUrl(url);
    }

    /**
     * @author Ryze
     * @date 2017/11/2 17:34
     * @description sku列表获取了不同类型的价格查询
     * @param params 参数
     * @return 返回列表
     */
    public List<ProdSku> findSkuPriceListByType(Map<String, Object> params){
        return entityDao.findSkuPriceListByType(params);
    }
    /**
     * @author Ryze
     * @date 2017/11/2 17:34
     * @description sku列表获取了不同类型的价格查询条数
     * @param params 参数
     * @return 返回列表
     */
    public Long countfindSkuPriceListByTypeTotal(Map<String, Object> params){
        return entityDao.countfindSkuPriceListByTypeTotal(params);
    }


    /**
     * @Description: 美得你智装 根据条件 查出一个生效的价格
     * @date: 2018/1/4  11:03
     * @param params 条件
     * @author: Ryze
     */
    public List<ProdSku> findByIdAndType(Map<String, Object> params){
        params.put("date",new Date());
        return  entityDao.findByIdAndType(params);
    }
}