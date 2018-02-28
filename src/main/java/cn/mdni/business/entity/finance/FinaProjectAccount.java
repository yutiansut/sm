package cn.mdni.business.entity.finance;

import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;

/**
 * Created by WS on 2017/11/17/017.
 */
public class FinaProjectAccount extends IdEntity {
    private String contractCode;//合同编号

    private String contractUuid;//订单唯一标识码

    private BigDecimal balanceAmount;//可用余额

    private BigDecimal depositTotalPayed;//定金累计已交

    private BigDecimal depositTotalDeduct;//定金累计抵扣

    private BigDecimal modifyExpectAmount;//拆改合同应收金额

    private BigDecimal modifyTotalPayed;//拆改费累计

    private BigDecimal constructExpectAmount;//施工合同应收金额

    private BigDecimal constructTotalPayed;//施工合同累计实收

    private BigDecimal changeAmount;//变更总金额

    private BigDecimal reparationAmount;//赔款总金额

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getReparationAmount() {
        return reparationAmount;
    }

    public void setReparationAmount(BigDecimal reparationAmount) {
        this.reparationAmount = reparationAmount;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getDepositTotalPayed() {
        return depositTotalPayed;
    }

    public void setDepositTotalPayed(BigDecimal depositTotalPayed) {
        this.depositTotalPayed = depositTotalPayed;
    }

    public BigDecimal getDepositTotalDeduct() {
        return depositTotalDeduct;
    }

    public void setDepositTotalDeduct(BigDecimal depositTotalDeduct) {
        this.depositTotalDeduct = depositTotalDeduct;
    }

    public BigDecimal getModifyExpectAmount() {
        return modifyExpectAmount;
    }

    public void setModifyExpectAmount(BigDecimal modifyExpectAmount) {
        this.modifyExpectAmount = modifyExpectAmount;
    }

    public BigDecimal getModifyTotalPayed() {
        return modifyTotalPayed;
    }

    public void setModifyTotalPayed(BigDecimal modifyTotalPayed) {
        this.modifyTotalPayed = modifyTotalPayed;
    }

    public BigDecimal getConstructExpectAmount() {
        return constructExpectAmount;
    }

    public void setConstructExpectAmount(BigDecimal constructExpectAmount) {
        this.constructExpectAmount = constructExpectAmount;
    }

    public BigDecimal getConstructTotalPayed() {
        return constructTotalPayed;
    }

    public void setConstructTotalPayed(BigDecimal constructTotalPayed) {
        this.constructTotalPayed = constructTotalPayed;
    }
}
