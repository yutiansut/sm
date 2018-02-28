package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更 选材用量</dd>
 * <dd>@date：2017/11/6  15:14</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface SmSkuChangeDosageDao extends CrudUUIDDao<SmSkuChangeDosage> {
    List<SmSkuChangeDosage> getBychangeCategoryUrl(@Param("contractCode") String contractCode, @Param("changeCategoryUrl") String changeCategoryUrl);

    void deleteByContractCode(@Param("contractCode") String contractCode);

    void deleteByConAndUrl(@Param("contractCode") String contractCode,  @Param("changeCategoryUrl") String changeCategoryUrl);

    List<SmSkuChangeDosage> getByOriDosGTZero(@Param("contractCode") String contractCode,  @Param("changeCategoryUrl") String changeCategoryUrl);

    void updateBudDosByOriDos(@Param("originalDosage") BigDecimal originalDosage,@Param("id") String id);
    /**
     * 批量插入
     * @param list
     */
    void batchInsert(List<SmSkuChangeDosage> list);
    /**
     * @Description: 美得你智装 根据合同编号 获取变更 的用量信息(除去套餐标配)
     * @date: 2017/11/10  16:02
     * @param contractCode 合同编号
     * @author: Ryze
     */
    List<SmSkuChangeDosage>findDosageByContractCodeList(String contractCode);

    List<SmSkuChangeDosage> getByContractCode(String contractCode);

    List<SmSkuChangeDosage> getDosage(@Param("contractCode") String contractCode,@Param("changeCategoryUrl") String changeCategoryUrl);
}
