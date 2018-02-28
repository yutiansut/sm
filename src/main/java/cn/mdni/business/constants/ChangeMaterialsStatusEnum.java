package cn.mdni.business.constants;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材变更 主材状态</dd>
 * <dd>@date：2017/11/15  22:17</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public enum ChangeMaterialsStatusEnum {
    NOT_MEASURED("未测量"),
    MEASURED("已测量"),
    NO_ORDERS("未下单"),
    ALREADY_ORDERED("已下单"),
    NOT_SHIPPED("未发货"),
    SHIPPED("已发货"),
    NOT_INSTALLED("未安装"),
    INSTALLED("已安装"),
    UNFILLED_ORDER_CONDITIONS("未满足下单条件"),
    ;
    private String label;

     ChangeMaterialsStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
