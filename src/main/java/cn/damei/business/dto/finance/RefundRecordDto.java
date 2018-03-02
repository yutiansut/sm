package cn.damei.business.dto.finance;

import cn.damei.business.entity.finance.FinaProjectAccount;
import cn.damei.business.entity.finance.RefundRecord;

import java.math.BigDecimal;

public class RefundRecordDto extends RefundRecord{

    /**
     * 可退定金
     */
    private BigDecimal depositAbleBackAmount;

    /**
     * 拆改费可退
     */
    private BigDecimal modifyAbleBackAmount;

    /**
     * 施工款可退
     */
    private BigDecimal constructAbleBackAmount;


    public BigDecimal getDepositAbleBackAmount() {
        return depositAbleBackAmount;
    }

    public void setDepositAbleBackAmount(BigDecimal depositAbleBackAmount) {
        this.depositAbleBackAmount = depositAbleBackAmount;
    }

    public BigDecimal getModifyAbleBackAmount() {
        return modifyAbleBackAmount;
    }

    public void setModifyAbleBackAmount(BigDecimal modifyAbleBackAmount) {
        this.modifyAbleBackAmount = modifyAbleBackAmount;
    }

    public BigDecimal getConstructAbleBackAmount() {
        return constructAbleBackAmount;
    }

    public void setConstructAbleBackAmount(BigDecimal constructAbleBackAmount) {
        this.constructAbleBackAmount = constructAbleBackAmount;
    }
}

