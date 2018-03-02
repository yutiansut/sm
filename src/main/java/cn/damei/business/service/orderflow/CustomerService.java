package cn.damei.business.service.orderflow;

import cn.damei.business.dao.orderflow.CustomerDao;
import cn.damei.business.entity.orderflow.Customer;
import cn.damei.core.base.service.CrudService;
import org.springframework.stereotype.Service;


@Service
public class CustomerService extends CrudService<CustomerDao,Customer> {

    public Customer getByCode(String code) {
        return this.entityDao.getByCode(code);
    }
}
