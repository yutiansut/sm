package cn.damei.business.entity.orderflow;


import cn.damei.core.base.entity.IdEntity;


public class ContractSingle extends IdEntity {

    /**
     * 串单标签id
     */
    private Integer singleTagId;

    /**
     * 订单编号
     */
    private String contractCode;

    /**
     * 删除状态
     */
    private String deleted;

    public Integer getSingleTagId() {
        return singleTagId;
    }

    public void setSingleTagId(Integer singleTagId) {
        this.singleTagId = singleTagId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}