package cn.mdni.core.shiro.token;

import java.util.List;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * <dl>
 * <dd>Description: 重写Shiro的用户名密码token,为了携带更多信息</dd>
 * <dd>Company: 美得你信息技术有限公司</dd>
 * <dd>@date：2017/3/20 下午1:33</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否是单点登录（单点登录不需要密码）
	 */
	private boolean ssoLogin;
	
	private Long id;
	
	private String username;

	private String name;

	private List<String> roles;

	private List<String> permission;

	// public CustomUsernamePasswordToken(String username, String password,
	// boolean ssoLogin) {
	// super(username, password);
	// this.ssoLogin = ssoLogin;
	// }

	public CustomUsernamePasswordToken(Long id,String username, String name, List<String> roles, List<String> permission,
			boolean ssoLogin) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.roles = roles;
		this.permission = permission;
		this.ssoLogin = ssoLogin;
	}
	
	public boolean isSsoLogin() {
		return ssoLogin;
	}

	public void setSsoLogin(boolean ssoLogin) {
		this.ssoLogin = ssoLogin;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getPermission() {
		return permission;
	}

	public void setPermission(List<String> permission) {
		this.permission = permission;
	}
}
