package cn.damei.core.shiro;


import java.util.List;

public class SSOShiroUser extends ShiroUser {

	private static final long serialVersionUID = 2899514607512650230L;

	private List<String> groupCodeList;

	private List<String> roleNameList;

	private List<String> permissionList;

	public SSOShiroUser(Long id,String username,String jobNum,String depCode,String orgCode,
						String name, String storeCode,String headimgurl) {
		super(id,username,jobNum,depCode,orgCode,name,storeCode,headimgurl);
	}

	public List<String> getGroupCodeList() {
		return groupCodeList;
	}

	public void setGroupCodeList(List<String> groupCodeList) {
		this.groupCodeList = groupCodeList;
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}
}
