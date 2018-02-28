package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

/**
 * @Description: 选材门店与产业工人门店关系映射
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/7 17:30.
 */
public class StoreRelationMapping extends IdEntity {
    /**
     * 产业工人门店id
     */
    private Integer mpsStoreId;

    /**
     * 产业工人门店名称
     */
    private String mpsStoreName;

    /**
     * 选材门店名称
     */
    private String smStoreName;

    /**
     * 选材门店code
     */
    private String smStoreCode;

    public Integer getMpsStoreId() {
        return mpsStoreId;
    }

    public void setMpsStoreId(Integer mpsStoreId) {
        this.mpsStoreId = mpsStoreId;
    }

    public String getMpsStoreName() {
        return mpsStoreName;
    }

    public void setMpsStoreName(String mpsStoreName) {
        this.mpsStoreName = mpsStoreName;
    }

    public String getSmStoreName() {
        return smStoreName;
    }

    public void setSmStoreName(String smStoreName) {
        this.smStoreName = smStoreName;
    }

    public String getSmStoreCode() {
        return smStoreCode;
    }

    public void setSmStoreCode(String smStoreCode) {
        this.smStoreCode = smStoreCode;
    }
}
