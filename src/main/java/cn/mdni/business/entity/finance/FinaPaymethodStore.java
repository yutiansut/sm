package cn.mdni.business.entity.finance;

/**
 * Created by ztw on 2017/12/4.
 */
public class FinaPaymethodStore {

    private String paymethodId;
    private String paymethodCode;
    private String storeCode;
    private String storeName;

    public String getPaymethodId() {
        return paymethodId;
    }

    public void setPaymethodId(String paymethodId) {
        this.paymethodId = paymethodId;
    }

    public String getPaymethodCode() {
        return paymethodCode;
    }

    public void setPaymethodCode(String paymethodCode) {
        this.paymethodCode = paymethodCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
