package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.FinaProjectAccount;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * Created by JAVAER on 2017/11/22.
 */
@Repository
public interface FinaProjectAccountDao extends CrudDao<FinaProjectAccount>{
    /**
     * 通过contractUuid查找对应的各种款项
     *
     * @param
     * @return
     */
    FinaProjectAccount getFinaProjectAccountByUuid(String contractUuid);
}
