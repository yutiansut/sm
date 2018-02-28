package cn.mdni.business.dto.material;

import java.util.List;

/**
 * 功能描述:推送选材单主材信息
 * @author Allen
 * 2017.11.23
 */
public class PushMaterialDto {
    /**
     * 项目编号
     */
    private String orderNumber;
    /**
     * 主材信息
     */
    private List<MaterialItem> materialInfo;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<MaterialItem> getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(List<MaterialItem> materialInfo) {
        this.materialInfo = materialInfo;
    }

}
