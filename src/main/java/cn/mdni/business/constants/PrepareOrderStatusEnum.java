package cn.mdni.business.constants;

/**
 * @Description: 预备单状态枚举
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/21 14:23.
 */
public enum PrepareOrderStatusEnum {

    WAITING_TRANSFERRED("待转单"),  ALREADY_TRANSFERRED("已转单"), HAS_NULLIFIED("已作废");

    private String label;

    PrepareOrderStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
