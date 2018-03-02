package cn.damei.business.service.material;

import cn.damei.business.dao.material.ProportionMoneyDao;
import cn.damei.business.entity.material.ProportionMoney;
import cn.damei.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class ProportionMoneyService extends CrudService<ProportionMoneyDao,ProportionMoney> {
   @Autowired
   private  SmSkuDosageService smSkuDosageService;
    public ProportionMoney getByContractCode(String contractCode){
        return entityDao.getByContractCode(contractCode);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteByContractCode(String contractCode){
        entityDao.deleteByContractCode(contractCode);
    }
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
