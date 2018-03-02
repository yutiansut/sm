package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.math.BigDecimal;

public class PayplanItem extends IdEntity{


    /**
     * 人工设定的编号
     */
    private String itemCode;

    /**
     * 所属规划模版的编号
     */
    private String planCode;


    /**
     * 规划模版节点名称
     */
    private String itemName;


    /**
     * 节点类型：定金、拆改费、施工款
     */
    private String itemType;


    /**
     * 节点顺序
     */
    private Double itemIndex;


    /**
     * 上一个节点的id
     */
    private Integer prevItemId;


    /**
     * 需要交够多少钱才能使财务阶段转移
     */
    private BigDecimal finaTransAmount;


    /**
     * 需要交够施工合同额的多少比例
     */
    private Double finaTransRate;


    /**
     * 是否允许自动结束收款
     */
    private Boolean allowAutoTrans;


    /**
     * 自动流转下阶段允许少交的钱
     */
    private BigDecimal allowLessAmount;


    /**
     * 节点状态
     */
    private String itemStatus;


    /**
     * 节点完成后是否需要向产业工人系统推送数据
     */
    private boolean needMpsPush;


    /**
     * 当前阶段可选的多个人工交款描述标记字符串
     */
    private String manulPayFlag;


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getFinaTransAmount() {
        return finaTransAmount;
    }

    public void setFinaTransAmount(BigDecimal finaTransAmount) {
        this.finaTransAmount = finaTransAmount;
    }

    public Double getFinaTransRate() {
        return finaTransRate;
    }

    public void setFinaTransRate(Double finaTransRate) {
        this.finaTransRate = finaTransRate;
    }

    public Boolean getAllowAutoTrans() {
        return allowAutoTrans;
    }

    public void setAllowAutoTrans(Boolean allowAutoTrans) {
        this.allowAutoTrans = allowAutoTrans;
    }

    public BigDecimal getAllowLessAmount() {
        return allowLessAmount;
    }

    public void setAllowLessAmount(BigDecimal allowLessAmount) {
        this.allowLessAmount = allowLessAmount;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Double getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Double itemIndex) {
        this.itemIndex = itemIndex;
    }

    public Integer getPrevItemId() {
        return prevItemId;
    }

    public void setPrevItemId(Integer prevItemId) {
        this.prevItemId = prevItemId;
    }

    public boolean isNeedMpsPush() {
        return needMpsPush;
    }

    public void setNeedMpsPush(boolean needMpsPush) {
        this.needMpsPush = needMpsPush;
    }

    public String getManulPayFlag() {
        return manulPayFlag;
    }

    public void setManulPayFlag(String manulPayFlag) {
        this.manulPayFlag = manulPayFlag;
    }


    /**
     * 交款规划模版节点类型
     */
    public enum PayplanItemTypeEnum{
        DEPOSIT("定金"),
        MODIFY("拆改费"),
        CONSTRUCT("施工款");

        private String label;

        PayplanItemTypeEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * 交款规划模版节点编号枚举
     */
    public enum PayplanItemCodeEnum{
        NODE_DEPOSIT("定金"),
        NODE_MODIFY("拆改费"),
        NODE_FIRST("首期款"),
        NODE_MEDIUM("中期款"),
        NODE_FINAL("尾款"),
        NODE_TWO("二期款"),
        NODE_THREE("三期款"),
        NODE_AFTERFINAL("尾款后款项");

        private String label;

        PayplanItemCodeEnum(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }


    }
}
