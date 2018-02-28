package cn.mdni.business.dto.orderflow;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 功能描述:产业工人系统同步项目信息Dto
 * @author Allen
 * 2017.11.23
 */
public class MpsSyncProjectInfoDto {
    /**
     * 类型
     * (同步项目经理 监理接口)1：项目经理信息 2：监理信息
     * (同步施工节点信息) 1：实际开工 2：基装完成通过  3：竣工 修改施工进度时间
     */
    private String type;
    /**
     * 项目编号
     */
    private String orderId;
    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date time;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态
     */
    private String status;
    /**
     * 密钥
     */
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String  type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderid) {
        this.orderId = orderid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


