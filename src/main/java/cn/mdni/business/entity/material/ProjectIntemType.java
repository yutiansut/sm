package cn.mdni.business.entity.material;

import cn.mdni.core.base.entity.IdEntity;

/**
 * @Description: 施工项分类实体
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 14:30.
 */
public class ProjectIntemType extends IdEntity {
    /**
     * 施工项分类名称
     */
    private String projectIntemTypeName;

    /**
     * 状态
     */
    private String status;

    /**
     * 签名key
     */
    private String key;

    public String getProjectIntemTypeName() {
        return projectIntemTypeName;
    }

    public void setProjectIntemTypeName(String projectIntemTypeName) {
        this.projectIntemTypeName = projectIntemTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
