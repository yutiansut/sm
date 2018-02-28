package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.SimpleCustomerContract;
import cn.mdni.business.entity.finance.FinaProjectAccount;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.*;


/**
 * Created by ztw on 2017/11/17/017.
 */
@Repository
public interface FinaCustomerContractDao extends CrudDao<FinaProjectAccount>{

    /**
     * 查询项目信息
     * @param
     * @return
     */
    Map projectInformation(String contractUuid);

    /**
     * 查询订单相关信息
     * @param paramMap
     * @return
     */
    Long contractTotal(Map<String, Object> paramMap);
    List<Map> findContractAll(Map<String, Object> paramMap);

    /**
     *查询账单信息
     * @param codeList
     * @return
     */
    List<Map<String, String>> getAccountByContractCode(@Param("codeList") List<String> codeList);

    /**
     * 通过条件查询财务汇总要导出的条数
     * @param paramMap
     * @return
     */
    Long countProjectSummarizByQuery(Map<String, Object> paramMap);

    /**
     * 财务汇总 ----导出
     * @param paramMap
     * @return
     */
    List<Map> exportProjectSummariz(Map<String, Object> paramMap);

    /**
     * 根据门店code和项目Uuid查询订单简要列表
     * @param contractUuidStr,storeCode
     * @return
     */
    List<SimpleCustomerContract> querySimpleCustomerContractList(@Param("contractUuidArr") String[] contractUuidArr);

    /**
     * 处理查到的对象
     * 函数功能描述:批量查询订单的定金情况
     * @param contractUuidStr
     * @return
     */
    List<Map<String,String>> queryFinanceDepositDetail(String[] contractUuidArr);
}
