package cn.damei.business.dao.material;

import cn.damei.business.entity.material.ProportionMoney;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

@Repository
public interface ProportionMoneyDao extends CrudDao<ProportionMoney> {
   ProportionMoney getByContractCode(String contractCode);
   void deleteByContractCode(String contractCode);

}
