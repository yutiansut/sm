package cn.mdni.business.entity.material;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更 选材用量</dd>
 * <dd>@date：2017/11/6  15:10</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Document(collection = "sm_sku_dosage")
public class SmSkuChangeDosage extends SmSkuDosage {


    /**
     * 增加的用量
     * @return
     */
    private BigDecimal addDosage;

    /**
     * 减少的用量
     * @return
     */
    private BigDecimal reduceDosage;

    public BigDecimal getAddDosage() {
        return addDosage;
    }

    public void setAddDosage(BigDecimal addDosage) {
        this.addDosage = addDosage;
    }

    public BigDecimal getReduceDosage() {
        return reduceDosage;
    }

    public void setReduceDosage(BigDecimal reduceDosage) {
        this.reduceDosage = reduceDosage;
    }
}