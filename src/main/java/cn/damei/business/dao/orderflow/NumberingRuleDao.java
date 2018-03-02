package cn.damei.business.dao.orderflow;

import cn.damei.business.entity.orderflow.NumberingRule;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface NumberingRuleDao extends CrudDao<NumberingRule> {
    /**
     * 获取单条编号规则
     *
     * @param params 门店编码
     * @param params 编号类型
     */
    NumberingRule getByStoreCodeAndNumType(Map<String,String> params);

    /**
     * 获取 门店
     *
     * @param prefix 门店 项目编号前缀
     */
    NumberingRule getStoreCodeByPrefix(String prefix);
}
