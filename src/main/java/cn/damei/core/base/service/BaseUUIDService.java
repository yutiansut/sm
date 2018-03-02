package cn.damei.core.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class BaseUUIDService<T> implements IBaseUUIDService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
}