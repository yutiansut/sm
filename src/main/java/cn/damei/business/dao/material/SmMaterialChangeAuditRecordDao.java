package cn.damei.business.dao.material;

import cn.damei.business.entity.material.SmMaterialChangeAuditRecord;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
