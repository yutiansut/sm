package cn.damei.business.dao.orderflow;

import cn.damei.business.entity.orderflow.ProjectSign;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSignDao extends CrudDao<ProjectSign> {
    ProjectSign getByCode(String contractCode);
}
