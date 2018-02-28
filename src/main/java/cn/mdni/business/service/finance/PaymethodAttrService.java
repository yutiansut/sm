package cn.mdni.business.service.finance;

import cn.mdni.business.dao.finance.PaymethodAttrDao;
import cn.mdni.business.entity.finance.PaymethodAttr;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 交款方式特殊属性
 * @Date Created in 2017/11/30 19:17
 */
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
