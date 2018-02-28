package cn.mdni.business.service.finance;

import cn.mdni.business.constants.FinancePayTypeEnum;
import cn.mdni.business.dao.finance.FinaCustomerContractDao;
import cn.mdni.business.entity.finance.FinaProjectAccount;
import cn.mdni.business.entity.finance.PaymoneyRecord;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.*;

/**
 * Created by WS on 2017/11/17/017.
 */
@Service
public class FinaCustomerContractService extends  CrudService<FinaCustomerContractDao , FinaProjectAccount> {

    @Autowired
    private PaymoneyRecordService paymoneyRecordService;

    /**
     * 查询项目信息
     * @param
     * @return
     */
    public Map projectInformation(String contractUuid) {
        Map infoMap = entityDao.projectInformation(contractUuid);
        //可以结束收款的record的payId,目前 只有拆改费和施工款可以结束收款
        PaymoneyRecord ableFinishPayRecord = paymoneyRecordService.getProjectAbleFinishPaymoneyRecord(contractUuid);
        infoMap.put("ableFinishPayid",ableFinishPayRecord == null ? "" : ableFinishPayRecord.getPayId());
        //可以红冲的收款
        PaymoneyRecord ableRcwPayRecord = paymoneyRecordService.getProjectAbleRcwPaymoneyRecord(contractUuid);
        infoMap.put("ableRcwPayid",ableRcwPayRecord == null ? "" : ableRcwPayRecord.getPayId());
        return infoMap;
    }


    /**
     * 查询订单相关信息
     * @param paramMap
     * @return
     */
    public BootstrapPage<Map> findContractAll(Map<String, Object> paramMap) {
        List<Map> pageData = Collections.emptyList();
        Long count = this.entityDao.contractTotal(paramMap);
        if (count > 0) {
            pageData = entityDao.findContractAll(paramMap);
        }
        return new BootstrapPage(count,pageData);
    }

    /**
     * 查询账单信息
     * @param codeList
     * @return
     */
    public List<Map<String, String>> getAccountByContractCode(List<String> codeList) {
        return this.entityDao.getAccountByContractCode(codeList);
    }

    /**
     * 通过条件查询财务汇总要导出的条数
     * @param paramMap
     * @return
     */
    public Long countProjectSummarizByQuery(Map<String, Object> paramMap) {
        return this.entityDao.countProjectSummarizByQuery(paramMap);
    }

    /**
     * 财务汇总 ----导出
     * @param paramMap
     * @return
     */
    public List<Map> exportProjectSummariz(Map<String, Object> paramMap) {
        return this.entityDao.exportProjectSummariz(paramMap);
    }
}
