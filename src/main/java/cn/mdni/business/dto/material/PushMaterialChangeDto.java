package cn.mdni.business.dto.material;

import java.util.List;

/**
 * 功能描述:推送主材变更单
 * @author Allen
 * 2017.12.26
 */
public class PushMaterialChangeDto {
    /**
     * 项目编号
     */
    private String orderNumber;
    /**
     * 变更单号
     */
    private String changeBillCode;
    /**
     * 设计师申请变更日期
     */
    private String changeApplyDate;
    /**
     * 设计师
     */
    private String designerName;
    /**
     * 变更单审核通过日期
     */
    private String changeCheckedDate;
    /**
     * 审计员
     */
    private String checkerName;
    /**
     * 变更原因
     */
    private String changeReason;
    /**
     * 变更详情
     */
    private List<MaterialChangeItem> changeItemInfo;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChangeBillCode() {
        return changeBillCode;
    }

    public void setChangeBillCode(String changeBillCode) {
        this.changeBillCode = changeBillCode;
    }

    public String getChangeApplyDate() {
        return changeApplyDate;
    }

    public void setChangeApplyDate(String changeApplyDate) {
        this.changeApplyDate = changeApplyDate;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getChangeCheckedDate() {
        return changeCheckedDate;
    }

    public void setChangeCheckedDate(String changeCheckedDate) {
        this.changeCheckedDate = changeCheckedDate;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public List<MaterialChangeItem> getChangeItemInfo() {
        return changeItemInfo;
    }

    public void setChangeItemInfo(List<MaterialChangeItem> changeItemInfo) {
        this.changeItemInfo = changeItemInfo;
    }


}
