package cn.damei.business.service.material;

import cn.damei.business.dao.material.OtherAddReduceAmountDao;
import cn.damei.business.entity.OtherAddReduceAmount;
import cn.damei.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OtherAddRecuceAmountService extends CrudService<OtherAddReduceAmountDao, OtherAddReduceAmount> {
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;

    public List<OtherAddReduceAmount> findByContractCodeList(Map<String, Object> map) {
        return this.entityDao.findByContractCodeList(map);
    }
    public List<Map<String, Object>> getOthermoneyAddOrReduceByContractCode(String contractCode) {
        return this.entityDao.getOthermoneyAddOrReduceByContractCode(contractCode);
    }

    public List<OtherAddReduceAmount> getChangeByContractCode(String contractCode) {
        return entityDao.getChangeByContractCode(contractCode, "1");
    }
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

