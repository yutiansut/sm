package cn.mdni.core.base.service;


import cn.mdni.core.dto.BootstrapPage;

import java.util.List;
import java.util.Map;

/**
 * @Description: service接口
 * @Company: 美得你智装科技有限公司
 * @Author Paul
 * @Date: 16:24 2017/10/30.
 */
public interface IBaseService<T> {

	public T getById(Long id);

	public void insert(T entity);

	public void update(T entity);

	public void deleteById(Long id);

	public List<T> findAll();

	public BootstrapPage<T> searchScrollPage(Map<String, Object> params);

	public void insertList(List<T> entity);
}
