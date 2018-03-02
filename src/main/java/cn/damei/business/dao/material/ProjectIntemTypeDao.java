package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProjectIntemType;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectIntemTypeDao extends CrudDao<ProjectIntemType> {

    List<ProjectIntemType> getProjectIntemType();
}
