package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ChangeAuditCollect;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ChangeAuditCollectDao extends CrudDao<ChangeAuditCollect>{
    /**
     * 获取审计员所审变更单
     */
    List<ChangeAuditCollect> findAuditChangeOrderInfo(Map<String,Object> map);
}
