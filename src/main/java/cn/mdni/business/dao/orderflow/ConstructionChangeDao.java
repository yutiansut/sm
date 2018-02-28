package cn.mdni.business.dao.orderflow;

import cn.mdni.business.entity.material.ConstructionChange;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 基装变更信息Dao
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/17.
 */
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
