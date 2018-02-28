package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ChangeLog;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更轨迹 dao</dd>
 * <dd>@date：2017/11/7 </dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
@Repository
public interface ChangeLogDao extends CrudDao<ChangeLog> {
    /**
     * @Description: 美得你智装 根据合同编号获取最大的版本号后两位
     * @date: 2017/11/21  15:37
     * @param contractCode 合同编号
     * @author: Ryze
     */
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
    /**
     * 根据项目编号获取变更版本号
     * @param contractCode
     * @return
     * @author: Allen
     */
    List<ChangeLog> findChangeVersionNoByContractCode(@Param("contractCode") String contractCode);
}
