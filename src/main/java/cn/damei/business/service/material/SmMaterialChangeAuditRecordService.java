package cn.damei.business.service.material;

import cn.damei.business.dao.material.SmMaterialChangeAuditRecordDao;
import cn.damei.business.entity.material.SmMaterialChangeAuditRecord;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SmMaterialChangeAuditRecordService extends CrudService<SmMaterialChangeAuditRecordDao,SmMaterialChangeAuditRecord>{
    public List<SmMaterialChangeAuditRecord> getByChangeNos(Long[] ids) {
        return this.entityDao.getByChangeNos(ids);
    }

    public Date getByChangeNo(String changeNo) {
        return this.entityDao.getByChangeNo(changeNo);
    }

    /**
     * 获取审计员审核通过记录
     */
    public SmMaterialChangeAuditRecord getRecordByChangeNo(String changeNo) {
        return this.entityDao.getRecordByChangeNo(changeNo);
    }

    public List<SmMaterialChangeAuditRecord> getNameByChangeNo(String changeNo) {
        return this.entityDao.getNameByChangeNo(changeNo);
    }

}
