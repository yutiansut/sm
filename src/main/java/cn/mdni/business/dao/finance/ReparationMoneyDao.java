package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.ReparationMoney;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 赔款Dao类
 * @Date Created in 2017/11/21 23:15
 */
@Repository
public interface ReparationMoneyDao extends CrudDao<ReparationMoney>{

    /**
     * 通过项目UUID查询赔款记录
     * @param
     * @return
     */
    List<Map<String,Object>> claimsRecord(String contractUuid);

    List<Map<String, String>> getFeparationAmountByContractCode(@Param("codeList") List<String> codeList);

    /**
     * 求影响指定阶段的未清算的赔款总额
     * @param stageCode
     * @return
     */
    BigDecimal sumUnClearedReparatAmount(String stageCode);


    /**
     * 批量清算影响某个阶段的赔款
     * @param reparationMoney
     */
    void batchClearReparationMoneyInStage(ReparationMoney reparationMoney);

}
