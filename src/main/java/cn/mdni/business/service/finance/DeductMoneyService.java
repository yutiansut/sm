package cn.mdni.business.service.finance;

import cn.mdni.business.dao.finance.DeductMoneyDao;
import cn.mdni.business.entity.finance.DeductMoney;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 抵扣金service类
 * @Date Created in 2017/12/1 23:44
 */
@Service
public class DeductMoneyService extends CrudService<DeductMoneyDao,DeductMoney>{

    /**
     * 根据动态条件查询抵扣金
     * @param queryParam 查询条件
     * @return
     */
    public List<DeductMoney> queryDeductWithCondition(Map<String,Object> queryParam){
        return entityDao.search(queryParam);
    }

    /**
     * 根据payId查询红冲数据
     * @param params
     * @return
     */
    public List<DeductMoney> findDeductMoney(Map<String, Object> params) {
        return entityDao.search(params);
    }
}
