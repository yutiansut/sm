/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.damei.core.base.service;

import cn.damei.core.base.dao.CrudDao;
import cn.damei.core.base.dao.CrudUUIDDao;
import cn.damei.core.dto.BootstrapPage;
import cn.damei.core.base.entity.IdEntity;
import cn.damei.core.base.entity.UUIDEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public abstract class CrudUUIDService<D extends CrudUUIDDao<T>, T extends UUIDEntity> extends BaseUUIDService<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D entityDao;

	@Override
	public T getById(String id) {
		return entityDao.getById(id);
	}

	@Override
	@Transactional
	public void insert(T entity) {
		if (entity == null) {
			return;
		}
		entityDao.insert(entity);
	}

	@Override
	@Transactional
	public void update(T entity) {
		if (entity == null){
			return;
		}
		entityDao.update(entity);
	}

	@Override
	public void deleteById(String id) {
		if (StringUtils.isBlank(id)){
			return;
		}
		this.entityDao.deleteById(id);
	}

	@Override
	public List<T> findAll() {
		return entityDao.findAll();
	}
	@Override
	public BootstrapPage<T> searchScrollPage(Map<String, Object> params) {

		List<T> pageData = Collections.emptyList();
		Long count = this.entityDao.searchTotal(params);
		if (count > 0) {
			pageData = entityDao.search(params);
		}
		return new BootstrapPage(count, pageData);
	}

	@Transactional
	public void updateList(List<T> entitys) {
		if (entitys != null && entitys.size() > 0) {
			for (T entity : entitys) {
				this.entityDao.update(entity);
			}
		}
	}

	/**
	 * 插入数据列表 主要在excel导入用
	 * 
	 * @param entitys
	 */
	@Override
	public void insertList(List<T> entitys) {
		if (entitys != null && entitys.size() > 0) {
			final int batchSize = 5000;
			int batchTimes = entitys.size() / batchSize;
			if ((entitys.size() % batchSize) != 0) {
				batchTimes++;
			}

			for (int i = 0; i < batchTimes; i++) {
				int startIdx = i * batchSize;
				int endIdx = Math.min(entitys.size(), startIdx + batchSize);
				List<T> subList = entitys.subList(startIdx, endIdx);
				this.entityDao.batchInsertList(subList);
			}
		}
	}

	/**
	 * 根据实体的属性获取对象列表,除去id属性
	 * 
	 * @param params
	 * @return
	 */
	public List<T> getByEntityProperties(T entity) {
		if (entity == null) {
			return Collections.emptyList();
		}
		return entityDao.getEntityByProperties(entity);
	}

}
