package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.PayplanItem;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayplanItemDao extends CrudDao<PayplanItem>{


    /**
     * 查询某个门店当前启用的交款规划的第一个节点
     * @param storeCode
     * @return
     */
    PayplanItem getFirstItemOfPlanWithStore(@Param("storeCode") String storeCode,@Param("itemStatus") String itemStatus);


    /**
     * 按顺序查询某个门店当前启用的交款规划的所有节点
     * @param storeCode
     * @param itemStatus
     * @return
     */
    List<PayplanItem> querySortedPlanItemListWithStore(@Param("storeCode") String storeCode,@Param("itemStatus") String itemStatus);

    /**
     * 获取 交款阶段 是否需要推送到产业工人
     * @param id 阶段id
     * @return
     */
    boolean getNeedPushToMps(Long id);
}
