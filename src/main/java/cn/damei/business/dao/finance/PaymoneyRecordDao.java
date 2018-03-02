package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.PaymoneyRecord;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

import javax.imageio.stream.IIOByteBuffer;
import java.util.List;
import java.util.Map;

@Repository
public interface PaymoneyRecordDao extends CrudDao<PaymoneyRecord>{

    /**
     * 查询财务阶段
     * @return
     * @param storeCode
     */
    List<Map<String,String>> getStageType(String storeCode);

    List<Map<String,Object>> paymentInformationInquiry(String contractUuid);

    /**
     * 交款记录查询
     * @param paramMap
     * @return
     */
    Long paymoneyRecordTotal(Map<String, Object> paramMap);
    List<PaymoneyRecord> paymoneyRecordFindAll(Map<String, Object> paramMap);

    /**
     * 综合查询
     * @param paramMap
     * @return
     */
    Long projectCompositeTotal(Map<String, Object> paramMap);
    List<Map> projectCompositeFindAll(Map<String, Object> paramMap);

    /**
     * 综合数据 ----导出
     * @param paramMap
     * @return
     */
    List<Map> exportProjectComposite(Map<String, Object> paramMap);

    /**
     * 交款记录 ----导出
     * @param paramMap
     * @return
     */
    List<PaymoneyRecord> exportPaymoneyRecord(Map<String, Object> paramMap);

    /**
     * 通过项目payId查询PaymoneyRecord
     * @param payId 交款id
     * @return
     */
    PaymoneyRecord getPaymoneyRecordByPayId(@Param(value="payId")  String payId);


    /**
     * 批量修改指定项目的交款记录状态
     * @param contractUuid
     */
    void batchUpdatePaymoneyRecordStatus(@Param("contractUuid") String contractUuid,
                                         @Param("payRecordStatus") String payRecordStatus);

    /**
     * 查询指定项目最近一次允许结束收款的交款
     * @param contractUuid 项目id
     * @param ablePayTypes 允许的交款类型
     * @param exNodes 禁止的交款类型
     * @return
     */
    PaymoneyRecord getProjectAbleFinishPaymoneyRecord(@Param("contractUuid") String contractUuid,
                                                  @Param("ablePayTypes") List<String> ablePayTypes,
                                                  @Param("exNodes") List<String> exNodes);


    /**
     * 查询指定项目最近一次允许红冲的交款
     * @param contractUuid
     * @param ablePayTypes
     * @return
     */
    PaymoneyRecord getProjectAbleRcwPaymoneyRecord(@Param("contractUuid") String contractUuid,
                                                      @Param("ablePayTypes") List<String> ablePayTypes);


    /**
     * 收据查询
     * @param paramMap
     * @return
     */
    Long receiptTotal(Map<String, Object> paramMap);
    List<PaymoneyRecord> findReceiptAll(Map<String, Object> paramMap);

    /**
     * 根据id查询单个数据 ----打印
     * @param map
     * @return
     */
    PaymoneyRecord getPaymoneyRecordById(Map map);

    /**
     * 修改打印次数
     * @param id
     * @return
     */
    void updatePrintCount(@Param("id") Long id);

    /**
     * 批量打印
     * @param map
     * @return
     */
    List<PaymoneyRecord> getManyPaymoneyRecordById(Map map);

    Integer getPrintCount(@Param("id")Long id);

    Map getStoreCode(String storeCode);
}
