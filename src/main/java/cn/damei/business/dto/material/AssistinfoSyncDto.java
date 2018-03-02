package cn.damei.business.dto.material;

import cn.damei.business.entity.material.AssistinfoSync;

import java.util.List;

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
