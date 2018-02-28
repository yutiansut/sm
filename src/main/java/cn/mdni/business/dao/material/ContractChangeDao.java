package cn.mdni.business.dao.material;

import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户合同信息变更Dao
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
@Repository
public interface ContractChangeDao extends CrudDao<CustomerContract>{

    CustomerContract getByCode(String contractCode);

}