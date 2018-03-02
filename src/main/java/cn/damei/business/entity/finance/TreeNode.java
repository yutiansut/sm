package cn.damei.business.entity.finance;

import java.util.List;

public class TreeNode {


    /**
     * kd_develop_interface.id (id)
     *
     *
     */
    private String id;

    /**
     * kd_develop_interface.parent_tId (父节点id)
     *
     *
     */
    private String parentId;

    /**
     * kd_develop_interface.name (节点名称/接口描述)
     *
     *
     */
    private String name;
    /**
     * 是否为叶子节点
     *
     *
     */
    private Boolean isParent;



    private List < TreeNode> childTreeNodes ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildTreeNodes() {
        return childTreeNodes;
    }

    public void setChildTreeNodes(List<TreeNode> childTreeNodes) {
        this.childTreeNodes = childTreeNodes;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean Parent) {
        isParent = Parent;
    }
}
