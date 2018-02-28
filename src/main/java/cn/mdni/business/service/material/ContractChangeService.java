package cn.mdni.business.service.material;

import cn.mdni.business.constants.*;
import cn.mdni.business.dao.material.ContractChangeDao;
import cn.mdni.business.dao.material.OtherAddReduceAmountDao;
import cn.mdni.business.dao.material.ProdCatalogDao;
import cn.mdni.business.dao.material.ProjectChangeMaterialDao;
import cn.mdni.business.dao.material.ProjectMaterialDao;
import cn.mdni.business.dao.material.SmSkuChangeDosageDao;
import cn.mdni.business.dao.material.SmSkuDosageDao;
import cn.mdni.business.entity.material.ChangeLog;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.material.ProjectChangeMaterial;
import cn.mdni.business.entity.material.ProjectMaterial;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.entity.material.ProdCatalog;
import cn.mdni.business.service.operatelog.OperateLogService;
import cn.mdni.commons.bean.BeanUtils;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.BootstrapPage;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Description: 客户合同信息变更Service
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
@Service
public class ContractChangeService extends CrudService<ContractChangeDao, CustomerContract> {

    @Autowired
    private SmSkuChangeDosageDao smSkuChangeDosageDao;
    @Autowired
    private ProjectChangeMaterialDao projectChangeMaterialDao;
    @Autowired
    private SmSkuDosageDao smSkuDosageDao;
    @Autowired
    private ProjectMaterialDao projectMaterialDao;
    @Autowired
    private OtherAddReduceAmountDao otherAddReduceAmountDao;
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;
    @Autowired
    private ChangeLogService changeLogService;
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProdCatalogDao prodCatalogDao;
    @Autowired
    private ContractOperateTimeSummaryService contractOperateTimeSummaryService;
    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;
    @Autowired
    private OperateLogService operateLogService;
    /**
     * mongo原始选材标识
     */
    private static final String MATERIAL_FLAG = "0";
    /**
     * 变更拆单返回值
     */
    private static String FLAG_ZERO = "0";

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
    /**
     * 发起变更业务
     * ①在mongo里存备份
     * ②把sku和用量表，拷贝存储到临时sku和用量表
     * ③变更项目表状态：变更中
     * ④在变更log里插入一条变更日志
     * ⑤往项目表里插入变更版本号
     *
     * @param id 项目表主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void startChange(Long id) {
        //根据id获取项目
        CustomerContract contract = this.entityDao.getById(id);
        //获取操作时间汇总信息并执行插入或者修改操作
        this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(contract.getContractCode(),CustomerContractEnum.CHANGING);
        //根据项目code获取选材sku
        List<ProjectMaterial> projectMaterial = this.projectMaterialDao.getByContractCode(contract.getContractCode());
        //根据项目code获取用量信息
        List<SmSkuDosage> skuDosageList = this.smSkuDosageDao.getByContractCode(contract.getContractCode());
        //根据合同编号获取其他金额
        List<OtherAddReduceAmount> otherMoneyAddOrReduce = this.otherAddReduceAmountDao.getChangeByContractCode(contract.getContractCode(), "0");
        //生成变更版本号
        String currentChangeVersion = this.changeLogService.getChangeVersion(contract.getContractCode());
        //构建变更Sku版本
        List<ProjectChangeMaterial> projectChangeMaterial = new ArrayList<>();
        //将原始选材sku复制一份到临时sku
        for (ProjectMaterial material : projectMaterial) {
            Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(material, false);
            ProjectChangeMaterial entity1 = BeanUtils.mapTransBean(stringObjectMap, ProjectChangeMaterial.class);
            projectChangeMaterial.add(entity1);
        }
        List<SmSkuChangeDosage> smSkuChangeDosageList = new ArrayList<>();
        //将原始用量复制一份到临时用量
        for (SmSkuDosage smSkuDosage : skuDosageList) {
            Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(smSkuDosage, false);
            SmSkuChangeDosage entity1 = BeanUtils.mapTransBean(stringObjectMap, SmSkuChangeDosage.class);
            entity1.setOriginalDosage(smSkuDosage.getLossDosage());
            smSkuChangeDosageList.add(entity1);
        }
        //将临时选材sku插入临时sku表
        this.projectChangeMaterialDao.batchInsert(projectChangeMaterial);
        //将临时用量批量插入临时用表
        this.smSkuChangeDosageDao.batchInsert(smSkuChangeDosageList);
        //构建变更日志
        ChangeLog changeLog = new ChangeLog();
        changeLog.setContractCode(contract.getContractCode());
        changeLog.setOperatUser(WebUtils.getLoggedUser().getName());
        changeLog.setOperatTime(new Date());
        changeLog.setChangeStatus(ChangeLogEnum.CHANGING);
        changeLog.setChangeVersionNo(currentChangeVersion);
        //插入变更日志
        this.changeLogService.insert(changeLog);
        //将选材sku插入mongo存备份
        if (!projectMaterial.isEmpty()) {
            //在mongo中存入变更版本号的冗余，方便查询
            for (ProjectMaterial material : projectMaterial) {
                material.setDbId(material.getId());
                material.setId(null);
                //如果没有发起过变更则存入当前版本号未 00
                if(StringUtils.isEmpty(contract.getCurrentChangeVersion())){
                    material.setChangeVersionNo(contract.getContractCode()+"00");
                }else {
                    material.setChangeVersionNo(currentChangeVersion);
                }
                material.setMaterialFlag(MATERIAL_FLAG);
            }
            this.mongoCustomerContractService.insertBatch(projectMaterial);
        }
        //将用量批量插入mongo存备份
        if (!skuDosageList.isEmpty()) {
            //在用量中存入项目编号和变更版本号冗余，方便查询
            for (SmSkuDosage smSkuDosage : skuDosageList) {
                smSkuDosage.setId(null);
                smSkuDosage.setContractCode(contract.getContractCode());
                //如果没有发起过变更则存入当前版本号未 00
                if(StringUtils.isEmpty(contract.getCurrentChangeVersion())){
                    smSkuDosage.setChangeVersionNo(contract.getContractCode()+"00");
                }else {
                    smSkuDosage.setChangeVersionNo(currentChangeVersion);
                }
                smSkuDosage.setSkuMaterialFlag(MATERIAL_FLAG);
            }
            this.mongoCustomerContractService.insertBatch(skuDosageList);
        }
        //将其他金额批量插入mongo备份
        if (!otherMoneyAddOrReduce.isEmpty()) {
            for (OtherAddReduceAmount otherAddReduceAmount : otherMoneyAddOrReduce) {
                otherAddReduceAmount.setId(null);
                otherAddReduceAmount.setContractCode(contract.getContractCode());
                //如果没有发起过变更则存入当前版本号未 00
                if(StringUtils.isEmpty(contract.getCurrentChangeVersion())){
                    otherAddReduceAmount.setChangeVersionNo(contract.getContractCode()+"00");
                }else {
                    otherAddReduceAmount.setChangeVersionNo(currentChangeVersion);
                }
                otherAddReduceAmount.setChangeFlag(MATERIAL_FLAG);
            }
            this.mongoCustomerContractService.insertBatch(otherMoneyAddOrReduce);
        }
        //将项目状态改为变更中
        contract.setContractStatus(CustomerContractEnum.CHANGING);
        //生成变更版本号
        contract.setCurrentChangeVersion(currentChangeVersion);
        this.customerContractService.update(contract);
    }

    /**
     * 查询原始其他金额信息
     * @param contractCode 单号
     */
    public List<OtherAddReduceAmount> findOtherAddReduceAmount(String contractCode){
        List<OtherAddReduceAmount> addReduceAmountList = this.mongoCustomerContractService.findOtherAddReduceAmountByChangeVersion(contractCode);
        return addReduceAmountList;
    }
    /**
     * 查询原始选材单信息
     *
     * @param contractCode 项目编号
     */
    public List<ProdCatalog> findMaterialByCodeAndFlag(String contractCode) {
        List<ProjectMaterial> materials = this.mongoCustomerContractService.findMaterialByCodeAndFlag(contractCode,MATERIAL_FLAG);
        List<SmSkuDosage> smSkuDosages = this.mongoCustomerContractService.findSmSkuDosageByCodeAndFlag(contractCode);
        //通过 一级分类url 查询所有分类
        Map<String, Object> catalogParams = new HashMap<String,Object>();
        String url = "";
        MapUtils.putNotNull(catalogParams, "url", url);
        List<ProdCatalog> catalogList = prodCatalogDao.findAll(catalogParams);
        for (ProjectMaterial projectMaterial : materials) {
            List<SmSkuDosage> smSkuDosageList = new ArrayList<>();
            //遍历本次变更单的用量
            for (SmSkuDosage smSkuDosage : smSkuDosages) {
                if (smSkuDosage.getProjectMaterialId().equals(projectMaterial.getDbId())) {
                    smSkuDosageList.add(smSkuDosage);
                }
            }
            projectMaterial.setSkuDosageList(smSkuDosageList);
        }
        for(ProdCatalog cat: catalogList){
            if(cat != null && cat.getParent() != null && cat.getParent().getId().equals(0L)){
                //是一级分类 去查找其子分类并将 二级分类对应的项目主材sku及sku用量
                cat.setSubCatalogList(findSbuCatalogsWithSku(cat, catalogList, materials, ProjectMaterial.class));
            }
        }
        return catalogList;
    }

