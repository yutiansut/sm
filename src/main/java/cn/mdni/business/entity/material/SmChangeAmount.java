package cn.mdni.business.entity.material;

import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <dl>
 * <dd>Description: 美得你智装  变更金额</dd>
 * <dd>@date：2017/11/22  11:24</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class SmChangeAmount extends IdEntity {

    /**
     * 变更单id
     */
    private Long changeDetailId;

    /**
     * 大类code
     */
    private String categoryCode;

    /**
     * 小类code
     */
    private String categoryDetailCode;

    /**
     * 原金额
     */
    private BigDecimal originalAmount;

    /**
     * 现金额
     */
    private BigDecimal cashAmount;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date updateTime;


    public Long getChangeDetailId() {
        return changeDetailId;
    }

    public void setChangeDetailId(Long changeDetailId) {
        this.changeDetailId = changeDetailId;
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

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}