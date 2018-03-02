package cn.damei.business.dao.orderflow;


import cn.damei.business.entity.orderflow.ContractSingle;
import cn.damei.business.entity.orderflow.SingleTag;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface SingleTagDao extends CrudDao<SingleTag> {

    List<SingleTag> getProjectManageById(@Param("id")Long id,@Param("storeCode")String storeCode);

    List<SingleTag> getProjectManageByCode(@Param("contractCode")String contractCode,@Param("storeCode")String storeCode);

    ContractSingle getContractSingleByContractCode(@Param("contractCode")String contractCode);

    int updateContractSingle(ContractSingle contractSingle);

    int insertContractSingle(ContractSingle contractSingle);

    List<SingleTag> getSingleTagList(Map<String, Object> params);
}