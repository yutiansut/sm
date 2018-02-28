package cn.mdni.business.dao.operatelog;

import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.business.entity.operatelog.RequestOutapiLog;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 调用外部接口日志Dao
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/12/29.
 */
@Repository
public interface RequestOutapiLogDao extends CrudDao<RequestOutapiLog>{

}
