package cn.damei.business.dao.material;

import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractChangeDao extends CrudDao<CustomerContract>{

    CustomerContract getByCode(String contractCode);

}