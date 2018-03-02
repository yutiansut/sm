package cn.damei.business.service.material;

import cn.damei.business.constants.SelectMaterialTypeEnmu;
import cn.damei.business.dao.material.SmOrderQuotaBillDao;
import cn.damei.business.entity.material.SmOrderQuotaBill;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmOrderQuotaBillService extends CrudService<SmOrderQuotaBillDao, SmOrderQuotaBill> {
    @Autowired
    private cn.damei.business.service.material.SmSkuDosageService smSkuDosageService;

    @Transactional(rollbackFor = Exception.class)
    public Map<String,BigDecimal> materialRationing(String contractCode) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String name = loggedUser.getName();
        String jobNum = loggedUser.getOrgCode();
        //计算
        Map<String, Object> dosageByContractCodeList = smSkuDosageService.findDosageByContractCodeList(contractCode);
        //存 批量添加
        ArrayList<SmOrderQuotaBill> list = new ArrayList<>();
        //增项的 基装定额 baseloadrating1
        SmOrderQuotaBill sm1 = new SmOrderQuotaBill();
        sm1.setContractCode(contractCode);
        sm1.setCategoryCode(SelectMaterialTypeEnmu.ADDITEM.toString());
        sm1.setCategoryDetailCode(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
        sm1.setAmount((BigDecimal) dosageByContractCodeList.get("baseloadrating1"));
        sm1.setCreater(name + "(" + jobNum + ")");
        sm1.setCreateTime(new Date());
        list.add(sm1);
        //增项的 基装增项综合费 comprehensivefee1
        SmOrderQuotaBill sm2 = new SmOrderQuotaBill();
        sm2.setContractCode(contractCode);
        sm2.setCategoryCode(SelectMaterialTypeEnmu.ADDITEM.toString());
        sm2.setCategoryDetailCode(SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
        sm2.setAmount((BigDecimal) dosageByContractCodeList.get("comprehensivefee1"));
        sm2.setCreater(name + "(" + jobNum + ")");
        sm2.setCreateTime(new Date());
        list.add(sm2);
        //减项 基装定额  baseloadrating2
        SmOrderQuotaBill sm3 = new SmOrderQuotaBill();
        sm3.setContractCode(contractCode);
        sm3.setCategoryCode(SelectMaterialTypeEnmu.REDUCEITEM.toString());
        sm3.setCategoryDetailCode(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
        sm3.setAmount((BigDecimal) dosageByContractCodeList.get("baseloadrating2"));
        sm3.setCreater(name + "(" + jobNum + ")");
        sm3.setCreateTime(new Date());
        list.add(sm3);
        //其他综合费 othercomprehensivefee
        SmOrderQuotaBill sm4 = new SmOrderQuotaBill();
        sm4.setContractCode(contractCode);
        sm4.setCategoryCode(SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE.toString());
        sm4.setCategoryDetailCode(SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES.toString());
        sm4.setAmount((BigDecimal) dosageByContractCodeList.get("othercomprehensivefee"));
        sm4.setCreater(name + "(" + jobNum + ")");
        sm4.setCreateTime(new Date());
        list.add(sm4);
        //旧房拆改工程·  基装定额 baseloadrating3
        SmOrderQuotaBill sm5 = new SmOrderQuotaBill();
        sm5.setContractCode(contractCode);
        sm5.setCategoryCode(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString());
        sm5.setCategoryDetailCode(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString());
        sm5.setAmount((BigDecimal)dosageByContractCodeList.get("baseloadrating3"));
        sm5.setCreater(name + "(" + jobNum + ")");
        sm5.setCreateTime(new Date());
        list.add(sm5);
        //旧房拆改工程·  基装增项综合费 comprehensivefee3
        SmOrderQuotaBill sm6 = new SmOrderQuotaBill();
        sm6.setContractCode(contractCode);
        sm6.setCategoryCode(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString());
        sm6.setCategoryDetailCode(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString());
        sm6.setAmount((BigDecimal) dosageByContractCodeList.get("comprehensivefee3"));
        sm6.setCreater(name + "(" + jobNum + ")");
        sm6.setCreateTime(new Date());
        list.add(sm6);
        //旧房拆改工程·  其他综合费 othercomprehensivefee3
        SmOrderQuotaBill sm7 = new SmOrderQuotaBill();
        sm7.setContractCode(contractCode);
        sm7.setCategoryCode(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString());
        sm7.setCategoryDetailCode(SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString());
        sm7.setAmount((BigDecimal) dosageByContractCodeList.get("othercomprehensivefee3"));
        sm7.setCreater(name + "(" + jobNum + ")");
        sm7.setCreateTime(new Date());
        list.add(sm7);
        entityDao.batchInsertList(list);
        //返回总金额和拆改
        HashMap<String, BigDecimal> stringBigDecimalHashMap = Maps.newHashMap();
        stringBigDecimalHashMap.put("totalWork",(BigDecimal)dosageByContractCodeList.get("oldhousedemolition"));
        stringBigDecimalHashMap.put("total",(BigDecimal)dosageByContractCodeList.get("totalBudget"));
        return stringBigDecimalHashMap;
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCode(String contractCode){
        entityDao.deleteByCode(contractCode);
    }
}
