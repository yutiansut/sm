package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ContractOperateTimeSummary;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;
@Repository
public interface ContractOperateTimeSummaryDao extends CrudDao<ContractOperateTimeSummary> {
    /**
     * 根据合同编号查询
     * @param contractCode 合同编号
     * @return 操作时间汇总
     */
    ContractOperateTimeSummary getByContractCode(String contractCode);
}
