package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 施工（基装）变更明细
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/22 11:30.
 */
public class ConstructionChangeDetail extends IdEntity{

    /**
     * 变更类型 1 增项  2 减项
     */
    private Integer  changeType;
    /**
     * 变更号
     */
    private String  changeNo;

    /**
     * 施工项code
     */
    private String  projectIntemCode;

    /**
     * 施工项名称
     */
    private String  changeProjectName;

    /**
     * 单位
     */
    private String  unit;

    /**
     * 数量
     */
    private BigDecimal  amount;

    /**
     * 损耗
     */
    private BigDecimal loss;
    /**
     * 人工费
     */
    private BigDecimal laborCosts;
    /**
     * 综合单价
     */
    private BigDecimal totalUnitPrice;
    /**
     * 总价
     */
    private BigDecimal unitProjectTotalPrice;
    /**
     * 说明
     */
    private String  explain;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date  createTime;

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getProjectIntemCode() {
        return projectIntemCode;
    }

    public void setProjectIntemCode(String projectIntemCode) {
        this.projectIntemCode = projectIntemCode;
    }

    public String getChangeProjectName() {
        return changeProjectName;
    }

    public void setChangeProjectName(String changeProjectName) {
        this.changeProjectName = changeProjectName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLoss() {
        return loss;
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss;
    }

    public BigDecimal getLaborCosts() {
        return laborCosts;
    }

    public void setLaborCosts(BigDecimal laborCosts) {
        this.laborCosts = laborCosts;
    }

    public BigDecimal getTotalUnitPrice() {
        return totalUnitPrice;
    }

    public void setTotalUnitPrice(BigDecimal totalUnitPrice) {
        this.totalUnitPrice = totalUnitPrice;
    }

    public BigDecimal getUnitProjectTotalPrice() {
        return unitProjectTotalPrice;
    }

    public void setUnitProjectTotalPrice(BigDecimal unitProjectTotalPrice) {
        this.unitProjectTotalPrice = unitProjectTotalPrice;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
