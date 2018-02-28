package cn.mdni.business.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Description: 业务配置属性获取(读取配置文件)
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 14:29 2017/10/30.
 */
@Component
@Lazy(false)
public class PropertyHolder{

    private static String uploadBaseUrl;
    private static String baseurl;

    /**
     * 上传路径
     */
    private static String uploadDir;

    /**
     * 产业工人系统接口密钥
     **/
    private static String mpsKey;
    /**
     * 根据当前登录人的门店获取所有设计组
     */
    private static String designGroupUrl;
    /**
     * 根据设计组获取设计师
     */
    private static String designerUrl;
    /**
     * 获取审计员
     */
    private static String auditorUrl;

    /**
     * 产业工人系统推送地址
     **/
    private static String mpsPushUrl;
    /**
     * 产业工人系统推送选材单地址
     **/
    private static String mpsPushMaterialUrl;
    /**
     * 产业工人系统推送变更单地址
     **/
    private static String mpsPushChangeMaterialUrl;

    /**
     * 老订单流转 同步开竣工时间
     **/
    private static String oldOrderSyncStartComplete;
    /**
     * 老订单流转 同步项目经理质检
     **/
    private static String oldOrderSyncPMorSupervisor;
    /**
     * 老订单流转 同步基装变更
     **/
    private static String oldOrderSyncConstructionChange;
    /**
     * 老订单流转 产业工人拒绝接单
     **/
    private static String oldOrderRejectProject;
    /**
     * 老订单流转 同步辅材
     **/
    private static String oldOrderSyncAssistMaterial;


    /**
     * crm接口的host域名
     */
    private static String crmApiHost;


    public static String getUploadBaseUrl() {
        return uploadBaseUrl;
    }

    @Value("${upload.base.url}")
    public void setUploadBaseUrl(String uploadBaseUrl) {
        PropertyHolder.uploadBaseUrl = uploadBaseUrl;
    }

    public static String getBaseurl() {
        return baseurl;
    }

    @Value("${base.url}")
    public void setBaseurl(String baseurl) {
        PropertyHolder.baseurl = baseurl;
    }



    /**
     * 获取产业工人系统接口密钥
     **/
    public static String getMpsKey() {
        return mpsKey;
    }
    /**
     * 设置产业工人系统接口密钥
     **/
    @Value("${mps.key}")
    public  void setMpsKey(String mpsKey) {
        PropertyHolder.mpsKey = mpsKey;
    }

    /**
     * 获取设计组
     * @return
     */
    public static String getDesignGroupUrl() {
        return designGroupUrl;
    }
    /**
     * 设置设计组
     * @param designGroupUrl
     */
    @Value("${design.group.url}")
    public void setDesignGroupUrl(String designGroupUrl) {
        PropertyHolder.designGroupUrl = designGroupUrl;
    }

    /**
     * 获取设计师
     * @return
     */
    public static String getDesignerUrl() {
        return designerUrl;
    }
    /**
     * 设置设计师
     * @param designerUrl
     */
    @Value("${designer.url}")
    public void setDesignerUrl(String designerUrl) {
        PropertyHolder.designerUrl = designerUrl;
    }
    /**
     * 获取审计员
     * @return
     */
    public static String getAuditorUrl() {
        return auditorUrl;
    }
    /**
     * 设置审计员
     * @param auditorUrl
     */
    @Value("${designer.audit.url}")
    public void setAuditorUrl(String auditorUrl) {
        PropertyHolder.auditorUrl = auditorUrl;
    }

    /**
     * 获取产业工人系统推送地址
     **/
    public static String getMpsPushUrl() {
        return mpsPushUrl;
    }
    /**
     * 设置产业工人系统推送地址
     **/
    @Value("${mps.pushUrl}")
    public  void setMpsPushUrl(String mpsPushUrl) {
        PropertyHolder.mpsPushUrl = mpsPushUrl;
    }


    public static String getCrmApiHost() {
        return crmApiHost;
    }

    @Value("${crm.apihost}")
    public void setCrmApiHost(String crmApiHost) {
        PropertyHolder.crmApiHost = crmApiHost;
    }


    public static String getMpsPushMaterialUrl() {
        return mpsPushMaterialUrl;
    }

    @Value("${mps.pushMaterial}")
    public void setMpsPushMaterialUrl(String mpsPushMaterialUrl) {
        PropertyHolder.mpsPushMaterialUrl = mpsPushMaterialUrl;
    }

    public static String getMpsPushChangeMaterialUrl() {
        return mpsPushChangeMaterialUrl;
    }

    @Value("${mps.pushChangeMaterial}")
    public void setMpsPushChangeMaterialUrl(String mpsPushChangeMaterialUrl) {
        PropertyHolder.mpsPushChangeMaterialUrl = mpsPushChangeMaterialUrl;
    }

    public static String getOldOrderSyncStartComplete() {
        return oldOrderSyncStartComplete;
    }

    @Value("${oldorder.syncStartComplete}")
    public void setOldOrderSyncStartComplete(String oldOrderSyncStartComplete) {
        PropertyHolder.oldOrderSyncStartComplete = oldOrderSyncStartComplete;
    }

    public static String getOldOrderSyncPMorSupervisor() {
        return oldOrderSyncPMorSupervisor;
    }

    @Value("${oldorder.syncPMorSupervisor}")
    public void setOldOrderSyncPMorSupervisor(String oldOrderSyncPMorSupervisor) {
        PropertyHolder.oldOrderSyncPMorSupervisor = oldOrderSyncPMorSupervisor;
    }

    public static String getOldOrderSyncConstructionChange() {
        return oldOrderSyncConstructionChange;
    }

    @Value("${oldorder.syncConstructionChange}")
    public void setOldOrderSyncConstructionChange(String oldOrderSyncConstructionChange) {
        PropertyHolder.oldOrderSyncConstructionChange = oldOrderSyncConstructionChange;
    }

    public static String getOldOrderRejectProject() {
        return oldOrderRejectProject;
    }

    @Value("${oldorder.rejectProject}")
    public void setOldOrderRejectProject(String oldOrderRejectProject) {
        PropertyHolder.oldOrderRejectProject = oldOrderRejectProject;
    }

    public static String getOldOrderSyncAssistMaterial() {
        return oldOrderSyncAssistMaterial;
    }

    @Value("${oldorder.syncAssistMaterial}")
    public void setOldOrderSyncAssistMaterial(String oldOrderSyncAssistMaterial) {
        PropertyHolder.oldOrderSyncAssistMaterial = oldOrderSyncAssistMaterial;
    }

    public static String getUploadDir() {
        return uploadDir;
    }
    @Value("${upload.dir}")
    public  void setUploadDir(String uploadDir) {
        PropertyHolder.uploadDir = uploadDir;
    }
}