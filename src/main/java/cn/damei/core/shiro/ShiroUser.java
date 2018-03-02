package cn.damei.core.shiro;

import java.io.Serializable;

public class ShiroUser implements Serializable {

    private static final long serialVersionUID = -1473281454547002154L;
    /**
     * 用户id
     */
    private Long id;
    private String username;
    /**
     * 员工唯一编码
     */
    private String jobNum;
    
    /**
     * 部门码
     */
    private String depCode;
    
    /**
     * 集团码
     */
    private String orgCode;

    /**
     * 姓名
     */
    private String name;

    /**
     * 岗位
     */
    private String position;

    /**
     * 门店编码--来自单点登录,多个用逗号拼接,且有顺序关系
     *      直属第一个,兼职随后跟上
     */
    private String storeCode;

    /**
     * 登录用户的 微信头像--来自wechat表
     */
    private String headimgurl;



    public ShiroUser() {
        super();
    }

    public ShiroUser(Long id, String username, String jobNum, String depCode, String orgCode,
                     String name, String storeCode, String headimgurl) {
        this.id = id;
        this.username = username;
        this.jobNum = jobNum;
        this.depCode = depCode;
        this.orgCode = orgCode;
        this.name = name;
        this.storeCode = storeCode;
        this.headimgurl = headimgurl;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiroUser other = (ShiroUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}