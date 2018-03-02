package cn.damei.business.dao.material;

import cn.damei.business.entity.material.SmSkuChangeDosage;
import cn.damei.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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
    List<SmSkuChangeDosage>findDosageByContractCodeList(String contractCode);

    List<SmSkuChangeDosage> getByContractCode(String contractCode);

    List<SmSkuChangeDosage> getDosage(@Param("contractCode") String contractCode,@Param("changeCategoryUrl") String changeCategoryUrl);
}
