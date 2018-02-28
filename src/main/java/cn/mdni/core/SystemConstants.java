
package cn.mdni.core;

/**
 * @Description: 系统级别常量
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:28 2017/10/30.
 */
public class SystemConstants {

	private SystemConstants() {
	}
	public static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * 接口响应状态码 -成功
	 */
	public static final String RESP_STATUS_CODE_SUCCESS = "1";
	/**
	 * 接口响应状态码 -失败
	 */
	public static final String RESP_STATUS_CODE_FAIL = "0";
	
	public static final String URL_PREFIX = "http://";
	
	/** 认证中心获取code url**/
	public static final String OAUTH_CENTER_CODE_URL = "/oauth/code";
	/** 认证中心获取token url**/
	public static final String OAUTH_CENTER_TOKEN_URL = "/oauth/token";
	/** 认证中心回调 url**/
	public static final String OAUTH_CALL_BACK = "/oauthCallBack";
	/** 认证中心登出url**/
	public static final String OAUTH_LOGOUT_URL = "/oauth/logout";

	/** 认证中心修改密码url**/
	public static final String OAUTH_PASSWORD_URL = "/oauth/password";
	public static final String PAGE_OFFSET = "offset";
	public static final String PAGE_SIZE = "pageSize";

}
