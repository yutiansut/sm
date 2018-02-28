package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.ChangeAuditCollectDao;
import cn.mdni.business.entity.material.ChangeAuditCollect;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 变更审计汇总service
 * Created by Allen on 2017/11/21.
 */
@Service
public class ChangeAuditCollectService extends CrudService<ChangeAuditCollectDao,ChangeAuditCollect>{
    /**
     * 获取审计员所审变更单
     */
    public List<ChangeAuditCollect> findAuditChangeOrderInfo(Map<String,Object> map){
        return this.entityDao.findAuditChangeOrderInfo(map);
    }
}
