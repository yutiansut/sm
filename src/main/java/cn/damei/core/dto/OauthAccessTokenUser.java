package cn.damei.core.dto;

import cn.damei.core.shiro.ShiroUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthAccessTokenUser {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("roles")
	private List<String> roleNameList;

	@JsonProperty("scope")
	private List<String> permissionList;

	@JsonProperty("userinfo")
	private ShiroUser shiroUser;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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

	public ShiroUser getShiroUser() {
		return shiroUser;
	}

	public void setShiroUser(ShiroUser shiroUser) {
		this.shiroUser = shiroUser;
	}
}
