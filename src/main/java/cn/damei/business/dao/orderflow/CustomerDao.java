package cn.damei.business.dao.orderflow;


import cn.damei.business.entity.orderflow.Customer;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends CrudDao<Customer> {
    Customer getByCode(String code);
}
