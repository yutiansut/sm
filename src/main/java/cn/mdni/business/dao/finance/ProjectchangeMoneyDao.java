package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.ProjectPayplanStage;
import cn.mdni.business.entity.finance.ProjectchangeMoney;
import cn.mdni.business.service.finance.ProjectChangeMoneyService;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 变更Dao类
 * @Date Created in 2017/11/21 23:15
 */
@Repository
public interface ProjectchangeMoneyDao extends CrudDao<ProjectchangeMoney> {

    /**
     * 通过项目UUID查询变更记录
     * @param
     * @return
     */
    List<Map<String,Object>> changeLog(String contractUuid);

    /**
     * 获取增项
     * @param codeList
     * @return
     */
    List<Map<String,String>> getAddAmountByContractCode(@Param("codeList") List<String> codeList);

    /**
     * 获取减项
     * @param codeList
     * @return
     */
    List<Map<String,String>> getFewAmountByContractCode(@Param("codeList") List<String> codeList);


    /**
     * 求影响指定阶段的未清算的变更总额
     * @param stageCode
     * @return
     */
    public BigDecimal sumUnClearedChangeAmount(String stageCode);


    /**
     * 更新影响指定阶段的
     * @param changeMoney
     */
    public void batchClearChangeMoneyInStage(ProjectchangeMoney changeMoney);
}
