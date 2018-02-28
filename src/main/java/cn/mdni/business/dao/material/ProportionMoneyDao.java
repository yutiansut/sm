package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.ProportionMoney;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

/**
 * <dl>
 * <dd>Description: 美得你智装  选材占比金额</dd>
 * <dd>@date：2017/11/6  15:14</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface ProportionMoneyDao extends CrudDao<ProportionMoney> {
    /**
     * @Description: 美得你智装 根据合同编号查询金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
   ProportionMoney getByContractCode(String contractCode);
    /**
     * @Description: 美得你智装 根据合同编号删除金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
   void deleteByContractCode(String contractCode);

}
