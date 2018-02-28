package cn.mdni.business.entity.orderflow;

import cn.mdni.core.base.entity.IdEntity;

/**
 * @Description: 客户信息
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/29.
 */
public class Customer extends IdEntity {

    /**客户代码*/
    private String code;
    /**客户名字*/
    private String name;
    /**客户手机号*/
    private String mobile;
    /**门店编码*/
    private String storeCode;
    /**客户来源*/
    private String incomeSource;
    /**客户标签*/
    private String customerTag;

    /**********************辅助数据传输的字段********************/
    /**项目的uuid*/
    private String contractUuid;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getIncomeSource() {
        return incomeSource;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public String getCustomerTag() {
        return customerTag;
    }

    public void setCustomerTag(String customerTag) {
        this.customerTag = customerTag;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }
}
