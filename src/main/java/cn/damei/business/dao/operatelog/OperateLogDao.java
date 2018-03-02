package cn.damei.business.dao.operatelog;

import cn.damei.business.entity.operatelog.OperateLog;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OperateLogDao extends CrudDao<OperateLog>{

    /**
     * 根据日志条件进行查询，比如系统类型、操作类型、项目编码
     * @param map
     * @return
     */
    List<OperateLog> findByConditions(Map<String,Object> map);
}
