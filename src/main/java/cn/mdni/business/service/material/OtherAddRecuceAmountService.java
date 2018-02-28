package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.OtherAddReduceAmountDao;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/11/13.
 */
@Service
public class OtherAddRecuceAmountService extends CrudService<OtherAddReduceAmountDao, OtherAddReduceAmount> {
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;

    /**
     * @Description: 美得你智装 通过合同编号获取其他费的列表
     * @date: 2017/11/13  14:04
     * @author: Ryze
     */
    public List<OtherAddReduceAmount> findByContractCodeList(Map<String, Object> map) {
        return this.entityDao.findByContractCodeList(map);
    }

    /**
     * 查询其他金额增减
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getOthermoneyAddOrReduceByContractCode(String contractCode) {
        return this.entityDao.getOthermoneyAddOrReduceByContractCode(contractCode);
    }

    /**
     * @param contractCode 合同编号
     * @Description: 美得你智装 根据合同编号获取变更其他金额增减列表
     * @date: 2017/11/17  18:39
     * @author: Ryze
     */
    public List<OtherAddReduceAmount> getChangeByContractCode(String contractCode) {
        return entityDao.getChangeByContractCode(contractCode, "1");
    }

    /**
     * 查询其他金额增减，计算原金额合计和现金额合计
     *
     * @param contractCode
     * @param changeNo
     * @return
     */
    public List<OtherAddReduceAmount> findOtherAddReduceAmount(String contractCode, String changeNo) {
        List<OtherAddReduceAmount> otherAddReduceAmountList = mongoCustomerContractService.findOtherAddReduceAmountByContrCode(contractCode, changeNo);
        return otherAddReduceAmountList;
    }

    public static void main(String[] args) {
    }

    /**
     * 查询变更标识为1的其他金额
     *
     * @return
     */
    public Long getOthermoneyAddOrReduceByChangeFlag(String contractCode) {
        return this.entityDao.getOthermoneyAddOrReduceByChangeFlag(contractCode);
    }

    /**
     * 查询其他金额根据合同号
     */
    public List<OtherAddReduceAmount> findOtherAddReduceAmountOnlyByContrCode(String contractCode) {
       return this.mongoCustomerContractService.findOtherAddReduceAmountOnlyByContrCode(contractCode);
    }

    /**
     * 将该项目下的其他金额变更标识更新为0
     *
     * @param contractCode
     * @param
     */
    public void updateByContractCodeAndStatus(String contractCode, String changeFlag) {
        this.entityDao.updateByContractCodeAndStatus(contractCode, changeFlag);
    }

    public  List<OtherAddReduceAmount> getOthrgAddReducePrice(String contractCode) {
        return this.entityDao.getOthrgAddReducePrice(contractCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByCodeAndFlag(String contractCode, String flag) {
        this.entityDao.deleteByCodeAndFlag(contractCode,flag);
    }

    public List<OtherAddReduceAmount> findOtherAddReduceAmountOnlyBychangevision(String changevision,String contractCode) {
        return mongoCustomerContractService.findOtherAmountByChanVerNo(contractCode,changevision.substring(0,changevision.length()-3));
    }


}

