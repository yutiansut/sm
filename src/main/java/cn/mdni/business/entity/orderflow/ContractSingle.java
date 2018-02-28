package cn.mdni.business.entity.orderflow;


import cn.mdni.core.base.entity.IdEntity;


/**
 * @describe 串单项目
 * @author zhangh
 * @date 2017-11-15 11:30:09
 *
 */
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