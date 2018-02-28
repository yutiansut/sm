package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你智装  选材用量</dd>
 * <dd>@date：2017/11/6  15:14</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface SmSkuDosageDao extends CrudUUIDDao<SmSkuDosage> {
    /**
     * 根据1级分类获取功能区列表
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:37
     */
    List<Map<String,String>> findDomainList(String catalogUrl);
    /**
     * 根据2级分类获取功能区列表
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:38
     */
    List<Map<String,String>> findConvertUnitList(String catalogUrl);
    /**
     * 根据2级分类获取耗损系数
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:39
     */
    Double getLossFactor(String catalogUrl);

    /**
     * 根据 skuId 获取门店采购价 和 门店销售价
     * @param  params skuId date priceType
     * @date 日期 2017-11-7 11:56:07
     */
    BigDecimal getPriceBySkuCode(Map<String,Object> params);

    /**
     *  通过 项目sku Id 删除sku用量
     * @param projectMaterialId
     */
    int deleteByMaterialId(String projectMaterialId);
    /**
     * 根据2级分类获取是否可以输入小数1
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 15点55分
     */
    String getFlg(String catalogUrl);
    /**
     * @Description: 美得你智装 根据合同编号 获取用量信息(除去套餐标配)
     * @date: 2017/11/10  16:02
     * @param contractCode 合同编号
     * @author: Ryze
     */
    List<SmSkuDosage>findDosageByContractCodeList(String contractCode);

    /**
     * 根据合同编号 获取合同计价面积 和套餐价
     * @param contractCode
     * @return
     */
    Map<String,BigDecimal> getContractAreaAndPrice(String contractCode);

    /**
     * 插入用量
     * @param smSkuChangeDosageList
     */
    void insertBySmSkuChangeDosage(List<SmSkuChangeDosage> smSkuChangeDosageList);

    void deleteByContractCode(@Param("contractCode") String contractCode);
     /** 根据项目编号查询用量
     * @param contractCode
     * @return
     */
    List<SmSkuDosage> getByContractCode(String contractCode);

    /**
     * 根据项目code和类目url查询用量
     * @param contractCode
     * @param changeCategoryUrl
     */
    List<SmSkuDosage> getByConCodeAndChUrl(String contractCode, String changeCategoryUrl);

    List<SmSkuDosage> findByConAndUrl(@Param("contractCode")String contractCode,@Param("changeCategoryUrl") String changeCategoryUrl);
}
