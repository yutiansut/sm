package cn.mdni.business.service.finance;

import cn.mdni.business.dao.finance.RefundRecordDao;
import cn.mdni.business.entity.finance.RefundRecord;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by WS on 2017/11/17/017.
 */
@Service
public class RefundRecordService extends CrudService<RefundRecordDao,RefundRecord>{

    /**
     * 通过项目UUID查询退款记录
     * @param
     * @return
     */
    public List<Map<String,Object>> refundRecord(String contractUuid) {
        return entityDao.refundRecord(contractUuid);
    }

    /**
     * 获取退单扣除费用
     * @param codeList
     * @return
     */
    public List<Map<String,String>> getChargebackAmount(List<String> codeList) {
        return this.entityDao.getChargebackAmount(codeList);
    }
}
