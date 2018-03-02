package cn.damei.business.dto.orderflow;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDto {

	private String  roomNum;
	private String  hallNum;
	private String toiletNum;
	private BigDecimal floorArea;
	private Integer isNew;
	private Integer isLift;
	private Integer isFDH;
	private String province;
	private String city;
	private String district;
	private String address;
	private String activity;
	private String discount;
	private Integer planDecorateYear;
	private Integer planDecorateMonth;
	private String serviceCode;
	private String serviceName;
	private String serviceMobile;
	private String areaFlag;
	private String storeCode;
	private String serviceUserId;
	private String promoteWay;
	private String provinceCode;
	private String cityCode;
	private String districtCode;

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getHallNum() {
		return hallNum;
	}

	public void setHallNum(String hallNum) {
		this.hallNum = hallNum;
	}

	public String getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(String toiletNum) {
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getAreaFlag() {
		return areaFlag;
	}

	public void setAreaFlag(String areaFlag) {
		this.areaFlag = areaFlag;
	}

	public String getServiceUserId() {
		return serviceUserId;
	}

	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}

	public String getPromoteWay() {
		return promoteWay;
	}

	public void setPromoteWey(String promotaWay) {
		this.promoteWay = promoteWay;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
}
