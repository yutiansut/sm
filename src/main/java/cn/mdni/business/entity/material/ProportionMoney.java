package cn.mdni.business.entity.material;

import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;


import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材占比金额 </dd>
 * <dd>@date：2017/12/13  18:41</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class ProportionMoney  extends IdEntity {

    /**
     * 集装定额
     */
    private BigDecimal baseloadrating1;

    /**
     * 装修工程总价
     */
    private BigDecimal renovationAmount;

    /**
     * 拆改基桩
     */
    private BigDecimal baseloadrating3;

    /**
     * 拆改总金额
     */
    private BigDecimal comprehensivefee4;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;


    public BigDecimal getBaseloadrating1() {
        return baseloadrating1;
    }

    public void setBaseloadrating1(BigDecimal baseloadrating1) {
        this.baseloadrating1 = baseloadrating1;
    }

    public BigDecimal getRenovationAmount() {
        return renovationAmount;
    }

    public void setRenovationAmount(BigDecimal renovationAmount) {
        this.renovationAmount = renovationAmount;
    }

    public BigDecimal getBaseloadrating3() {
        return baseloadrating3;
    }

    public void setBaseloadrating3(BigDecimal baseloadrating3) {
        this.baseloadrating3 = baseloadrating3;
    }

    public BigDecimal getComprehensivefee4() {
        return comprehensivefee4;
    }

    public void setComprehensivefee4(BigDecimal comprehensivefee4) {
        this.comprehensivefee4 = comprehensivefee4;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}