package cn.damei.business.entity;

import cn.damei.core.base.entity.IdEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "sm_other_add_reduce_amount")
public class OtherAddReduceAmount extends IdEntity {

    /**
     * 项目编码
     */
    private String contractCode;

    /**
     * 增减项（优惠税，优惠管理费。。）
     */
    private String itemName;

    /**
     * 增加原因
     */
    private String addReduceReason;

    /**
     * 增减类型（1 增加、0 减少）
     */
    private String addReduceType;

    /**
     * 1是，0否 (是否税后减额)
     */
    private String taxedAmount;

    /**
     * 额度
     */
    private BigDecimal quota;

    /**
     * 批准人
     */
    private String approver;
    /**
     * 变更标识，1是，0否
     */
    private String changeFlag;

    private String pageType;

    private String changeNo;//变更号



    private String changeVersionNo;//变更批次
    /**
     * mongo存入的时候原始选材用量的标识
     */
    private String materialFlag;

    /**
     * 变更查看使用
     */
    private BigDecimal oldPriceTotal;//原合计
    private BigDecimal nowPriceTotal;//现合计
    private BigDecimal oldAddPrice;//原增
    private BigDecimal nowAddPrice;//现增
    private BigDecimal oldReducePrice;//原减
    private BigDecimal nowReducePrice;//现减

    @Id
    private String MongoId;

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAddReduceReason() {
        return addReduceReason;
    }

    public void setAddReduceReason(String addReduceReason) {
        this.addReduceReason = addReduceReason;
    }

    public String getAddReduceType() {
        return addReduceType;
    }

    public void setAddReduceType(String addReduceType) {
        this.addReduceType = addReduceType;
    }

    public String getTaxedAmount() {
        return taxedAmount;
    }

    public void setTaxedAmount(String taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public BigDecimal getOldPriceTotal() {
        return oldPriceTotal;
    }

    public void setOldPriceTotal(BigDecimal oldPriceTotal) {
        this.oldPriceTotal = oldPriceTotal;
    }

    public BigDecimal getNowPriceTotal() {
        return nowPriceTotal;
    }

    public void setNowPriceTotal(BigDecimal nowPriceTotal) {
        this.nowPriceTotal = nowPriceTotal;
    }

    public String getChangeVersionNo() {
        return changeVersionNo;
    }

    public void setChangeVersionNo(String changeVersionNo) {
        this.changeVersionNo = changeVersionNo;
    }

    public BigDecimal getOldAddPrice() {
        return oldAddPrice;
    }

    public void setOldAddPrice(BigDecimal oldAddPrice) {
        this.oldAddPrice = oldAddPrice;
    }

    public BigDecimal getNowReducePrice() {
        return nowReducePrice;
    }

    public void setNowReducePrice(BigDecimal nowReducePrice) {
        this.nowReducePrice = nowReducePrice;
    }

    public BigDecimal getNowAddPrice() {
        return nowAddPrice;
    }

    public void setNowAddPrice(BigDecimal nowAddPrice) {
        this.nowAddPrice = nowAddPrice;
    }

    public BigDecimal getOldReducePrice() {
        return oldReducePrice;
    }

    public void setOldReducePrice(BigDecimal oldReducePrice) {
        this.oldReducePrice = oldReducePrice;
    }

    public String getMaterialFlag() {
        return materialFlag;
    }

    public void setMaterialFlag(String materialFlag) {
        this.materialFlag = materialFlag;
    }

    public String getMongoId() {
        return MongoId;
    }

    public void setMongoId(String mongoId) {
        MongoId = mongoId;
    }
}