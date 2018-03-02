package cn.damei.business.service.finance;

import cn.damei.business.constants.*;
import cn.damei.business.dao.material.CustomerContractDao;
import cn.damei.business.dto.finance.ProjectPayStageDto;
import cn.damei.business.dto.finance.RefundRecordDto;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.*;
import cn.damei.business.entity.operatelog.OperateLog;
import cn.damei.business.service.api.CrmApiService;
import cn.damei.business.service.operatelog.OperateLogService;
import cn.damei.business.service.orderflow.ContractService;
import cn.damei.business.service.orderflow.NumberingRuleService;
import cn.damei.business.service.api.OutApiService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.json.JsonUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;

    @Autowired
    private ProjectChangeMoneyService projectChangeMoneyService;

    @Autowired
    private ReparationMoneyService reparationMoneyService;

    @Autowired
    private NumberingRuleService numberingRuleService;

    @Autowired
    private ContractService projectService;

    @Autowired
    private PaymoneyRecordService paymoneyRecordService;

    @Autowired
    private FinaProjectAccountService finaProjectAccountService;

    @Autowired
    private PayplanService payplanService;

    @Autowired
    private FinaOperatLogService finaOperatLogService;

    @Autowired
    private RefundRecordService refundRecordService;

    @Autowired
    private FinaPaymethodService paymethodService;

    @Autowired
    private PaymethodAttrService paymethodAttrService;

    @Autowired
    private DeductMoneyService deductMoneyService;

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private OutApiService outApiService;

    @Autowired
    private CustomerContractDao customerContractDao;

    @Autowired
    private CrmApiService crmApiService;


    /**
     * 查询项目当前的交款阶段,用于展示财务收款
     * @param contractUuid 项目的uuid
     * @return
     */
    public StatusDto buildPaymentSituation(String contractUuid){

        //查到项目
        CustomerContract project = projectService.getContractByUuid(contractUuid);
        if(project == null){
            return StatusDto.buildFailureStatusDto("订单不存在");
        }

        //页面展示用的dto
        ProjectPayStageDto payStageDto = new ProjectPayStageDto();

        //项目编号
        payStageDto.setContractNo(project.getContractCode());
        payStageDto.setContractUuid(project.getContractUuid());

        //查询项目客户信息
        String customerName = project.getCustomerName();
        if(StringUtils.isNotBlank(project.getSecondContact())){
            customerName = project.getSecondContact();
        }
        String customerMobile = project.getCustomerMobile();
        if(StringUtils.isNotBlank(project.getSecondContactMobile())){
            customerMobile = project.getSecondContactMobile();
        }
        payStageDto.setCustomerName(customerName);
        payStageDto.setCustomerMobile(customerMobile);

        //定金是否可退、是否签定金合同
        payStageDto.setSignedDepositContract(project.getSignDeposit() != null &&  project.getSignDeposit() >0);
        payStageDto.setDepositEnableReturnBack(project.getReturnWord() != null && project.getReturnWord()>0);

        //获取本次页面要用的收据号
        payStageDto.setReceiptNum(numberingRuleService.getNumber(project.getStoreCode(), NumberingTypeEnum.RECEIPT_NUMBER));

        //查询项目当前阶段相关信息
        ProjectPayplanStage currentStage = projectPayplanStageService.getProjectCurrentStage(contractUuid);
        if (currentStage != null) {
            payStageDto.setPayStage(currentStage);

            //查询影响本阶段的变更
            List<ProjectchangeMoney> changeList = projectChangeMoneyService.queryEffectStageChangeMoneyList(contractUuid, currentStage.getStageCode(), false);
            payStageDto.setStageChangeMoneyList(changeList);
            payStageDto.setStageTotalChange(projectChangeMoneyService.sumChangeAmount(changeList));

            //查询影响本阶段的赔款
            List<ReparationMoney> reparationList = reparationMoneyService.queryEffectStageReparationMoneyList(contractUuid, currentStage.getStageCode(), false);
            payStageDto.setStageReparationMoneyList(reparationList);
            payStageDto.setStageTotalReparation(reparationMoneyService.sumReparationAmount(reparationList));

            //计算当前阶段应收
            payStageDto.setStageExpectedPay(projectPayplanStageService.calculateStageSummaryExpectedReceived(currentStage));

            //查询当前门店当前阶段可用的支付方式
            payStageDto.setCurrentStageMethodList(paymethodService.queryPayMethodListWithStoreAndStage(project.getStoreCode(), currentStage.getStageTemplateCode()));

            //设置当前阶段可用的人工交款标记选项
            List<String> curStagePaymentFlagList = new ArrayList<String>();
            PayplanItem curentStageTemplate = payplanService.getPlanItemWithItemId(Long.valueOf(currentStage.getStageTemplateId().longValue()));
            if(StringUtils.isNotBlank(curentStageTemplate.getManulPayFlag())){
                curStagePaymentFlagList = Arrays.asList(curentStageTemplate.getManulPayFlag().split(","));
            }
            payStageDto.setCurStagePaymentFlagList(curStagePaymentFlagList);
        }

        //*************设置专为交定金界面展示用的值***************
        List<String> depositPaymentFlagList = new ArrayList<String>();
        ProjectPayplanStage depositStage = projectPayplanStageService.getProjectStageWithItemTempCode(contractUuid,PayplanItem.PayplanItemCodeEnum.NODE_DEPOSIT);
        if(depositStage != null){
            payStageDto.setDepositStageCode(depositStage.getStageCode());
            BigDecimal depositExpectPay = depositStage.getExpectReceived().subtract(depositStage.getActualTotalReceived());
            if(depositExpectPay.compareTo(BigDecimal.ZERO)<0){
                depositExpectPay = BigDecimal.ZERO;
            }
            payStageDto.setDepositExpectPay(depositExpectPay);
            //——查询当前门店定金阶段可用的支付方式
            payStageDto.setDepositStageMethodList(paymethodService.queryPayMethodListWithStoreAndStage(project.getStoreCode(),depositStage.getStageTemplateCode()));
            //设置定金阶段的人工交款标记
            PayplanItem depositTemplate = payplanService.getPlanItemWithItemId(Long.valueOf(depositStage.getStageTemplateId().toString()));
            if(StringUtils.isNotBlank(depositTemplate.getManulPayFlag())){
                depositPaymentFlagList = Arrays.asList(depositTemplate.getManulPayFlag().split(","));
            }
        }
        else{
            payStageDto.setDepositExpectPay(BigDecimal.ZERO);
            //——查询当前门店可用的支付方式，由于没有定金阶段，所以无法根据阶段来查，所以抵定金支付方式需要手动移除
            List<FinaPaymethod> methodListForStore = paymethodService.queryPayMethodListWithStoreAndStage(project.getStoreCode(),null);
            payStageDto.setDepositStageMethodList(methodListForStore.stream().filter(
                    method ->! FinaPaymethod.PaymethodSpecialCodeEnum.PAY_DDJSJDK.toString().equals(method.getMethodCode())
            ).collect(Collectors.toList()));
            //交款规划中没有定金阶段模板，用默认的
            depositPaymentFlagList.add("小订");
            depositPaymentFlagList.add("大定");
        }
        payStageDto.setDepositPaymentFlagList(depositPaymentFlagList);
        return StatusDto.buildDataSuccessStatusDto(payStageDto);
    }


    /**
     * 保存交款记录
     * @param paymentRecord
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto savePaymentRecord(PaymoneyRecord paymentRecord){

        //查询需要的基础信息(支付方式、支付属性、账户等)
        //——交款方式
        FinaPaymethod paymethod = paymethodService.getById(paymentRecord.getPaymethodId());
        PaymethodAttr paymethodAttr = paymethodAttrService.getById(paymentRecord.getPaymethodAttrId());
        //校验交款记录信息是否正确
        StatusDto checkPaymentResult = checkPaymentRecord(paymentRecord,paymethod,paymethodAttr);
        if(checkPaymentResult != null){
            return checkPaymentResult;
        }

        //保存本次交款
        //——累加财务阶段的累计实收金额
        ProjectPayplanStage currentStage = effectFinanceStageAfterPayment(paymentRecord);

        //——根据当前阶段数据补充paymoneyRecord的字段
        fillNewPaymentRecordInfo(paymentRecord,currentStage,paymethod,paymethodAttr);

        //——影响账户金额
        FinaProjectAccount financeAccount = effectAccountDeductAfterPayment(paymentRecord);

        //如果允许，则自动完成当前收款阶段并流转下一阶段
        List<ProjectPayplanStage> processedStageList = autoFinishFinanceStage(currentStage);

        //记录财务交款记录
        finaOperatLogService.addFinaPaymentLog(paymentRecord);


        //持久化本次交款影响的实体
        //——持久化交款记录
        paymoneyRecordService.insert(paymentRecord);
        //——持久化交款阶段
        if(processedStageList != null){
            for (ProjectPayplanStage processedStage : processedStageList){
                projectPayplanStageService.update(processedStage);

            }
        }
        else if(currentStage != null){
            projectPayplanStageService.update(currentStage);
        }
        //——持久化项目财务账号
        finaProjectAccountService.update(financeAccount);

        //如果是定金交款，通知CRM定金累计交款金额信息
        if(FinancePayTypeEnum.DEPOSIT.toString().equals(paymentRecord.getPayType())){
            crmApiService.sendDepositInfoToCrm(financeAccount,null);
        }

        return StatusDto.buildSuccessStatusDto("交款成功");
    }


    /**
     * 人工结束当前阶段收款
     * @param payId  进行人工结束收款的交款记录的id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto finishFinanceStageManual(String payId){
        //校验
        if(StringUtils.isBlank(payId)){
            return StatusDto.buildFailureStatusDto("缺失交款记录id");
        }
        //——查到交款
        PaymoneyRecord aimPaymentRecord = paymoneyRecordService.getPaymoneyRecordByPayId(payId);
        //——取到可结束收款的交款记录
        PaymoneyRecord latestPayment = paymoneyRecordService.getProjectAbleFinishPaymoneyRecord(aimPaymentRecord.getContractUuid());
        //——判断要结束收款的交款记录是否有效,不支持定金类型的交款，不支持退款类型的
        if(!CommonStatusEnum.VALID.toString().equals(aimPaymentRecord.getPayStatus()) || !payId.equals(latestPayment.getPayId())
                || FinancePayTypeEnum.DEPOSIT.toString().equals(aimPaymentRecord.getPayType())){
            return StatusDto.buildFailureStatusDto("无法对该交款进行结束收款操作");
        }
        //——查询要结束收款的阶段
        ProjectPayplanStage currentStage = projectPayplanStageService.getProjectStageWithCode(aimPaymentRecord.getStageCode());
        if(currentStage == null ){
            return StatusDto.buildFailureStatusDto("获取要结束的阶段异常");
        }
        //——要结束的阶段状态异常
        if(!currentStage.getCurentFlag() || currentStage.getStageFinished()){
            return StatusDto.buildFailureStatusDto("无法结束指定的交款阶段");
        }

        //进行结束当前收款阶段
        List<ProjectPayplanStage> processedStages = finishFinanceStage(currentStage,OpreatModeEnum.MANUAL);
        //——持久化交款阶段
        if(processedStages != null){
            for (ProjectPayplanStage processedStage : processedStages){
                projectPayplanStageService.update(processedStage);
            }
        }
        else if(currentStage != null){
            projectPayplanStageService.update(currentStage);
        }

        //成功
        return StatusDto.buildSuccessStatusDto("手动结束收款成功");
    }


    /**
     * 构建财务退款页面展示需要的DTO类
     * @param contractUuid 项目唯一uuid
     * @return
     */
    public StatusDto buildRefundSituation(String contractUuid){
        //查到订单
        CustomerContract project = projectService.getContractByUuid(contractUuid);
        if(project == null){
            return StatusDto.buildFailureStatusDto("订单不存在");
        }
        //构造退款页面展示用的dto的对象
        RefundRecordDto refundCondition = new RefundRecordDto();
        //——项目相关
        refundCondition.setContractUuid(project.getContractUuid());
        refundCondition.setContractCode(project.getContractCode());
        refundCondition.setRefundReceiverName(project.getCustomerName());
        refundCondition.setRefundReceiverMobile(project.getCustomerMobile());
        //——账户相关
        FinaProjectAccount finaAccount = finaProjectAccountService.getFinaceAccountByContratUuid(contractUuid);
        refundCondition.setDepositAbleBackAmount(finaAccount.getDepositTotalPayed().subtract(finaAccount.getDepositTotalDeduct()));
        refundCondition.setModifyAbleBackAmount(finaAccount.getModifyTotalPayed());
        refundCondition.setConstructAbleBackAmount(finaAccount.getConstructTotalPayed());
        //退款号
        refundCondition.setRefundNo(numberingRuleService.getNumber(project.getStoreCode(), NumberingTypeEnum.BACK_MONEY_NUMBER));
        return StatusDto.buildDataSuccessStatusDto(refundCondition);
    }



    /**
     * 执行退款
     * @param refundRecord
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveProjectRefundRecord(RefundRecord refundRecord,RefundRecord.RefundTypeEnum reundType){
        //校验传入的退款金额是否正确
        StatusDto checkRefundResult = checkRefundRecord(refundRecord);
        if(checkRefundResult != null){
            return checkRefundResult;
        }
        //当前阶段
        ProjectPayplanStage currentStage = projectPayplanStageService.getProjectCurrentStage(refundRecord.getContractUuid());
        //补全退款记录的值
        refundRecord.setOperator(WebUtils.getCurrentUserNameWithOrgCode());
        refundRecord.setOperateTime(new Date());
        refundRecord.setRefundType(reundType.toString());
        if(currentStage != null) {
            refundRecord.setCreateStageId(currentStage.getStageCode());
        }

        //用来存放新增的交款记录
        List<PaymoneyRecord> refundRecordList = new ArrayList<PaymoneyRecord>();

        //影响各个表
        FinaProjectAccount finaAccount = finaProjectAccountService.getFinaceAccountByContratUuid(refundRecord.getContractUuid());
        //——退 定金 造成的影响
        if(refundRecord.getRefundDepositAmount() != null && refundRecord.getRefundDepositAmount().compareTo(BigDecimal.ZERO)==1){
            //————转成负数
            BigDecimal refundDepositAmount = BigDecimal.ZERO.subtract(refundRecord.getRefundDepositAmount());

            ProjectPayplanStage depositStage = projectPayplanStageService.getProjectStageWithItemTempCode(refundRecord.getContractUuid(),
                    PayplanItem.PayplanItemCodeEnum.NODE_DEPOSIT);
            //————增加一条定金的负交款
            PaymoneyRecord depositRefundRecord = buildRefundRecord(refundRecord,depositStage);
            //————影响定金的payPlanStage
            if(depositStage != null){
                //————如果定金阶段是当前阶段
                if(depositStage.getStageCode().equals(currentStage.getStageCode())){
                    currentStage.setActualTotalReceived(currentStage.getActualTotalReceived().add(refundDepositAmount));
                }
                else{
                    depositStage.setActualTotalReceived(depositStage.getActualTotalReceived().add(refundDepositAmount));
                    projectPayplanStageService.update(depositStage);
                }
            }
            else{   //由于没有定金阶段，所以在buildRefundRecord方法中没有为以下字段赋值
                depositRefundRecord.setPayType(FinancePayTypeEnum.RETURN_DEPOSIT.toString());
                depositRefundRecord.setPayManualFlag(FinancePayTypeEnum.RETURN_DEPOSIT.getLabel());
                depositRefundRecord.setExpectReceived(refundRecord.getRefundDepositAmount());
                depositRefundRecord.setActualReceived(refundRecord.getRefundDepositAmount());
            }
            refundRecordList.add(depositRefundRecord);

            //————影响FinaProjectAccount
            finaAccount.setDepositTotalPayed(finaAccount.getDepositTotalPayed().add(refundDepositAmount));

            //退定金影响抵扣款
            Map<String,Object> depositDetailMap = JsonUtils.fromJson(refundRecord.getDepositRefundDetailStr(),Map.class);
            for(Map.Entry<String,Object> depositRefund : depositDetailMap.entrySet()){
                DeductMoney deductMoney = deductMoneyService.getById(Long.parseLong(depositRefund.getKey()));
                deductMoney.setMaxDeductAmount(deductMoney.getMaxDeductAmount().subtract(new BigDecimal(depositRefund.getValue().toString())));
                if(deductMoney.getMaxDeductAmount().compareTo(deductMoney.getDeductedAmount()) < 1){
                    deductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.USE_UP.toString());
                }
                deductMoneyService.update(deductMoney);
            }
        }
        //——退 拆改费 造成的影响
        if(refundRecord.getRefundModifyAmount() != null && refundRecord.getRefundModifyAmount().compareTo(BigDecimal.ZERO)==1){
            //————转成负数
            BigDecimal refundModifyAmount = BigDecimal.ZERO.subtract(refundRecord.getRefundModifyAmount());

            ProjectPayplanStage modifyStage = projectPayplanStageService.getProjectStageWithItemTempCode(refundRecord.getContractUuid(),
                    PayplanItem.PayplanItemCodeEnum.NODE_MODIFY);
            //————增加一条拆改费的负交款
            PaymoneyRecord modifyRefundRecord = buildRefundRecord(refundRecord,modifyStage);
            refundRecordList.add(modifyRefundRecord);

            //————影响FinaProjectAccount
            finaAccount.setModifyTotalPayed(finaAccount.getModifyTotalPayed().add(refundModifyAmount));

            //————影响拆改费的payPlanStage
            if(modifyStage != null){
                //————如果定金阶段是当前阶段
                if(modifyStage.getStageCode().equals(currentStage.getStageCode())){
                    currentStage.setActualTotalReceived(currentStage.getActualTotalReceived().add(refundModifyAmount));
                }
                else {
                    modifyStage.setActualTotalReceived(modifyStage.getActualTotalReceived().add(refundModifyAmount));
                    projectPayplanStageService.update(modifyStage);
                    //如果当前阶段是拆改费后的阶段，则将退的拆改费累加在当前阶段的往期少交款字段上
                    if(currentStage.getStageidx().compareTo(modifyStage.getStageidx()) == 1 ){
                        currentStage.setAgoUnpayAmount(currentStage.getAgoUnpayAmount().add(refundRecord.getRefundModifyAmount()));
                    }
                }
            }
        }
        //——退施工款造成的影响
        if(refundRecord.getRefundConstructAmount() != null && refundRecord.getRefundConstructAmount().compareTo(BigDecimal.ZERO)==1){
            //————转成负数
            BigDecimal refundConstructAmount = BigDecimal.ZERO.subtract(refundRecord.getRefundConstructAmount());
            //————增加一条施工款的负交款
            PaymoneyRecord constractRefundRecord = buildRefundRecord(refundRecord,currentStage);
            refundRecordList.add(constractRefundRecord);

            //————影响施工款的当前阶段
            BigDecimal agoUnpayAmount = BigDecimal.ZERO;
            //————当前阶段已收>应退
            if(refundRecord.getRefundConstructAmount().compareTo(currentStage.getActualTotalReceived()) < 1 ){
                currentStage.setActualTotalReceived(currentStage.getActualTotalReceived().subtract(refundRecord.getRefundConstructAmount()));
            }
            else{
                agoUnpayAmount = refundRecord.getRefundConstructAmount().subtract(currentStage.getActualTotalReceived());
                currentStage.setAgoUnpayAmount(currentStage.getAgoUnpayAmount().add(agoUnpayAmount));
                currentStage.setActualTotalReceived(BigDecimal.ZERO);
            }
            //————影响往期施工款阶段
            String aimAgoStageCode = currentStage.getPrevStageCode();
            while ( agoUnpayAmount.compareTo(BigDecimal.ZERO) > 0 && StringUtils.isNotBlank(aimAgoStageCode)){
                ProjectPayplanStage aimAgoStage = projectPayplanStageService.getProjectStageWithCode(aimAgoStageCode);
                if(agoUnpayAmount.compareTo(aimAgoStage.getActualTotalReceived()) < 1){
                    aimAgoStage.setActualTotalReceived(aimAgoStage.getActualTotalReceived().subtract(agoUnpayAmount));
                    agoUnpayAmount = BigDecimal.ZERO;
                }
                else{
                    agoUnpayAmount = agoUnpayAmount.subtract(aimAgoStage.getActualTotalReceived());
                    aimAgoStage.setActualTotalReceived(BigDecimal.ZERO);
                    aimAgoStageCode = aimAgoStage.getPrevStageCode();
                }
                projectPayplanStageService.update(aimAgoStage);
            }
            //————影响FinaProjectAccount
            finaAccount.setConstructTotalPayed(finaAccount.getConstructTotalPayed().add(refundConstructAmount));
        }

        //退单时 产生的扣除设计费扣款
        if(refundRecord.getDeductDesignAmount() != null && refundRecord.getDeductDesignAmount().compareTo(BigDecimal.ZERO) == 1){
            PaymoneyRecord deductDesignRecord= buildRefundRecord(refundRecord,null);
            deductDesignRecord.setPayType(FinancePayTypeEnum.DEDUCT_DESIGN_FEE.toString());
            deductDesignRecord.setPayManualFlag(FinancePayTypeEnum.DEDUCT_DESIGN_FEE.getLabel());
            deductDesignRecord.setExpectReceived(refundRecord.getDeductDesignAmount());
            deductDesignRecord.setActualReceived(refundRecord.getDeductDesignAmount());
            refundRecordList.add(deductDesignRecord);
        }
        //退单时 产生的扣除其他费扣款
        if(refundRecord.getDeductOtherAmount() != null && refundRecord.getDeductOtherAmount().compareTo(BigDecimal.ZERO) == 1){
            PaymoneyRecord deductOtherRecord= buildRefundRecord(refundRecord,null);
            deductOtherRecord.setPayType(FinancePayTypeEnum.DEDUCT_OTHER_FEE.toString());
            deductOtherRecord.setPayManualFlag(FinancePayTypeEnum.DEDUCT_OTHER_FEE.getLabel());
            deductOtherRecord.setExpectReceived(refundRecord.getDeductOtherAmount());
            deductOtherRecord.setActualReceived(refundRecord.getDeductOtherAmount());

            refundRecordList.add(deductOtherRecord);
        }

        //更新当前阶段
        projectPayplanStageService.update(currentStage);

        //批量插入新增的退款类型的交款记录
        paymoneyRecordService.insertList(refundRecordList);

        //插入退款记录
        refundRecordService.insert(refundRecord);

        //更新项目的财务账户
        finaProjectAccountService.update(finaAccount);

        //插入财务操作记录
        finaOperatLogService.addFinaRefundLog(refundRecord);

        //顺利完成
        return StatusDto.buildSuccessStatusDto("退款成功");
    }


    /**
     * 人工结束定金阶段收款
     * @return
     */
    public StatusDto finishDepositStageManual(String contractUuid){
        //校验
        CustomerContract project = projectService.getContractByUuid(contractUuid);
        if(project == null){
            return StatusDto.buildFailureStatusDto("项目不存在");
        }
        if(project.getSignDeposit() !=null && project.getSignDeposit() > 0){
            return StatusDto.buildFailureStatusDto("该项目已完成预定合同");
        }
        ProjectPayplanStage depositStage = projectPayplanStageService.getProjectStageWithItemTempCode(contractUuid,
                PayplanItem.PayplanItemCodeEnum.NODE_DEPOSIT);
        if(depositStage == null){
            return StatusDto.buildFailureStatusDto("该项目无需通过定金交款阶段");
        }
        //此处只是为了结束定金阶段收款，并且防止覆盖交定金对stage的影响，所以不update阶段实收，将金额设为null
        depositStage.setActualTotalReceived(null);
        depositStage.setAgoUnpayAmount(null);
        depositStage.setExpectReceived(null);

        //进行结束当前收款阶段
        List<ProjectPayplanStage> processedStages = finishFinanceStage(depositStage,OpreatModeEnum.MANUAL);
        //——持久化交款阶段
        if(processedStages != null){
            for (ProjectPayplanStage processedStage : processedStages){
                projectPayplanStageService.update(processedStage);
            }
        }
        else if(depositStage != null){
            projectPayplanStageService.update(depositStage);
        }

        return StatusDto.buildSuccessStatusDto("预定合同签约成功");
    }


    /**
     * 执行退单退款
     * @param refundRecord
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto closeProjectAfterRefund(RefundRecord refundRecord){
        //执行退款
        StatusDto refundResult = saveProjectRefundRecord(refundRecord, RefundRecord.RefundTypeEnum.ORDERCLOSEREFUND);
        if(!refundResult.isSuccess()){
            return refundResult;
        }

        //将交款记录状态 改为无效
        paymoneyRecordService.updateProjectPaymentStatus(refundRecord.getContractUuid(),CommonStatusEnum.INVALID.toString());

        //执行退单，更改订单状态为关闭
        CustomerContract constract = projectService.getContractByUuid(refundRecord.getContractUuid());
        constract.setOrderFlowStatus(CustomerContractEnum.ORDER_CLOSE);
        projectService.updateContractStatus(constract );

        //插入订单操作日志
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateType(OperateLogEnum.ORDER_CLOSE);
        operateLog.setOperator(WebUtils.getLoggedUser().getName());
        operateLog.setOperateTime(new Date());
        operateLog.setOperateDescription(OperateLogEnum.ORDER_CLOSE.getLabel());
        operateLog.setSystemType("2");
        operateLog.setContractCode(constract.getContractCode());
        this.operateLogService.insert(operateLog);

        //通知crm发生了退单
        crmApiService.sendDepositInfoToCrm(null,constract);

        //插入退单操作记录
        finaOperatLogService.addFinaBackRefundLog(refundRecord);

        //退单成功
        return StatusDto.buildSuccessStatusDto("退单成功");
    }



    /**
     * 完善要保存的交款记录信息
     * @param paymentRecord 要保存的交款记录
     * @param currentStage 当前交款阶段
     * @return
     */
    private PaymoneyRecord fillNewPaymentRecordInfo(PaymoneyRecord paymentRecord,ProjectPayplanStage currentStage,
                                                    FinaPaymethod paymethod,PaymethodAttr paymethodAttr){
        //交款记录id
        paymentRecord.setPayId(UUID.randomUUID().toString());
        if(currentStage != null){
            paymentRecord.setTemplateStageId(currentStage.getStageTemplateId());
            paymentRecord.setTemplateStageCode(currentStage.getStageTemplateCode());
            paymentRecord.setPayType(currentStage.getStageType());
        }
        //因为有些交款规划中没有定金阶段，所以currentStage为null，但是交款类型是定金
        else{
            paymentRecord.setPayType(FinancePayTypeEnum.DEPOSIT.toString());
        }
        paymentRecord.setIfRcw(false);
        paymentRecord.setCreateTime(new Date());
        paymentRecord.setCreator(WebUtils.getCurrentUserNameWithOrgCode());
        paymentRecord.setCashier(WebUtils.getCurrentUserNameWithOrgCode());
        paymentRecord.setPayStatus(CommonStatusEnum.VALID.toString());
        paymentRecord.setPrintCount(0);
        if(FinancePayTypeEnum.DEPOSIT.toString().equals(paymentRecord.getPayType())){
            paymentRecord.setIfAbleDeduct(true);
        }
        else{
            paymentRecord.setIfAbleDeduct(false);
        }

        //交款方式
        if(paymethod != null) {
            paymentRecord.setPaymethodCode(paymethod.getMethodCode());
            paymentRecord.setPaymethodName(paymethod.getMethodName());
        }
        //交款方式属性
        if(paymethodAttr != null) {
            paymentRecord.setPaymethodAttrId(Long.parseLong(paymethodAttr.getId().toString()));
        }

        //关联使用的使用抵扣金
        if(paymentRecord.getUsedDeductMoneyId() != null){
            paymentRecord.setRelateRecordId(paymentRecord.getUsedDeductMoneyId().toString());
        }
        return paymentRecord;
    }


    /**
     * 交款后影响项目的财务账户和抵扣金
     * @param paymentRecord 交款记录
     * @return
     */
    private FinaProjectAccount effectAccountDeductAfterPayment(PaymoneyRecord paymentRecord){
        FinaProjectAccount projectAccount = finaProjectAccountService.getFinaceAccountByContratUuid(paymentRecord.getContractUuid());
        //交的定金
        if(FinancePayTypeEnum.DEPOSIT.toString().equals(paymentRecord.getPayType())){
            projectAccount.setDepositTotalPayed(projectAccount.getDepositTotalPayed().add(paymentRecord.getActualReceived()));
            //——创建一笔抵定金
            DeductMoney depositDeductMoney = new DeductMoney();
            depositDeductMoney.setPayId(paymentRecord.getPayId());
            depositDeductMoney.setReceiptNo(paymentRecord.getReceiptNum());
            depositDeductMoney.setContractNo(paymentRecord.getContractCode());
            depositDeductMoney.setContractUuid(paymentRecord.getContractUuid());
            depositDeductMoney.setMaxDeductAmount(paymentRecord.getActualReceived());
            depositDeductMoney.setDeductType(DeductMoney.DeductMoneyTypeEnum.DEPOSIT_DEDUCT.toString());
            depositDeductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.ABLE_DEDUCT.toString());
            depositDeductMoney.setDeductedAmount(BigDecimal.ZERO);
            depositDeductMoney.setCreator(paymentRecord.getCreator());
            depositDeductMoney.setCreatTime(new Date());
            deductMoneyService.insert(depositDeductMoney);

        }
        //交的拆改费
        else if(FinancePayTypeEnum.MODIFY.toString().equals(paymentRecord.getPayType())){
            projectAccount.setModifyTotalPayed(projectAccount.getModifyTotalPayed().add(paymentRecord.getActualReceived()));
        }
        //交的施工款
        else if(FinancePayTypeEnum.CONSTRUCT.toString().equals((paymentRecord.getPayType()))){
            projectAccount.setConstructTotalPayed(projectAccount.getConstructTotalPayed().add(paymentRecord.getActualReceived()));
        }
        //如果是抵定金等需要消耗已交定金的支付方式，则累加已抵扣量
        if(FinaPaymethod.PaymethodSpecialCodeEnum.PAY_DDJSJDK.toString().equals(paymentRecord.getPaymethodCode())){
            projectAccount.setDepositTotalDeduct(projectAccount.getDepositTotalDeduct().add(paymentRecord.getActualReceived()));
            //——使用抵定金
            DeductMoney depositDeductMoney = deductMoneyService.getById(paymentRecord.getUsedDeductMoneyId());
            depositDeductMoney.setLatestDeductTime(new Date());
            depositDeductMoney.setDeductedAmount(depositDeductMoney.getDeductedAmount().add(paymentRecord.getActualReceived()));
            //——抵扣额度已用尽
            if(depositDeductMoney.getMaxDeductAmount().compareTo(depositDeductMoney.getDeductedAmount()) < 1){
                depositDeductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.USE_UP.toString());
            }
            else{
                depositDeductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.ABLE_DEDUCT.toString());
            }
            //——累加抵扣次数
            depositDeductMoney.setDeductTimes(depositDeductMoney.getDeductTimes() + 1);
            deductMoneyService.update(depositDeductMoney);
        }
        return projectAccount;
    }



    /**
     * 交款后累加财务阶段的累计实收金额
     * @currentStage paymentRecord
     * @return
     */
    private ProjectPayplanStage effectFinanceStageAfterPayment(PaymoneyRecord paymentRecord){
        ProjectPayplanStage currentStage = null;
        if (StringUtils.isNotEmpty(paymentRecord.getStageCode())){
            currentStage = projectPayplanStageService.getProjectStageWithCode(paymentRecord.getStageCode());
        }
        if (currentStage != null) {
            currentStage.setActualTotalReceived(currentStage.getActualTotalReceived().add(paymentRecord.getActualReceived()));
        }
        return currentStage;
    }

    /**
     * 自动结束当前收款阶段
     * @param currentStage 当前收款阶段
     * @return
     */
    private List<ProjectPayplanStage> autoFinishFinanceStage(ProjectPayplanStage currentStage){
        //用来存放财务阶段流转后的前后阶段
        List<ProjectPayplanStage> processedStages = null;
        if(currentStage == null){
            return null;
        }
        PayplanItem stageTemp = payplanService.getPlanItemWithItemId(Long.valueOf(currentStage.getStageTemplateId().longValue()));
        //应该交的钱<=0，1=大于；0=等于；-1=小于
        int hasStageFinished = projectPayplanStageService.calculateStageSummaryExpectedReceived(currentStage).subtract(stageTemp.getAllowLessAmount())
                .compareTo(BigDecimal.ZERO);
        //该阶段完成交款且允许自动流转
        if(stageTemp.getAllowAutoTrans() && hasStageFinished < 1)
        {
            processedStages = finishFinanceStage(currentStage,OpreatModeEnum.SYSTEM);
        }
        return processedStages;
    }


    /**
     * 结束指定财务收款阶段
     * @param currentStage
     * @param optMode
     * @return
     */
    private List<ProjectPayplanStage> finishFinanceStage(ProjectPayplanStage currentStage,OpreatModeEnum optMode){
        //用来存放财务阶段流转后的前后阶段
        List<ProjectPayplanStage> processedStages = null;

        currentStage.setStageFinished(true);
        currentStage.setFinishMode(optMode.toString());

        String optorName = "";
        if(OpreatModeEnum.MANUAL.equals(optMode)){
            optorName = WebUtils.getCurrentUserNameWithOrgCode();
        }
        else{
            optorName = OpreatModeEnum.SYSTEM.toString();
        }
        currentStage.setFinishOperator(optorName);
        currentStage.setFinishTime(new Date());

        //执行各种类型的交款阶段完成做的特殊操作
        CustomerContract project = projectService.getContractByUuid(currentStage.getContractUuid());
        //定金交款类型
        if (PayplanItem.PayplanItemTypeEnum.DEPOSIT.toString().equals(currentStage.getStageType()) &&
                (project.getSignDeposit() == null ? Integer.valueOf(0) :project.getSignDeposit()) < 1){
            //调用更改项目状态的接口
            project.setOrderFlowStatus(CustomerContractEnum.SUPERVISOR_STAY_ASSIGNED);
            project.setSignDeposit(1);
            projectService.updateContractStatus(project);
            //通知CRM，更改是否已签定金合同
            crmApiService.sendDepositInfoToCrm(null,project);
        }
        else{
            //清算变更
            ProjectchangeMoney updChangeMoneyParam = new ProjectchangeMoney();
            updChangeMoneyParam.setCleared(true);
            updChangeMoneyParam.setClearStageId(currentStage.getStageCode());
            updChangeMoneyParam.setClearStageName(currentStage.getStageName());
            updChangeMoneyParam.setClearedTime(new Date());
            updChangeMoneyParam.setEffectStageId(currentStage.getStageCode());
            projectChangeMoneyService.clearChangeMoneyInStage(updChangeMoneyParam);

            //清算赔款
            ReparationMoney updReparationParam = new ReparationMoney();
            updReparationParam.setCleared(true);
            updReparationParam.setClearStageId(currentStage.getStageCode());
            updReparationParam.setClearStageName(currentStage.getStageName());
            updReparationParam.setClearTime(new Date());
            updReparationParam.setEffectStageId(currentStage.getStageCode());
            reparationMoneyService.clearReparationMoneyInStage(updReparationParam);
        }

        //如果指定了下个阶段，则进行财务阶段流转，定金阶段在签约前无法转移，因为签约前无法确定下阶段是哪个
        if (StringUtils.isNotBlank(currentStage.getNextStageCode())){
            ProjectPayplanStage nextStage = projectPayplanStageService.getProjectStageWithCode(currentStage.getNextStageCode());
            processedStages = projectPayplanStageService.financeStageTransform(currentStage,nextStage);
        }

        //调用产业工人接口，推送交款信息
        outApiService.pushAmountToMps(currentStage);
        return processedStages;
    }


    /**
     * 校验收款记录
     * @param paymentRecord 交款记录
     * @return
     */
    private StatusDto checkPaymentRecord(PaymoneyRecord paymentRecord,FinaPaymethod paymethod,PaymethodAttr paymethodAttr){
        //校验必要字段
        if(paymentRecord.getActualReceived() == null || StringUtils.isBlank(paymentRecord.getContractUuid()) ||
                StringUtils.isBlank(paymentRecord.getReceiptNum())){
            return StatusDto.buildFailureStatusDto("缺失交款操作必要的数据");
        }
        if(paymentRecord.getActualReceived().compareTo(BigDecimal.ZERO) <1){
            return StatusDto.buildFailureStatusDto("交款金额为0不科学");
        }
        //校验交款方式是否合适
        if(paymethod == null || !CommonStatusEnum.ENABLE.toString().equals(paymethod.getMethodStatus())){
            return StatusDto.buildFailureStatusDto("交款方式异常");
        }
        //不是定金阶段的交款阶段如果已经结束，不能进行交款
        ProjectPayplanStage aimStage = projectPayplanStageService.getProjectStageWithCode(paymentRecord.getStageCode());
        if(aimStage != null && !PayplanItem.PayplanItemCodeEnum.NODE_DEPOSIT.toString().equals(aimStage.getStageTemplateCode())
                && aimStage.getStageFinished()){
            return StatusDto.buildFailureStatusDto("目标交款阶段已结束");
        }

        //校验手续费
        //——后台计算手续费，和前台比较
        Double costRate = paymethod.getCostRate();
        BigDecimal minBackCostFee = paymethod.getMinCostfee();
        BigDecimal maxBackCostFee = paymethod.getMaxCostfee();
        if(paymethodAttr != null){
            costRate = paymethodAttr.getCostRate();
            minBackCostFee = paymethodAttr.getMinCostfee();
            maxBackCostFee = paymethodAttr.getMaxCostfee();
        }
        BigDecimal backCostFee = paymentRecord.getActualReceived().multiply(new BigDecimal(costRate));
        //过滤最小手续费
        if(minBackCostFee != null && minBackCostFee.compareTo(backCostFee) > 0 ){
            backCostFee = minBackCostFee;
        }
        //过滤最大手续费
        if(maxBackCostFee != null && maxBackCostFee.compareTo(backCostFee) <0 ){
            backCostFee = maxBackCostFee;
        }
        //入参出来的手续费和后台计算的误差允许0.1元
        if(paymentRecord.getCostfeeAmount() == null || paymentRecord.getCostfeeAmount().subtract(backCostFee).abs()
                .compareTo(new BigDecimal(0.1)) >0){
            return StatusDto.buildFailureStatusDto("手续费不正确");
        }


        //如果是抵定金交款方式要校验抵定金
        if(FinaPaymethod.PaymethodSpecialCodeEnum.PAY_DDJSJDK.toString().equals(paymentRecord.getPaymethodCode())){
            DeductMoney deductMoney = deductMoneyService.getById(paymentRecord.getUsedDeductMoneyId());
            if(deductMoney == null || !deductMoney.getContractUuid().equals(paymentRecord.getContractUuid())
                    || !DeductMoney.DeductMoneyStatusEnum.ABLE_DEDUCT.toString().equals(deductMoney.getDeductStatus())){
                return StatusDto.buildFailureStatusDto("无发使用的抵定金");
            }
            if (paymentRecord.getActualReceived().compareTo(deductMoney.getAbleDeductAmount()) > 0 ){
                return StatusDto.buildFailureStatusDto("抵定金抵扣不了这么多");
            }
        }
        if(paymoneyRecordService.paymentReceiptExist(paymentRecord.getReceiptNum())){
            return StatusDto.buildFailureStatusDto("收据号已存在");
        }

        //校验通过
        return null;
    }


    /**
     * 对退款的入参进行校验
     * @param refundRecord
     * @return
     */
    private StatusDto checkRefundRecord(RefundRecord refundRecord){
        //校验传入的退款金额是否正确
        //——必要字段判空，以及某些字段非空处理
        if(refundRecord == null || StringUtils.isBlank(refundRecord.getContractUuid()) || refundRecord.getRefundActualAmount() == null){
            return StatusDto.buildFailureStatusDto("缺失退款操作必要的数据");
        }
        if(refundRecord.getRefundDepositAmount() == null && refundRecord.getRefundModifyAmount() == null
                && refundRecord.getRefundConstructAmount() == null){
            return StatusDto.buildFailureStatusDto("缺失退款的具体出处");
        }

        //——校验某阶段是否可退，顺便计算当前要退各款项最大可退金额
        BigDecimal maxAbleReturnAmount = BigDecimal.ZERO ;
        FinaProjectAccount finaAccount = finaProjectAccountService.getFinaceAccountByContratUuid(refundRecord.getContractUuid());
        if(finaAccount == null){
            return StatusDto.buildFailureStatusDto("项目财务账户异常");
        }
        //————定金
        if(refundRecord.getRefundDepositAmount() != null ){
            if(StringUtils.isBlank(refundRecord.getDepositRefundDetailStr())){
                return StatusDto.buildFailureStatusDto("缺失定金退款具体配置");
            }
            maxAbleReturnAmount = maxAbleReturnAmount.add(finaAccount.getDepositTotalPayed());
            if(finaAccount.getDepositTotalPayed().subtract(finaAccount.getDepositTotalDeduct()).compareTo(refundRecord.getRefundDepositAmount()) < 0) {
                return StatusDto.buildFailureStatusDto("可退定金不足");
            }
            //——————退定金影响抵扣款的影响
            //解析前台传的json
            Map<String,Object> depositDetailMap = JsonUtils.fromJson(refundRecord.getDepositRefundDetailStr(),Map.class);
            for(Map.Entry<String,Object> depositRefund : depositDetailMap.entrySet()){
                DeductMoney deductMoney = deductMoneyService.getById(Long.parseLong(depositRefund.getKey()));
                //——————要退的定金比可退的多
                if(new BigDecimal(depositRefund.getValue().toString()).compareTo(deductMoney.getAbleDeductAmount()) > 0){
                    StringBuffer errSb = new StringBuffer();
                    errSb.append(deductMoney.getReceiptNo()).append("抵定金退不了这么多");
                    return StatusDto.buildFailureStatusDto(errSb.toString());
                }
            }
        }
        //————拆改费
        if(refundRecord.getRefundModifyAmount() != null ){
            maxAbleReturnAmount = maxAbleReturnAmount.add(finaAccount.getModifyTotalPayed());
            if(finaAccount.getModifyTotalPayed().compareTo(refundRecord.getRefundModifyAmount()) < 0) {
                return StatusDto.buildFailureStatusDto("可退拆改费不足");
            }
        }
        //————施工款
        if(refundRecord.getRefundConstructAmount() != null ){
            maxAbleReturnAmount = maxAbleReturnAmount.add(finaAccount.getConstructTotalPayed());
            if(finaAccount.getConstructTotalPayed().compareTo(refundRecord.getRefundConstructAmount()) < 0){
                return StatusDto.buildFailureStatusDto("可退施工款不足");
            }
        }

        //——计算最大可退金额
        //————算上设计费、其他费等扣款
        if(refundRecord.getDeductDesignAmount() != null){
            maxAbleReturnAmount = maxAbleReturnAmount.subtract(refundRecord.getDeductDesignAmount());
        }
        if(refundRecord.getDeductOtherAmount() != null){
            maxAbleReturnAmount = maxAbleReturnAmount.subtract(refundRecord.getDeductOtherAmount());
        }
        if(refundRecord.getRefundActualAmount().compareTo(maxAbleReturnAmount) == 1){
            return StatusDto.buildFailureStatusDto("期望退款金额大于实际可退金额");
        }

        //校验通过
        return null;
    }


    /**
     * 构造退款的交款记录
     * @param refundRecord 退款记录实体
     * @param payplanStage 退款针对的交款阶段
     * @return
     */
    private PaymoneyRecord buildRefundRecord(RefundRecord refundRecord,ProjectPayplanStage payplanStage){
        Date nowTime = new Date();

        PaymoneyRecord refundPaymentRecord = new PaymoneyRecord();
        refundPaymentRecord.setPayId(UUID.randomUUID().toString());
        refundPaymentRecord.setContractCode(refundRecord.getContractCode());
        refundPaymentRecord.setContractUuid(refundRecord.getContractUuid());

        refundPaymentRecord.setPayerName(refundRecord.getRefundReceiverName());
        refundPaymentRecord.setPayerMobile(refundRecord.getRefundReceiverMobile());
        refundPaymentRecord.setPayTime(nowTime);
        refundPaymentRecord.setReceiptNum(refundRecord.getRefundNo());
        refundPaymentRecord.setIfRcw(false);
        refundPaymentRecord.setCreateTime(nowTime);
        refundPaymentRecord.setCreator(WebUtils.getCurrentUserNameWithOrgCode());
        refundPaymentRecord.setPayStatus(CommonStatusEnum.VALID.toString());
        refundPaymentRecord.setPrintCount(0);
        refundPaymentRecord.setCashier(refundPaymentRecord.getCreator());
        refundPaymentRecord.setIfAbleDeduct(false);
        refundPaymentRecord.setRemark(refundRecord.getRefundNo());

        if(payplanStage != null) {
            refundPaymentRecord.setStageCode(payplanStage.getStageCode());
            refundPaymentRecord.setTemplateStageId(payplanStage.getStageTemplateId());
            refundPaymentRecord.setTemplateStageCode(payplanStage.getStageTemplateCode());
            //需要根据阶段类型特殊赋值的字段
            if (FinancePayTypeEnum.DEPOSIT.toString().equals(payplanStage.getStageType())) {
                refundPaymentRecord.setPayType(FinancePayTypeEnum.RETURN_DEPOSIT.toString());
                refundPaymentRecord.setPayManualFlag(FinancePayTypeEnum.RETURN_DEPOSIT.getLabel());
                refundPaymentRecord.setExpectReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundDepositAmount()));
                refundPaymentRecord.setActualReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundDepositAmount()));
            } else if (FinancePayTypeEnum.MODIFY.toString().equals(payplanStage.getStageType())) {
                refundPaymentRecord.setPayType(FinancePayTypeEnum.RETURN_MODIFY.toString());
                refundPaymentRecord.setPayManualFlag(FinancePayTypeEnum.RETURN_MODIFY.getLabel());
                refundPaymentRecord.setExpectReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundModifyAmount()));
                refundPaymentRecord.setActualReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundModifyAmount()));
            } else if (FinancePayTypeEnum.CONSTRUCT.toString().equals(payplanStage.getStageType())) {
                refundPaymentRecord.setPayType(FinancePayTypeEnum.RETURN_CONSTRUCT.toString());
                refundPaymentRecord.setPayManualFlag(FinancePayTypeEnum.RETURN_CONSTRUCT.getLabel());
                refundPaymentRecord.setExpectReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundConstructAmount()));
                refundPaymentRecord.setActualReceived(BigDecimal.ZERO.subtract(refundRecord.getRefundConstructAmount()));
            }
        }

        return refundPaymentRecord;
    }


    /**
     * 红冲
     * @param payId 交款记录id
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto redPunch(String payId) {
        //校验
        //——校验主要记录是否存在
        PaymoneyRecord paymoneyRecord = paymoneyRecordService.getPaymoneyRecordByPayId(payId);
        if(paymoneyRecord == null){
            return StatusDto.buildFailureStatusDto("要红冲的交款记录不存在");
        }
        FinaProjectAccount finaceAccount = finaProjectAccountService.getFinaceAccountByContratUuid(paymoneyRecord.getContractUuid());
        if(finaceAccount == null){
            return StatusDto.buildFailureStatusDto("项目的交款账户不存在");
        }
        //校验可红冲类型
        PaymoneyRecord ableRcwPayRecord = paymoneyRecordService.getProjectAbleRcwPaymoneyRecord(paymoneyRecord.getContractUuid());
        if(ableRcwPayRecord == null || !payId.equals(ableRcwPayRecord.getPayId())){
            return StatusDto.buildDataSuccessStatusDto("选中的交款记录不能红冲");
        }

        //创建红冲记录，影响paymoneyRecord
        PaymoneyRecord payRecordRedPunch = new PaymoneyRecord();
        BeanUtils.copyProperties(paymoneyRecord , payRecordRedPunch);
        payRecordRedPunch.setIfRcw(true);
        payRecordRedPunch.setRcwPayid(payId);
        payRecordRedPunch.setActualReceived(BigDecimal.ZERO.subtract(payRecordRedPunch.getActualReceived()));
        payRecordRedPunch.setExpectReceived(payRecordRedPunch.getActualReceived());
        paymoneyRecordService.insert(payRecordRedPunch);
        paymoneyRecord.setIfRcw(true);
        paymoneyRecordService.update(paymoneyRecord);

        //影响抵扣金一整套的数据
        //——红冲定金类型的交款
        if(PayplanItem.PayplanItemTypeEnum.DEPOSIT.toString().equals(paymoneyRecord.getPayType())){
            //——撤回fina_deduct_money表
            Map<String, Object> params = Maps.newHashMap();
            MapUtils.putNotNull(params, "payId", paymoneyRecord.getPayId());
            List<DeductMoney> deductMoneyList = deductMoneyService.findDeductMoney(params);
            if(deductMoneyList != null && deductMoneyList.size() > 0){
                DeductMoney deductMoney = deductMoneyList.get(0);
                if(deductMoney.getDeductedAmount().compareTo(BigDecimal.ZERO) < 1){
                    BigDecimal maxDeductAmount = deductMoney.getMaxDeductAmount().subtract(paymoneyRecord.getActualReceived());
                    deductMoney.setMaxDeductAmount(maxDeductAmount);
                    deductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.INVALID.toString());
                    deductMoneyService.update(deductMoney);
                }
            }
        }
        //——红冲交款方式为抵定金的交款
        else if (FinaPaymethod.PaymethodSpecialCodeEnum.PAY_DDJSJDK.toString().equals(paymoneyRecord.getPaymethodCode())) {
            //————影响抵定金
            DeductMoney deductMoney = deductMoneyService.getById(Long.parseLong(paymoneyRecord.getRelateRecordId()));
            BigDecimal deductedAmount = deductMoney.getDeductedAmount().subtract(paymoneyRecord.getActualReceived());
            deductMoney.setDeductedAmount(deductedAmount);
            deductMoney.setDeductStatus(DeductMoney.DeductMoneyStatusEnum.ABLE_DEDUCT.toString());
            deductMoneyService.update(deductMoney);
            //————影响account
            finaceAccount.setDepositTotalDeduct(finaceAccount.getDepositTotalDeduct().subtract(paymoneyRecord.getActualReceived()));
        }

        //影响交款阶段（payplanStage）
        if(payRecordRedPunch.getStageCode() != null){
            ProjectPayplanStage projectPayplanStage = projectPayplanStageService.getProjectStageWithCode(payRecordRedPunch.getStageCode());
            if(projectPayplanStage != null ){
                //——假如是当前阶段,修改stage表中的实收金额-红冲金额
                BigDecimal actualTotalReceived = projectPayplanStage.getActualTotalReceived().add(payRecordRedPunch.getActualReceived());
                projectPayplanStage.setActualTotalReceived(actualTotalReceived);

                //——如果被红冲的是定金类型的交款，不影响当前阶段的往期未缴款
                if(!projectPayplanStage.getCurentFlag() && !FinancePayTypeEnum.DEPOSIT.toString().equals(paymoneyRecord.getPayType())){
                    //——假如不是当前阶段,查出当前阶段，并且修改往期累计未交金额，加上本次红冲金额
                    ProjectPayplanStage currentStage = projectPayplanStageService.getProjectCurrentStage(paymoneyRecord.getContractUuid());
                    if(currentStage != null){
                        currentStage.setAgoUnpayAmount(currentStage.getAgoUnpayAmount().add(paymoneyRecord.getActualReceived()));
                        projectPayplanStageService.update(currentStage);
                    }
                }
                //持久化交款阶段
                projectPayplanStageService.update(projectPayplanStage);
            }
        }

        //修改account表中对应阶段金额
        //假如是定金类型的交款,定金实收金额-红冲金额
        if(PayplanItem.PayplanItemTypeEnum.DEPOSIT.toString().equals(payRecordRedPunch.getPayType())){
            BigDecimal depositTotalPayed = finaceAccount.getDepositTotalPayed().add(payRecordRedPunch.getActualReceived());
            finaceAccount.setDepositTotalPayed(depositTotalPayed);
        }
        //假如是拆改阶段,拆改实收金额-红冲金额
        else if (PayplanItem.PayplanItemTypeEnum.MODIFY.toString().equals(payRecordRedPunch.getPayType())) {
            BigDecimal modifyTotalPayed = finaceAccount.getModifyTotalPayed().add(payRecordRedPunch.getActualReceived());
            finaceAccount.setModifyTotalPayed(modifyTotalPayed);
        }
        //假如是施工阶段,施工实收金额-红冲金额
        else if (PayplanItem.PayplanItemTypeEnum.CONSTRUCT.toString().equals(payRecordRedPunch.getPayType())) {
            BigDecimal constructTotalPayed = finaceAccount.getConstructTotalPayed().add(payRecordRedPunch.getActualReceived());
            finaceAccount.setConstructTotalPayed(constructTotalPayed);
        }
        //持久化Account
        finaProjectAccountService.update(finaceAccount);

        //插入红冲日志
        finaOperatLogService.addFinaPayRcwLog(paymoneyRecord);

        //操作成功
        return StatusDto.buildSuccessStatusDto("红冲成功");
    }


    /**
     * 根据动态条件查询抵扣金
     * @param queryParam
     * @return
     */
    public List<DeductMoney> queryDeductMoneyWithCondition(Map<String,Object> queryParam){
        return deductMoneyService.queryDeductWithCondition(queryParam);
    }

    /**
     * 修改有无可退字样
     * @return
     */
    public StatusDto checkRefundable(String contractUuid,int returnWord){
        //校验
        CustomerContract project = projectService.getContractByUuid(contractUuid);
        if(project == null){
            return StatusDto.buildFailureStatusDto("项目不存在");
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("returnWord", returnWord);
        params.put("contractUuid", contractUuid);
        customerContractDao.updateRefundable(params);

        //通知CRM，更改定金是否可退信息
        project.setReturnWord(returnWord);
        crmApiService.sendDepositInfoToCrm(null,project);
        return StatusDto.buildSuccessStatusDto("可退字样修改成功");
    }


}
