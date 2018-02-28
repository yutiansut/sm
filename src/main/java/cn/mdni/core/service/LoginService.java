package cn.mdni.core.service;

import cn.mdni.core.dto.StatusDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <dl>
 * <dd>Description: 登录登出service</dd>
 * <dd>Company: 美得你信息技术有限公司</dd>
 * <dd>@date：2016/10/27 13:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * @return 登录成功返回User对象，登录失败返回状态码为400的ResponseEntity对象
     */
    public Object login(String loginName, String password, boolean rememberMe, HttpServletRequest request,
                        HttpServletResponse response) {
        try {
            login(new UsernamePasswordToken(loginName, password, rememberMe), request, response);
            return StatusDto.buildSuccessStatusDto();
        } catch (AuthenticationException e) {
            return StatusDto.buildFailureStatusDto();
        }
    }

    /**
     * 登录
     * @param token
     * @throws AuthenticationException
     */
    public void login(AuthenticationToken token, HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        //如果已登录，先退出
        if (subject.getPrincipal() != null) {
            this.logout(request, response);
        }
        //登录
        subject.login(token);
    }

    /**
     * 登出
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            subject.logout();
        }
    }
}
