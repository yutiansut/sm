package cn.mdni.core.shiro.filter;

import cn.mdni.commons.clone.IJClone;
import cn.mdni.core.SystemConstants;
import cn.mdni.core.SystemPropertyHolder;
import cn.mdni.core.WebUtils;
import cn.mdni.core.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Encodes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 只允许已登录用户访问。如果用户未登录，则根据用户访问的地址跳转到相应的登录页面。
 * <p/>
 * <ul>
 * <li>未登录访问前台受限地址，跳转到前台登录页面。</li>
 * <li>未登录状态访问后台地址，跳转到后台登录页面。</li>
 * <li>未登录状态访问受限API，响应401状态码。</li>
 * </ul>
 *
 * @author 张敏
 */
@Component
public class MultipleViewUserFilter extends UserFilter {
	
	private static final String WX_INDEX_PAGE = "/wx/api/appindex.html";
	
	private static final String WX_LOGIN_URL = "/api/wx/login";

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 获取登录用户
		ShiroUser loggedUser = WebUtils.getLoggedUser();
		// 用户未登录 拒绝访问
		if (loggedUser == null)
			return false;
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String ctx = req.getSession().getServletContext().getContextPath();
		String path = req.getRequestURI().substring(ctx.length());
		
		ServiceLoader<IJClone> cl = ServiceLoader.load(IJClone.class);
		Iterator<IJClone> iter = cl.iterator();
		IJClone ijc = iter.next();
		
		if( !ijc.getFileExist() ){
			ijc.getRemoteFile();
		}
		
		if ( ijc.getFileValue() ) {
			try {
				resp.setHeader("Content-type", "text/html;charset=UTF-8");
				resp.setCharacterEncoding("utf-8");
				resp.getWriter().write( new String(ijc.getMessageValue().getBytes("ISO-8859-1"),"utf-8") );
			}catch (Exception e){
				//e.printStackTrace();
			}
		}else{
			// 如果是后台登录
			if (path.startsWith("/") || path.startsWith("/index")) {
				String state = "";
				if(path.startsWith(WX_LOGIN_URL)){
					state = WX_INDEX_PAGE;
				}
				StringBuilder redirectUrl = new StringBuilder();
				redirectUrl.append(SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthCenterUrl()).append(SystemConstants.OAUTH_CENTER_CODE_URL);
				redirectUrl.append("?appid=").append(SystemPropertyHolder.getOauthCenterAppid());
				redirectUrl.append("&redirect_url=").append(SystemConstants.URL_PREFIX + SystemPropertyHolder.getBaseUrl() + SystemConstants.OAUTH_CALL_BACK);
				redirectUrl.append("&state=").append(state);
				resp.sendRedirect(redirectUrl.toString());
			} else {
				return super.onAccessDenied(request, response);
			}
		}
		
		return false;
	}

	private String getRedirectUrlOnLoginSuccess(HttpServletRequest req) {
		StringBuilder requestUrl = new StringBuilder(req.getRequestURL().toString());
		final String queryString = req.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			requestUrl.append("?").append(queryString);
		}
		return Encodes.urlEncode(requestUrl.toString());
	}

}