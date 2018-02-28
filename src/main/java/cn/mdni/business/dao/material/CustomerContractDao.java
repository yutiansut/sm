package cn.mdni.business.dao.material;

import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Description: 客户合同信息Dao
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
@Repository
public interface CustomerContractDao extends CrudDao<CustomerContract>{

    CustomerContract getByCode(String contractCode);

    void updateStatus(@Param("contractCode") String contractCode, @Param("contractStatus") String contractStatus);

    void updateRefundable(Map<String,Object> map);

    void updateSingleId(CustomerContract customerContract);
}