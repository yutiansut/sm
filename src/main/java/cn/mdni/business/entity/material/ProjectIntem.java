package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 施工项信息实体
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 14:30.
 */
public class ProjectIntem extends IdEntity {


    /**
     * 施工项分类id
     */
    private Integer projectIntemTypeId;

    /**
     * 施工项类型 1.增项；2.减项
     */
    private String projectIntemMold;

    /**
     * 施工项编码
     */
    private String projectIntemCode;

    /**
     * 施工项名称
     */
    private String projectIntemName;

    /**
     * 施工项计量单位
     */
    private String projectIntemUnit;

    /**
     * 状态: 1可用, 0不可用
     */
    private String status;

    /**
     * 套餐类型 1.套餐内；2套餐外
     */
    private String groupType;

    /**
     * 施工项详情
     */
    private String projectIntemDetail;

    /**
     * 使用平台
     */
    private String usingPlatform;

    /**
     * 计价方式
     */
    private String valuationMethod;

    /**
     * 所属类别
     */
    private String subordinateCategory;
    /**
     * 是否默认项
     */
    private String isDefault;
    /**
     * 是否默认项
     */
    private String key;

    /**
     * 定额名称
     */
    private String projectIntemTypeName;

    /**
     * 单价或占比
     */
    private BigDecimal projectIntemCostPrice;
    /**
     * 价格
     */

    private BigDecimal projectIntemPrice;

    /**
     * 生效日期
     * @return
     */
    private Date effectDate;

    /**
     * 项目code
     * @return
     */
    private String contractCode;

    /**
     * 用量
     */
    private BigDecimal dosage;



    public BigDecimal getDosage() {
        return dosage;
    }

    public void setDosage(BigDecimal dosage) {
        this.dosage = dosage;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getProjectIntemTypeId() {
        return projectIntemTypeId;
    }

    public void setProjectIntemTypeId(Integer projectIntemTypeId) {
        this.projectIntemTypeId = projectIntemTypeId;
    }

    public String getProjectIntemMold() {
        return projectIntemMold;
    }

    public void setProjectIntemMold(String projectIntemMold) {
        this.projectIntemMold = projectIntemMold;
    }

    public String getProjectIntemCode() {
        return projectIntemCode;
    }

    public void setProjectIntemCode(String projectIntemCode) {
        this.projectIntemCode = projectIntemCode;
    }

    public String getProjectIntemName() {
        return projectIntemName;
    }

    public void setProjectIntemName(String projectIntemName) {
        this.projectIntemName = projectIntemName;
    }

    public String getProjectIntemUnit() {
        return projectIntemUnit;
    }

    public void setProjectIntemUnit(String projectIntemUnit) {
        this.projectIntemUnit = projectIntemUnit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getProjectIntemDetail() {
        return projectIntemDetail;
    }

    public void setProjectIntemDetail(String projectIntemDetail) {
        this.projectIntemDetail = projectIntemDetail;
    }

    public String getUsingPlatform() {
        return usingPlatform;
    }

    public void setUsingPlatform(String usingPlatform) {
        this.usingPlatform = usingPlatform;
    }

    public String getValuationMethod() {
        return valuationMethod;
    }

    public void setValuationMethod(String valuationMethod) {
        this.valuationMethod = valuationMethod;
    }

    public String getSubordinateCategory() {
        return subordinateCategory;
    }

    public void setSubordinateCategory(String subordinateCategory) {
        this.subordinateCategory = subordinateCategory;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProjectIntemTypeName() {
        return projectIntemTypeName;
    }

    public void setProjectIntemTypeName(String projectIntemTypeName) {
        this.projectIntemTypeName = projectIntemTypeName;
    }

    public BigDecimal getProjectIntemCostPrice() {
        return projectIntemCostPrice;
    }

    public void setProjectIntemCostPrice(BigDecimal projectIntemCostPrice) {
        this.projectIntemCostPrice = projectIntemCostPrice;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public BigDecimal getProjectIntemPrice() {
        return projectIntemPrice;
    }

    public void setProjectIntemPrice(BigDecimal projectIntemPrice) {
        this.projectIntemPrice = projectIntemPrice;
    }
}
