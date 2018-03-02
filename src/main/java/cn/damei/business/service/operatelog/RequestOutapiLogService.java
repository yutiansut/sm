package cn.damei.business.service.operatelog;

import cn.damei.business.dao.operatelog.RequestOutapiLogDao;
import cn.damei.business.entity.operatelog.RequestOutapiLog;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;


@Service
public class RequestOutapiLogService extends CrudService<RequestOutapiLogDao,RequestOutapiLog> {
    public  void insertRequestLog(RequestOutapiLog requestOutapiLog){
        try {
            this.entityDao.insert(requestOutapiLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
