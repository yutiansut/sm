package cn.damei.business.dao.material;

import cn.damei.business.entity.material.SmSkuChangeDosage;
import cn.damei.business.entity.material.SmSkuDosage;
import cn.damei.core.base.dao.CrudUUIDDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface SmSkuDosageDao extends CrudUUIDDao<SmSkuDosage> {
    List<Map<String,String>> findDomainList(String catalogUrl);
    List<Map<String,String>> findConvertUnitList(String catalogUrl);
    Double getLossFactor(String catalogUrl);

    BigDecimal getPriceBySkuCode(Map<String,Object> params);

    int deleteByMaterialId(String projectMaterialId);
    String getFlg(String catalogUrl);
    List<SmSkuDosage>findDosageByContractCodeList(String contractCode);

    Map<String,BigDecimal> getContractAreaAndPrice(String contractCode);

    void insertBySmSkuChangeDosage(List<SmSkuChangeDosage> smSkuChangeDosageList);

    void deleteByContractCode(@Param("contractCode") String contractCode);
    List<SmSkuDosage> getByContractCode(String contractCode);

    List<SmSkuDosage> getByConCodeAndChUrl(String contractCode, String changeCategoryUrl);

    List<SmSkuDosage> findByConAndUrl(@Param("contractCode")String contractCode,@Param("changeCategoryUrl") String changeCategoryUrl);
}
