package cn.mdni.core.dto;

import cn.mdni.core.shiro.ShiroUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Description: OauthAccessTokenUser 认证token
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:28 2017/10/30.
 */
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
