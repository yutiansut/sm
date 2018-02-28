package cn.mdni.business.dao.operatelog;

import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 操作日志Dao
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/14.
 */
@Repository
public interface OperateLogDao extends CrudDao<OperateLog>{

    /**
     * 根据日志条件进行查询，比如系统类型、操作类型、项目编码
     * @param map
     * @return
     */
    List<OperateLog> findByConditions(Map<String,Object> map);
}
