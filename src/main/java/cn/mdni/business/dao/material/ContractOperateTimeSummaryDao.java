package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ContractOperateTimeSummary;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * <dl>
 * <dd>Description: 美得你智装 操作时间汇总dao</dd>
 * <dd>@date：2017/12/19</dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
@Repository
public interface ContractOperateTimeSummaryDao extends CrudDao<ContractOperateTimeSummary> {
    /**
     * 根据合同编号查询
     * @param contractCode 合同编号
     * @return 操作时间汇总
     */
    ContractOperateTimeSummary getByContractCode(String contractCode);
}
