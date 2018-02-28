package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.ProjectCloseApply;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退单申请记录Dao
 * @Company: 美得你智装科技有限公司
 * @Author zhanghan
 * @Date: 2017/12/20.
 */
@Repository
public interface ProjectCloseApplyDao extends CrudDao<ProjectCloseApply>{

    /**
     * 通过项目UUID查询退单申请记录
     * @param
     * @return
     */
    List<Map<String,Object>> backRecord(String contractUuid);
}
