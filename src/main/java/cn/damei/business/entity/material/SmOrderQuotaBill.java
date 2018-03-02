package cn.damei.business.entity.material;

import cn.damei.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SmOrderQuotaBill  extends IdEntity {

    /**
     * 项目编码
     */
    private String contractCode;

    /**
     * 大类code
     */
    private String categoryCode;

    /**
     * 小类code
     */
    private String categoryDetailCode;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDetailCode() {
        return categoryDetailCode;
    }

    public void setCategoryDetailCode(String categoryDetailCode) {
        this.categoryDetailCode = categoryDetailCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}