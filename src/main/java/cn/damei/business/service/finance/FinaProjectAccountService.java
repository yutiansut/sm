package cn.damei.business.service.finance;

import cn.damei.business.dao.finance.FinaProjectAccountDao;
import cn.damei.business.entity.finance.FinaProjectAccount;
import cn.damei.core.base.service.CrudService;
import cn.mdni.commons.collection.MapUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FinaProjectAccountService extends CrudService<FinaProjectAccountDao,FinaProjectAccount>{

    /**
     * 通过contractUuid查找对应的各种款项
     *
     * @param
     * @return
     */
    public FinaProjectAccount getFinaProjectAccountByUuid(String contractUuid) {
        return entityDao.getFinaProjectAccountByUuid(contractUuid);
    }

    /**
     * 根据项目的uuid查询财务账户
     * @param contractUuid 项目唯一uuid
     * @return
     */
    public FinaProjectAccount getFinaceAccountByContratUuid(String contractUuid){
        Map<String,Object> queryParam = new HashMap<String,Object>();
        MapUtils.putNotNull(queryParam,"contractUuid",contractUuid);

        List<FinaProjectAccount> accountList = entityDao.search(queryParam);
        return accountList.size()>0?accountList.get(0):null;
    }
}
