package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.time.LocalDateTime;

/**
 * @Description: 选材数据转换预备订单异常日志表
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 18:27.
 */
public class PrepareOrderAbnormalLog extends IdEntity {

    //项目编码
    private String contractCode;
    //数据来源，（选材、变更）--枚举(Constants.PAGE_TYPE_SELECT): select/change
    private String dataSource;
    //是否处理, 0: 不处理, 1: 处理
    private String dealStatus;
    //操作人
    private String dealUser;
    //操作时间
    private LocalDateTime dealTime;
    //创建时间
    private LocalDateTime createTime;
    //异常日志内容
    private String abnormalContent;


    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    public String getDealStatus() {
        return dealStatus;
    }
    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }
    public String getDealUser() {
        return dealUser;
    }
    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }
    public LocalDateTime getDealTime() {
        return dealTime;
    }
    public void setDealTime(LocalDateTime dealTime) {
        this.dealTime = dealTime;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getAbnormalContent() {
        return abnormalContent;
    }
    public void setAbnormalContent(String abnormalContent) {
        this.abnormalContent = abnormalContent;
    }
}
