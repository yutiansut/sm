package cn.damei.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.net.ssl.*;
import javax.servlet.ServletContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

@Component
@Lazy(false)
public class SystemPropertyHolder implements ServletContextAware, ApplicationContextAware {

    public static ApplicationContext appCtx;
    private static ServletContext servletContext;
    private static String baseurl;
    /*保存外围系统调用本系统open接口的callId和secret*/
    public static Map<String, String> callCertificateMap; 
    
    /** 微信授权中心APPID **/
    private static String wechatAppid;
    /** 大美综管平台url **/
    private static String oaBaseUrl;

    /************************ 认证中心相关  start ************************/
    /** 认证中心URL**/
    private static String oauthCenterUrl;
    /** 认证中心appid **/
    private static String oauthCenterAppid;
    /** 认证中心secret **/
    private static String oauthCenterSecret;
    /** 认证中心获取token url **/
    private static String oauthAccessTokenUrl;
    /**认证中心获取appToken**/
    private static String oauthAppTokenUrl;
    /**认证中心获取用户**/
    private static String oauthAppUserUrl;
    /**获取设计师参数**/
    private static String roleDesignerName;
    /**获取审计员参数**/
    private static String roleAuditorName;
    /************************ 认证中心相关  end ************************/



    @Value("${oauth.accessToken.url}")
    public void setOAuthAccessTokenUrl(String oauthAccessTokenUrl) {
        SystemPropertyHolder.oauthAccessTokenUrl = oauthAccessTokenUrl;
    }

    public static String getOauthAccessTokenUrl() {
        return SystemPropertyHolder.oauthAccessTokenUrl;
    }





    static {
        disableSslVerification();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext ctx) {
        SystemPropertyHolder.servletContext = ctx;
        ctx.setAttribute("ctx", ctx.getContextPath());
    }

    public static String getBaseUrl() {
        return SystemPropertyHolder.baseurl;
    }
    
    @Value("${base.url}")
    public void setBaseUrl(String baseurl) {
        SystemPropertyHolder.baseurl = baseurl;
    }
    
    /** 获取 认证中心url **/
    public static String getOauthCenterUrl() {
        return SystemPropertyHolder.oauthCenterUrl;
    }
    
    /** 设置 认证中心url **/
    @Value("${oauth.center.url}")
    public void setOauthCenterUrl(String oauthCenterUrl) {
        SystemPropertyHolder.oauthCenterUrl = oauthCenterUrl;
    }

    /** 获取 微信授权中心appid **/
    public static String getWechatAppid() {
        return SystemPropertyHolder.wechatAppid;
    }
    
    /** 设置 微信授权中心appid **/
    @Value("${wechat.appid}")
    public void setWechatAppid(String wechatAppid) {
        SystemPropertyHolder.wechatAppid = wechatAppid;
    }
    
    /** 获取 认证中心appid **/
    public static String getOauthCenterAppid() {
        return SystemPropertyHolder.oauthCenterAppid;
    }
    
    /** 设置 认证中心appid **/
    @Value("${oauth.center.appid}")
    public void setOauthCenterAppid(String oauthCenterAppid) {
        SystemPropertyHolder.oauthCenterAppid = oauthCenterAppid;
    }
    
    /** 获取 认证中心secret **/
    public static String getOauthCenterSecret() {
        return SystemPropertyHolder.oauthCenterSecret;
    }
    
    /** 设置 认证中心secret **/
    @Value("${oauth.center.secret}")
    public void setOauthCenterSecret(String oauthCenterSecret) {
        SystemPropertyHolder.oauthCenterSecret = oauthCenterSecret;
    }

    public static String getOauthAppTokenUrl() {
        return oauthAppTokenUrl;
    }
    /** 设置 认证中心appToken **/
    @Value("${oauth.appToken.url}")
    public void setOauthAppTokenUrl(String oauthAppTokenUrl) {
        SystemPropertyHolder.oauthAppTokenUrl = oauthAppTokenUrl;
    }

    /**获取 认证中心用户**/
    public static String getOauthAppUserUrl() {
        return oauthAppUserUrl;
    }
    /**设置 认证中心用户**/
    @Value("${oauth.appUser.url}")
    public void setOauthAppUserUrl(String oauthAppUserUrl) {
        SystemPropertyHolder.oauthAppUserUrl = oauthAppUserUrl;
    }

    /**获取 设计师参数**/
    public static String getRoleDesignerName() {
        return roleDesignerName;
    }
    /**设置 设计师参数**/
    @Value("${role.designer.name}")
    public void setRoleDesignerName(String roleDesignerName) {
        SystemPropertyHolder.roleDesignerName = roleDesignerName;
    }
    /**获取 审计员参数**/
    public static String getRoleAuditorName() {
        return roleAuditorName;
    }
    /**设置 审计员参数**/
    @Value("${role.auditor.name}")
    public void setRoleAuditorName(String roleAuditorName) {
        SystemPropertyHolder.roleAuditorName = roleAuditorName;
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }
}