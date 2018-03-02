package cn.damei.business.entity.commodity.prodskuprice;

import cn.damei.business.constants.PriceTypeEnum;
import cn.damei.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class ProdSkuPrice extends IdEntity {


    /**
     * skuId
     */
    private Long skuId;

    /**
     * SUPPLY("网真采购价"), STORE("门店采购价"), SALE("门店销售价"),UPGRADE("升级项价"),INCREASED("增项"),MINUS("减项")
     */
    private PriceTypeEnum priceType;

    /**
     * 单价
     */
    private BigDecimal price;
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date priceStartDate;

    /**
     * 最后编辑人
     */
    private Long editor;

    /**
     * 最后编辑时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date editTime;

    private String editorName;

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public PriceTypeEnum getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceTypeEnum priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getPriceStartDate() {
        return priceStartDate;
    }

    public void setPriceStartDate(Date priceStartDate) {
        this.priceStartDate = priceStartDate;
    }

    public Long getEditor() {
        return editor;
    }

    public void setEditor(Long editor) {
        this.editor = editor;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}