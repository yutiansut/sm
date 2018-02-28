package cn.mdni.business.dto.finance;

import cn.mdni.business.entity.finance.FinaProjectAccount;
import cn.mdni.business.entity.finance.RefundRecord;

import java.math.BigDecimal;

/**
 * @author liupengfei
 * @Description 退款页面展示用的Dto
 * @Date Created in 2017/11/27 15:41
 */
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

