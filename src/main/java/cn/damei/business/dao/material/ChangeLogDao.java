package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ChangeLog;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeLogDao extends CrudDao<ChangeLog> {
    String getChangeVersionTwo(String contractCode);

    /**
     * 根据变更版本号查询变更详情
     * @param changeVersionNo
     * @return
     */
    List<ChangeLog> findChangeLogByChNo(@Param("changeVersionNo") String changeVersionNo);
    /**
     * 根据变更版本号查询变更单
     * @param changeVersionNo
     */
    ChangeLog getChangeLogByChNo(@Param("changeVersionNo") String changeVersionNo);
    List<ChangeLog> findChangeVersionNoByContractCode(@Param("contractCode") String contractCode);
}
