package cn.damei.business.dao.material;

import cn.damei.business.entity.OtherAddReduceAmount;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OtherAddReduceAmountDao extends CrudDao<OtherAddReduceAmount>{
    List<OtherAddReduceAmount> findByContractCodeList(Map<String,Object> map);

    List<Map<String,Object>> getOthermoneyAddOrReduceByContractCode(@Param("contractCode") String contractCode);

    List<OtherAddReduceAmount> getChangeByContractCode(@Param("contractCode") String contractCode,@Param("changeFlag")String changeFlag);

    /**
     * 查询变更标识为1的其他金额
     * @return
     */
    Long getOthermoneyAddOrReduceByChangeFlag(String contractCode);

    /**
     * 将该项目下的其他金额变更标识更新为0
     * @param contractCode
     * @param
     */
    void updateByContractCodeAndStatus(@Param("contractCode") String contractCode, @Param("changeFlag") String changeFlag);

    List<OtherAddReduceAmount> getOthrgAddReducePrice(String contractCode);

    void deleteByCodeAndFlag(@Param("contractCode")String contractCode,@Param("flag") String flag);
}
