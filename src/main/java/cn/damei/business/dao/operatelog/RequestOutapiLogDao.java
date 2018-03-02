package cn.damei.business.dao.operatelog;

import cn.damei.business.entity.operatelog.OperateLog;
import cn.damei.business.entity.operatelog.RequestOutapiLog;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RequestOutapiLogDao extends CrudDao<RequestOutapiLog>{

}
