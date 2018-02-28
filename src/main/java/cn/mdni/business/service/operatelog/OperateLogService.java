package cn.mdni.business.service.operatelog;

import cn.mdni.business.constants.OperateLogEnum;
import cn.mdni.business.dao.operatelog.OperateLogDao;
import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 操作日志Service
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/14.
 */
@Service
public class OperateLogService extends CrudService<OperateLogDao,OperateLog> {

    @Autowired OperateLogDao operateLogDao;

    /**
     * 根据日志条件进行查询，比如系统类型、操作类型、项目编码
     * @param map
     * @return
     */
    public List<OperateLog> findByConditions(Map<String,Object> map){
        return operateLogDao.findByConditions(map);
    }

    /**
     * 插入接口调用日志  本系统调用其他系统 和 其他系统调用本系统
     * @param contractCode 项目编号
     * @param systemType 系统类型
     * @param logDesc 日志描述
     * @param operateLogEnum  操作类型
     * @param operator 操作人
     * @return
     */
    public void  insertApiLog(String contractCode, String systemType, String logDesc, OperateLogEnum operateLogEnum,String operator){
        OperateLog operateLog = new OperateLog();
        operateLog.setContractCode(contractCode);
        operateLog.setSystemType(systemType);
        operateLog.setOperateDescription(logDesc);
        operateLog.setOperator(operator);
        operateLog.setOperateTime(new Date());
        operateLog.setOperateType(operateLogEnum);
        this.entityDao.insert(operateLog);
    }
}
