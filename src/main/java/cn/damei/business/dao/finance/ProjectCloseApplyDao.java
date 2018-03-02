package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.ProjectCloseApply;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectCloseApplyDao extends CrudDao<ProjectCloseApply>{

    /**
     * 通过项目UUID查询退单申请记录
     * @param
     * @return
     */
    List<Map<String,Object>> backRecord(String contractUuid);
}
