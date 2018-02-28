package cn.mdni.business.service.operatelog;

import cn.mdni.business.dao.operatelog.RequestOutapiLogDao;
import cn.mdni.business.entity.operatelog.RequestOutapiLog;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;


/**
 * @Description: 外部接口调用日志Service
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/12/29.
 */
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
