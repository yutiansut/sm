package cn.mdni.business.service.finance;

import cn.mdni.business.dao.finance.ProjectCloseApplyDao;
import cn.mdni.business.entity.finance.ProjectCloseApply;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by zhanghan on 2017/12/20
 */
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
