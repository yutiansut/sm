package cn.damei.business.dao.orderflow;


import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractDao extends CrudDao<CustomerContract> {

    /**
     * 修改量房信息
     * @param customerContract
     */
    void updateBookHouse(CustomerContract customerContract);

    /**
     * 修改出图信息
     * @param customerContract
     */
    void updateOutMap(CustomerContract customerContract);

    /**
     * 获取推送到产业工人系统的项目信息
     * @param contractCode
     * @return
     */
    Map<String,Object> getPushMpsInfoByCode(String contractCode);

    /**
     * 同步开竣工时间及状态
     * @param customerContract
     * @return
     */
    void syncProjectInfoFromMps(CustomerContract customerContract);


    /**
     * 通过项目合同的uuid查询项目信息
     * @param contractUuid
     * @return
     */
    CustomerContract getByUuid(String contractUuid);

    /**
     * 修改项目状态
     */
    void updateContractStatus(CustomerContract customerContract);

    /**
     * 根据合同code查询
     * @param contractCode 合同编码
     * @return
     */
    CustomerContract getContractByContractCode(String contractCode);

    /**
     * 通过项目合同的uuid查询项目信息(crm)
     * @param contractUuid
     * @return
     */
    CustomerContract getContractByContractUuid(String contractUuid);

    /**
     * 分配申请退回
     * @param customerContract
     */
    void updateContractRetreat(CustomerContract customerContract);

    /**
     * 督导组长收回
     * @param customerContract
     */
    void updateRecovery(CustomerContract customerContract);

    /**
     * 分配设计组
     * @param customerContract
     */
    void updateDesignerGroup(CustomerContract customerContract);

    /**
     * 根据串单id查询串单信息
     * @param singleId 串单id
     * @return
     */
    List<CustomerContract> findSingleDetailBySingleId(@Param("singleId") Long singleId, @Param("storeCode") String storeCode);
}
