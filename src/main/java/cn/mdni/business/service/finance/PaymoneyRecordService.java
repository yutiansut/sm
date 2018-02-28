package cn.mdni.business.service.finance;

import cn.mdni.business.constants.CommonStatusEnum;
import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.FinancePayTypeEnum;
import cn.mdni.business.dao.finance.PaymoneyRecordDao;
import cn.mdni.business.entity.finance.PaymoneyRecord;
import cn.mdni.business.entity.finance.PayplanItem;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.commons.collection.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 交款记录Service
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/02.
 */
@Service
public class PaymoneyRecordService extends CrudService<PaymoneyRecordDao,PaymoneyRecord>{

    @Autowired
    private PaymoneyRecordDao paymoneyRecordDao;

    /**
     * 查询财务阶段
     * @return
     * @param storeCode
     */
    public List<Map<String,String>> getStageType(String storeCode) {
        return this.paymoneyRecordDao.getStageType(storeCode);
    }

    /**
     * 交款记录查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<PaymoneyRecord> paymoneyRecordFindAll(Map<String, Object> paramMap){
        List<PaymoneyRecord> pageData = Collections.emptyList();
        Long count = this.paymoneyRecordDao.paymoneyRecordTotal(paramMap);
        if (count > 0) {
            pageData = paymoneyRecordDao.paymoneyRecordFindAll(paramMap);
        }
        return new BootstrapPage(count, pageData);
    }

    /**
     * 综合查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<Map> projectCompositeFindAll(Map<String, Object> paramMap) {
        List<Map> pageData = Collections.emptyList();
        Long count = this.paymoneyRecordDao.projectCompositeTotal(paramMap);
        if (count > 0) {
            pageData = paymoneyRecordDao.projectCompositeFindAll(paramMap);
        }
        return new BootstrapPage(count, pageData);
    }

    /**
     * 通过项目UUID查询交款信息
     * @param
     * @return
     */
    public List<Map<String,Object>> paymentInformationInquiry(String contractUuid) {
        return paymoneyRecordDao.paymentInformationInquiry(contractUuid);
    }

    /**
     * 综合数据 ----导出
     * @param paramMap
     * @return
     */
    public List<Map> exportProjectComposite(Map<String, Object> paramMap) {
        return this.paymoneyRecordDao.exportProjectComposite(paramMap);
    }

    /**
     * 交款记录 ----导出
     * @param paramMap
     * @return
     */
    public List<PaymoneyRecord> exportPaymoneyRecord(Map<String, Object> paramMap) {
        return this.paymoneyRecordDao.exportPaymoneyRecord(paramMap);
    }

    /**
     * 通过项目payId查询交款信息
     * @param payId 交款id
     * @return
     */
    public List<PaymoneyRecord> redPunch(String payId) {
        Map<String,Object> paramMap = new HashedMap();
        MapUtils.putNotNull(paramMap,"payId",payId);
        return entityDao.search(paramMap);
    }

    /**
     * 通过项目payId查询PaymoneyRecord
     * @param payId 交款id
     * @return
     */
    public PaymoneyRecord getPaymoneyRecordByPayId(String payId) {
        return paymoneyRecordDao.getPaymoneyRecordByPayId(payId);
    }



    /**
     * 批量修改指定项目的交款记录状态
     * @param contractUuid
     * @param status
     */
    public void updateProjectPaymentStatus(String contractUuid,String status){
        entityDao.batchUpdatePaymoneyRecordStatus(contractUuid,status);
    }


    /**
     * 查询指定项目可以结束收款的一条交款记录
     * @param contractUuid
     * @return
     */
    public PaymoneyRecord getProjectAbleFinishPaymoneyRecord(String contractUuid){
        //允许的pay_type
        List<String> ableFinishTypes = new ArrayList<>();
        ableFinishTypes.add(FinancePayTypeEnum.MODIFY.toString());
        ableFinishTypes.add(FinancePayTypeEnum.CONSTRUCT.toString());
        //禁止的node_code
        List<String> exNodes = new ArrayList<>();
        exNodes.add(PayplanItem.PayplanItemCodeEnum.NODE_AFTERFINAL.toString());
        return entityDao.getProjectAbleFinishPaymoneyRecord(contractUuid,ableFinishTypes,exNodes);
    }


    /**
     * 查询指定项目可以红冲的一条交款记录
     * @param contractUuid
     * @return
     */
    public PaymoneyRecord getProjectAbleRcwPaymoneyRecord(String contractUuid){
        List<String> ableRcwTypes = new ArrayList<>();
        ableRcwTypes.add(FinancePayTypeEnum.DEPOSIT.toString());
        ableRcwTypes.add(FinancePayTypeEnum.MODIFY.toString());
        ableRcwTypes.add(FinancePayTypeEnum.CONSTRUCT.toString());
        //取出最新的交易流水，如果最新的交易流水是交款类型的
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("contractUuid",contractUuid);;
        queryMap.put("offset",0);
        queryMap.put("offset",1);
        queryMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.DESC, "id"));
        //从数据库中查出的最新一条交易流水
        List<PaymoneyRecord> latestRecords = entityDao.search(queryMap);
        if(latestRecords==null || latestRecords.size()<1){
            return null;
        }
        PaymoneyRecord latestRecord = latestRecords.get(0);
        if(latestRecord == null){
            return null;
        }
        //判断查出的最新一条交易流水是否可以红冲
        if(!latestRecord.getIfRcw() && CommonStatusEnum.VALID.toString().equals(latestRecord.getPayStatus())
                && ableRcwTypes.contains(latestRecord.getPayType())){
            return latestRecord;
        }
        else{
            return null;
        }
    }


    /**
     * 收据查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<PaymoneyRecord> findReceiptAll(Map<String, Object> paramMap) {
        List<PaymoneyRecord> pageData = Collections.emptyList();
        Long count = this.paymoneyRecordDao.receiptTotal(paramMap);
        if (count > 0) {
            pageData = paymoneyRecordDao.findReceiptAll(paramMap);
        }
        return new BootstrapPage(count, pageData);
    }

    /**
     * 根据id查询单个数据 ----打印
     * @param id
     * @param storeCode
     * @return
     */
    public PaymoneyRecord getPaymoneyRecordById(Long id, String storeCode) {
        Map map = new HashMap();
        map.put("id",id);
        map.put("storeCode",storeCode);
        return this.entityDao.getPaymoneyRecordById(map);
    }

    /**
     * 修改打印次数
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePrintCount(Long id) {
        this.entityDao.updatePrintCount(id);
    }

    /**
     * 批量打印
     * @param list
     * @param storeCode
     * @return
     */
    public  List<PaymoneyRecord> getManyPaymoneyRecordById(List<String> list, String storeCode) {
        Map map = new HashMap();
        map.put("list",list);
        map.put("storeCode",storeCode);
        return this.entityDao.getManyPaymoneyRecordById(map);
    }

    /**
     * 查询收据号是否已经存在
     * @param receiptNo 收据号
     * @return
     */
    public boolean paymentReceiptExist(String receiptNo){
        Map<String,Object> queryMap = new HashMap<String,Object>();
        MapUtils.putNotNull(queryMap,"receiptNum",receiptNo);
        return entityDao.searchTotal(queryMap) > 0L;
    }

    /**
     * 获取打印次数
     * @param id
     * @return
     */
    public Integer getPrintCount(Long id) {
        return this.entityDao.getPrintCount(id);
    }

    public Map getStoreCode(String storeCode) {
        return this.entityDao.getStoreCode(storeCode);
    }
}
