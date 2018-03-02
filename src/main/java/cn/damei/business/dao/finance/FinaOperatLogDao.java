package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.FinaOperatLog;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FinaOperatLogDao extends CrudDao<FinaOperatLog>{

    /**
     * 查询操作记录
     * @param contractUuid
     * @return
     */
    List<Map<String,Object>> operationRecord(String contractUuid);
}
