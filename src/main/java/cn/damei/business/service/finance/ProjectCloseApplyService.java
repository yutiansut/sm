package cn.damei.business.service.finance;

import cn.damei.business.dao.finance.ProjectCloseApplyDao;
import cn.damei.business.entity.finance.ProjectCloseApply;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProjectCloseApplyService extends CrudService<ProjectCloseApplyDao,ProjectCloseApply>{

    /**
     * 通过项目UUID查询退单申请记录
     * @param
     * @return
     */
    public List<Map<String,Object>> backRecord(String contractUuid) {
        return entityDao.backRecord(contractUuid);
    }
}
