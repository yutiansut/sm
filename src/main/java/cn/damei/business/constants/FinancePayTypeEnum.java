package cn.damei.business.constants;

public enum FinancePayTypeEnum {

    DEPOSIT("交定金"),
    MODIFY("交拆改"),
    CONSTRUCT("交施工款"),
    DEDUCT_DESIGN_FEE("扣设计费"),
    DEDUCT_OTHER_FEE("扣其他费"),
    RETURN_DEPOSIT("退定金"),
    RETURN_MODIFY("退拆改"),
    RETURN_CONSTRUCT("退施工款");

    private String label;

    FinancePayTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
