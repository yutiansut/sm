package cn.damei.business.service.material;

import cn.damei.business.dao.material.ProjectIntemTypeDao;
import cn.damei.business.entity.material.ProjectIntemType;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectIntemTypeService extends CrudService<ProjectIntemTypeDao,ProjectIntemType>{

    public List<ProjectIntemType> getProjectIntemType() {
        List<ProjectIntemType> projectIntemType = this.entityDao.getProjectIntemType();
        return projectIntemType;
    }
}
