package cn.mdni.business.dto.finance;

import cn.mdni.business.entity.finance.FinaPaymethod;
import cn.mdni.business.entity.finance.ProjectPayplanStage;
import cn.mdni.business.entity.finance.ProjectchangeMoney;
import cn.mdni.business.entity.finance.ReparationMoney;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liupengfei
 * @Description 项目交款阶段Dto类
 * @Date Created in 2017/11/23 19:03
 */
public class ProjectPayStageDto {

    /**
     * 交款阶段(可能为空，因为交款规划中可能没有定金阶段)
     */
    private ProjectPayplanStage payStage;

    /**
     * 定金阶段的uuid code
     */
    private String depositStageCode;


    /**
     * 本阶段考虑变更、赔款后的综合应收
     */
    private BigDecimal stageExpectedPay;

    /**
     * 定金阶段应收
     */
    private BigDecimal depositExpectPay;

    /**
     * 影响本阶段的变更总额
     */
    private BigDecimal stageTotalChange;

    /**
     * 影响本阶段的变更列表
     */
    private List<ProjectchangeMoney> stageChangeMoneyList;

    /**
     * 影响本阶段的赔款总额
     */
    private BigDecimal stageTotalReparation;


    /**
     * 影响本阶段的赔款列表
     */
    private List<ReparationMoney> stageReparationMoneyList;


    /**
     * 项目唯一uuid编号
     */
    private String contractUuid;


    /**
     * 项目号
     */
    private String contractNo;


    /**
     * 客户姓名
     */
    private String customerName;


    /**
     * 客户电话
     */
    private String customerMobile;


    /**
     * 每次打开交款页面时都重新生成的收据号
     */
    private String receiptNum;


    /**
     * 已签定金合同
     */
    private Boolean signedDepositContract;


    /**
     * 定金是否可退
     */
    private Boolean depositEnableReturnBack;


    /**
     * 当前阶段允许使用的交款方式
     */
    private List<FinaPaymethod> currentStageMethodList;


    /**
     * 定金阶段允许使用的交款方式
     */
    private List<FinaPaymethod> depositStageMethodList;


    /**
     * 当前阶段可选的交款描述字符串
     */
    private List<String> curStagePaymentFlagList;


    /**
     * 定金界面可选的交款描述字符串
     */
    private List<String> depositPaymentFlagList;



    public ProjectPayplanStage getPayStage() {
        return payStage;
    }

    public void setPayStage(ProjectPayplanStage payStage) {
        this.payStage = payStage;
    }


    public BigDecimal getStageExpectedPay() {
        return stageExpectedPay;
    }

    public void setStageExpectedPay(BigDecimal stageExpectedPay) {
        this.stageExpectedPay = stageExpectedPay;
    }

    public BigDecimal getStageTotalChange() {
        return stageTotalChange;
    }

    public void setStageTotalChange(BigDecimal stageTotalChange) {
        this.stageTotalChange = stageTotalChange;
    }

    public List<ProjectchangeMoney> getStageChangeMoneyList() {
        return stageChangeMoneyList;
    }

    public void setStageChangeMoneyList(List<ProjectchangeMoney> stageChangeMoneyList) {
        this.stageChangeMoneyList = stageChangeMoneyList;
    }

    public BigDecimal getStageTotalReparation() {
        return stageTotalReparation;
    }

    public void setStageTotalReparation(BigDecimal stageTotalReparation) {
        this.stageTotalReparation = stageTotalReparation;
    }

    public List<ReparationMoney> getStageReparationMoneyList() {
        return stageReparationMoneyList;
    }

    public void setStageReparationMoneyList(List<ReparationMoney> stageReparationMoneyList) {
        this.stageReparationMoneyList = stageReparationMoneyList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Boolean getSignedDepositContract() {
        return signedDepositContract;
    }

    public void setSignedDepositContract(Boolean signedDepositContract) {
        this.signedDepositContract = signedDepositContract;
    }

    public Boolean getDepositEnableReturnBack() {
        return depositEnableReturnBack;
    }

    public void setDepositEnableReturnBack(Boolean depositEnableReturnBack) {
        this.depositEnableReturnBack = depositEnableReturnBack;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getDepositExpectPay() {
        return depositExpectPay;
    }

    public void setDepositExpectPay(BigDecimal depositExpectPay) {
        this.depositExpectPay = depositExpectPay;
    }

    public List<FinaPaymethod> getCurrentStageMethodList() {
        return currentStageMethodList;
    }

    public void setCurrentStageMethodList(List<FinaPaymethod> currentStageMethodList) {
        this.currentStageMethodList = currentStageMethodList;
    }

    public List<FinaPaymethod> getDepositStageMethodList() {
        return depositStageMethodList;
    }

    public void setDepositStageMethodList(List<FinaPaymethod> depositStageMethodList) {
        this.depositStageMethodList = depositStageMethodList;
    }

    public String getDepositStageCode() {
        return depositStageCode;
    }

    public void setDepositStageCode(String depositStageCode) {
        this.depositStageCode = depositStageCode;
    }

    public List<String> getCurStagePaymentFlagList() {
        return curStagePaymentFlagList;
    }

    public void setCurStagePaymentFlagList(List<String> curStagePaymentFlagList) {
        this.curStagePaymentFlagList = curStagePaymentFlagList;
    }

    public List<String> getDepositPaymentFlagList() {
        return depositPaymentFlagList;
    }

    public void setDepositPaymentFlagList(List<String> depositPaymentFlagList) {
        this.depositPaymentFlagList = depositPaymentFlagList;
    }
}
