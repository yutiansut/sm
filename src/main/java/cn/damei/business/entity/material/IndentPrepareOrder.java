package cn.damei.business.entity.material;

import cn.damei.core.base.entity.IdEntity;

import java.time.LocalDateTime;

public class IndentPrepareOrder extends IdEntity {

    //项目编码
    private String contractCode;
    //数据来源，（选材、变更）--枚举(Constants.PAGE_TYPE_SELECT): select/change
    private String dataSource;
    //预备单状态:待转单、已转单、已作废
    private String status;
    //品牌id
    private Long brandId;
    //品牌名称
    private String brandName;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //更新人
    private String updateAccount;
    //转单时间
    private LocalDateTime switchTime;


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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateAccount() {
        return updateAccount;
    }
    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }
    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }
    public Long getBrandId() {
        return brandId;
    }
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
