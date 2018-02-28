package cn.mdni.business.constants;
/**
 * <dl>
 * <dd>Description: 美得你智装 计价方式 </dd>
 * <dd>@date：2017/11/10  14:06</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
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
