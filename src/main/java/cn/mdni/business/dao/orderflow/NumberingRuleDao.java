package cn.mdni.business.dao.orderflow;

import cn.mdni.business.entity.orderflow.NumberingRule;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Description: 编号规则Dao
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/17.
 */
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
