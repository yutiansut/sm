package cn.mdni.business.dto.orderflow;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Allen on 2017/11/23.
 */
public class CustomerDto {

    /**
     *客户姓名
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerMobile;

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 客户级别
     */
    private String customerLevel;
    /**
     * 客户标签
     */
    private String customerTagName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerTagName() {
        return customerTagName;
    }

    public void setCustomerTagName(String customerTagName) {
        this.customerTagName = customerTagName;
    }
}
