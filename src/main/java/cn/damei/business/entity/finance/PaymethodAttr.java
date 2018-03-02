package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

public class PaymethodAttr extends IdEntity{

    /**
     * 属性名称
     */
    private String attrName;

    /**
     * 属性编号
     */
    private String attrCode;

    /**
     * 交款方式Id
     */
    private String methodId;

    /**
     * 父级属性Id
     */
    private String parentAttrId;

    /**
     * 从一级属性到当前属性ID的路径，如XX,BB,CC,
     */
    private String attrPath;


    /**
     * 状态
     */
    private String attrStatus;

    /**
     * 手续费率
     */
    private Double costRate;

    /**
     * 最低手续费
     */
    private BigDecimal minCostfee;

    /**
     * 最高手续费
     */
    private BigDecimal maxCostfee;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;


    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getParentAttrId() {
        return parentAttrId;
    }

    public void setParentAttrId(String parentAttrId) {
        this.parentAttrId = parentAttrId;
    }

    public String getAttrPath() {
        return attrPath;
    }

    public void setAttrPath(String attrPath) {
        this.attrPath = attrPath;
    }

    public String getAttrStatus() {
        return attrStatus;
    }

    public void setAttrStatus(String attrStatus) {
        this.attrStatus = attrStatus;
    }

    public Double getCostRate() {
        return costRate;
    }

    public void setCostRate(Double costRate) {
        this.costRate = costRate;
    }

    public BigDecimal getMinCostfee() {
        return minCostfee;
    }

    public void setMinCostfee(BigDecimal minCostfee) {
        this.minCostfee = minCostfee;
    }

    public BigDecimal getMaxCostfee() {
        return maxCostfee;
    }

    public void setMaxCostfee(BigDecimal maxCostfee) {
        this.maxCostfee = maxCostfee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
