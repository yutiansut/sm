package cn.damei.core.controller;

import cn.damei.core.SystemConstants;
import cn.damei.core.SystemPropertyHolder;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.OauthAccessTokenUser;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.service.LoginService;
import cn.damei.core.shiro.ShiroSSORealm;
import cn.damei.core.shiro.ShiroUser;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.commons.json.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);// 日志

	@Autowired
	private LoginService loginService;

	/**
	 * 认证中心 相关参数 start
	 **/
	private static final String CODE = "code"; // 返回码
	private static final String CODE_1 = "1"; // 返回码1-成功
	private static final String MESSAGE = "message"; // 返回信息
	private static final String ROLES = "roles"; // 角色信息
	private static final String SCOPE = "scope"; // 操作权限
	private static final String DATA = "data"; // 数据
	private static final String DATA_USERINFO = "userinfo"; // 用户信息
	private static final String DATA_USERINFO_NAME = "name"; // 用户名
	private static final String DATA_USERINFO_MOBILE = "mobile"; // 手机号
	private static final String DATA_USERINFO_USERNAME = "username"; // 员工号(对应jobNo)
	private static final String DATA_USERINFO_DEPCODE = "depCode"; // 部门码
	private static final String DATA_USERINFO_ORGCODE = "orgCode"; // 集团码
	private static final String INDEX_PAGE = "redirect:/index";// 默认跳转地址

	/* 修改密码相关 */
	private static final String JOB_NUM = "jobNum";
	private static final String REDIRECT_URL = "redirectUrl";

	/**
	 * 认证中心回调本系统自动登录
	 *
	 * @param code
	 * @param state
	 */
	@RequestMapping(value = "/oauthCallBack", method = RequestMethod.GET)
	public Object getAccessTokenAndAutoLogin(@RequestParam String code, @RequestParam String state,
			HttpServletRequest request, HttpServletResponse response) {

		StatusDto<OauthAccessTokenUser> status = getAccessToken(code);
		if (status == null) {
			return INDEX_PAGE;
		}

		if (!status.isSuccess()) {
			logger.error("获取单点登录系统OAuth accessToken失败,失败原因：{}", status.getMessage());
			return INDEX_PAGE;
		}
		loginService.login(new ShiroSSORealm.SSOToken(status.getData()), request, response);
		logger.info("用户{}单点登录成功,登录时间:{}", status.getData().getShiroUser().getUsername(),
				DateUtils.parseStr(new Date(), DateUtils.DF_YMDHMS_EN));
		return INDEX_PAGE;
	}

	/**
	 * 执行登录
	 *
	 * @param code
	 * @throws AuthenticationException
	 */
	private StatusDto<OauthAccessTokenUser> getAccessToken(String code) {
		String url = SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthAccessTokenUrl();
		HashMap<String, Object> param = Maps.newHashMap();
		param.put("appid", SystemPropertyHolder.getOauthCenterAppid());
		param.put("secret", SystemPropertyHolder.getOauthCenterSecret());
		param.put("code", code);
		param.put("scope", "true");
		String respJson = HttpUtils.post(url, param);
		if (StringUtils.isNotBlank(respJson)) {
			JavaType javaType = JsonUtils.normalMapper.getMapper().getTypeFactory()
					.constructParametricType(StatusDto.class, OauthAccessTokenUser.class);
			StatusDto<OauthAccessTokenUser> tokenUserStatusDto = (StatusDto<OauthAccessTokenUser>) JsonUtils
					.fromJson(respJson, javaType);

			return tokenUserStatusDto;
		}

		return null;
	}

	/**
	 * 登出,调用单点登录登出接口
	 *
	 * @description 修改原登出接口,调用单点登录服务器
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Object logout(HttpServletRequest request, HttpServletResponse response) {
		ShiroUser loggedUser = WebUtils.getLoggedUser();
		if (loggedUser != null) {
			try {
				String logout = logoutUse(loggedUser, request, response);
				return StatusDto.buildSuccessStatusDto(logout);
			} catch (Exception e) {
				e.printStackTrace();
				return StatusDto.buildFailureStatusDto("退出失败!" + e.getMessage());
			}
		} else {
			return StatusDto.buildSuccessStatusDto();
		}
	}

	/**
	 * @Ryze
	 * @date 2017-7-31 登出 调用口的公共方法
	 */
	public String logoutUse(ShiroUser loggedUser, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String logoutUrl = SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthCenterUrl()
				+ SystemConstants.OAUTH_LOGOUT_URL;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("appid", SystemPropertyHolder.getOauthCenterAppid());
			param.put("secret", SystemPropertyHolder.getOauthCenterSecret());
			param.put("username", loggedUser.getOrgCode());
			param.put("appid", SystemPropertyHolder.getOauthCenterAppid());
			String logout = HttpUtils.post(logoutUrl, param);
			loginService.logout(request, response);
			return logout;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.GET)
	public Object modifyPassword(HttpServletRequest request) {
		String hostName = getHostName(request);
		ShiroUser loggedUser = WebUtils.getLoggedUser();
		if (loggedUser != null) {
			String callBack = hostName + "/logout";
			String updateUrl = SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthCenterUrl()
					+ SystemConstants.OAUTH_PASSWORD_URL + "?" + JOB_NUM + "=" + loggedUser.getOrgCode() + "&"
					+ REDIRECT_URL + "=" + callBack;
			return new ModelAndView("redirect:" + updateUrl);
		} else {
			return StatusDto.buildFailureStatusDto("用户未登录");
		}
	}

	/**
	 * //获取 域名
	 * 
	 * @param request
	 * @return
	 */
	private String getHostName(HttpServletRequest request) {
		// 获取 域名
		StringBuffer url = request.getRequestURL();
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
				.append(request.getServletContext().getContextPath()).toString();
		return tempContextUrl;
	}
}
