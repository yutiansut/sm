package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;
import cn.mdni.commons.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更详细 </dd>
 * <dd>@date：2017/11/14  17:18</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
public class SmChangeDetail extends IdEntity {

    private static final long serialVersionUID = 6340730527826430772L;
    /**
     * 项目编码
     */
    private String contractCode;

    /**
     * 变更日志主键
     */
    private Long changeLogId;

    /**
     * 变更类目id(一级类目)
     */
    private String changeCategoryUrl;

    /**
     * 变更单号
     */
    private String changeNo;

    /**
     * 变更类目名称
     */
    private String changeCategoryName;

    /**
     * 当前状态(材料部审核中、设计总监审核中、审计审核中、审核通过、审核未通过)
     */
    private String currentStatus;

    /**
     * 当前审计人
     */
    private String currentAduitUser;

    /**
     * 当前审计时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date currentAuditTime;

    /**
     * 变更金额
     */
    private BigDecimal changeAmount;

    /**
     * 变更前金额
     */
    private BigDecimal changeAmountBefore;

    /**
     * 变更后金额
     */
    private BigDecimal changeAmountAfter;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DF_YMDHMS_EN, timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否过材料部 '0' 否 '1'过材料部
     */
    private String passMaterialsDepartment;
    /**
     * 设计师
     */
    private String  designer;
    /**
     * 客户名字
     */
    private String  name;
    /**
     * 客户手机号码
     */
    private String  mobile;
    /**
     * 装修地址
     */
    private String  addressProvince;
    /**
     * 装修地址
     */
    private String  addressCity;
    /**
     * 装修地址
     */
    private String  addressArea;
    /**
     * 装修地址
     */
    private String  houseAddr;
    /**
     * 设计师电话
     */
    private String  designerMobile;
    /**
     * 计价面积
     */
    private BigDecimal  valuateArea;

    /**
     * 材料审核备注
     */
    private String  materialRemarks;

    /**
     * 设计总监备注
     */
    private String  designDirectorRemarks;

    /**
     * 下载状态
     */
    private String downloadStatus;

    /**
     * 下载次数
     */
    private Integer downloadTimes;

    /**
     * 打印次数
     */
    private Integer printCount;

    private String secondContact;

    private String secondContactMobile;

    public String getMaterialRemarks() {
        return materialRemarks;
    }

    public void setMaterialRemarks(String materialRemarks) {
        this.materialRemarks = materialRemarks;
    }

    public String getDesignDirectorRemarks() {
        return designDirectorRemarks;
    }

    public void setDesignDirectorRemarks(String designDirectorRemarks) {
        this.designDirectorRemarks = designDirectorRemarks;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getHouseAddr() {
        return houseAddr;
    }

    public void setHouseAddr(String houseAddr) {
        this.houseAddr = houseAddr;
    }

    public String getDesignerMobile() {
        return designerMobile;
    }

    public void setDesignerMobile(String designerMobile) {
        this.designerMobile = designerMobile;
    }

    public BigDecimal getValuateArea() {
        return valuateArea;
    }

    public void setValuateArea(BigDecimal valuateArea) {
        this.valuateArea = valuateArea;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getChangeLogId() {
        return changeLogId;
    }

    public void setChangeLogId(Long changeLogId) {
        this.changeLogId = changeLogId;
    }

    public String getChangeCategoryUrl() {
        return changeCategoryUrl;
    }

    public void setChangeCategoryUrl(String changeCategoryUrl) {
        this.changeCategoryUrl = changeCategoryUrl;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getChangeCategoryName() {
        return changeCategoryName;
    }

    public void setChangeCategoryName(String changeCategoryName) {
        this.changeCategoryName = changeCategoryName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentAduitUser() {
        return currentAduitUser;
    }

    public void setCurrentAduitUser(String currentAduitUser) {
        this.currentAduitUser = currentAduitUser;
    }

    public Date getCurrentAuditTime() {
        return currentAuditTime;
    }

    public void setCurrentAuditTime(Date currentAuditTime) {
        this.currentAuditTime = currentAuditTime;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getChangeAmountBefore() {
        return changeAmountBefore;
    }

    public void setChangeAmountBefore(BigDecimal changeAmountBefore) {
        this.changeAmountBefore = changeAmountBefore;
    }

    public BigDecimal getChangeAmountAfter() {
        return changeAmountAfter;
    }

    public void setChangeAmountAfter(BigDecimal changeAmountAfter) {
        this.changeAmountAfter = changeAmountAfter;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPassMaterialsDepartment() {
        return passMaterialsDepartment;
    }

    public void setPassMaterialsDepartment(String passMaterialsDepartment) {
        this.passMaterialsDepartment = passMaterialsDepartment;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getSecondContact() {
        return secondContact;
    }

    public void setSecondContact(String secondContact) {
        this.secondContact = secondContact;
    }

    public String getSecondContactMobile() {
        return secondContactMobile;
    }

    public void setSecondContactMobile(String secondContactMobile) {
        this.secondContactMobile = secondContactMobile;
    }
}