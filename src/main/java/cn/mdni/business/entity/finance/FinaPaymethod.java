package cn.mdni.business.entity.finance;

import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ztw on 2017/11/27.
 */

public class FinaPaymethod extends IdEntity {
    private String  methodName  ;// '支付方式名称',
    private String  storeName  ;//门店名称
    private String  storeIds  [];//门店ID集合
    private String methodCode  ;// '支付方式编号',
    private String ablestageTemplateCode  ;// 适用阶段,
    private String methodType  ;// '类型',
    private String  methodStatus  ;// '状态',
    private Integer  ifCustome  ;// '是否进行了定制',
    private Double  costRate  ;// '手续费率',
    protected BigDecimal minCostfee ;// '最低手续费',
    private BigDecimal  maxCostfee  ;// '封顶手续费',
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime ;//'创建时间',
    private String  creator ;// '创建人',
    /**
     * 支付方式备注
     */
    private String remark;




    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getMethodStatus() {
        return methodStatus;
    }

    public void setMethodStatus(String methodStatus) {
        this.methodStatus = methodStatus;
    }

    public Integer getIfCustome() {
        return ifCustome;
    }

    public void setIfCustome(Integer ifCustome) {
        this.ifCustome = ifCustome;
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


    public String getAblestageTemplateCode() {
        return ablestageTemplateCode;
    }

    public void setAblestageTemplateCode(String ablestageTemplateCode) {
        this.ablestageTemplateCode = ablestageTemplateCode;
    }

    public String[] getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(String[] storeIds) {
        this.storeIds = storeIds;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 交款方式枚举类
     */
    public enum PaymethodTypeEnum{

        BANKTRANS("银行转账"),
        CASH("现金"),
        THIRDPAY("第三方支付"),
        POS("POS机"),
        DEDUCT("抵扣"),
        DEPOSITDEDUCT("预付款抵扣"),
        CHECK("支票"),
        OTHER("其他");

        private String label;

        PaymethodTypeEnum(String label){
            this.label = label;
        }

        public String getLabel(){
            return label;
        }
    }


    /**
     * 特殊交款方式的编号集合枚举
     */
    public enum PaymethodSpecialCodeEnum{
        //抵定金收据抵扣
        PAY_DDJSJDK;
    }
}
