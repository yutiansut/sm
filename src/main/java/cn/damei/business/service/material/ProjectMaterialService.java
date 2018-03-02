package cn.damei.business.service.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.constants.SelectMaterialTypeEnmu;
import cn.damei.business.dao.material.ProjectChangeMaterialDao;
import cn.damei.business.dao.material.ProjectMaterialDao;
import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.ProjectMaterial;
import cn.damei.business.entity.material.SmSkuChangeDosage;
import cn.damei.business.entity.material.SmSkuDosage;
import cn.damei.business.entity.OtherAddReduceAmount;
import cn.mdni.commons.bean.BeanUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudUUIDService;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectMaterialService extends CrudUUIDService<ProjectMaterialDao, ProjectMaterial> {

    @Autowired
    private ProjectMaterialDao projectMaterialDao;
    @Autowired
    private SmSkuDosageService smSkuDosageService;
    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;
    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;
    @Autowired
    private ProjectChangeMaterialDao projectChangeMaterialDao;

    /**
     * 通过合同编码查询  所有 项目主材sku 及 其下面的所有 sku用量信息
     *
     * @param materialParams
     * @return
     */
    public List<ProjectMaterial> findWithSubListByMaterialParams(Map<String, Object> materialParams) {
        return projectMaterialDao.findWithSubListByMaterialParams(materialParams);
    }

    /**
     * 保存定额
     *
     * @param projectMaterial
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(ProjectMaterial projectMaterial) {

        Date date = new Date();
        String createUser = WebUtils.getLoggedUser().getName() + "(" + WebUtils.getLoggedUser().getOrgCode() + ")";
        projectMaterial.setId(UUID.randomUUID().toString());
        projectMaterial.setCreateUser(createUser);
        projectMaterial.setCreateTime(date);
        this.entityDao.insert(projectMaterial);
        SmSkuDosage skuDosage = projectMaterial.getSkuDosageList().get(0);
        skuDosage.setId(UUID.randomUUID().toString());
        skuDosage.setProjectMaterialId(projectMaterial.getId());
        skuDosage.setCreateUser(createUser);
        skuDosage.setCreateTime(date);
        smSkuDosageService.insert(skuDosage);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("projectMaterialId", projectMaterial.getId());
        maps.put("skuDosageId", projectMaterial.getSkuDosageList().get(0).getId());
        return maps;
    }

    /**
     * 查询套餐标配
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getPackagestandardByContractCode(String contractCode) {
        return this.entityDao.getPackagestandardByContractCode(contractCode);
    }


    /**
     * 查询升级项的主材
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getUpMaterialByContractCode(String contractCode) {
        return this.entityDao.getUpMaterialByContractCode(contractCode);
    }

    /**
     * 查询增项
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getAddItemByContractCode(String contractCode, String addItemType) {
        return this.entityDao.getAddItemByContractCode(contractCode, addItemType);
    }


    /**
     * 查询减项
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getReduceitemByContractCode(String contractCode, String reduceItem) {
        return this.entityDao.getReduceitemByContractCode(contractCode, reduceItem);
    }


    /**
     * 查询其它综合费
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getOthercateFeesByContractCode(String contractCode) {
        return this.entityDao.getOthercateFeesByContractCode(contractCode);
    }

    /**
     * 查询老房拆除基装定额
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getDismantlebaseinstallquotaByContractCode(String contractCode, String oldHouseType) {
        return this.entityDao.getDismantlebaseinstallquotaByContractCode(contractCode, oldHouseType);
    }

    /**
     * 删除主表里的数据
     *
     * @param
     */
    public void delete(String contractCode) {
        this.entityDao.delete(contractCode);
    }

    /**
     * 把临时表里的数据插入到主表
     *
     * @param projectChangeMaterialList
     */
    public void insertByprojectChangeMaterial(List<ProjectChangeMaterial> projectChangeMaterialList) {
        this.entityDao.insertByprojectChangeMaterial(projectChangeMaterialList);
    }

    /**
     * 根据项目code和类目url查询
     *
     * @param contractCode
     * @param changeCategoryUrl
     * @return
     */
    public List<ProjectMaterial> getByConCodeAndChUrl(String contractCode, String changeCategoryUrl) {
        return this.entityDao.getByConCodeAndChUrl(contractCode, changeCategoryUrl);
    }

    /**
     * 根据项目code和变更版本号查询
     *
     * @return
     */
    public Map<String, List<Map<String, Object>>> getMaterial(String changeCategoryUrl, String contractCode, String changeNo) {
        List<ProjectChangeMaterial> proMatrlByContrCodeList = new ArrayList<>();
        List<SmSkuChangeDosage> dosageByContrCodeList = new ArrayList<>();
        proMatrlByContrCodeList = mongoCustomerContractService.findAllMaterial(contractCode, changeNo);
        dosageByContrCodeList = mongoCustomerContractService.findAllDosage(contractCode, changeNo);
        if (proMatrlByContrCodeList.size() == 0 || dosageByContrCodeList.size() == 0) {
            proMatrlByContrCodeList = projectChangeMaterialService.getMaterial(contractCode, changeCategoryUrl);
            dosageByContrCodeList = smSkuChangeDosageService.getDosage(contractCode, changeCategoryUrl);
        }
        //把用量根据材料id分组对应
        Map<String, List<SmSkuChangeDosage>> collect = dosageByContrCodeList.stream().collect(Collectors.groupingBy(SmSkuChangeDosage::getProjectMaterialId));
        List<Map<String, Object>> addList = new ArrayList<>();
        List<Map<String, Object>> reduceList = new ArrayList<>();
        List<Map<String, Object>> noChangeList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> returnMap = Maps.newHashMap();
        for (ProjectChangeMaterial projectMaterial : proMatrlByContrCodeList) {
            List<SmSkuChangeDosage> smSkuChangeDosageList = collect.get(projectMaterial.getId());
            if (smSkuChangeDosageList != null) {
                //原用量（变更前用量）
                BigDecimal originalDosage = smSkuChangeDosageList.stream().filter(b -> b.getOriginalDosage() != null).map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //现用量（最终用量）
                BigDecimal lossDosage = smSkuChangeDosageList.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (lossDosage.compareTo(originalDosage) == 1) {//获取变更增加的材料
                    SmSkuChangeDosage smSkuChangeDosage = getSmSkuChangeDosage(smSkuChangeDosageList, originalDosage, lossDosage);
                    addList.add(DosageExcelDeal(projectMaterial, smSkuChangeDosage));
                } else if (lossDosage.compareTo(originalDosage) == -1) {//获取变更减少的材料
                    SmSkuChangeDosage smSkuChangeDosage = getSmSkuChangeDosage(smSkuChangeDosageList, originalDosage, lossDosage);
                    reduceList.add(DosageExcelDeal(projectMaterial, smSkuChangeDosage));
                } else if (lossDosage.compareTo(originalDosage) == 0) {//获取不变更的材料
                    String url = projectMaterial.getProductCategoryUrl();
                    if (url != null) {
                        int i = url.indexOf("-");
                        int i1 = url.indexOf("-", i + 1);
                        url = url.substring(0, i1 + 1);
                        Integer materialIsCheckScale = projectChangeMaterialDao.getMaterialIsCheckScale(url);
                        if (materialIsCheckScale != null && materialIsCheckScale.intValue() == 1) {
                            SmSkuChangeDosage smSkuChangeDosage = getSmSkuChangeDosage(smSkuChangeDosageList, originalDosage, lossDosage);
                            noChangeList.add(DosageExcelDeal(projectMaterial, smSkuChangeDosage));
                        }
                    }

                }
            }
        }
        returnMap.put("add",addList);
        returnMap.put("reduce",reduceList);
        returnMap.put("noChange",noChangeList);
        return returnMap;
    }

    private Map<String, Object> DosageExcelDeal(ProjectChangeMaterial projectMaterial, SmSkuChangeDosage smSkuChangeDosage) {
        String type = "";
        if (StringUtils.isNotBlank(projectMaterial.getCategoryCode())) {
            switch (projectMaterial.getCategoryCode()) {
                case ("PACKAGESTANDARD"):
                    type = "套餐标配";
                    break;
                case ("UPGRADEITEM"):
                    type = "升级项";
                    break;
                case ("ADDITEM"):
                    type = "增项";
                    break;
                case ("REDUCEITEM"):
                    type = "减项";
                    break;
                case ("OTHERMONEYADDORREDUCE"):
                    type = "其它金额增减";
                    break;
                case ("OTHERCOMPREHENSIVEFEE"):
                    type = "其它综合费";
                    break;
                case ("OLDHOUSEDEMOLITION"):
                    type = "旧房拆改";
                    break;
            }
        }
        String materialtype = "";
        if (StringUtils.isNotBlank(projectMaterial.getMaterialsStatus())) {
            switch (projectMaterial.getMaterialsStatus()) {
                case ("NOT_MEASURED"):
                    materialtype = "未测量";
                    break;
                case ("MEASURED"):
                    materialtype = "已测量";
                    break;
                case ("NO_ORDERS"):
                    materialtype = "未下单";
                    break;
                case ("ALREADY_ORDERED"):
                    materialtype = "已下单";
                    break;
                case ("NOT_SHIPPED"):
                    materialtype = "未发货";
                    break;
                case ("SHIPPED"):
                    materialtype = "已发货";
                    break;
                case ("NOT_INSTALLED"):
                    materialtype = "未安装";
                    break;
                case ("INSTALLED"):
                    materialtype = "已安装";
                    break;
                case ("UNFILLED_ORDER_CONDITIONS"):
                    materialtype = "未满足下单条件";
                    break;
            }
        }
        Map<String, Object> map = Maps.newHashMap();

        map.put("category_code", type);
        map.put("catalog_name", projectMaterial.getCataLogName());
        map.put("productCategoryUrl", projectMaterial.getProductCategoryUrl());
        map.put("brand", projectMaterial.getBrand());
        map.put("sku_model", projectMaterial.getSkuModel());
        map.put("attribute1", projectMaterial.getAttribute1());
        map.put("materials_status", materialtype);
        map.put("domain_name", smSkuChangeDosage.getDomainName());


        //如果是平米转片
        BigDecimal multiply = BigDecimal.ZERO;
        BigDecimal newOriginalDosage = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(smSkuChangeDosage.getConvertUnit())) {
            if (StringUtils.isNotBlank(projectMaterial.getSkuSqec())) {//如果规格不为空，则把规格进行处理
                String skuSqec = projectMaterial.getSkuSqec();
                //去掉空格
                skuSqec = skuSqec.replace(" ", "");
                if (skuSqec.indexOf("X") > 0 || skuSqec.indexOf("x") > 0) {
                    skuSqec = skuSqec.replaceAll("X", "*");
                    skuSqec = skuSqec.replaceAll("x", "*");
                }
                String[] split = skuSqec.split("\\*");
                //规格
                BigDecimal sqec = BigDecimal.ONE;
                if (split != null && split.length > 0) {
                    for (String a : split) {
                        sqec = new BigDecimal(a);
                        sqec = sqec.multiply(sqec);
                    }
                    sqec = sqec.divide(new BigDecimal(1000000));
                }
                //含损用量（最终用量）
                BigDecimal lossDosage = smSkuChangeDosage.getLossDosage();
                //变更后的预算用量
                multiply = (lossDosage.multiply(sqec)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //变更前的预算用量
                newOriginalDosage = (smSkuChangeDosage.getOriginalDosage().multiply(sqec)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            String materialUnit = "";
            //如果单位不为空进行处理
            if (StringUtils.isNotBlank(projectMaterial.getMaterialUnit())) {
                materialUnit = projectMaterial.getMaterialUnit();
                String substring = materialUnit.substring(materialUnit.length() - 1, materialUnit.length());//截取单位的最后一个字符
                materialUnit = "㎡/" + substring;
            }
            map.put("material_unit", materialUnit);
            map.put("original_dosage", newOriginalDosage + "/" + smSkuChangeDosage.getOriginalDosage().setScale(2, BigDecimal.ROUND_HALF_UP));
            map.put("dosage", multiply.subtract(newOriginalDosage).abs() + "/" + smSkuChangeDosage.getLossDosage().subtract(smSkuChangeDosage.getOriginalDosage()).abs().setScale(2, BigDecimal.ROUND_HALF_UP));
            map.put("loss_dosage", multiply + "/" + smSkuChangeDosage.getLossDosage().setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            map.put("material_unit", projectMaterial.getMaterialUnit());
            map.put("original_dosage", smSkuChangeDosage.getOriginalDosage().setScale(2, BigDecimal.ROUND_HALF_UP));
            map.put("dosage", smSkuChangeDosage.getLossDosage().subtract(smSkuChangeDosage.getOriginalDosage()).setScale(2, BigDecimal.ROUND_HALF_UP));
            map.put("loss_dosage", smSkuChangeDosage.getLossDosage().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.ADDITEM.toString())) {
            map.put("price", smSkuChangeDosage.getStoreIncreasePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            smSkuChangeDosage.setStoreSalePrice(smSkuChangeDosage.getStoreIncreasePrice());
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
            map.put("price", smSkuChangeDosage.getStoreReducePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            smSkuChangeDosage.setStoreSalePrice(smSkuChangeDosage.getStoreReducePrice());
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.UPGRADEITEM.toString())) {
            map.put("price", smSkuChangeDosage.getStoreUpgradeDifferencePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            smSkuChangeDosage.setStoreSalePrice(smSkuChangeDosage.getStoreUpgradeDifferencePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        map.put("design_remark", projectMaterial.getDesignRemark());
        if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
            map.put("priceTotal", (smSkuChangeDosage.getLossDosage().subtract(smSkuChangeDosage.getOriginalDosage()).multiply(smSkuChangeDosage.getStoreSalePrice())).negate().setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            map.put("priceTotal", (smSkuChangeDosage.getLossDosage().subtract(smSkuChangeDosage.getOriginalDosage()).multiply(smSkuChangeDosage.getStoreSalePrice())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (type.equals("套餐标配")) {
            map.put("priceTotal", BigDecimal.ZERO);
        }

        return map;
    }

    public List<OtherAddReduceAmount> getOthrgAddReducePrice(String contractCode, String changeNo) {
        String changeVersionNo = changeNo.substring(0, changeNo.length() - 3);
        List<OtherAddReduceAmount> otherAddReduceAmountList = mongoCustomerContractService.findOtherAmountByChanVerNo(contractCode, changeVersionNo);
        return otherAddReduceAmountList;
    }


    public static void main(String[] args) {
        System.out.println(BigDecimal.ONE.subtract(BigDecimal.TEN).abs());
    }

    /**
     * 增加或修改
     * 新增时,返回主键id
     */
    @Transactional(rollbackFor = Exception.class)
    public Object saveAndReturnId(ProjectMaterial projectMaterial) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (loggedUser == null) {
            return StatusDto.buildFailureStatusDto("回话失效,请重新登录！");
        }
        String loggerUsername = loggedUser.getName() + "(" + loggedUser.getOrgCode() + ")";
        Date date = new Date();
        String pageType = projectMaterial.getPageType();

        if (StringUtils.isNotBlank(projectMaterial.getId())) {
            //更新操作
            projectMaterial.setUpdateUser(loggerUsername);
            projectMaterial.setUpdateTime(date);
            //选材
            if (Constants.PAGE_TYPE_SELECT.equals(pageType)) {
                update(projectMaterial);
                //变更 貌似不需要
            }
        } else {
            projectMaterial.setCreateUser(loggerUsername);
            projectMaterial.setCreateTime(date);
            //选材
            if (Constants.PAGE_TYPE_SELECT.equals(pageType)) {
                projectMaterial.setId(UUID.randomUUID().toString());
                insert(projectMaterial);
                //变更
            } else if (Constants.PAGE_TYPE_CHANGE.equals(pageType)) {
                Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(projectMaterial, false);
                ProjectChangeMaterial projectMaterial1 = BeanUtils.mapTransBean(stringObjectMap, ProjectChangeMaterial.class);
                projectMaterial1.setId(UUID.randomUUID().toString());
                projectChangeMaterialService.insert(projectMaterial1);
                projectMaterial.setId(projectMaterial1.getId());
            }
            //新增后 将本次新增主键id返回
            return StatusDto.buildDataSuccessStatusDto(String.valueOf(projectMaterial.getId()));
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    public List<OtherAddReduceAmount> getOthrgPriceByChanVerNo(String contractCode, String changeNo) {
        String changeVersionNo = changeNo.substring(0, changeNo.length() - 3);
        List<OtherAddReduceAmount> otherAddReduceAmountList = mongoCustomerContractService.findOtherAmountByChanVerNo(contractCode, changeVersionNo);
        return otherAddReduceAmountList;
    }

//    public List<Map<String, Object>> getMaterialCustomization(String contractCode, String changeNo) {
//        List<ProjectChangeMaterial> proMatrlByContrCodeList = mongoCustomerContractService.getMaterialCustomization(contractCode, changeNo);
//        List<SmSkuChangeDosage> dosageByContrCodeList = mongoCustomerContractService.getDosageCustomization(contractCode, changeNo);
//
//        Map<String, List<SmSkuChangeDosage>> collect = dosageByContrCodeList.stream().collect(Collectors.groupingBy(SmSkuChangeDosage::getProjectMaterialId));
//        Map<String, Object> map = Maps.newHashMap();
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (ProjectChangeMaterial projectMaterial : proMatrlByContrCodeList) {
//            List<SmSkuChangeDosage> smSkuChangeDosageList = collect.get(projectMaterial.getId());
//            BigDecimal originalDosage = smSkuChangeDosageList.stream().map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
//            BigDecimal lossDosage = smSkuChangeDosageList.stream().map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
//            if (lossDosage.compareTo(originalDosage) == 0) {
//                SmSkuChangeDosage smSkuChangeDosage = getSmSkuChangeDosage(smSkuChangeDosageList, originalDosage, lossDosage);
//                DosageExcelDeal(map, list, projectMaterial, originalDosage, smSkuChangeDosage);
//            }
//
//        }
//        return list;
//    }

    private SmSkuChangeDosage getSmSkuChangeDosage(List<SmSkuChangeDosage> smSkuChangeDosageList, BigDecimal originalDosage, BigDecimal lossDosage) {
        StringBuilder stringBuilder = new StringBuilder();
        smSkuChangeDosageList.stream().map(a -> a.getDomainName()).distinct().forEach(b -> stringBuilder.append(b).append(","));
        SmSkuChangeDosage smSkuChangeDosage = smSkuChangeDosageList.get(0);
        smSkuChangeDosage.setOriginalDosage(originalDosage);
        smSkuChangeDosage.setDomainName(stringBuilder.toString());
        smSkuChangeDosage.setLossDosage(lossDosage);
        return smSkuChangeDosage;
    }

    public List<ProjectMaterial> findByConAndUrl(String contractCode, String changeCategoryUrl) {
        return this.entityDao.findByConAndUrl(contractCode, changeCategoryUrl);
    }

}
