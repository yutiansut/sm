package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ProjectIntemType;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 施工项分类dao
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 14:50.
 */
@Repository
public interface ProjectIntemTypeDao extends CrudDao<ProjectIntemType> {

    List<ProjectIntemType> getProjectIntemType();
}
