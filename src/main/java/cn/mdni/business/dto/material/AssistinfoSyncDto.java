package cn.mdni.business.dto.material;

import cn.mdni.business.entity.material.AssistinfoSync;

import java.util.List;

/**
 * 同步辅料dto
 * Created by Allen on 2017/12/30.
 */
public class AssistinfoSyncDto {
    /**
     * 项目编号
     */
    private String orderNumber;
    /**
     * 辅料信息
     */
    private List<AssistinfoSync> assistInfo;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<AssistinfoSync> getAssistInfo() {
        return assistInfo;
    }

    public void setAssistInfo(List<AssistinfoSync> assistInfo) {
        this.assistInfo = assistInfo;
    }
}
