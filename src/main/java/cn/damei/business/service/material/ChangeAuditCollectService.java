package cn.damei.business.service.material;

import cn.damei.business.dao.material.ChangeAuditCollectDao;
import cn.damei.business.entity.material.ChangeAuditCollect;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChangeAuditCollectService extends CrudService<ChangeAuditCollectDao,ChangeAuditCollect>{
    /**
     * 获取审计员所审变更单
     */
    public List<ChangeAuditCollect> findAuditChangeOrderInfo(Map<String,Object> map){
        return this.entityDao.findAuditChangeOrderInfo(map);
    }
}
