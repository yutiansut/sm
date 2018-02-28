package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.FinaOperatLog;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 财务操作日志Dao类
 * @Date Created in 2017/11/19 16:02
 */
@Repository
public interface FinaOperatLogDao extends CrudDao<FinaOperatLog>{

    /**
     * 查询操作记录
     * @param contractUuid
     * @return
     */
    List<Map<String,Object>> operationRecord(String contractUuid);
}
