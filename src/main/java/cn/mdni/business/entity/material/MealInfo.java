package cn.mdni.business.entity.material;


import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @describe 套餐管理
 * @author Zh
 * @date 2017-11-2 11:30:09
 *
 */
public class MealInfo extends IdEntity {

    /**
     * 所属门店id
     */
    private String storeCode;

    /**
     * 所属门店名称
     */
    private String storeName;

    /**
     * 套餐名称
     */
    private String mealName;

    /**
     * 套餐价格
     */
    private BigDecimal mealSalePrice;

    /**
     * 有效期起始时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date validityDate;

    /**
     * 有效期截止时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMD_EN, timezone = "GMT+8")
    private Date expirationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String mealStatus;

    /**
     * 是否无期限
     */
    private Integer noDeadline;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public BigDecimal getMealSalePrice() {
        return mealSalePrice;
    }

    public void setMealSalePrice(BigDecimal mealSalePrice) {
        this.mealSalePrice = mealSalePrice;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMealStatus() {
        return mealStatus;
    }

    public void setMealStatus(String mealStatus) {
        this.mealStatus = mealStatus;
    }

    public Integer getNoDeadline() {
        return noDeadline;
    }

    public void setNoDeadline(Integer noDeadline) {
        this.noDeadline = noDeadline;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}