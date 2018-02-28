package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.SmMaterialChangeAuditRecord;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by 刘铎 on 2017/11/21.
 */
@Repository
public interface SmMaterialChangeAuditRecordDao extends CrudDao<SmMaterialChangeAuditRecord>{
    List<SmMaterialChangeAuditRecord> getByChangeNos(Long[] ids);

    Date getByChangeNo(String changeNo);

    /**
     * 获取审计员审核通过记录
     */
    SmMaterialChangeAuditRecord getRecordByChangeNo(String changeNo);

    List<SmMaterialChangeAuditRecord> getNameByChangeNo(String changeNo);

}
