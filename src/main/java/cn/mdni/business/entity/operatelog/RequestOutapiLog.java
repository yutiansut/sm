package cn.mdni.business.entity.operatelog;

import cn.mdni.core.base.entity.IdEntity;

import java.util.Date;

/**
 * Created by Allen on 2017/12/27.
 */
public class RequestOutapiLog extends IdEntity {
    /**
     * 推送类型 （调用接口类型）
     */
    private String pushType;
    /**
     * 是否需要重新推送 1 需要重新推送 0 不需要
     */
    private Integer needAgainPush;
    /**
     * 推送内容
     */
    private String pushContent;
    /**
     * 响应内容
     */
    private String responseContent;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public Integer getNeedAgainPush() {
        return needAgainPush;
    }

    public void setNeedAgainPush(Integer needAgainPush) {
        this.needAgainPush = needAgainPush;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
