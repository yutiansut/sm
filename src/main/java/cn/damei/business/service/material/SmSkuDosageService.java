package cn.damei.business.service.material;

import cn.damei.business.constants.PricingMethodEnum;
import cn.damei.business.constants.SelectMaterialTypeEnmu;
import cn.damei.business.dao.material.SmSkuDosageDao;
import cn.damei.business.entity.material.SmSkuChangeDosage;
import cn.damei.business.entity.material.SmSkuDosage;
import cn.damei.business.entity.OtherAddReduceAmount;
import cn.damei.business.service.material.OtherAddRecuceAmountService;
import cn.damei.core.base.service.CrudUUIDService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SmSkuDosageService extends CrudUUIDService<SmSkuDosageDao,SmSkuDosage> {
    /**
     * 套餐价格
     */
    private  final String PACKAGESTANDARDPRICE = "packagestandardprice";
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
    private final String STRING_ONE = "1";
    private final String STRING_ZERO = "0";
    private   final BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(0);
    @Autowired
    private  OtherAddRecuceAmountService otherAddRecuceAmountService;
    public List<Map<String,String>>  findDomainList(String catalogUrl) {
        String[]  split = catalogUrl.split("-");
        if (split.length >= 3) {
            String url = catalogUrl.substring(0, StringUtils.ordinalIndexOf(catalogUrl, "-", 2) + 1);
            return entityDao.findDomainList(url);
        }
        return entityDao.findDomainList(catalogUrl);

    }


    /**
     * 根据2级分类获取功能区列表
     * @param catalogUrl 分类
     * @date 2017-11-2 17:28:37
     */
    public List<Map<String,String>> findConvertUnitList(String catalogUrl) {
        return entityDao.findConvertUnitList(catalogUrl);
    }

    public Double getLossFactor(String catalogUrl) {
        return entityDao.getLossFactor(catalogUrl);
    }
    /**
     * 根据 skuId 获取门店采购价 和 门店销售价
     * @param  params skuId date priceType
     * @date 日期 2017-11-7 11:56:07
     */
    public BigDecimal getPriceBySkuCode(Map<String,Object> params) {
        return entityDao.getPriceBySkuCode(params);
    }

    /**
     *  通过 项目sku Id 删除sku用量
     * @param projectMaterialId
     */
    public int deleteByMaterialId(String projectMaterialId) {
        return entityDao.deleteByMaterialId(projectMaterialId);
    }
    public String getflg(String catalogUrl) {
      return   entityDao.getFlg(catalogUrl);
    }
    public Map<String,Object> findDosageByContractCodeList(String contractCode){
        Map<String, Object> params = Maps.newHashMap();
        //获取面积和套餐价格
        Map<String, BigDecimal> contractAreaAndPrice = entityDao.getContractAreaAndPrice(contractCode);
        //用量列表
        List<SmSkuDosage> dosageByContractCodeList = entityDao.findDosageByContractCodeList(contractCode);
        //其他金额
        Map<String, Object> stringObjectHashMap = Maps.newHashMap();
        stringObjectHashMap.put("contractCode",contractCode);
        stringObjectHashMap.put("changeFlag","0");
        List<OtherAddReduceAmount> byContractCodeList = otherAddRecuceAmountService.findByContractCodeList(stringObjectHashMap);
        //用量分组
        Map<String, List<SmSkuDosage>> collect = dosageByContractCodeList.stream().collect(groupingBy(SmSkuDosage::getCategoryCode));
        //1 套餐价格
        BigDecimal packagestandardprice=new BigDecimal(0);
            //计价面积
            BigDecimal area = contractAreaAndPrice.get("area");
            //套餐单价
            BigDecimal price = contractAreaAndPrice.get("price");
            params.put("area",area);
            params.put("price",price);
            packagestandardprice=area.multiply(price);
            params.put(PACKAGESTANDARDPRICE,packagestandardprice);
        //2 升级项
        BigDecimal upgradeItemPrice=new BigDecimal(0);
        upgradeItemPrice=upgradeItemPrice.add(combiner(SmSkuDosage::getStoreUpgradeDifferencePrice,collect,SelectMaterialTypeEnmu.UPGRADEITEM,null,null,null));
        params.put(UPGRADEITEMPRICE,upgradeItemPrice);
        //3 增项
        BigDecimal increment=new BigDecimal(0);
            //主材
            BigDecimal mainmaterial1=new BigDecimal(0);
            mainmaterial1=mainmaterial1.add(combiner(SmSkuDosage::getStoreIncreasePrice,collect,SelectMaterialTypeEnmu.ADDITEM,SelectMaterialTypeEnmu.MAINMATERIAL,null,null));
            params.put(MAINMATERIAL_1,mainmaterial1);
            //基装定额
            BigDecimal baseloadrating1=new BigDecimal(0);
            baseloadrating1=baseloadrating1.add(combiner(SmSkuDosage::getStoreIncreasePrice,collect,SelectMaterialTypeEnmu.ADDITEM,SelectMaterialTypeEnmu.BASEINSTALLQUOTA,null,null));
            params.put(BASELOADRATING_1,baseloadrating1);
            //基装增项综合费  会有基装增项占比---
             BigDecimal comprehensivefee1=new BigDecimal(0);
            // 固定单价
            comprehensivefee1=comprehensivefee1.add(combiner(SmSkuDosage::getStoreIncreasePrice,collect,SelectMaterialTypeEnmu.ADDITEM,SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE, PricingMethodEnum.fixedUnitPrice,null));
             // 占比
            comprehensivefee1=comprehensivefee1.add(combiner(null,collect,SelectMaterialTypeEnmu.ADDITEM,SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE, PricingMethodEnum.foundationPileTotal,baseloadrating1));
            params.put(COMPREHENSIVEFEE_1,comprehensivefee1);
        increment=mainmaterial1.add(baseloadrating1).add(comprehensivefee1);
        params.put(INCREMENT,increment);
        //4 减项
        BigDecimal subtraction=new BigDecimal(0);
            //主材
            BigDecimal mainmaterial2=new BigDecimal(0);
            mainmaterial2=mainmaterial2.add(combiner(SmSkuDosage::getStoreReducePrice,collect,SelectMaterialTypeEnmu.REDUCEITEM,SelectMaterialTypeEnmu.MAINMATERIAL,null,null));
            params.put(MAINMATERIAL_2,mainmaterial2);
            //基装定额
            BigDecimal baseloadrating2=new BigDecimal(0);
            baseloadrating2=baseloadrating2.add(combiner(SmSkuDosage::getStoreReducePrice,collect,SelectMaterialTypeEnmu.REDUCEITEM,SelectMaterialTypeEnmu.BASEINSTALLQUOTA,null,null));
            params.put(BASELOADRATING_2,baseloadrating2);
        subtraction=mainmaterial2.add(baseloadrating2);
        params.put(SUBTRACTION,subtraction);
        //5 其他金额增减合计
        Optional<List<OtherAddReduceAmount>> byContractCodeList1 = Optional.ofNullable(byContractCodeList);
        BigDecimal otheramountsincreaseordecrease=new BigDecimal(0);
            //增
            BigDecimal otherincrease=new BigDecimal(0);
            otherincrease=otherincrease.add(byContractCodeList1.map(a->a.stream().filter(b-> STRING_ONE.equals(b.getAddReduceType())).map(c->c.getQuota()).reduce(BIG_DECIMAL_ZERO,BigDecimal::add)).orElse(otherincrease));
            params.put(OTHERINCREASE,otherincrease);
            //减
            BigDecimal otherminus=new BigDecimal(0);
            otherminus = otherminus.add(byContractCodeList1.map(a -> a.stream().filter(b -> STRING_ZERO.equals(b.getAddReduceType())).map(c -> c.getQuota()).reduce(BIG_DECIMAL_ZERO, BigDecimal::add)).orElse(otherminus));
            params.put(OTHERMINUS,otherminus);
            otheramountsincreaseordecrease=otherincrease.subtract(otherminus);
            params.put(OTHERAMOUNTSINCREASEORDECREASE,otheramountsincreaseordecrease);
        //6 其他综合费  会有 基装增项占比-------装修工程占比
        BigDecimal othercomprehensivefee=new BigDecimal(0);
            //固定单价
            othercomprehensivefee=othercomprehensivefee.add(combiner(SmSkuDosage::getStoreSalePrice,collect,SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE,SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES, PricingMethodEnum.fixedUnitPrice,null));
            //基装增项占比
            othercomprehensivefee=othercomprehensivefee.add(combiner(null,collect,SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE,SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES, PricingMethodEnum.foundationPileTotal,baseloadrating1));
            //装修工程总价
            BigDecimal renovationAmount=new BigDecimal(0);
            renovationAmount=renovationAmount.subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice).add(otheramountsincreaseordecrease);
            renovationAmount=renovationAmount.add(byContractCodeList1.map(a->a.stream().filter(b-> STRING_ONE.equals(b.getTaxedAmount())).map(c->c.getQuota()).reduce(BIG_DECIMAL_ZERO,BigDecimal::add)).orElse(new BigDecimal(0)));
            renovationAmount=renovationAmount.add(othercomprehensivefee);
            params.put("renovationAmount",renovationAmount);
            //装修工程占比
            othercomprehensivefee=othercomprehensivefee.add(combiner(null,collect,SelectMaterialTypeEnmu.OTHERCOMPREHENSIVEFEE,SelectMaterialTypeEnmu.OTHERCATEGORIESOFSMALLFEES, PricingMethodEnum.renovationFoundationPile,renovationAmount));
        params.put(OTHERCOMPREHENSIVEFEE,othercomprehensivefee);
        //7 旧房拆改工程·
        BigDecimal oldhousedemolition=new BigDecimal(0);
            //基装定额
            BigDecimal baseloadrating3=new BigDecimal(0);
            baseloadrating3=baseloadrating3.add(combiner(SmSkuDosage::getStoreSalePrice,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA,null,null));
            params.put(BASELOADRATING_3,baseloadrating3);
            //基装增项综合费  会有拆除基装定额占比----------
            BigDecimal comprehensivefee3=new BigDecimal(0);
            comprehensivefee3=comprehensivefee3.add(combiner(SmSkuDosage::getStoreSalePrice,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE, PricingMethodEnum.fixedUnitPrice,null));
            comprehensivefee3=comprehensivefee3.add(combiner(null,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE, PricingMethodEnum.dismantleFoundationPile,baseloadrating3));
            params.put(COMPREHENSIVEFEE_3,comprehensivefee3);
            //其他综合费   会有拆除基装定额占比----------拆除工程占比
            BigDecimal othercomprehensivefee3=new BigDecimal(0);
            othercomprehensivefee3=othercomprehensivefee3.add(combiner(SmSkuDosage::getStoreSalePrice,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE, PricingMethodEnum.fixedUnitPrice,null));
            BigDecimal comprehensivefee4=baseloadrating3.add(comprehensivefee3).add(othercomprehensivefee3);
            othercomprehensivefee3=othercomprehensivefee3.add(combiner(null,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE, PricingMethodEnum.dismantleFoundationPile,baseloadrating3));
            //装修工程占比的计算机额
            params.put("comprehensivefee4",comprehensivefee4);
            othercomprehensivefee3=othercomprehensivefee3.add(combiner(null,collect,SelectMaterialTypeEnmu.OLDHOUSEDEMOLITION,SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE, PricingMethodEnum.demolitionProjectTotal,comprehensivefee4));
            params.put(OTHERCOMPREHENSIVEFEE_3,othercomprehensivefee3);
            oldhousedemolition=oldhousedemolition.add(baseloadrating3).add(othercomprehensivefee3).add(comprehensivefee3);
            params.put(OLDHOUSEDEMOLITION,oldhousedemolition);
        //8 合计
            //装修工程合计
            BigDecimal totalRenovationWorks=othercomprehensivefee.add(otheramountsincreaseordecrease).subtract(subtraction).add(increment).add(upgradeItemPrice).add(packagestandardprice);
            params.put("totalRenovationWorks",totalRenovationWorks);
            //预算合计
            BigDecimal totalBudget=totalRenovationWorks.add(oldhousedemolition);
            params.put("totalBudget",totalBudget);
        return params;
    }
    /**
     * @Description: 大美智装  金额统计通用方法 （参数都可为空）
     * @date: 2017/11/17  11:24
     * @param collect 分组用量集合
     * @param selectMaterialTypeEnmu1 材料大类
     * @param selectMaterialTypeEnmu2 材料小类
     * @param pricingMethodEnum 计价方式
     *
     */
    private BigDecimal combiner(Function<SmSkuDosage, BigDecimal> lab, Map<String, List<SmSkuDosage>> collect, SelectMaterialTypeEnmu selectMaterialTypeEnmu1, SelectMaterialTypeEnmu selectMaterialTypeEnmu2, PricingMethodEnum pricingMethodEnum, BigDecimal money){
        if(collect!=null){
            List<SmSkuDosage> smSkuDosages = collect.get(selectMaterialTypeEnmu1.toString());
            if( smSkuDosages!=null){
                if(selectMaterialTypeEnmu2!=null){
                        //固定单价
                     if(pricingMethodEnum !=null && pricingMethodEnum.equals(PricingMethodEnum.fixedUnitPrice) ){
                         return smSkuDosages.stream().filter(b->selectMaterialTypeEnmu2.toString().equals(b.getCategoryDetailCode())).filter(c-> pricingMethodEnum.toString().equals(c.getDosagePricingMode())).map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                        //占比
                     }else if(pricingMethodEnum !=null && !pricingMethodEnum.equals(PricingMethodEnum.fixedUnitPrice )&& money!=null ){
                         BigDecimal bigDecimal1 = new BigDecimal(100);
                         return smSkuDosages.stream().filter(b->selectMaterialTypeEnmu2.toString().equals(b.getCategoryDetailCode())).filter(c-> pricingMethodEnum.toString().equals(c.getDosagePricingMode())).map(a -> a.getProjectProportion().multiply(money).divide(bigDecimal1)).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                     }
                        //固定单价
                    return smSkuDosages.stream().filter(b->selectMaterialTypeEnmu2.toString().equals(b.getCategoryDetailCode())).map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                }else {
                        //固定单价
                    return smSkuDosages.stream().map(a -> lab.apply(a).multiply(a.getLossDosage())).reduce(BIG_DECIMAL_ZERO, BigDecimal::add);
                }
            }

        }
        return BIG_DECIMAL_ZERO;
    }

    /**
     * 批量插入用量
     * @param smSkuChangeDosageList
     */
    public void insertBySmSkuChangeDosage(List<SmSkuChangeDosage> smSkuChangeDosageList) {
        this.entityDao.insertBySmSkuChangeDosage(smSkuChangeDosageList);
    }

    /**
     * 删除用量
     * @param contractCode
     */
    public void deleteByContractCode(String contractCode) {
        this.entityDao.deleteByContractCode(contractCode);
    }

    /**
     * 根据项目code和类目url查询用量
     * @param contractCode
     * @param changeCategoryUrl
     * @return
     */
    public List<SmSkuDosage> getByConCodeAndChUrl(String contractCode, String changeCategoryUrl) {
       return this.entityDao.getByConCodeAndChUrl(contractCode,changeCategoryUrl);
    }

    /**
     * 根据项目编号获取用量
     * @param contractCode 项目编号
     */
    public List<SmSkuDosage> getByContractCode(String contractCode){
        return this.entityDao.getByContractCode(contractCode);
    }

    public List<SmSkuDosage> findByConAndUrl(String contractCode, String changeCategoryUrl) {
        return this.entityDao.findByConAndUrl(contractCode,changeCategoryUrl);
    }
}
