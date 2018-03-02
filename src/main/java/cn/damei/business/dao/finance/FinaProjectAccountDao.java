package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.FinaProjectAccount;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

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
