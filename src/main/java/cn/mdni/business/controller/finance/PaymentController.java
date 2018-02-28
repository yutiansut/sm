package cn.mdni.business.controller.finance;

import cn.mdni.business.entity.finance.DeductMoney;
import cn.mdni.business.entity.finance.PaymoneyRecord;
import cn.mdni.business.entity.finance.RefundRecord;
import cn.mdni.business.service.finance.PaymentService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 付款
 * @Company: 美得你智装科技有限公司
 * @Author zhangh
 * @Date: 2017/11/24
 */
@RestController
@RequestMapping("/finance/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    /**
     * 查询项目当前的交款阶段,用于展示财务收款
     * @param contractUuid 项目的uuid
     * @return
     */
    @RequestMapping("/buildpaymentsituation")
    public Object projectInformation(String contractUuid) {
        return paymentService.buildPaymentSituation(contractUuid);
    }

    /**
     * 保存交款记录
     * @param paymoneyRecord
     * @return
     */
    @RequestMapping("/savepaymentrecord")
    public Object insert(@RequestBody PaymoneyRecord paymoneyRecord){
        return paymentService.savePaymentRecord(paymoneyRecord);
    }

    /**
     * 查询项目当前的阶段的退款信息
     * @param contractUuid 项目的uuid
     * @return
     */
    @RequestMapping("/buildrefundsituation")
    public Object buildRefundSituation(String contractUuid) {
        return paymentService.buildRefundSituation(contractUuid);
    }

    /**
     * 保存退款记录
     * @param refundRecord
     * @return
     */
    @RequestMapping("/saveprojectrefundrecord")
    public Object saveProjectRefundRecord(@RequestBody RefundRecord refundRecord){
        return paymentService.saveProjectRefundRecord(refundRecord, RefundRecord.RefundTypeEnum.COMMONREFUND);
    }

    /**
     * 红冲
     * @param payId
     * @return
     */
    @RequestMapping("/redpunch")
    public Object redPunch(String payId){
        return paymentService.redPunch(payId);
    }


    /**
     * 查询项目下可用的抵定金类型的抵扣款
     * @param contratUuid
     * @return
     */
    @RequestMapping("/querydepositdeduct")
    public Object queryDeductMoney(@RequestParam("contratUuid") String contratUuid){
        Map<String,Object> queryMap = new HashMap<String,Object>();
        MapUtils.putNotNull(queryMap,"contractUuid",contratUuid);
        MapUtils.putNotNull(queryMap,"deductType", DeductMoney.DeductMoneyTypeEnum.DEPOSIT_DEDUCT.toString());
        MapUtils.putNotNull(queryMap,"attrStatus", DeductMoney.DeductMoneyStatusEnum.ABLE_DEDUCT.toString());

        List<DeductMoney> depositDeductList = paymentService.queryDeductMoneyWithCondition(queryMap);
        return StatusDto.buildDataSuccessStatusDto(depositDeductList);
    }

    /**
     * 结束收款
     * @param payId
     * @return
     */
    @RequestMapping("/endreceipt")
    public Object endReceipt(String payId){
        return paymentService.finishFinanceStageManual(payId);
    }
}
