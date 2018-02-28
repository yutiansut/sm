package cn.mdni.business.dao.orderflow;

import cn.mdni.business.entity.orderflow.ProjectSign;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * @Description: 签约Dao
 * @Company: 美得你智装科技有限公司
 * @Author: Chaos
 * @Date: 2017/11/17
 */
@Repository
public interface ProjectSignDao extends CrudDao<ProjectSign> {
    ProjectSign getByCode(String contractCode);
}
