package cn.damei.business.entity.material;

import cn.damei.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ConstructionChange extends IdEntity{

    /**
     * 项目编码
     */
    private String  contractCode;

    /**
     * 变更号
     */
    private String  changeNo;

    /**
     * 变更申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date changeApplyDate;

    /**
     * 变更原因
     */
    private String  changeReason;

    /**
     * 增项变更合计
     */
    private BigDecimal addProjectTotalPrice;

    /**
     * 减项变更合计
     */
    private BigDecimal cutProjectTotalPrice;

    /**
     * 变更总合计
     */
    private BigDecimal changeListTotalPrice;

    /**
     *  打印次数
     */
    private Integer printCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 变更明细
     */
    private List<ConstructionChangeDetail> constructionChangeInfo;

    /**
     * 扩展 产业工人变更号
     */
    private String constructionChangeNo;

    /**
     * 扩展 产业工人订单号
     */
    private String orderNo;
    /**
     * 扩展 产业工人订单号
     */
    private String key;

    //客户姓名
    private String customerName;
    //客户电话
    private String customerMobile;
    //第二联系人
    private String secondContractName;
    //第二联系电话
    private String secondContractMobile;

    //变更单详情集合
    private List<ConstructionChangeDetail> detailList;



    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public Date getChangeApplyDate() {
        return changeApplyDate;
    }

    public void setChangeApplyDate(Date changeApplyDate) {
        this.changeApplyDate = changeApplyDate;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public BigDecimal getAddProjectTotalPrice() {
        return addProjectTotalPrice;
    }

    public void setAddProjectTotalPrice(BigDecimal addProjectTotalPrice) {
        this.addProjectTotalPrice = addProjectTotalPrice;
    }

    public BigDecimal getCutProjectTotalPrice() {
        return cutProjectTotalPrice;
    }

    public void setCutProjectTotalPrice(BigDecimal cutProjectTotalPrice) {
        this.cutProjectTotalPrice = cutProjectTotalPrice;
    }

    public BigDecimal getChangeListTotalPrice() {
        return changeListTotalPrice;
    }

    public void setChangeListTotalPrice(BigDecimal changeListTotalPrice) {
        this.changeListTotalPrice = changeListTotalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ConstructionChangeDetail> getConstructionChangeInfo() {
        return constructionChangeInfo;
    }

    public void setConstructionChangeInfo(List<ConstructionChangeDetail> constructionChangeInfo) {
        this.constructionChangeInfo = constructionChangeInfo;
    }

    public String getConstructionChangeNo() {
        return constructionChangeNo;
    }

    public void setConstructionChangeNo(String constructionChangeNo) {
        this.constructionChangeNo = constructionChangeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

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

    public String getSecondContractName() {
        return secondContractName;
    }

    public void setSecondContractName(String secondContractName) {
        this.secondContractName = secondContractName;
    }

    public String getSecondContractMobile() {
        return secondContractMobile;
    }

    public void setSecondContractMobile(String secondContractMobile) {
        this.secondContractMobile = secondContractMobile;
    }

    public List<ConstructionChangeDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ConstructionChangeDetail> detailList) {
        this.detailList = detailList;
    }
}
