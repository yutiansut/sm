package cn.damei.business.dao.orderflow;

import cn.damei.business.entity.material.ConstructionChange;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstructionChangeDao extends CrudDao<ConstructionChange> {

    /**
     * 获取单条数据  获取基装变更
     * @param changeNo
     */
    ConstructionChange getByChangeNo(String changeNo);

    /**
     * 关联客户表-获取客户信息
     * @param ids 通过ids获取多个信息
     * @return
     */
    List<ConstructionChange> findMoreByIds(Long[] ids);

    /**
     * 批量更新打印次数
     * @param ids id数组
     */
    void updatePrintCount(Long[] ids);
}