    /**
     * @Description: 美得你智装 选材 根据合同编号 统计金额
     * @date: 2017/11/10  16:02
     * @param contractCode 合同编号
     * @author: Ryze
     */
    public Map<String,Object> findDosageByContractCodeList(String contractCode){
        Map<String, Object> params = Maps.newHashMap();
        //获取面积和套餐价格
        CustomerContract customerContract = customerContractService.getByCode(contractCode);
        //用量列表
        List<SmSkuDosage> dosageByContractCodeList = this.mongoCustomerContractService.findSmSkuDosageByCodeAndFlag(contractCode);
        //其他金额
        List<OtherAddReduceAmount> byContractCodeList = this.mongoCustomerContractService.findOtherAddReduceAmountByChangeVersion(contractCode);
        //用量分组
        Map<String, List<SmSkuDosage>> collect = dosageByContractCodeList.stream().collect(groupingBy(SmSkuDosage::getCategoryCode));
        //1 套餐价格
        BigDecimal packagestandardprice=new BigDecimal(0);
        //计价面积
        BigDecimal area = customerContract.getValuateArea();
        //套餐单价
        BigDecimal price = customerContract.getMealPrice();
        params.put("area",area);
        params.put("price",price);
        packagestandardprice=area.multiply(price);
        params.put(PACKAGESTANDARDPRICE,packagestandardprice);
        //2 升级项
        BigDecimal upgradeItemPrice=new BigDecimal(0);
        upgradeItemPrice=upgradeItemPrice.add(combiner(SmSkuDosage::getStoreUpgradeDifferencePrice,collect, SelectMaterialTypeEnmu.UPGRADEITEM,null,null,null));
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
     * @Description: 美得你智装  金额统计通用方法 （参数都可为空）
     * @date: 2017/11/17  11:24
     * @param collect 分组用量集合
     * @param selectMaterialTypeEnmu1 材料大类
     * @param selectMaterialTypeEnmu2 材料小类
     * @param pricingMethodEnum 计价方式
     * @author: Ryze
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
     * 获取 一层子分类, 并将二级分类对应的项目主材sku及sku用量
     * @param catalog
     * @param catalogList
     * @Author: Paul
     * @return
     */
    private <T> List<ProdCatalog> findSbuCatalogsWithSku(ProdCatalog catalog, List<ProdCatalog> catalogList,
                                                         List<T> projectMaterialList, Class clazz) {
        ArrayList<ProdCatalog> subCatalogList = new ArrayList<>(0);

        if(catalog == null || catalogList == null || catalogList.size() == 0){
            return subCatalogList;
        }
        //目标对象分类的url
        String url = catalog.getUrl();

        //二级分类对应的项目sku集合
        List<ProjectChangeMaterial> newMaterialList = null;
        for(ProdCatalog cat: catalogList){
            //判断当前url 是否包含当前url
            if(cat != null && StringUtils.isNotBlank(cat.getUrl())
                    && cat.getUrl() != url  && cat.getUrl().indexOf(url) != -1){
                newMaterialList = new ArrayList<ProjectChangeMaterial>();
                //是对应的二级分类, 去查找对应的 项目sku
                if(projectMaterialList != null && projectMaterialList.size() > 0){
                    for(T projectMaterial : projectMaterialList){
                        if( projectMaterial != null && cat.getUrl().equals(
                                ((ProjectMaterial)projectMaterial).getProductCategoryUrl()) ){
                            //是当前二级分类的 项目sku
                            if(projectMaterial.getClass().equals(clazz)){
                                //选材对象,需要转为选材变更对象
                                ProjectChangeMaterial projectChangeMaterial = new ProjectChangeMaterial();
                                try {
                                    org.apache.commons.beanutils.BeanUtils.copyProperties(projectChangeMaterial, projectMaterial);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                newMaterialList.add(projectChangeMaterial);
                            }else{
                                newMaterialList.add((ProjectChangeMaterial)projectMaterial);
                            }

                        }
                    }
                }
                //添加到该二级分类中
                cat.setProjectMaterialList(newMaterialList);
                subCatalogList.add(cat);
            }
        }
        return subCatalogList;
    }


    /**
     * 获取项目列表 隐藏手机号中间四位
     * @param paramMap
     * @return
     */
    public BootstrapPage<CustomerContract> searchCustomerContractScrollPage(Map<String, Object> paramMap) {
        BootstrapPage<CustomerContract> result = this.searchScrollPage(paramMap);
        result.setRows(customerContractService.replaceAllMobile(result.getRows()));
        return result;
    }

    /**
     * 变更审核状态
     * @param id 项目id
     * @param status 项目状态
     */
    public void updateChangeStatus(Long id, CustomerContractEnum status){
        CustomerContract customerContract = this.customerContractService.getById(id);
        //是否提交变更审核
        if(status.equals(CustomerContractEnum.CHANGE_AUDIT)){
            ChangeLog changeLog = this.changeLogService.getChangeLogByChNo(customerContract.getCurrentChangeVersion());
            String flag = this.smChangeDetailService.submitChangeReview(changeLog.getId(), customerContract.getCurrentChangeVersion(), customerContract.getContractCode());
            if(flag.equals(FLAG_ZERO)){
                //删除临时用量表的数据
                smSkuChangeDosageService.deleteByContractCode(customerContract.getContractCode());
                //删除临时主材表的数据
                projectChangeMaterialService.delete(customerContract.getContractCode());
                customerContract.setContractStatus(CustomerContractEnum.TRANSFER_COMPLETE);
            }else {
                customerContract.setContractStatus(status);
            }
            //记录操作时间
            this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.CHANGE_AUDIT);
        }else {
            customerContract.setContractStatus(status);
        }
        this.customerContractService.update(customerContract);
        OperateLog operateLog = this.buildOperateLog(customerContract, status);
        this.operateLogService.insert(operateLog);
    }
    /**
     * 构建操作日志:每一个状态对应一个操作类型，
     * 根据不同的状态构建不同的操作类型
     * @param customerContract 项目信息
     * @param status 当前状态
     */
    private OperateLog buildOperateLog(CustomerContract customerContract,CustomerContractEnum status){
        OperateLog operateLog = new OperateLog();
        operateLog.setContractCode(customerContract.getContractCode());
        operateLog.setSystemType("1");
        if(CustomerContractEnum.CHANGE_AUDIT.equals(status)){
            operateLog.setOperateType(OperateLogEnum.SUBMIT_CHANGE_AUDIT);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.CHANGE_AUDIT_SUCCESS.equals(status)){
            operateLog.setOperateType(OperateLogEnum.CHANGE_AUDIT_PASS);
            operateLog.setOperateDescription(status.getLabel());
        }else if(CustomerContractEnum.CHANGE_AUDIT_NOT_PASS.equals(status)){
            operateLog.setOperateType(OperateLogEnum.CHANGE_AUDIT_FAILED);
            operateLog.setOperateDescription(status.getLabel());
        }
        String operator = WebUtils.getLoggedUser().getName()+"("+WebUtils.getLoggedUser().getOrgCode()+")";
        operateLog.setOperator(operator);
        operateLog.setOperateTime(new Date());
        return operateLog;
    }
}