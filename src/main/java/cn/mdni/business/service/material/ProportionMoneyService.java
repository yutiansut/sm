package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.ProportionMoneyDao;
import cn.mdni.business.entity.material.ProportionMoney;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材占比金额</dd>
 * <dd>@date：2017/11/6  15:15</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class ProportionMoneyService extends CrudService<ProportionMoneyDao,ProportionMoney> {
   @Autowired
   private  SmSkuDosageService smSkuDosageService;
    /**
     * @Description: 美得你智装 根据合同编号查询金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
    public ProportionMoney getByContractCode(String contractCode){
        return entityDao.getByContractCode(contractCode);
    }
    /**
     * @Description: 美得你智装 根据合同编号删除金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByContractCode(String contractCode){
        entityDao.deleteByContractCode(contractCode);
    }
    /**
     * @Description: 美得你智装 根据合同编号插入金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertByContractCode(String contractCode){
        Map<String, Object> dosageByContractCodeList = smSkuDosageService.findDosageByContractCodeList(contractCode);
        ProportionMoney proportionMoney = new ProportionMoney();
        proportionMoney.setBaseloadrating1((BigDecimal) dosageByContractCodeList.get("baseloadrating1"));
        proportionMoney.setBaseloadrating3((BigDecimal) dosageByContractCodeList.get("baseloadrating3"));
        proportionMoney.setRenovationAmount((BigDecimal) dosageByContractCodeList.get("renovationAmount"));
        proportionMoney.setComprehensivefee4((BigDecimal) dosageByContractCodeList.get("comprehensivefee4"));
        proportionMoney.setContractCode(contractCode);
        proportionMoney.setCreateTime(new Date());
        entityDao.insert(proportionMoney);
    }

}
