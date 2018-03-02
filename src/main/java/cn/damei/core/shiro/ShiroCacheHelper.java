package cn.damei.core.shiro;

import cn.damei.core.WebUtils;
import cn.damei.core.shiro.cache.ShiroCacheManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ShiroCacheHelper {

    /**
     * Shiro缓存管理类
     */
    private ShiroCacheManager cacheManager;

    private static Logger log = LoggerFactory.getLogger(ShiroCacheHelper.class);


    /**
     * 清除用户的授权信息
     *
     * @param username 用户名
     */
    public void clearAuthorizationInfo(String username) {
        if (log.isDebugEnabled()) {
            log.debug("clear the " + username + " authorizationInfo");
        }
        //因为无法判断要清除缓存的用户是通过那个Realm登录的，所以要遍历所有的realm
        Cache<Object, Object> dbCache = cacheManager.getCache(cn.damei.core.shiro.ShiroSSORealm.REAL_NAME);
        dbCache.remove(username);
    }

    /**
     * 清除当前用户的授权信息
     */
    public void clearAuthorizationInfo() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            String username = Optional.ofNullable(WebUtils.getLoggedUser()).map(cn.damei.core.shiro.ShiroUser::getOrgCode).orElse(null);
            if (username != null) {
                clearAuthorizationInfo(username);
            }
        }
    }

    public ShiroCacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(ShiroCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


}