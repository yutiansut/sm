package cn.mdni.business.service.orderflow;

import cn.mdni.business.dao.orderflow.CustomerDao;
import cn.mdni.business.entity.orderflow.Customer;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;


/**
 * @Description: 客户Service
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/29.
 */
@Service
public class CustomerService extends CrudService<CustomerDao,Customer> {

    public Customer getByCode(String code) {
        return this.entityDao.getByCode(code);
    }
}
