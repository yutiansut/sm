package cn.mdni.business.dao.orderflow;


import cn.mdni.business.entity.orderflow.ContractSingle;
import cn.mdni.business.entity.orderflow.SingleTag;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @describe 串单管理dao
 * @author zhangh
 * @date 2017-11-14 11:30:32
 *
 */
@Repository
public interface SingleTagDao extends CrudDao<SingleTag> {

    List<SingleTag> getProjectManageById(@Param("id")Long id,@Param("storeCode")String storeCode);

    List<SingleTag> getProjectManageByCode(@Param("contractCode")String contractCode,@Param("storeCode")String storeCode);

    ContractSingle getContractSingleByContractCode(@Param("contractCode")String contractCode);

    int updateContractSingle(ContractSingle contractSingle);

    int insertContractSingle(ContractSingle contractSingle);

    List<SingleTag> getSingleTagList(Map<String, Object> params);
}