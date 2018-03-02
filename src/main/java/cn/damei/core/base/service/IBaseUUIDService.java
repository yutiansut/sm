package cn.damei.core.base.service;

import cn.damei.core.dto.BootstrapPage;

import java.util.List;
import java.util.Map;

public interface IBaseUUIDService<T> {

	public T getById(String id);

	public void insert(T entity);

	public void update(T entity);

	public void deleteById(String id);

	public List<T> findAll();

	public BootstrapPage<T> searchScrollPage(Map<String, Object> params);

	public void insertList(List<T> entity);
}
