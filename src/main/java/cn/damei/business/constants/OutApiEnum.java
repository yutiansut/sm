package cn.damei.business.constants;


public  enum OutApiEnum {

    SYNC_PM_SUP(PropertyHolder.getOldOrderSyncPMorSupervisor(),"同步项目经理质检"),
    SYNC_START_COMPLETE(PropertyHolder.getOldOrderSyncStartComplete(),"同步开工竣工时间"),
    SYNC_CONSTRUCTION_CHANGE(PropertyHolder.getOldOrderSyncConstructionChange(),"同步基装变更"),
    REJECT_PROJECT(PropertyHolder.getOldOrderRejectProject(),"拒绝接单"),
    SYNC_ASSIST_MATERIAL(PropertyHolder.getOldOrderSyncAssistMaterial(),"同步辅料信息"),
    PUSH_PROJECT(PropertyHolder.getMpsPushUrl(),"往产业工人推送项目信息"),
    PUSH_AMMOUNT(PropertyHolder.getMpsPushUrl(),"往产业工人推送中期款尾款"),
    PUSH_MATERIALS(PropertyHolder.getMpsPushMaterialUrl(),"往产业工人推送选材单"),
    PUSH_CHANGE_MATERIAL(PropertyHolder.getMpsPushChangeMaterialUrl(),"往产业工人推送变更单");



    // 成员变量
    private String apiurl;
    private String apiname;

    // 构造方法
    private OutApiEnum(String apiurl, String apiname) {
        this.apiurl = apiurl;
        this.apiname = apiname;
    }

    public String getApiurl() {
        return apiurl;
    }

    public String getApiname() {
        return apiname;
    }
}
