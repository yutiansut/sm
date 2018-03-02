package cn.damei.business.constants;

public enum NumberingTypeEnum {
    /**
     * 生成编号类型
     */
    PROJECT_NUMBER("项目编号"),
    RECEIPT_NUMBER("收据号"),
    REPARATIONS_NUMBER("赔款单号"),
    BACK_MONEY_NUMBER("退款单号");
    private String label;

    NumberingTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
