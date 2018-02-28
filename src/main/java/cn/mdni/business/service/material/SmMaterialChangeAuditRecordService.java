package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.SmMaterialChangeAuditRecordDao;
import cn.mdni.business.entity.material.SmMaterialChangeAuditRecord;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 刘铎 on 2017/11/21.
 */
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
