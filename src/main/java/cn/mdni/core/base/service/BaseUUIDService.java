package cn.mdni.core.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: service基类
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:24 2017/10/30.
 */
@SuppressWarnings("all")
public abstract class BaseUUIDService<T> implements IBaseUUIDService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
}