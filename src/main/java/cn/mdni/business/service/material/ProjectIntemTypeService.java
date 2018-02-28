package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.ProjectIntemTypeDao;
import cn.mdni.business.entity.material.ProjectIntemType;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 刘铎 on 2017/11/13.
 */
@Service
public class ProjectIntemTypeService extends CrudService<ProjectIntemTypeDao,ProjectIntemType>{

    public List<ProjectIntemType> getProjectIntemType() {
        List<ProjectIntemType> projectIntemType = this.entityDao.getProjectIntemType();
        return projectIntemType;
    }
}
