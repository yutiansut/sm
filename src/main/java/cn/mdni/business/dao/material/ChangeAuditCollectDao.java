package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ChangeAuditCollect;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 变更审计汇总dao
 * Created by Allen on 2017/11/21.
 */
@Repository
public interface ChangeAuditCollectDao extends CrudDao<ChangeAuditCollect>{
    /**
     * 获取审计员所审变更单
     */
    List<ChangeAuditCollect> findAuditChangeOrderInfo(Map<String,Object> map);
}
