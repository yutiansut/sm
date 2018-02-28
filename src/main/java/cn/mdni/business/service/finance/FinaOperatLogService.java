package cn.mdni.business.service.finance;

import cn.mdni.business.constants.FinancePayTypeEnum;
import cn.mdni.business.dao.finance.FinaOperatLogDao;
import cn.mdni.business.entity.orderflow.Customer;
import cn.mdni.business.entity.finance.*;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.commons.date.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 财务操作日志service类
 * @Date Created in 2017/11/19 23:47
 */
@Service
public class FinaOperatLogService extends CrudService<FinaOperatLogDao,FinaOperatLog>{

    /**
     * 插入财务操作日志
     * @param optLog
     */
    public boolean addFinaOperatLog(FinaOperatLog optLog){
        try{
            entityDao.insert(optLog);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 记录财务阶段流转的日志
     * ｛XXX时间｝流转到｛XXX｝阶段
     * @param aimStage 流转到的阶段
     * @return
     */
    public FinaOperatLog  addStageTransformMessage(ProjectPayplanStage aimStage){
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.STAGE_TRANSFORM.toString());
        finaLog.setContractCode(aimStage.getContractCode());
        finaLog.setContractUuid(aimStage.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_project_payplan_stage.toString());
        finaLog.setTargetVal(aimStage.getStageCode());
        finaLog.setOperator(WebUtils.getCurrentUserNameWithOrgCode());
        finaLog.setOperatTime(aimStage.getStartTime());

        StringBuffer msgSb = new StringBuffer();
        msgSb.append(DateUtils.parseStrYMDHMS(aimStage.getStartTime())).append("流转到").append(aimStage.getStageName()).append("阶段。");
        finaLog.setOperatMsg(msgSb.toString());

        addFinaOperatLog(finaLog);
        return finaLog;
    }


    /**
     * 构造新增赔款的操作描述
     * ｛XXX}人｛XXX｝时间,新增赔款
     * @param reparationMoney
     * @return
     */
    public FinaOperatLog addReparationMoneyfromMessage(ReparationMoney reparationMoney){
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.CREATE_REPARATION.toString());
        finaLog.setContractCode(reparationMoney.getContractCode());
        finaLog.setContractUuid(reparationMoney.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_reparation_money.toString());
        finaLog.setTargetVal(reparationMoney.getCreateStageId());
        finaLog.setOperator(WebUtils.getCurrentUserNameWithOrgCode());
        finaLog.setOperatTime(reparationMoney.getCreateTime());

        StringBuffer msgSb = new StringBuffer();
        msgSb.append(reparationMoney.getCreator()).append(",").append(DateUtils.parseStrYMDHMS(reparationMoney.getCreateTime())).append("新增赔款,金额为:").append(reparationMoney.getReparationAmount()).append(" 元");
        finaLog.setOperatMsg(msgSb.toString());

        addFinaOperatLog(finaLog);
        return finaLog;
    }


    /**
     * 记录交款后触发的日志
     * ｛XXX时间｝｛XXX付款人｝为{XXX阶段名称}交款{XXX元}
     * @param paymentRecord 交款记录
     * @return
     */
    public FinaOperatLog addFinaPaymentLog(PaymoneyRecord paymentRecord){
        FinaOperatLog finaLog = new FinaOperatLog();
        String financeOperatType = "";
        if(FinancePayTypeEnum.DEPOSIT.toString().equals(paymentRecord.getPayType())){
            financeOperatType = FinaOperatLog.FinanceOperateTypeEnum.PAY_DEPOSIT.toString();
        }
        else if(FinancePayTypeEnum.MODIFY.toString().equals(paymentRecord.getPayType())){
            financeOperatType = FinaOperatLog.FinanceOperateTypeEnum.PAY_MODIFY.toString();
        }
        else if(FinancePayTypeEnum.CONSTRUCT.toString().equals(paymentRecord.getPayType())){
            financeOperatType = FinaOperatLog.FinanceOperateTypeEnum.PAY_CONSTRUCT.toString();
        }

        finaLog.setOperatType(financeOperatType);
        finaLog.setContractCode(paymentRecord.getContractCode());
        finaLog.setContractUuid(paymentRecord.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_project_payplan_stage.toString());
        finaLog.setTargetVal(paymentRecord.getPayId());
        finaLog.setOperator(paymentRecord.getCashier());
        finaLog.setOperatTime(paymentRecord.getPayTime());

        StringBuffer operatMsgSb = new StringBuffer();
        operatMsgSb.append(DateUtils.parseStrYMDHMS(paymentRecord.getPayTime())).append(paymentRecord.getPayerName()).append("交")
                .append(paymentRecord.getPayManualFlag()).append(paymentRecord.getActualReceived()).append("元。")
                .append("收款人：").append(paymentRecord.getCashier());

        finaLog.setOperatMsg(operatMsgSb.toString());
        addFinaOperatLog(finaLog);
        return finaLog;
    }

