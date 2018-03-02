package cn.damei.core.shiro.session;

import cn.mdni.commons.serialize.SerializeUtils;
import cn.damei.core.shiro.JedisManager;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

public class JedisShiroSessionRepository implements cn.damei.core.shiro.session.ShiroSessionRepository {

	public static final String REDIS_SHIRO_SESSION = "shiro-session:";

	private static final long DEFAULT_SESSION_TIMEOUT = 1800000L;

	private JedisManager jedisManager;

	private long sessionTimeOut = DEFAULT_SESSION_TIMEOUT;

	private final Logger logger = LoggerFactory.getLogger(JedisShiroSessionRepository.class);

	@Override
	public void saveSession(Session session) {
		if (session == null || session.getId() == null)
			throw new NullPointerException("session is empty");
		try {
			logger.debug("save session {}", session.getId());
			byte[] key = SerializeUtils.serialize(buildRedisSessionKey(session.getId()));
			byte[] value = SerializeUtils.serialize(session);
			session.setTimeout(sessionTimeOut);
			Long expireTime = sessionTimeOut / 1000;

			// WebUtils.threadSession.set(session);
			getJedisManager().saveValueByKey(key, value, expireTime.intValue());
		} catch (Exception e) {
			logger.error("save session error", e);
		}
	}

	@Override
	public void deleteSession(Serializable id) {
		if (id == null) {
			throw new NullPointerException("session id is empty");
		}
		try {
			logger.debug("delete session {}", id);
			// WebUtils.threadSession.remove();
			getJedisManager().deleteByKey(SerializeUtils.serialize(buildRedisSessionKey(id)));
		} catch (Exception e) {
			logger.error("delete session error", e);
		}
	}

	@Override
	public Session getSession(Serializable id) {
		if (id == null)
			throw new NullPointerException("session id is empty");
		Session session = null;
		try {
			byte[] value = getJedisManager().getValueByKey(SerializeUtils.serialize(buildRedisSessionKey(id)));
			session = SerializeUtils.deserialize(value, Session.class);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get session error {}", id);
		}
		return session;
	}

	@Override
	public Collection<Session> getAllSessions() {
		logger.debug("get all sessions");
		return null;
	}

	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}

	public JedisManager getJedisManager() {
		return jedisManager;
	}

	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}

	public long getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(long sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}
}
