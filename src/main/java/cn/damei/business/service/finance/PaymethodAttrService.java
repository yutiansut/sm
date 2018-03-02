package cn.damei.business.service.finance;

import cn.damei.business.dao.finance.PaymethodAttrDao;
import cn.damei.business.entity.finance.PaymethodAttr;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymethodAttrService extends CrudService<PaymethodAttrDao,PaymethodAttr>{


    /**
     * 查询交款方式属性
     * @param queryParam
     * @return
     */
    public List<PaymethodAttr> queryPayMethodAttrWithCondition(Map<String,Object> queryParam){
        return entityDao.search(queryParam);
    }
}
