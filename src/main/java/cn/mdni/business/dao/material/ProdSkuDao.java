package cn.mdni.business.dao.material;


import cn.mdni.business.entity.commodity.prodsku.ProdSku;
import cn.mdni.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
@Repository
public interface ProdSkuDao  extends CrudDao<ProdSku> {
    /**
     * @author Ryze
     * @date   2017/11/2 17:33
     * @description  价格设置列表查询条数  Dao
     * @param params 参数
     * @return 返回条数
     */
    Long countSkuPriceTotal(Map<String, Object> params);
    /**
     * @author Ryze
     * @date 2017/11/2 17:34
     * @description 价格设置列表查询
     * @param params 参数
     * @return 返回列表
     */
    List<ProdSku> findSkuPriceList(Map<String, Object> params);
    /**
     * @author Ryze
     * @date 2017/11/2 17:34
     * @description sku列表获取了不同类型的价格查询
     * @param params 参数
     * @return 返回列表
     */
    List<ProdSku> findSkuPriceListByType(Map<String, Object> params);
    /**
     * @author Ryze
     * @date 2017/11/2 17:34
     * @description sku列表获取了不同类型的价格查询条数
     * @param params 参数
     * @return 返回列表
     */
    Long countfindSkuPriceListByTypeTotal(Map<String, Object> params);

    /**
     * @author Ryze
     * @date 2017/11/2 18:45
     * @description 所有门店下拉框
     * @return 返回列表
     */
    List<Map<String,String>> findStoreList();
    /**
     * @author Ryze
     * @date 2017/11/3 10:33
     * @description 根据门店编号获取区域供应商下拉框
     * @param   code 门店编号
     * @return 返回列表
     */
    List<Map<String,String>> findRegionSupplierByStoreCode(String code);
    /**
     * @author Ryze
     * @date 2017/11/3 10:35
     * @description 根据区域供应商id获取商品供应商下拉框
     * @param   id 区域供应商
     * @return 返回列表
     */
    List<Map<String,String>> findSupplierByRegionId(Integer id);
    /**
     * @author Ryze
     * @date 2017/11/3 11:33
     * @description  获取所有的品牌
     * @return 返回列表
     */
    List<Map<String,String>> findAllBrand();
    /**
     * @author Ryze
     * @date 2017/11/3 11:47
     * @description 获取一级类目
     * @return
     */
    List<Map<String,String>> findCatalogFirstList();
    /**
     * @author Ryze
     * @date 2017/11/3 11:47
     * @description 根据id获取下级类目
     * @return
     */
    List<Map<String,String>> findCatalogListByUrl(String url);

    List<ProdSku> findSkuListByCodeList(List<String> codeList);


    /**
     * @Description: 美得你智装 根据时间id类型 查出一个生效的价格
     * @date: 2018/1/4  11:03
     * @param id sku id
     * @param type 价格类型
     * @param date 生效日期
     * @author: Ryze
     */
    ProdSku getByIdAndType(@Param("id") Long id,@Param("type") String type, @Param("date") Date date);
    /**
     * @Description: 美得你智装 根据条件 查出一个生效的价格
     * @date: 2018/1/4  11:03
     * @param params 条件
     * @author: Ryze
     */
    List<ProdSku> findByIdAndType(Map<String, Object> params);

}