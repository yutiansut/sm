package cn.damei.business.entity.finance;

import cn.damei.core.base.entity.IdEntity;

import java.util.Date;

public class Payplan extends IdEntity{

    /**
     * 规划模版编号uuid
     */
    private String planCode;


    /**
     * 规划模版名称
     */
    private String planName;


    /**
     * 所属门店编号
     */
    private String storeCode;


    /**
     * 规划模版的状态，当前是否激活
     */
    private String planStatus;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 创建人
     */
    private String creator;


    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


}

