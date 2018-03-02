package cn.damei.business.dao.material;

import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CustomerContractDao extends CrudDao<CustomerContract>{

    CustomerContract getByCode(String contractCode);

    void updateStatus(@Param("contractCode") String contractCode, @Param("contractStatus") String contractStatus);

    void updateRefundable(Map<String,Object> map);

    void updateSingleId(CustomerContract customerContract);
}