package cn.damei.business.constants;
public enum PricingMethodEnum {

    fixedUnitPrice("固定单价"),foundationPileTotal("基桩增项总价占比"),renovationFoundationPile("装修工程总价占比"),
    dismantleFoundationPile("拆除基桩定额总价占比"),demolitionProjectTotal("拆除工程总价占比"),;

    private String label;

    PricingMethodEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
