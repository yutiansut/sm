package cn.damei.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SimpleCustomerContract {
	
	
	/**
	 * 订单号
	 */
	public String orderNo;
	
	
	/**
	 * 订单Id
	 */
	public String orderId;
	
	
	/**
	 * 客户姓名
	 */
	public String customerName;
	
	
	/**
	 *下单客服姓名 
	 */
	public String serviceName;
	
	
	/**
	 * 设计师姓名
	 */
	public String stylistName;
	
	
	/**
	 * 监理
	 */
	public String supervisorName;
	
	
	/**
	 * 合同编号
	 */
	public String contractNo;
	
	
	/**
	 * 合同签约日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date signFinishTime;
	
	
	
	/**
	 * 订单创建时间 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date createTime;
	
	
	/**
	 * 预算总金额
	 */
	public BigDecimal budgetAmount;

	
	/**
	 * 协商要交的定金金额
	 */
	public BigDecimal imprest;
	
	
	/**
	 * 标记订单串单用的标签
	 */
	public String orderTagName;
	
	
	/**
	 * 订单状态名称
	 */
	public String orderStatus;


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getStylistName() {
		return stylistName;
	}


	public void setStylistName(String stylistName) {
		this.stylistName = stylistName;
	}


	public String getSupervisorName() {
		return supervisorName;
	}


	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}


	public String getContractNo() {
		return contractNo;
	}


	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}


	public Date getSignFinishTime() {
		return signFinishTime;
	}


	public void setSignFinishTime(Date signFinishTime) {
		this.signFinishTime = signFinishTime;
	}


	public BigDecimal getBudgetAmount() {
		return budgetAmount;
	}


	public void setBudgetAmount(BigDecimal budgetAmount) {
		this.budgetAmount = budgetAmount;
	}


	public BigDecimal getImprest() {
		return imprest;
	}


	public void setImprest(BigDecimal imprest) {
		this.imprest = imprest;
	}


	public String getOrderTagName() {
		return orderTagName;
	}


	public void setOrderTagName(String orderTagName) {
		this.orderTagName = orderTagName;
	}


	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
