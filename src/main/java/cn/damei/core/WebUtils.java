package cn.damei.core;

import cn.damei.business.constants.PropertyHolder;
import cn.damei.core.shiro.SSOShiroUser;
import cn.damei.core.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

    /**
     * 加上threadSession解决shiro一次请求多次从redis读取session的问题 解决完之后:每次非静态资源请求读取一次session,静态资源请求不会读取session
     */
//    public static final ThreadLocal threadSession = new ThreadLocal();

    private WebUtils() {
    }

    /**
     * 返回站点访问Base路径
     *
     * @return http://localhost:8080/ctxPath
     */
    public static String getBaseSiteUrl(HttpServletRequest request) {
        final StringBuilder basePath = new StringBuilder();
        basePath.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80) {
            basePath.append(":").append(request.getServerPort());
        }
        basePath.append(request.getContextPath());
        return basePath.toString();
    }

    /**
     * 判断当前是否有用户登录
     *
     * @param req request
     * @return
     */
    public static boolean isLogin(HttpServletRequest req) {
        if (getLoggedUser() != null)
            return true;
        return false;
    }


    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    public static ShiroUser getLoggedUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Object principal = subject.getPrincipal();
            return (ShiroUser) principal;
        } catch (UnavailableSecurityManagerException ex) {
            return null;
        }
    }


    /**
     * 获取登录用户信息 以及角色权限信息
     *
     * @return 登录用户信息
     */
    public static SSOShiroUser getSSOShiroUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Object principal = subject.getPrincipal();
            return (SSOShiroUser) principal;
        } catch (UnavailableSecurityManagerException ex) {
            return null;
        }
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public static Long getLoggedUserId() {
        ShiroUser user = getLoggedUser();
        return user == null ? null : user.getId();
    }

    /**
     * 返回当前登录用户的带工号的姓名，
     * 如:张三（00013）
     * @return
     */
    public static String getCurrentUserNameWithOrgCode(){
        ShiroUser user = getLoggedUser();
        return user == null ? null : new StringBuffer(user.getName()).append("（").append(user.getOrgCode()).append("）").toString();
    }


    /**
     * 返回当前登录用户所属的名店，有兼职为多个,
     * 没有门店返回null
     * @return
     */
    public static String[] getLoginedUserStores(){
        try {
            ShiroUser user = getLoggedUser();
            String allStoreStr = user.getStoreCode();
            if (StringUtils.isBlank(allStoreStr)) {
                return null;
            }
            return allStoreStr.split(",");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回当前登录用户所属的主门店
     * 没有门店返回 ""
     * @return
     */
    public static String getLoginedUserMainStore(){
        try {
            String[] allStoreStr = getLoginedUserStores();
            if ( null == allStoreStr ) {
                return "";
            }
            //主门店为空时,下一个存在的话,取下一个
            for(int i = 0; i < allStoreStr.length; i++){
                if(StringUtils.isNotBlank(allStoreStr[i])){
                    return allStoreStr[i];
                }
            }
            return "";
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取由shiro管理的session
     *
     * @return
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }
    /**
     * 获取文件上传的访问路径
     *
     * @param path
     * @return
     */
    public static String getUploadFilePath(String path) {
        return PropertyHolder.getUploadBaseUrl() + path;
    }

    /**
     * 获取文件上传的绝对访问路径
     *
     * @param imagePath
     * @return
     */
    public static String getFullUploadFilePath(String imagePath) {
        return PropertyHolder.getBaseurl() + PropertyHolder.getUploadBaseUrl() + imagePath;
    }

}
