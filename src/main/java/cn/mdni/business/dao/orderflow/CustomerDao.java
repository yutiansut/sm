package cn.mdni.business.dao.orderflow;


import cn.mdni.business.entity.orderflow.Customer;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户Dao
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 2017/11/29.
 */
@Repository
public interface CustomerDao extends CrudDao<Customer> {
    Customer getByCode(String code);
}
