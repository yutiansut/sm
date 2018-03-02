package cn.damei.business.entity.material;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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