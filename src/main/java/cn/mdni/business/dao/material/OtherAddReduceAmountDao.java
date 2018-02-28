package cn.mdni.business.dao.material;

import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/11/13.
 */
@Repository
public interface OtherAddReduceAmountDao extends CrudDao<OtherAddReduceAmount>{
    /**
     * @Description: 美得你智装 通过合同编号获取其他费的列表
     * @date: 2017/11/13  14:04
     * @param map 合同编号 和标记
     * @author: Ryze
     */
    List<OtherAddReduceAmount> findByContractCodeList(Map<String,Object> map);

    List<Map<String,Object>> getOthermoneyAddOrReduceByContractCode(@Param("contractCode") String contractCode);

    /**
     * @Description: 美得你智装 根据合同编号获取其他金额增减列表
     * @date: 2017/11/17  18:39
     * @param contractCode 合同编号
     * @param changeFlag 0 选材的  1变更的
     * @author: Ryze
     */
    List<OtherAddReduceAmount> getChangeByContractCode(@Param("contractCode") String contractCode,@Param("changeFlag")String changeFlag);

    /**
     * 查询变更标识为1的其他金额
     * @return
     */
    Long getOthermoneyAddOrReduceByChangeFlag(String contractCode);

    /**
     * 将该项目下的其他金额变更标识更新为0
     * @param contractCode
     * @param
     */
    void updateByContractCodeAndStatus(@Param("contractCode") String contractCode, @Param("changeFlag") String changeFlag);

    List<OtherAddReduceAmount> getOthrgAddReducePrice(String contractCode);

    void deleteByCodeAndFlag(@Param("contractCode")String contractCode,@Param("flag") String flag);
}
