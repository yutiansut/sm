package cn.damei.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public class Order {

	private String orderId;//订单id

	private String orderNo;//订单编号

	private Integer roomNum;//房屋户型-室

	private Integer hallNum;//厅

	private Integer toiletNum;//卫

	private BigDecimal floorArea;//房屋面积

	private Integer isNew;//房屋类型

	private Integer isLift;//有无电梯

	private Integer isFDH;//是否期房

	private String province;//房屋地址-省

	private String city;//市

	private String district;//区

	private String address;

	private String activity;//活动名称

	private String discount;//折扣名称

	private Integer planDecorateYear;//计划装修时间-年

	private Integer planDecorateMonth;//计划装修时间-月

	private String serviceName;//客服名称

	private String serviceMobile;//客服电话

	private String stylistName;//设计师

	private String stylistMobile;//设计师电话

	private String supervisorName;//监理

	private String supervisorMobile;//监工电话

	private String contact;//项目经理

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date contractStartTime;//开工日期

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date contractCompleteTime;//竣工日期

	private String comboType;//套餐类型

	private String tagName;//串单标签名称

	private Integer allotState;//订单状态

	public Integer getAllotState() {
		return allotState;
	}

	public void setAllotState(Integer allotState) {
		this.allotState = allotState;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

	public Integer getHallNum() {
		return hallNum;
	}

	public void setHallNum(Integer hallNum) {
		this.hallNum = hallNum;
	}

	public Integer getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(Integer toiletNum) {
		this.toiletNum = toiletNum;
	}

	public BigDecimal getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(BigDecimal floorArea) {
		this.floorArea = floorArea;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getIsLift() {
		return isLift;
	}

	public void setIsLift(Integer isLift) {
		this.isLift = isLift;
	}

	public Integer getIsFDH() {
		return isFDH;
	}

	public void setIsFDH(Integer isFDH) {
		this.isFDH = isFDH;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Integer getPlanDecorateYear() {
		return planDecorateYear;
	}

	public void setPlanDecorateYear(Integer planDecorateYear) {
		this.planDecorateYear = planDecorateYear;
	}

	public Integer getPlanDecorateMonth() {
		return planDecorateMonth;
	}

	public void setPlanDecorateMonth(Integer planDecorateMonth) {
		this.planDecorateMonth = planDecorateMonth;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceMobile() {
		return serviceMobile;
	}

	public void setServiceMobile(String serviceMobile) {
		this.serviceMobile = serviceMobile;
	}

	public String getStylistName() {
		return stylistName;
	}

	public void setStylistName(String stylistName) {
		this.stylistName = stylistName;
	}

	public String getStylistMobile() {
		return stylistMobile;
	}

	public void setStylistMobile(String stylistMobile) {
		this.stylistMobile = stylistMobile;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisorMobile() {
		return supervisorMobile;
	}

	public void setSupervisorMobile(String supervisorMobile) {
		this.supervisorMobile = supervisorMobile;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Date getContractStartTime() {
		return contractStartTime;
	}

	public void setContractStartTime(Date contractStartTime) {
		this.contractStartTime = contractStartTime;
	}

	public Date getContractCompleteTime() {
		return contractCompleteTime;
	}

	public void setContractCompleteTime(Date contractCompleteTime) {
		this.contractCompleteTime = contractCompleteTime;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