    /**
     * 记录退款后触发的日志
     * ｛XXX时间｝实退｛XXX收钱人｝{XXX元}。执行人：{XXX｝
     * @param refundRecord 退款记录
     * @return
     */
    public FinaOperatLog addFinaRefundLog(RefundRecord refundRecord){
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.PAY_RETURN.toString());
        finaLog.setContractCode(refundRecord.getContractCode());
        finaLog.setContractUuid(refundRecord.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_refund_record.toString());
        finaLog.setTargetVal(refundRecord.getRefundNo());
        finaLog.setOperator(refundRecord.getOperator());
        finaLog.setOperatTime(refundRecord.getOperateTime());

        StringBuffer operatMsgSb = new StringBuffer();
        operatMsgSb.append(DateUtils.parseStrYMDHMS(refundRecord.getOperateTime())).append("实退").append(refundRecord.getRefundReceiverName())
                .append("，").append(refundRecord.getRefundActualAmount()).append("元。")
                .append("执行人：").append(refundRecord.getOperator());

        finaLog.setOperatMsg(operatMsgSb.toString());
        addFinaOperatLog(finaLog);
        return finaLog;
    }


    /**
     * 查询操作记录
     * @param contractUuid
     * @return
     */
    public List<Map<String,Object>> operationRecord(String contractUuid) {
        return entityDao.operationRecord(contractUuid);
    }

    /**
     * 记录红冲的日志
     * {XXX人}｛XXX时间}｛XXX｝阶段 红冲 {XXX金额}
     * @param paymoneyRecord
     * @return
     */
    public FinaOperatLog  addFinaPayRcwLog(PaymoneyRecord paymoneyRecord){
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.PAY_RCW.toString());
        finaLog.setContractCode(paymoneyRecord.getContractCode());
        finaLog.setContractUuid(paymoneyRecord.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_paymoney_record.toString());
        finaLog.setTargetVal(paymoneyRecord.getStageCode());
        finaLog.setOperator(WebUtils.getCurrentUserNameWithOrgCode());
        finaLog.setOperatTime(paymoneyRecord.getPayTime());

        StringBuffer msgSb = new StringBuffer();
        msgSb.append(paymoneyRecord.getCashier()).append(",").append(DateUtils.parseStrYMDHMS(paymoneyRecord.getPayTime())).append(",").append(paymoneyRecord.getPayManualFlag()).append("时,发生红冲,金额为").append(paymoneyRecord.getActualReceived());
        finaLog.setOperatMsg(msgSb.toString());

        addFinaOperatLog(finaLog);
        return finaLog;
    }


    /**
     * 记录修改用户信息的日志
     * @param customerInfo
     * @return
     */
    public FinaOperatLog addAlterCustomerInfoLog(Customer customerInfo){
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.ALTER_CUSTOMER_INFO.toString());
        finaLog.setContractCode("");
        finaLog.setContractUuid(customerInfo.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.customer.toString());
        finaLog.setTargetVal(customerInfo.getId().toString());
        finaLog.setOperator(WebUtils.getCurrentUserNameWithOrgCode());
        finaLog.setOperatTime(new Date());

        StringBuffer msgSb = new StringBuffer();
        msgSb.append(finaLog.getOperator()).append("于").append(DateUtils.parseStrYMDHMS(finaLog.getOperatTime())).
                append("修改用户信息为").append(customerInfo.getName()).append("（").append(customerInfo.getMobile()).
                append("）");
        finaLog.setOperatMsg(msgSb.toString());

        addFinaOperatLog(finaLog);
        return finaLog;
    }

    /**
     * 记录退单后触发的日志
     * ｛XXX时间｝执行人：{XXX｝执行退单操作,订单关闭。
     * @param refundRecord 退单记录
     * @return
     */
    public FinaOperatLog addFinaBackRefundLog(RefundRecord refundRecord) {
        FinaOperatLog finaLog = new FinaOperatLog();
        finaLog.setOperatType(FinaOperatLog.FinanceOperateTypeEnum.PAY_BACKRETURN.toString());
        finaLog.setContractCode(refundRecord.getContractCode());
        finaLog.setContractUuid(refundRecord.getContractUuid());
        finaLog.setTargetKey(FinaOperatLog.FinanceTableName.fina_refund_record.toString());
        finaLog.setTargetVal(refundRecord.getRefundNo());
        finaLog.setOperator(refundRecord.getOperator());
        finaLog.setOperatTime(refundRecord.getOperateTime());

        StringBuffer operatMsgSb = new StringBuffer();
        operatMsgSb.append(DateUtils.parseStrYMDHMS(refundRecord.getOperateTime())).append("执行人：").append(refundRecord.getOperator()).append("执行退单操作,订单关闭.");

        finaLog.setOperatMsg(operatMsgSb.toString());
        addFinaOperatLog(finaLog);
        return finaLog;
    }
}
