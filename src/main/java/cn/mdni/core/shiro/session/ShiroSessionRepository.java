package cn.mdni.core.shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * <dl>
 * <dd>Description: shiro session manager interface</dd>
 * <dd>Company: 美得你信息技术有限公司</dd>
 * <dd>@date：2017/3/7 19:11</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public interface ShiroSessionRepository {

    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    Session getSession(Serializable sessionId);

    Collection<Session> getAllSessions();
}
