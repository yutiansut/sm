package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 同步辅料信息
 * Created by Allen on 2017/12/30.
 */
public class AssistinfoSync extends IdEntity{
    /**
     * 项目编号
     */
    private String contractCode;
    /**
     * 辅料名称
     */
    private String assistItemName;
    /**
     * 单位
     */
    private String unit;
    /**
     * 销售价
     */
    private BigDecimal unitPrice;
    /**
     * 工人结算价
     */
    private BigDecimal workerPrice;
    /**
     * 供应商结算价
     */
    private BigDecimal supplierPrice;
    /**
     * 数量
     */
    private BigDecimal lastCount;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 辅料分类
     */
    private String materialCateName;
    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date acceptanceDate;
    /**
     *品牌
     */
    private String brand;
    /**
     * 辅料编码
     */
    private String assistItemNo;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getAssistItemName() {
        return assistItemName;
    }

    public void setAssistItemName(String assistItemName) {
        this.assistItemName = assistItemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getWorkerPrice() {
        return workerPrice;
    }

    public void setWorkerPrice(BigDecimal workerPrice) {
        this.workerPrice = workerPrice;
    }

    public BigDecimal getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(BigDecimal supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public BigDecimal getLastCount() {
        return lastCount;
    }

    public void setLastCount(BigDecimal lastCount) {
        this.lastCount = lastCount;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMaterialCateName() {
        return materialCateName;
    }

    public void setMaterialCateName(String materialCateName) {
        this.materialCateName = materialCateName;
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAssistItemNo() {
        return assistItemNo;
    }

    public void setAssistItemNo(String assistItemNo) {
        this.assistItemNo = assistItemNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
