package cn.mdni.business.service.material;

import cn.mdni.business.constants.PricingMethodEnum;
import cn.mdni.business.constants.SelectMaterialTypeEnmu;
import cn.mdni.business.dao.material.SmOrderQuotaBillDao;
import cn.mdni.business.dao.material.SmSkuChangeDosageDao;
import cn.mdni.business.dao.material.SmSkuDosageDao;
import cn.mdni.business.entity.material.SmOrderQuotaBill;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.service.material.OtherAddRecuceAmountService;
import cn.mdni.core.base.service.CrudUUIDService;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更 选材用量</dd>
 * <dd>@date：2017/11/6  15:15</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class SmSkuChangeDosageService extends CrudUUIDService<SmSkuChangeDosageDao, SmSkuChangeDosage> {
    /**
     * 套餐价格
     */
    private final String PACKAGESTANDARDPRICE = "packagestandardprice";
    /**
     * 升级项价格
     */
    private final String UPGRADEITEMPRICE = "upgradeitemprice";
    /**
     * 增项价格
     */
    private final String INCREMENT = "increment";
    /**
     * 减项价格
     */
    private final String SUBTRACTION = "subtraction";
    /**
     * 其他金额增减
     */
    private final String OTHERAMOUNTSINCREASEORDECREASE = "otheramountsincreaseordecrease";
    /**
     * 其他综合费
     */
    private final String OTHERCOMPREHENSIVEFEE = "othercomprehensivefee";
    /**
     * 旧房拆改费
     */
    private final String OLDHOUSEDEMOLITION = "oldhousedemolition";
    private final String MAINMATERIAL_1 = "mainmaterial1";
    private final String BASELOADRATING_1 = "baseloadrating1";
    private final String COMPREHENSIVEFEE_1 = "comprehensivefee1";
    private final String MAINMATERIAL_2 = "mainmaterial2";
    private final String BASELOADRATING_2 = "baseloadrating2";
    private final String OTHERINCREASE = "otherincrease";
    private final String OTHERMINUS = "otherminus";
    private final String BASELOADRATING_3 = "baseloadrating3";
    private final String COMPREHENSIVEFEE_3 = "comprehensivefee3";
    private final String OTHERCOMPREHENSIVEFEE_3 = "othercomprehensivefee3";
    private final String STRING = "1";
    private final String STRING1 = "0";
    private final BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0);
    @Autowired
    private SmSkuDosageDao smSkuDosageDao;
    @Autowired
    private OtherAddRecuceAmountService otherAddRecuceAmountService;
    @Autowired
    private SmOrderQuotaBillDao smOrderQuotaBillDao;

    /**
     * @param id
     * @Description: 美得你智装 变更 的用量删除（ 原来有用量删除 -》修改 后添加的用量删除）
     * @date: 2017/11/21  14:46
     * @author: Ryze
     */
    public Object deleteOrUpdate(String id) {
        SmSkuChangeDosage byId = entityDao.getById(id);
        //原来有用量删除 -》修改
        if (byId.getOriginalDosage().compareTo(new BigDecimal(0)) == 1) {
            SmSkuChangeDosage entity1 = new SmSkuChangeDosage();
            entity1.setId(id);
            entity1.setNoLossDosage(new BigDecimal(0));
            entity1.setLossDosage(new BigDecimal(0));
            entity1.setBudgetDosage(new BigDecimal(0));
            entityDao.update(entity1);
        } else {
            entityDao.deleteById(id);
        }
        return StatusDto.buildSuccessStatusDto("删除操作成功！");
    }


    /**
     * 查询用量
     *
     * @param contractCode
     * @param changeCategoryUrl
     */
    public List<SmSkuChangeDosage> getBychangeCategoryUrl(String contractCode, String changeCategoryUrl) {
        return this.entityDao.getBychangeCategoryUrl(contractCode, changeCategoryUrl);
    }

    /**
     * 根据项目编号和类目url删除用量表的数据
     *
     * @param contractCode
     * @param changeCategoryUrl
     */
    public void deleteByContractCode(String contractCode) {
        this.entityDao.deleteByContractCode(contractCode);
    }

    /**
     * 删除原用量为0的数据
     *
     * @param contractCode
     * @param changeCategoryUrl
     */
    public void deleteByConAndUrl(String contractCode, String changeCategoryUrl) {
        this.entityDao.deleteByConAndUrl(contractCode, changeCategoryUrl);
    }

    public List<SmSkuChangeDosage> getByOriDosGTZero(String contractCode, String changeCategoryUrl) {
        return this.entityDao.getByOriDosGTZero(contractCode, changeCategoryUrl);
    }

    public void updateBudDosByOriDos(BigDecimal originalDosage, String id) {
        this.entityDao.updateBudDosByOriDos(originalDosage, id);
    }

    /**
     * @param contractCode 合同编号
     * @Description: 美得你智装 变更 根据合同编号 统计金额
     * @date: 2017/11/10  16:02
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> findDosageByContractCodeList(String contractCode) {
        Map<String, Object> params = Maps.newHashMap();
        //获取面积和套餐价格
        Map<String, BigDecimal> contractAreaAndPrice = smSkuDosageDao.getContractAreaAndPrice(contractCode);
        //用量列表
        List<SmSkuChangeDosage> dosageByContractCodeList = entityDao.findDosageByContractCodeList(contractCode);
        //其他金额
        Map<String, Object> stringObjectHashMap = Maps.newHashMap();
        stringObjectHashMap.put("contractCode", contractCode);
        List<OtherAddReduceAmount> byContractCodeList = otherAddRecuceAmountService.findByContractCodeList(stringObjectHashMap);
        //定额列表
        List<SmOrderQuotaBill> byCode = smOrderQuotaBillDao.findByCode(contractCode);
        Map<String, Map<String, List<SmOrderQuotaBill>>> collect1 = byCode.stream().collect(Collectors.groupingBy(SmOrderQuotaBill::getCategoryCode, Collectors.groupingBy(SmOrderQuotaBill::getCategoryDetailCode)));
        //用量分组
        Map<String, List<SmSkuChangeDosage>> collect = dosageByContractCodeList.stream().collect(groupingBy(SmSkuChangeDosage::getCategoryCode));
        //1 套餐价格
        BigDecimal packagestandardprice = new BigDecimal(0);
        //计价面积
        BigDecimal area = contractAreaAndPrice.get("area");
        //套餐单价
        BigDecimal price = contractAreaAndPrice.get("price");
        packagestandardprice = area.multiply(price);
        params.put("area", area);
        params.put("price", price);
        params.put(PACKAGESTANDARDPRICE, packagestandardprice);
        //2 升级项
        BigDecimal upgradeItemPrice = new BigDecimal(0);
        upgradeItemPrice = upgradeItemPrice.add(combiner(SmSkuChangeDosage::getStoreUpgradeDifferencePrice, collect, SelectMaterialTypeEnmu.UPGRADEITEM, null, null));
        params.put(UPGRADEITEMPRICE, upgradeItemPrice);
        //3 增项
        BigDecimal increment = new BigDecimal(0);
        //主材
        BigDecimal mainmaterial1 = new BigDecimal(0);
        mainmaterial1 = mainmaterial1.add(combiner(SmSkuChangeDosage::getStoreIncreasePrice, collect, SelectMaterialTypeEnmu.ADDITEM, SelectMaterialTypeEnmu.MAINMATERIAL, null));
        params.put(MAINMATERIAL_1, mainmaterial1);
        //基装定额
        BigDecimal baseloadrating1 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills = collect1.get(SelectMaterialTypeEnmu.ADDITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills != null && smOrderQuotaBills.size() == 1) {
                baseloadrating1 = smOrderQuotaBills.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_1, baseloadrating1);
        //基装增项综合费  会有基装增项占比---
        BigDecimal comprehensivefee1 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills1 = collect1.get(SelectMaterialTypeEnmu.ADDITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
            if (smOrderQuotaBills1 != null && smOrderQuotaBills1.size() == 1) {
                comprehensivefee1 = smOrderQuotaBills1.get(0).getAmount();
            }
        }
        params.put(COMPREHENSIVEFEE_1, comprehensivefee1);
        increment = mainmaterial1.add(baseloadrating1).add(comprehensivefee1);
        params.put(INCREMENT, increment);
        //4 减项
        BigDecimal subtraction = new BigDecimal(0);
        //主材
        BigDecimal mainmaterial2 = new BigDecimal(0);
        mainmaterial2 = mainmaterial2.add(combiner(SmSkuChangeDosage::getStoreReducePrice, collect, SelectMaterialTypeEnmu.REDUCEITEM, SelectMaterialTypeEnmu.MAINMATERIAL, null));
        params.put(MAINMATERIAL_2, mainmaterial2);
        //基装定额
        BigDecimal baseloadrating2 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills2 = collect1.get(SelectMaterialTypeEnmu.REDUCEITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills2 != null && smOrderQuotaBills2.size() == 1) {
                baseloadrating2 = smOrderQuotaBills2.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_2, baseloadrating2);
        subtraction = mainmaterial2.add(baseloadrating2);
        params.put(SUBTRACTION, subtraction);
        //5 其他金额增减合计
        Optional<List<OtherAddReduceAmount>> byContractCodeList1 = Optional.ofNullable(byContractCodeList);
        BigDecimal otheramountsincreaseordecrease = new BigDecimal(0);
        //增
        BigDecimal otherincrease = new BigDecimal(0);
        otherincrease = otherincrease.add(byContractCodeList1.map(a -> a.stream().filter(b -> STRING.equals(b.getAddReduceType())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(otherincrease));
        params.put(OTHERINCREASE, otherincrease);
        //减
        BigDecimal otherminus = new BigDecimal(0);
        otherminus = otherminus.add(byContractCodeList1.map(a -> a.stream().filter(b -> STRING1.equals(b.getAddReduceType())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(otherminus));
        params.put(OTHERMINUS, otherminus);
        otheramountsincreaseordecrease = otherincrease.subtract(otherminus);
        params.put(OTHERAMOUNTSINCREASEORDECREASE, otheramountsincreaseordecrease);
        //6 其他综合费  会有 基装增项占比-------装修工程占比
        BigDecimal othercomprehensivefee = new BigDecimal(0);
        BigDecimal renovationAmount = new BigDecimal(0);
        renovationAmount = renovationAmount.subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice).add(otheramountsincreaseordecrease);
        renovationAmount = renovationAmount.subtract(byContractCodeList1.map(a -> a.stream().filter(b -> STRING.equals(b.getTaxedAmount())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(new BigDecimal(0)));
        params.put("renovationAmount", renovationAmount);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills3 = collect1.get(SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE.toString()).get(SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES.toString());
            if (smOrderQuotaBills3 != null && smOrderQuotaBills3.size() == 1) {
                othercomprehensivefee = smOrderQuotaBills3.get(0).getAmount();
            }
        }
        params.put(OTHERCOMPREHENSIVEFEE, othercomprehensivefee);
        //7 旧房拆改工程·
        BigDecimal oldhousedemolition = new BigDecimal(0);
        //基装定额
        BigDecimal baseloadrating3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills4 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills4 != null && smOrderQuotaBills4.size() == 1) {
                baseloadrating3 = smOrderQuotaBills4.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_3, baseloadrating3);
        //基装增项综合费  会有拆除基装定额占比----------
        BigDecimal comprehensivefee3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills5 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString());
            if (smOrderQuotaBills5 != null && smOrderQuotaBills5.size() == 1) {
                comprehensivefee3 = smOrderQuotaBills5.get(0).getAmount();
            }
        }
        params.put(COMPREHENSIVEFEE_3, comprehensivefee3);
        //其他综合费   会有拆除基装定额占比----------拆除工程占比
        BigDecimal othercomprehensivefee3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills6 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString());
            if (smOrderQuotaBills6 != null && smOrderQuotaBills6.size() == 1) {
                othercomprehensivefee3 = smOrderQuotaBills6.get(0).getAmount();
            }
        }
        BigDecimal comprehensivefee4 = baseloadrating3.add(comprehensivefee3);
        params.put("comprehensivefee4", comprehensivefee4);
        params.put(OTHERCOMPREHENSIVEFEE_3, othercomprehensivefee3);
        oldhousedemolition = oldhousedemolition.add(comprehensivefee4).add(othercomprehensivefee3);
        params.put(OLDHOUSEDEMOLITION, oldhousedemolition);
        //8 合计
        //装修工程合计
        BigDecimal totalRenovationWorks = othercomprehensivefee.add(otheramountsincreaseordecrease).subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice);
        params.put("totalRenovationWorks", totalRenovationWorks);
        //预算合计
        BigDecimal totalBudget = totalRenovationWorks.add(oldhousedemolition);
        params.put("totalBudget", totalBudget);
        return params;
    }
    /**
     * @param contractCode 合同编号
     * @Description: 美得你智装 变更 根据合同编号 统计金额
     * @date: 2017/11/10  16:02
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> findDosageByContractCodeList2(String contractCode) {
        Map<String, Object> params = Maps.newHashMap();
        //获取面积和套餐价格
        Map<String, BigDecimal> contractAreaAndPrice = smSkuDosageDao.getContractAreaAndPrice(contractCode);
        //用量列表
        List<SmSkuDosage> dosageByContractCodeList = smSkuDosageDao.findDosageByContractCodeList(contractCode);
        //其他金额
        Map<String, Object> stringObjectHashMap = Maps.newHashMap();
        stringObjectHashMap.put("contractCode", contractCode);
        stringObjectHashMap.put("changeFlag","0");
        List<OtherAddReduceAmount> byContractCodeList = otherAddRecuceAmountService.findByContractCodeList(stringObjectHashMap);
        //定额列表
        List<SmOrderQuotaBill> byCode = smOrderQuotaBillDao.findByCode(contractCode);
        Map<String, Map<String, List<SmOrderQuotaBill>>> collect1 = byCode.stream().collect(Collectors.groupingBy(SmOrderQuotaBill::getCategoryCode, Collectors.groupingBy(SmOrderQuotaBill::getCategoryDetailCode)));
        //用量分组
        Map<String, List<SmSkuDosage>> collect = dosageByContractCodeList.stream().collect(groupingBy(SmSkuDosage::getCategoryCode));
        //1 套餐价格
        BigDecimal packagestandardprice = new BigDecimal(0);
        //计价面积
        BigDecimal area = contractAreaAndPrice.get("area");
        //套餐单价
        BigDecimal price = contractAreaAndPrice.get("price");
        packagestandardprice = area.multiply(price);
        params.put("area", area);
        params.put("price", price);
        params.put(PACKAGESTANDARDPRICE, packagestandardprice);
        //2 升级项
        BigDecimal upgradeItemPrice = new BigDecimal(0);
        upgradeItemPrice = upgradeItemPrice.add(combiner1(SmSkuDosage::getStoreUpgradeDifferencePrice, collect, SelectMaterialTypeEnmu.UPGRADEITEM, null, null));
        params.put(UPGRADEITEMPRICE, upgradeItemPrice);
        //3 增项
        BigDecimal increment = new BigDecimal(0);
        //主材
        BigDecimal mainmaterial1 = new BigDecimal(0);
        mainmaterial1 = mainmaterial1.add(combiner1(SmSkuDosage::getStoreIncreasePrice, collect, SelectMaterialTypeEnmu.ADDITEM, SelectMaterialTypeEnmu.MAINMATERIAL, null));
        params.put(MAINMATERIAL_1, mainmaterial1);
        //基装定额
        BigDecimal baseloadrating1 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills = collect1.get(SelectMaterialTypeEnmu.ADDITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills != null && smOrderQuotaBills.size() == 1) {
                baseloadrating1 = smOrderQuotaBills.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_1, baseloadrating1);
        //基装增项综合费  会有基装增项占比---
        BigDecimal comprehensivefee1 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills1 = collect1.get(SelectMaterialTypeEnmu.ADDITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
            if (smOrderQuotaBills1 != null && smOrderQuotaBills1.size() == 1) {
                comprehensivefee1 = smOrderQuotaBills1.get(0).getAmount();
            }
        }
        params.put(COMPREHENSIVEFEE_1, comprehensivefee1);
        increment = mainmaterial1.add(baseloadrating1).add(comprehensivefee1);
        params.put(INCREMENT, increment);
        //4 减项
        BigDecimal subtraction = new BigDecimal(0);
        //主材
        BigDecimal mainmaterial2 = new BigDecimal(0);
        mainmaterial2 = mainmaterial2.add(combiner1(SmSkuDosage::getStoreReducePrice, collect, SelectMaterialTypeEnmu.REDUCEITEM, SelectMaterialTypeEnmu.MAINMATERIAL, null));
        params.put(MAINMATERIAL_2, mainmaterial2);
        //基装定额
        BigDecimal baseloadrating2 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills2 = collect1.get(SelectMaterialTypeEnmu.REDUCEITEM.toString()).get(SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills2 != null && smOrderQuotaBills2.size() == 1) {
                baseloadrating2 = smOrderQuotaBills2.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_2, baseloadrating2);
        subtraction = mainmaterial2.add(baseloadrating2);
        params.put(SUBTRACTION, subtraction);
        //5 其他金额增减合计
        Optional<List<OtherAddReduceAmount>> byContractCodeList1 = Optional.ofNullable(byContractCodeList);
        BigDecimal otheramountsincreaseordecrease = new BigDecimal(0);
        //增
        BigDecimal otherincrease = new BigDecimal(0);
        otherincrease = otherincrease.add(byContractCodeList1.map(a -> a.stream().filter(b -> STRING.equals(b.getAddReduceType())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(otherincrease));
        params.put(OTHERINCREASE, otherincrease);
        //减
        BigDecimal otherminus = new BigDecimal(0);
        otherminus = otherminus.add(byContractCodeList1.map(a -> a.stream().filter(b -> STRING1.equals(b.getAddReduceType())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(otherminus));
        params.put(OTHERMINUS, otherminus);
        otheramountsincreaseordecrease = otherincrease.subtract(otherminus);
        params.put(OTHERAMOUNTSINCREASEORDECREASE, otheramountsincreaseordecrease);
        //6 其他综合费  会有 基装增项占比-------装修工程占比
        BigDecimal othercomprehensivefee = new BigDecimal(0);
        BigDecimal renovationAmount = new BigDecimal(0);
        renovationAmount = renovationAmount.subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice).add(otheramountsincreaseordecrease);
        renovationAmount = renovationAmount.subtract(byContractCodeList1.map(a -> a.stream().filter(b -> STRING.equals(b.getTaxedAmount())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(new BigDecimal(0)));
        params.put("renovationAmount", renovationAmount);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills3 = collect1.get(SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE.toString()).get(SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES.toString());
            if (smOrderQuotaBills3 != null && smOrderQuotaBills3.size() == 1) {
                othercomprehensivefee = smOrderQuotaBills3.get(0).getAmount();
            }
        }
        params.put(OTHERCOMPREHENSIVEFEE, othercomprehensivefee);
        //7 旧房拆改工程·
        BigDecimal oldhousedemolition = new BigDecimal(0);
        //基装定额
        BigDecimal baseloadrating3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills4 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString());
            if (smOrderQuotaBills4 != null && smOrderQuotaBills4.size() == 1) {
                baseloadrating3 = smOrderQuotaBills4.get(0).getAmount();
            }
        }
        params.put(BASELOADRATING_3, baseloadrating3);
        //基装增项综合费  会有拆除基装定额占比----------
        BigDecimal comprehensivefee3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills5 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString());
            if (smOrderQuotaBills5 != null && smOrderQuotaBills5.size() == 1) {
                comprehensivefee3 = smOrderQuotaBills5.get(0).getAmount();
            }
        }
        params.put(COMPREHENSIVEFEE_3, comprehensivefee3);
        //其他综合费   会有拆除基装定额占比----------拆除工程占比
        BigDecimal othercomprehensivefee3 = new BigDecimal(0);
        if (collect1 != null && collect1.size() > 0) {
            List<SmOrderQuotaBill> smOrderQuotaBills6 = collect1.get(SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION.toString()).get(SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString());
            if (smOrderQuotaBills6 != null && smOrderQuotaBills6.size() == 1) {
                othercomprehensivefee3 = smOrderQuotaBills6.get(0).getAmount();
            }
        }
        BigDecimal comprehensivefee4 = baseloadrating3.add(comprehensivefee3);
        params.put("comprehensivefee4", comprehensivefee4);
        params.put(OTHERCOMPREHENSIVEFEE_3, othercomprehensivefee3);
        oldhousedemolition = oldhousedemolition.add(comprehensivefee4).add(othercomprehensivefee3);
        params.put(OLDHOUSEDEMOLITION, oldhousedemolition);
        //8 合计
        //装修工程合计
        BigDecimal totalRenovationWorks = othercomprehensivefee.add(otheramountsincreaseordecrease).subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice);
        params.put("totalRenovationWorks", totalRenovationWorks);
        //预算合计
        BigDecimal totalBudget = totalRenovationWorks.add(oldhousedemolition);
        params.put("totalBudget", totalBudget);
        return params;
    }

    /**
     * @param collect                 分组用量集合
     * @param selectMaterialTypeEnmu1 材料大类
     * @param selectMaterialTypeEnmu2 材料小类
     * @param pricingMethodEnum           计价方式
     * @Description: 美得你智装  金额统计通用方法 （参数都可为空）
     * @date: 2017/11/17  11:24
     * @author: Ryze
     */
    @SuppressWarnings("all")
    private BigDecimal combiner(Function<SmSkuChangeDosage, BigDecimal> lab, Map<String, List<SmSkuChangeDosage>> collect, SelectMaterialTypeEnmu selectMaterialTypeEnmu1, SelectMaterialTypeEnmu selectMaterialTypeEnmu2, PricingMethodEnum pricingMethodEnum) {
        if (collect != null) {
            List<SmSkuChangeDosage> smSkuDosages = collect.get(selectMaterialTypeEnmu1.toString());
            if (smSkuDosages != null) {
                if (selectMaterialTypeEnmu2 != null) {
                    //固定单价
                    return smSkuDosages.stream().filter(b -> selectMaterialTypeEnmu2.toString().equals(b.getCategoryDetailCode())).map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                } else {
                    //固定单价
                    return smSkuDosages.stream().map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                }
            }

        }
        return BIG_DECIMAL_ZERO;
    }

    /**
     * @param collect                 分组用量集合
     * @param selectMaterialTypeEnmu1 材料大类
     * @param selectMaterialTypeEnmu2 材料小类
     * @param pricingMethodEnum           计价方式
     * @Description: 美得你智装  金额统计通用方法 （参数都可为空）
     * @date: 2017/11/17  11:24
     * @author: Ryze
     */
    @SuppressWarnings("all")
    private BigDecimal combiner1(Function<SmSkuDosage, BigDecimal> lab, Map<String, List<SmSkuDosage>> collect, SelectMaterialTypeEnmu selectMaterialTypeEnmu1, SelectMaterialTypeEnmu selectMaterialTypeEnmu2, PricingMethodEnum pricingMethodEnum) {
        if (collect != null) {
            List<SmSkuDosage> smSkuDosages = collect.get(selectMaterialTypeEnmu1.toString());
            if (smSkuDosages != null) {
                if (selectMaterialTypeEnmu2 != null) {
                    //固定单价
                    return smSkuDosages.stream().filter(b -> selectMaterialTypeEnmu2.toString().equals(b.getCategoryDetailCode())).map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                } else {
                    //固定单价
                    return smSkuDosages.stream().map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                }
            }

        }
        return BIG_DECIMAL_ZERO;
    }

    public List<SmSkuChangeDosage> getByContractCode(String contractCode) {
        return this.entityDao.getByContractCode(contractCode);
    }


    /**
     * 查询临时表的变更用量
     * @param contractCode
     * @return
     */
    public List<SmSkuChangeDosage> getDosage(String contractCode,String changeCategoryUrl) {
        return this.entityDao.getDosage(contractCode,changeCategoryUrl);
    }
}
