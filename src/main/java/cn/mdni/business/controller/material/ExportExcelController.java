package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.business.constants.RoleNameFromCenter;
import cn.mdni.business.constants.SelectMaterialTypeEnmu;
import cn.mdni.business.dao.material.ProjectChangeMaterialDao;
import cn.mdni.business.dao.material.ProportionMoneyDao;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.material.ProportionMoney;
import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.business.service.material.*;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.orderflow.projectsign.ProjectSignService;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.core.WebUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 刘铎 on 2017/11/4.
 */
@RestController
@RequestMapping("/material/exportexcel")
public class ExportExcelController {


    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProjectMaterialService projectMaterialService;
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;
    @Autowired
    private OtherAddRecuceAmountService otherAddRecuceAmountService;
    @Autowired
    private ProjectIntemService projectIntemService;
    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private ProportionMoneyDao proportionMoneyDao;
    @Autowired
    private SmSkuDosageService smSkuDosageService;
    @Autowired
    private ProjectChangeMaterialDao projectChangeMaterialDao;
    @Autowired
    private SmMaterialChangeAuditRecordService smMaterialChangeAuditRecordService;
    @Autowired
    private ProjectSignService projectSignService;


    @RequestMapping("/metarialexport")
    public void metarialexport(HttpServletResponse resp, HttpServletRequest res, @RequestParam() String contractCode, @RequestParam() String type) {

        //1.创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("北京美得你装饰设计有限公司");
        HSSFFont font = workbook.createFont();
        HSSFFont font2 = workbook.createFont();
        font.setFontName("黑体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 14);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor((short) 15);
        cellStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);//设置背景色

        cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle3.setFont(font2);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setWrapText(true);
        cellStyle4.setWrapText(true);
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setFont(font);
        sheet.addMergedRegion(new Region(0, (short) 0, 2, (short) 14));
        String imgerFullFile = res.getSession().getServletContext().getRealPath("/") + "/static/business/template/mdniLog.png";
        sheet.createRow(0).setHeightInPoints(80);//设置行高
        //插入图片
        try {
            InputStream is = new FileInputStream(imgerFullFile);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            // 图片插入坐标(行,列)
            anchor.setCol1(4);
            anchor.setRow1(0);
            // 插入图片
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(6, 1);//控制图片的大小
        } catch (Exception e) {
            e.printStackTrace();
        }

        getHeader(sheet, 2, cellStyle3, "北京美得你装饰设计有限公司");
        getHeader(sheet, 2, cellStyle3, "北京美得你装饰设计有限公司");
        //查询客户合同
        CustomerContract customerContract = getCustomerContract(contractCode);
        if (null != customerContract && null != customerContract.getContractCode()) {
            getHeader(sheet, 3, cellStyle3, customerContract.getContractCode().toString());
        } else {
            getHeader(sheet, 3, "");
        }
        getHeader(sheet, 4, cellStyle, "选材预算单信息");
        if (StringUtils.isNotBlank(customerContract.getSecondContact())) {
            getRowCellData(sheet, customerContract, 5, "第二联系人：", "主案设计师姓名：", "工程地址：");
            getRowCellData(sheet, customerContract, 6, "第二联系人电话：", "主案设计师联系方式：", "房屋户型：");
            getRowCellData(sheet, customerContract, 11, "计划开工时间：", "客户姓名：", "客户联系方式：");
        } else {
            getRowCellData(sheet, customerContract, 5, "客户姓名：", "主案设计师姓名：", "工程地址：");
            getRowCellData(sheet, customerContract, 6, "客户联系方式：", "主案设计师联系方式：", "房屋户型：");
            getRowCellData(sheet, customerContract, 11, "计划开工时间：", "第二联系人：", "第二联系人电话：");
        }
        getRowCellData(sheet, customerContract, 7, "监理姓名：", "产品设计师：", "所选套餐：");
        getRowCellData(sheet, customerContract, 8, "监理联系方式：", "产品设计师联系方式：", "房屋状况：");
        getRowCellData(sheet, customerContract, 9, "深化设计师姓名：", "建筑面积：", "有无电梯：");
        getRowCellData(sheet, customerContract, 10, "深化设计师联系方式：", "计价面积：", "房屋类型：");
        getRowCellData(sheet, customerContract, 12, "计划完成时间：", "", "");
        getRowCellData(sheet, customerContract, 13, "设计备注：", "", "");
        getRowCellData(sheet, customerContract, 14, "审计备注：", "", "");
        getHeader(sheet, 15, cellStyle, "套餐标配");
        getHeader(sheet, 16, "基础装修（水电基材及人工）");
        getHeader(sheet, 17, customerContract.getOtherInstallInfo());
        getHeader(sheet, 18, "主材");

        int rownum = 19;
        List<String> cellNameList = Arrays.asList("类型", "商品类目", "商品名称", "品牌", "型号", "属性", "单位", "规格", "位置", "预算用量", "含损耗用量", "实际用量");
        rownum = getCell(sheet, rownum, cellStyle2, cellNameList);
        //查询套餐标配的主材
        List<Map<String, Object>> projectMaterialList = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            projectMaterialList = projectMaterialService.getPackagestandardByContractCode(contractCode);
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                projectMaterialList = projectMaterialService.getPackagestandardByContractCode(contractCode);
            } else {
                projectMaterialList = projectChangeMaterialService.getPackagestandardByContractCode(contractCode);
            }
        }

        BigDecimal multiply = BigDecimal.ZERO;
        BigDecimal bugetDosage = BigDecimal.ZERO;
        getConvertUnit(projectMaterialList);

        List<String> modelFiles = Arrays.asList("category_code", "cataLogName", "product_name", "brand", "sku_model", "attribute1", "material_unit", "sku_sqec", "domain_name", "budget_dosage", "loss_dosage", "");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, projectMaterialList, modelFiles); //list<Object> List<Map> List<String[]>
        rownum = getHeader(sheet, rownum, "套餐标配价");
        rownum = getTotal(sheet, rownum, customerContract.getMealPrice() + " * " + customerContract.getValuateArea() + "=" + (customerContract.getMealPrice().multiply(customerContract.getValuateArea())).setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "升级项");
        rownum = getHeader(sheet, rownum, "主材");
        List<String> upgradeMaterialList = Arrays.asList("类型", "商品类目", "商品名称", "品牌", "型号", "属性", "单位", "升级差价", "规格", "位置", "预算用量", "含损耗用量", "实际用量", "升级合价");
        rownum = getCell(sheet, rownum, cellStyle2, upgradeMaterialList);
        //查询升级项的主材
        List<Map<String, Object>> upgradeMaterials = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            upgradeMaterials = projectMaterialService.getUpMaterialByContractCode(contractCode);
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                upgradeMaterials = projectMaterialService.getUpMaterialByContractCode(contractCode);
            } else {
                upgradeMaterials = projectChangeMaterialService.getUpMaterialByContractCode(contractCode);
            }
        }

        for (Map<String, Object> obj : upgradeMaterials) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            BigDecimal storeUpgradeDifferencePrice = (BigDecimal) obj.get("store_upgrade_difference_price");
            obj.put("priceTotal", priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("store_upgrade_difference_price", storeUpgradeDifferencePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        getConvertUnit(upgradeMaterials);
        BigDecimal upTtotal = new BigDecimal(0);
        for (Map<String, Object> obj : upgradeMaterials) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            upTtotal = upTtotal.add(priceTotal);
        }
        List<String> upgradeMaterialCellList = Arrays.asList("category_code", "cataLogName", "product_name", "brand", "sku_model", "attribute2", "material_unit", "store_upgrade_difference_price", "sku_sqec", "domain_name", "budget_dosage", "loss_dosage", "", "priceTotal");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, upgradeMaterials, upgradeMaterialCellList);
        rownum = getHeader(sheet, rownum, "升级差价合计");
        rownum = getTotal(sheet, rownum, "合计：+" + upTtotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "增项");
        rownum = getHeader(sheet, rownum, "主材");
        List<String> addItemMaterialList = Arrays.asList("类型", "商品类目", "商品名称", "品牌", "型号", "属性", "单位", "增项单价", "规格", "位置", "预算用量", "含损耗用量", "实际用量", "增项合价");
        rownum = getCell(sheet, rownum, cellStyle2, addItemMaterialList);
        //查询增项的主材
        List<Map<String, Object>> aadItemMaterials = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            aadItemMaterials = projectMaterialService.getAddItemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                aadItemMaterials = projectMaterialService.getAddItemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
            } else {
                aadItemMaterials = projectChangeMaterialService.getAddItemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
            }
        }
        for (Map<String, Object> obj : aadItemMaterials) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            BigDecimal storeIncreasePrice = (BigDecimal) obj.get("store_increase_price");
            obj.put("priceTotal", priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("store_increase_price", storeIncreasePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        getConvertUnit(aadItemMaterials);
        BigDecimal addMaterPriceTotal = new BigDecimal(0);//增项主材的合计
        for (Map<String, Object> obj : aadItemMaterials) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            addMaterPriceTotal = priceTotal.add(addMaterPriceTotal);
        }
        List<String> addItemMaterialCellList = Arrays.asList("category_detail_code", "cataLogName", "product_name", "brand", "sku_model", "attribute1", "material_unit", "store_increase_price", "sku_sqec", "domain_name", "budget_dosage", "loss_dosage", "", "priceTotal");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, aadItemMaterials, addItemMaterialCellList);
        rownum = getHeader(sheet, rownum, "基装定额");
        List<String> addItemBaseinstallquotaList = Arrays.asList("定额分类", "定额名称", "单位", "数量", "计价方式", "单价或占比", "合价", "设计备注", "工艺、做法及说明");
        rownum = getCell(sheet, rownum, cellStyle2, addItemBaseinstallquotaList);
        //查询增项的基装定额
        List<Map<String, Object>> addItemBaseinstallquota = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            addItemBaseinstallquota = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                addItemBaseinstallquota = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            } else {
                addItemBaseinstallquota = projectChangeMaterialService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            }
        }

        BigDecimal addBaseTotal = new BigDecimal(0);//增项基装定额的合计
        for (Map<String, Object> obj : addItemBaseinstallquota) {
            //合价
            BigDecimal price = (BigDecimal) obj.get("totalPrice");
            //数量
            BigDecimal lossDosage = (BigDecimal) obj.get("loss_dosage");
            //单价
            BigDecimal storeSalePrice = (BigDecimal) obj.get("store_sale_price");
            price = lossDosage.multiply(storeSalePrice);
            addBaseTotal = addBaseTotal.add(price);
            obj.put("totalPrice", price.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("store_sale_price", storeSalePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("loss_dosage", lossDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> addItemBaseinstallquotaCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "store_sale_price", "totalPrice", "design_remark", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, addItemBaseinstallquota, addItemBaseinstallquotaCellList);
        rownum = getHeader(sheet, rownum, "基装增项综合费");
        rownum = getCell(sheet, rownum, cellStyle2, addItemBaseinstallquotaList);
        ProportionMoney byContractCode = new ProportionMoney();
        //调用计算金额的方法
        if (type.equals("1")) {
            byContractCode = proportionMoneyDao.getByContractCode(contractCode);
        } else {
            Map<String, Object> dosageByContractCodeList = smSkuDosageService.findDosageByContractCodeList(contractCode);
            byContractCode.setBaseloadrating1((BigDecimal) dosageByContractCodeList.get("baseloadrating1"));
            byContractCode.setBaseloadrating3((BigDecimal) dosageByContractCodeList.get("baseloadrating3"));
            byContractCode.setComprehensivefee4((BigDecimal) dosageByContractCodeList.get("comprehensivefee4"));
            byContractCode.setRenovationAmount((BigDecimal) dosageByContractCodeList.get("renovationAmount"));
        }
        //获得增项的基装定额的价格
        BigDecimal baseloadrating1 = byContractCode.getBaseloadrating1();
        //查询增项的基装增项综合费
        List<Map<String, Object>> baseinstallComprehenSivefee = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            baseinstallComprehenSivefee = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                baseinstallComprehenSivefee = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
            } else {
                baseinstallComprehenSivefee = projectChangeMaterialService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.ADDITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLCOMPREHENSIVEFEE.toString());
            }
        }
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal loss_dosage = new BigDecimal(0);
        BigDecimal store_sale_price = new BigDecimal(0);
        BigDecimal baseComSiveFeeTotal = new BigDecimal(0);
        BigDecimal projectProportion = new BigDecimal(0);
        for (Map<String, Object> obj : baseinstallComprehenSivefee) {
            //获得计价方式是固定单价还是占比
            Object dosage_pricing_mode = obj.get("dosage_pricing_mode");
            //获得计算的总价
            totalPrice = (BigDecimal) obj.get("totalPrice");
            //获得用量
            loss_dosage = (BigDecimal) obj.get("loss_dosage");
            //获得单价
            store_sale_price = (BigDecimal) obj.get("store_sale_price");
            //获得占比
            projectProportion = (BigDecimal) obj.get("project_proportion");
            if ("固定单价".equals(dosage_pricing_mode)) {
                totalPrice = loss_dosage.multiply(store_sale_price);
                baseComSiveFeeTotal = baseComSiveFeeTotal.add(totalPrice);
                obj.put("totalPrice", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if ("基装增项总价占比".equals(dosage_pricing_mode)) {
                totalPrice = (projectProportion.multiply(baseloadrating1)).divide(new BigDecimal(100));
                baseComSiveFeeTotal = baseComSiveFeeTotal.add(totalPrice);
                obj.put("totalPrice", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            obj.put("project_proportion", projectProportion.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("loss_dosage", loss_dosage.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> baseinstallComprehenSivefeeCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "project_proportion", "totalPrice", "design_remark", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, baseinstallComprehenSivefee, baseinstallComprehenSivefeeCellList);
        rownum = getHeader(sheet, rownum, "增项单价合计");
        rownum = getTotal(sheet, rownum, "主材：+" + addMaterPriceTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "基装定额：+" + addBaseTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "基装增项综合费：+" + baseComSiveFeeTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "合计：+" + (addMaterPriceTotal.add(addBaseTotal).add(baseComSiveFeeTotal)).setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "减项");
        rownum = getHeader(sheet, rownum, "主材");
        List<String> subtractItemMaterialList = Arrays.asList("类型", "商品类目", "商品名称", "品牌", "型号", "属性", "单位", "减项单价", "规格", "位置", "预算用量", "含损耗用量", "实际用量", "减项合价");
        rownum = getCell(sheet, rownum, cellStyle2, subtractItemMaterialList);
        //查询减项的主材
        List<Map<String, Object>> reduceitemMaterial = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            reduceitemMaterial = projectMaterialService.getReduceitemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                reduceitemMaterial = projectMaterialService.getReduceitemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
            } else {
                reduceitemMaterial = projectChangeMaterialService.getReduceitemByContractCode(contractCode, SelectMaterialTypeEnmu.MAINMATERIAL.toString());
            }
        }
        getConvertUnit(reduceitemMaterial);
        BigDecimal reduceMaterTotal = new BigDecimal(0);//减项主材合计
        for (Map<String, Object> obj : reduceitemMaterial) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            BigDecimal store_reduce_price = (BigDecimal) obj.get("store_reduce_price");
            obj.put("priceTotal", priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("store_reduce_price", store_reduce_price.setScale(2, BigDecimal.ROUND_HALF_UP));
            reduceMaterTotal = priceTotal.add(reduceMaterTotal);
        }
        List<String> reduceitemMaterialCellList = Arrays.asList("category_detail_code", "cataLogName", "product_name", "brand", "sku_model", "attribute1", "material_unit", "store_reduce_price", "sku_sqec", "domain_name", "budget_dosage", "loss_dosage", "", "priceTotal");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, reduceitemMaterial, reduceitemMaterialCellList);
        rownum = getHeader(sheet, rownum, "基装定额");
        List<String> reduceitemBaseinstallquotaList = Arrays.asList("定额分类", "定额名称", "单位", "数量", "计价方式", "单价或占比", "合价", "设计备注", "工艺、做法及说明");
        rownum = getCell(sheet, rownum, cellStyle2, reduceitemBaseinstallquotaList);//填充套餐标配的列名
        //查询减项的基装定额
        List<Map<String, Object>> reduceItemBaseinstallquota = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            reduceItemBaseinstallquota = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.REDUCEITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                reduceItemBaseinstallquota = projectIntemService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.REDUCEITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            } else {
                reduceItemBaseinstallquota = projectChangeMaterialService.getProIntemByContractCode(contractCode, SelectMaterialTypeEnmu.REDUCEITEM.toString(), SelectMaterialTypeEnmu.BASEINSTALLQUOTA.toString());
            }
        }

        BigDecimal reduceBaseTotal = new BigDecimal(0);//减项的基装定额
        for (Map<String, Object> obj : reduceItemBaseinstallquota) {
            BigDecimal totalrice = (BigDecimal) obj.get("totalPrice");
            BigDecimal storeSalePrice = (BigDecimal) obj.get("store_sale_price");
            BigDecimal lossDosage = (BigDecimal) obj.get("loss_dosage");
            reduceBaseTotal = totalrice.add(reduceBaseTotal);
            obj.put("store_sale_price", storeSalePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("loss_dosage", lossDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("totalPrice", totalrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> reduceItemBaseinstallquotaCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "store_sale_price", "totalPrice", "design_remark", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, reduceItemBaseinstallquota, reduceItemBaseinstallquotaCellList);
        rownum = getHeader(sheet, rownum, "减项单价合计");
        rownum = getTotal(sheet, rownum, "主材：-" + reduceMaterTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "基装定额：-" + reduceBaseTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "合计：-" + (reduceMaterTotal.add(reduceBaseTotal)).setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "活动、优惠及其它金额增减");
        List<String> othermoneyaddorreduceList = Arrays.asList("名称", "类型", "是否税后减项", "额度", "批准人", "介绍");
        rownum = getCell(sheet, rownum, cellStyle2, othermoneyaddorreduceList);
        //查询其他金额增减
        List<Map<String, Object>> othermoneyAddOrReduce = otherAddRecuceAmountService.getOthermoneyAddOrReduceByContractCode(contractCode);
        BigDecimal addReduceTotal = new BigDecimal(0);//其他金额增减的合计
        BigDecimal addTotal = new BigDecimal(0);
        BigDecimal reduceTotal = new BigDecimal(0);
        for (Map<String, Object> obj : othermoneyAddOrReduce) {
            //获得增减类型
            String addReduceType = (String) obj.get("add_reduce_type");
            //获得额度
            BigDecimal quota = (BigDecimal) obj.get("quota");

            if ("增加".equals(addReduceType)) {
                addTotal = quota.add(addTotal);
            } else {
                reduceTotal = quota.add(reduceTotal);
            }
        }
        List<String> othermoneyAddOrReduceCellList = Arrays.asList("item_name", "add_reduce_type", "is_taxed_amount", "quota", "approver", "add_reduce_reason");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, othermoneyAddOrReduce, othermoneyAddOrReduceCellList);
        rownum = getHeader(sheet, rownum, "合计");
        rownum = getTotal(sheet, rownum, "增：+" + addTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getTotal(sheet, rownum, "减：-" + reduceTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, "合计：" + addTotal.subtract(reduceTotal).setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "其它综合费");
        List<String> othercategoriesofsmallfeesList = Arrays.asList("定额分类", "定额名称", "单位", "数量", "计价方式", "单价或占比", "合价", "设计备注", "工艺、做法及说明");
        rownum = getCell(sheet, rownum, cellStyle2, othercategoriesofsmallfeesList);//填充套餐标配的列名
        //查询其他综合费
        List<Map<String, Object>> othercategoriesofsmallfees = new ArrayList<>();
        if (StringUtils.isBlank(customerContract.getCurrentChangeVersion())) {
            othercategoriesofsmallfees = projectMaterialService.getOthercateFeesByContractCode(contractCode);
        } else {
            if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                othercategoriesofsmallfees = projectMaterialService.getOthercateFeesByContractCode(contractCode);
            } else {
                othercategoriesofsmallfees = projectChangeMaterialService.getOthercateFeesByContractCode(contractCode);
            }
        }


        //获得其他综合费的价格
        BigDecimal renovationAmount = byContractCode.getRenovationAmount();//装修工程占比
        BigDecimal otherFeeTotal = new BigDecimal(0);
        BigDecimal otherFeeprojectProportion = new BigDecimal(0);
        BigDecimal baseinstallquotaTotal = new BigDecimal(0);
        for (Map<String, Object> obj : othercategoriesofsmallfees) {
            //获得计价方式是固定单价还是占比
            Object dosage_pricing_mode = obj.get("dosage_pricing_mode");
            //获得计算的总价
            baseinstallquotaTotal = (BigDecimal) obj.get("baseinstallquotaTotal");
            //获得用量
            loss_dosage = (BigDecimal) obj.get("loss_dosage");
            //获得单价
            store_sale_price = (BigDecimal) obj.get("store_sale_price");
            //获得占比
            otherFeeprojectProportion = (BigDecimal) obj.get("project_proportion");
            if ("固定单价".equals(dosage_pricing_mode)) {
                baseinstallquotaTotal = loss_dosage.multiply(store_sale_price);
                otherFeeTotal = otherFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if ("基装增项总价占比".equals(dosage_pricing_mode)) {
                baseinstallquotaTotal = (otherFeeprojectProportion.multiply(baseloadrating1)).divide(new BigDecimal(100));
                otherFeeTotal = otherFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if ("装修工程总价占比".equals(dosage_pricing_mode)) {
                baseinstallquotaTotal = (otherFeeprojectProportion.multiply(renovationAmount)).divide(new BigDecimal(100));
                otherFeeTotal = otherFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            obj.put("project_proportion", otherFeeprojectProportion.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("loss_dosage", loss_dosage.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        List<String> othercategoriesofsmallfeesCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "project_proportion", "baseinstallquotaTotal", "design_remark", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, othercategoriesofsmallfees, othercategoriesofsmallfeesCellList);
        rownum = getTotal(sheet, rownum, "合计");
        rownum = getHeader(sheet, rownum, "合计：" + otherFeeTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "预算合计");
        rownum = getTotal(sheet, rownum, "合计：￥" + ((customerContract.getMealPrice().multiply(customerContract.getValuateArea()))
                .add(upTtotal).add(addMaterPriceTotal.add(addBaseTotal).add(baseComSiveFeeTotal))
                .subtract((reduceMaterTotal.add(reduceBaseTotal)))
                .add((addTotal.subtract(reduceTotal))).add(otherFeeTotal)
                .setScale(2, BigDecimal.ROUND_HALF_UP) + "元"));
        rownum = getTotal(sheet, rownum, "");
        rownum = getHeader(sheet, rownum, "备注");
        rownum = getHeader(sheet, rownum, "1、本材料单所标注材料如遇市场供应短缺,经与甲方协商后可使用市场同等产品材料或由甲方自行解决.");
        rownum = getHeader(sheet, rownum, "2、地砖、墙砖、木地板在计算时需加正常损耗系数。");
        rownum = getHeader(sheet, rownum, "3、石膏板、木工板把面积折算成张（2.8㎡/张）填入实发数量栏，不足一张算一张。");
        rownum = getHeader(sheet, rownum, "4、石膏线条2.5m/根折算成根数填入实发数量栏。");
        rownum = getHeader(sheet, rownum, "温馨提示：");
        rownum = getHeader(sheet, rownum, "1、如果您先做预算（指尚未出施工图纸），此报价仅供您参考或作为您与公司签定施工合同的参考依据，我公司保留依施工图纸所用材料和加工件复杂系数、修订项目单价的权力。");
        rownum = getHeader(sheet, rownum, "2、对于材料需人工搬运上楼的，我们的所有基材、主材、家具等运费只负责至1楼，每加1层搬运费增加1000元，依次类推.货汽车无法到达业主电梯口20米的，由此造成2次搬运费须由客户承担，");
        rownum = getHeader(sheet, rownum, "     垃圾清运费仅仅是由客户家至当地物业指定的垃圾摆放点，45米之内免费清运，超过45米，增收500元/户。垃圾外运15元/㎡。");
        rownum = getHeader(sheet, rownum, "3、由于开工前无法确定，墙面的实际平整度及墙面原有找平层是否老化，所以如果现场墙、顶面需要石膏找平，费用另计。");
        rownum = getHeader(sheet, rownum, "4、乙方材料进场甲方须在两天内进行验收，逾期没验收视为甲方认可，乙方可直接使用。");
        rownum = getHeader(sheet, rownum, "5、任何承诺必须以书面文字为准，口头承诺无效。");
        rownum = getHeader(sheet, rownum, "6、本报价所规定材料的品牌、规格、型号如遇市场断货、缺货，可选用同档次其它品牌、规格、型号。如甲方所挑选的主材价格与本报价所规定的价格不同，则需多退少补。");
        rownum = getHeader(sheet, rownum, "7、本报价中厨房和卫生间墙、地砖均未包含腰线、花片，如甲方需要配装腰线、花片，主材费用另计。");
        rownum = getHeader(sheet, rownum, "8、施工项目依照本预算所立项目为依据，预算外项目应按实际发生计算，多退少补。");
        rownum = getHeader(sheet, rownum, "9、本报价以外（或超出规定范围）的项目属于增项，按照本公司报价标准，须另行收费。");
        rownum = getHeader(sheet, rownum, "10、此报价未含物业管理处各项收费及其他任何办证费用（物业公司要求的装修押金、物业公司要求乙方装修相关费用、管理费、垃圾费 、工人出入证办证费用等均由甲方承担并交纳）。");
        rownum = getHeader(sheet, rownum, "11、乙方承担因施工违反物业规定而被扣费用。");
        rownum = getHeader(sheet, rownum, "12、本预(结)算定额的综合单价=（主材单价+辅料单价）*损耗+人工单价。");
        rownum = getHeader(sheet, rownum, "13、工地所在小区内需要指定特殊基材品牌的，根据实际情况，费用另计。");
        rownum = getHeader(sheet, rownum, "");
        getHeader(sheet, rownum, "业主签字：                           设计师签字                           设计部负责人：                           审核员签字：                           设计总监签字：                                   年     月     日");
        try {
            ServletOutputStream out = null;
            resp.setContentType("application/x-msdownload");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(customerContract.getCustomerName() + customerContract.getContractCode() + "选材单.xls", "UTF-8") + "\"");
            out = resp.getOutputStream();
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getConvertUnit(List<Map<String, Object>> projectMaterialList) {
        BigDecimal bugetDosage;
        BigDecimal multiply;
        for (Map<String, Object> obj : projectMaterialList) {
            //对平米转片进行处理
            String convertUnit = (String) obj.get("convert_unit");
            String materialUnit = (String) obj.get("material_unit");
            if (convertUnit != null && convertUnit.equals(Constants.SQUARE_METER_TURN)) {
                String substring = materialUnit.substring(materialUnit.length() - 1, materialUnit.length());//截取单位的最后一个字符
                materialUnit = "㎡/" + substring;
                obj.put("material_unit", materialUnit);
                //规格
                String skuSqec = (String) obj.get("sku_sqec");
                if (StringUtils.isNotBlank(skuSqec)) {//如果规格不为空，则把规格进行处理
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
                    BigDecimal budgetDosage = (BigDecimal) obj.get("budget_dosage");
                    bugetDosage = (budgetDosage.multiply(BigDecimal.ONE)).setScale(0, BigDecimal.ROUND_UP);
                    //含损用量（最终用量）
                    BigDecimal lossDosage = (BigDecimal) obj.get("loss_dosage");
                    //变更后的预算用量
                    multiply = (lossDosage.multiply(sqec)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    obj.put("loss_dosage", lossDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
                    obj.put("budget_dosage", budgetDosage.setScale(2, BigDecimal.ROUND_HALF_UP) + "/" + bugetDosage);
                }
            } else {
                BigDecimal lossDosage = (BigDecimal) obj.get("loss_dosage");
                BigDecimal budgetDosage = (BigDecimal) obj.get("budget_dosage");
                obj.put("loss_dosage", lossDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
                obj.put("budget_dosage", budgetDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
            }

        }
    }

    /**
     * 变更单下载
     */
    @RequestMapping("/changeorderexport")
    public void change(HttpServletResponse resp,
                       @RequestParam() String contractCode,
                       @RequestParam() String currentStatus,
                       @RequestParam() String changeCategoryUrl,
                       @RequestParam() Boolean downLoadFlag,
                       @RequestParam() String changeNo) {
        //1.创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFFont font = workbook.createFont();
        HSSFFont font2 = workbook.createFont();
        font.setFontName("黑体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 14);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor((short) 15);
        cellStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);//设置背景色

        cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle3.setFont(font2);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setWrapText(true);
        cellStyle4.setWrapText(true);
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setFont(font);
        int rownum = 0;
        //查询客户合同.
        CustomerContract customerContract = getCustomerContract(contractCode);
        Date changeTime = smMaterialChangeAuditRecordService.getByChangeNo(changeNo);
        HSSFSheet sheet = workbook.createSheet("美得你工程变更联系单（主材）");
        rownum = getHeader(sheet, rownum, cellStyle3, "美得你工程变更联系单（主材）");
        String aaa = "";
        if (changeTime != null) {
            aaa = "变更单编号：" + changeNo + "  " + "合同编号：" + customerContract.getContractCode() + "  " + "变更完成日期：" + DateUtils.parseStrYMDHMS(changeTime);
        } else {
            aaa = "变更单编号：" + changeNo + "  " + "合同编号：" + customerContract.getContractCode() + "  " + "变更完成日期：" + "";
        }
        rownum = getHeader(sheet, rownum, cellStyle3, aaa);
        rownum = getHeader(sheet, rownum, cellStyle, "基本信息");
        getChageRowCellData(sheet, rownum, customerContract, "第二联系人：", "建筑面积：", "设计师姓名：", "客户姓名：", "工程地址：");
        rownum = rownum + 1;
        getChageRowCellData(sheet, rownum, customerContract, "第二联系人电话：", "计价面积：", "设计师电话：", "客户联系电话：", "户型：");
        rownum = rownum + 1;
        rownum = getHeader(sheet, rownum, cellStyle, "主材增加项目");
        List<String> metareilAddProject = Arrays.asList("类型", "商品类目", "品牌", "型号", "属性", "位置", "单位", "变更前用量", "用量增加", "最终用量", "单价/差价", "设计备注", "总价");
        rownum = getCell(sheet, rownum, cellStyle2, metareilAddProject);
        //查询主材增加
        List<Map<String, Object>> materialAdd = new ArrayList<>();
        Map<String, List<Map<String, Object>>> material = projectMaterialService.getMaterial(changeCategoryUrl, contractCode, changeNo);
        materialAdd = material.get("add");
        BigDecimal materialAddPriceTotal = new BigDecimal(0);//主材增加项目合计
        for (Map<String, Object> obj : materialAdd) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            obj.put("priceTotal", priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            materialAddPriceTotal = materialAddPriceTotal.add(priceTotal);
        }
        List<String> materialAddCellList = Arrays.asList("category_code", "catalog_name", "brand", "sku_model", "attribute1", "domain_name", "material_unit", "original_dosage", "dosage", "loss_dosage", "price", "design_remark", "priceTotal");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, materialAdd, materialAddCellList);
        rownum = getHeader(sheet, rownum, "合计：" + materialAddPriceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        rownum = getHeader(sheet, rownum, cellStyle, "主材减少项目");
        List<String> metareilReduceProject = Arrays.asList("类型", "商品类目", "品牌", "型号", "属性", "材料状态", "位置", "单位", "变更前用量", "用量减少", "最终用量", "单价/差价", "设计备注", "总价");
        rownum = getCell(sheet, rownum, cellStyle2, metareilReduceProject);//填充套餐标配的列名
        //查询主材
        List<Map<String, Object>> metarialReduce = new ArrayList<>();
        metarialReduce = material.get("reduce");
        BigDecimal materialReducePriceTotal = new BigDecimal(0);//主材减少项目合计
        for (Map<String, Object> obj : metarialReduce) {
            BigDecimal priceTotal = (BigDecimal) obj.get("priceTotal");
            obj.put("priceTotal", priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            materialReducePriceTotal = materialReducePriceTotal.add(priceTotal);
        }
        List<String> collect2 = metarialReduce.stream().filter(a -> a.get("domain_name") != null).map(b -> b.get("domain_name").toString()).collect(Collectors.toList());
        String domainName2 = StringUtils.join(collect2, ",");
        List<String> metarialReduceCellList = Arrays.asList("category_code", "catalog_name", "brand", "sku_model", "attribute1", "materials_status", "domain_name", "material_unit", "original_dosage", "dosage", "loss_dosage", "price", "design_remark", "priceTotal");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, metarialReduce, metarialReduceCellList);
        rownum = getHeader(sheet, rownum, "合计：" + materialReducePriceTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        List<Map<String, Object>> noChange = material.get("noChange");
        if (noChange != null && noChange.size() > 0) {
            List<String> collect3 = noChange.stream().filter(a -> a.get("domain_name") != null).map(b -> b.get("domain_name").toString()).collect(Collectors.toList());
            String domainName3 = StringUtils.join(collect3, ",");
            rownum = getHeader(sheet, rownum, cellStyle, "未发生变更商品");
            List<String> noChangeProject = Arrays.asList("类型", "商品类目", "品牌", "型号", "属性", "材料状态", "位置", "用量", "设计备注");
            rownum = getCell(sheet, rownum, cellStyle2, noChangeProject);//填充套餐标配的列名
            List<String> metarialCustomizationCellList = Arrays.asList("category_code", "catalog_name", "brand", "sku_model", "attribute1", "materials_status", "domain_name", "loss_dosage", "design_remark");
            rownum = fillExcelCell(sheet, rownum, cellStyle4, noChange, metarialCustomizationCellList);
        }

        List<String> roleNameList = WebUtils.getSSOShiroUser().getRoleNameList();
        String roleName = roleNameList.toString();//所有角色
        if (!changeCategoryUrl.equals("other")) {
            rownum = getHeader(sheet, rownum, cellStyle, "主材变更单合计");
            BigDecimal metarialPriceTotal = BigDecimal.ZERO;
            metarialPriceTotal = materialAddPriceTotal.add(materialReducePriceTotal);
            rownum = getHeader(sheet, rownum, metarialPriceTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
            rownum = getHeader(sheet, rownum, cellStyle, "优惠及其它金额增减");
            List<String> otherPriceAddOrReduce = Arrays.asList("类型", "增减原因", "增减金额");
            rownum = getCell(sheet, rownum, cellStyle2, otherPriceAddOrReduce);//填充套餐标配的列名

            //查询其他金额增减
            List<OtherAddReduceAmount> otherAddReduceAmountList = new ArrayList<>();
//        if (changeCategoryUrl.equals("other")) {
                otherAddReduceAmountList = projectMaterialService.getOthrgAddReducePrice(contractCode, changeNo);
                if (otherAddReduceAmountList == null || (otherAddReduceAmountList != null && otherAddReduceAmountList.size() == 0)) {
                    //当前变更版本号 是的当前的变更的去查
                    if(customerContract.getCurrentChangeVersion().equals(changeNo.substring(0,changeNo.length()-3))) {
                        otherAddReduceAmountList = otherAddRecuceAmountService.getOthrgAddReducePrice(contractCode);
                    }
                }
//        }

            //是材料部的人并且按照变更单号查询后是没有值时按照变更版本号查询
            if (roleName.contains(RoleNameFromCenter.MATERIAL_DEPARTMENT_AUDITOR) && otherAddReduceAmountList == null) {
                otherAddReduceAmountList = projectMaterialService.getOthrgPriceByChanVerNo(contractCode, changeNo);
            }
            BigDecimal addPrice = BigDecimal.ZERO;
            BigDecimal reducePrice = BigDecimal.ZERO;
            for (OtherAddReduceAmount otherAddReduceAmount : otherAddReduceAmountList) {
                String addReduceType = otherAddReduceAmount.getAddReduceType();
                if ("1".equals(addReduceType)) {
                    otherAddReduceAmount.setAddReduceType("增加");
                    addPrice = addPrice.add(otherAddReduceAmount.getQuota());
                } else {
                    otherAddReduceAmount.setAddReduceType("减少");
                    reducePrice = reducePrice.add(otherAddReduceAmount.getQuota());
                }
            }
            List<String> otherAddReduceAmountCellList = Arrays.asList("addReduceType", "addReduceReason", "quota");
            rownum = fillExcelCell(sheet, rownum, cellStyle4, otherAddReduceAmountList, otherAddReduceAmountCellList);
            getHeader(sheet, rownum, "合计：" + (addPrice.subtract(reducePrice)).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            rownum = getHeader(sheet, rownum, cellStyle, "优惠及其它金额增减(仅供查看)");
            List<String> otherPriceAddOrReduce = Arrays.asList("类型", "增减原因", "增减金额");
            rownum = getCell(sheet, rownum, cellStyle2, otherPriceAddOrReduce);//填充套餐标配的列名

            //查询其他金额增减
            List<OtherAddReduceAmount> otherAddReduceAmountList = new ArrayList<>();
//        if (changeCategoryUrl.equals("other")) {
            otherAddReduceAmountList = projectMaterialService.getOthrgAddReducePrice(contractCode, changeNo);
            if (otherAddReduceAmountList == null || (otherAddReduceAmountList != null && otherAddReduceAmountList.size() == 0)) {
                //当前变更版本号 是的当前的变更的去查
                if(customerContract.getCurrentChangeVersion().equals(changeNo.substring(0,changeNo.length()-3))) {
                    otherAddReduceAmountList = otherAddRecuceAmountService.getOthrgAddReducePrice(contractCode);
                }
            }
//        }

            //是材料部的人并且按照变更单号查询后是没有值时按照变更版本号查询
            if (roleName.contains(RoleNameFromCenter.MATERIAL_DEPARTMENT_AUDITOR) && otherAddReduceAmountList == null) {
                otherAddReduceAmountList = projectMaterialService.getOthrgPriceByChanVerNo(contractCode, changeNo);
            }
            BigDecimal addPrice = BigDecimal.ZERO;
            BigDecimal reducePrice = BigDecimal.ZERO;
            for (OtherAddReduceAmount otherAddReduceAmount : otherAddReduceAmountList) {
                String addReduceType = otherAddReduceAmount.getAddReduceType();
                if ("1".equals(addReduceType)) {
                    otherAddReduceAmount.setAddReduceType("增加");
                    addPrice = addPrice.add(otherAddReduceAmount.getQuota());
                } else {
                    otherAddReduceAmount.setAddReduceType("减少");
                    reducePrice = reducePrice.add(otherAddReduceAmount.getQuota());
                }
            }
            List<String> otherAddReduceAmountCellList = Arrays.asList("addReduceType", "addReduceReason", "quota");
            rownum = fillExcelCell(sheet, rownum, cellStyle4, otherAddReduceAmountList, otherAddReduceAmountCellList);
            rownum = getHeader(sheet, rownum, "合计：" + (addPrice.subtract(reducePrice)).setScale(2, BigDecimal.ROUND_HALF_UP));
            rownum = getHeader(sheet, rownum, cellStyle, "主材变更单合计");
            BigDecimal metarialPriceTotal = BigDecimal.ZERO;
            if (changeCategoryUrl.equals("other")) {
                metarialPriceTotal = addPrice.subtract(reducePrice);
            } else {
                metarialPriceTotal = materialAddPriceTotal.add(materialReducePriceTotal);
            }
            getHeader(sheet, rownum, metarialPriceTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        }
        try {
            ServletOutputStream out = null;
            resp.setContentType("application/x-msdownload");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(customerContract.getCustomerName() + customerContract.getContractCode() + "变更单.xls", "UTF-8") + "\"");
            out = resp.getOutputStream();
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (downLoadFlag) {
            //更改下载状态
            smChangeDetailService.updateDownloadStatus(changeNo);
            //修改下载次数
            SmChangeDetail smChangeDetail = smChangeDetailService.findDownloadTimesByChangeNo(changeNo);
            Integer downloadTimes = smChangeDetail.getDownloadTimes();
            downloadTimes += 1;
            smChangeDetail.setDownloadTimes(downloadTimes);
            smChangeDetailService.update(smChangeDetail);
        }

    }

    @RequestMapping("/oldhouseexport")
    public void oldHouse(HttpServletResponse resp, @RequestParam() String contractCode, @RequestParam() String contractStatus) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("美得你（尊贵套餐）附加老房拆除项目报价单");

        HSSFFont font = workbook.createFont();
        HSSFFont font2 = workbook.createFont();
        font.setFontName("黑体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 14);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor((short) 15);
        cellStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);//设置背景色

        cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle3.setFont(font2);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setWrapText(true);
        cellStyle4.setWrapText(true);
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        cellStyle2.setFont(font);
        //查询客户合同.
        CustomerContract customerContract = getCustomerContract(contractCode);
        int rownum = 0;
        rownum = getHeader(sheet, rownum, cellStyle3, "美得你（尊贵套餐）附加老房拆除项目报价单");
        String aaa = "合同编号：" + customerContract.getContractCode() + "  " + "订单编号：" + customerContract.getContractCode();
        rownum = getHeader(sheet, rownum, cellStyle3, aaa);
        rownum = getHeader(sheet, rownum, "尊敬的客户：");
        rownum = getHeader(sheet, rownum, "           您好，感谢您选择美得你装饰设计有限公司推出的装修新模式！公司全体员工努力为您提供优质的服务！期待您随时为我们提供宝贵的意见，便于更好的让您省时、省心、省力、省钱！谢谢！");
        rownum = getHeader(sheet, rownum, cellStyle, "基本信息：");
        getOldHouseCellData(sheet, rownum, customerContract, "客户姓名：", "建筑面积：", "主案设计师姓名：", "工程地址：");
        rownum = rownum + 1;
        getOldHouseCellData(sheet, rownum, customerContract, "客户电话：", "计价面积：", "主案设计师电话：", "有无电梯：");
        rownum = rownum + 1;
        getOldHouseCellData(sheet, rownum, customerContract, "户型：", "备注：", "房屋类型：", "第二联系人\\电话：");
        rownum = rownum + 1;
        rownum = getHeader(sheet, rownum, cellStyle, "老房拆除基装定额");
        List<String> dismantlebaseinstallquotaList = Arrays.asList("定额分类", "定额名称", "单位", "数量", "计价方式", "单价或占比", "合价", "工艺、做法及说明");
        rownum = getCell(sheet, rownum, cellStyle2, dismantlebaseinstallquotaList);
        //查询老房拆除的基装定额
        List<Map<String, Object>> dismantlebaseinstallquota = new ArrayList<>();
        if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
            dismantlebaseinstallquota = projectMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString());
        } else {
            dismantlebaseinstallquota = projectChangeMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLQUOTA.toString());
        }
        BigDecimal dismantlebaseinstallquotaTotal = new BigDecimal(0);
        for (Map<String, Object> obj : dismantlebaseinstallquota) {
            //获得合计
            BigDecimal baseinstallquotaTotal = (BigDecimal) obj.get("baseinstallquotaTotal");
            BigDecimal loss_dosage = (BigDecimal) obj.get("loss_dosage");
            BigDecimal store_sale_price = (BigDecimal) obj.get("store_sale_price");
            dismantlebaseinstallquotaTotal = dismantlebaseinstallquotaTotal.add(baseinstallquotaTotal);
            obj.put("loss_dosage", loss_dosage.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("store_sale_price", store_sale_price.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> dismantlebaseinstallquotaCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "store_sale_price", "baseinstallquotaTotal", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, dismantlebaseinstallquota, dismantlebaseinstallquotaCellList);
        rownum = getHeader(sheet, rownum, "合计：" + dismantlebaseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "老房拆除基装综合服务");
        rownum = getCell(sheet, rownum, cellStyle2, dismantlebaseinstallquotaList);
        //查询老房拆除的基装综合服务
        List<Map<String, Object>> dismantlebaseinstallcompfee = new ArrayList<>();
        if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
            dismantlebaseinstallcompfee = projectMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString());
        } else {
            dismantlebaseinstallcompfee = projectChangeMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEBASEINSTALLCOMPFEE.toString());
        }
        ProportionMoney byContractCode = new ProportionMoney();
        //调用计算金额的方法
        if (contractStatus.equals(CustomerContractEnum.TRANSFER_COMPLETE.toString())) {
            byContractCode = proportionMoneyDao.getByContractCode(contractCode);
        } else {
            Map<String, Object> dosageByContractCodeList = smSkuDosageService.findDosageByContractCodeList(contractCode);
            byContractCode.setBaseloadrating1((BigDecimal) dosageByContractCodeList.get("baseloadrating1"));
            byContractCode.setBaseloadrating3((BigDecimal) dosageByContractCodeList.get("baseloadrating3"));
            byContractCode.setComprehensivefee4((BigDecimal) dosageByContractCodeList.get("comprehensivefee4"));
            byContractCode.setRenovationAmount((BigDecimal) dosageByContractCodeList.get("renovationamount"));
        }
        //获得旧房拆改的基装定额价格
        BigDecimal baseloadrating3 = byContractCode.getBaseloadrating3();
        BigDecimal disBaseComFeeTotal = new BigDecimal(0);//老房拆除基装综合服务费合计
        for (Map<String, Object> obj : dismantlebaseinstallcompfee) {
            //获得合计
            BigDecimal baseinstallquotaTotal = (BigDecimal) obj.get("baseinstallquotaTotal");
            //获得占比
            BigDecimal projectProportion = (BigDecimal) obj.get("project_proportion");
            //获得计价方式
            String dosagePricingMode = (String) obj.get("dosage_pricing_mode");
            if ("拆除基装定额总价占比".equals(dosagePricingMode)) {
                baseinstallquotaTotal = (projectProportion.multiply(baseloadrating3)).divide(new BigDecimal(100));
                disBaseComFeeTotal = disBaseComFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            BigDecimal loss_dosage = (BigDecimal) obj.get("loss_dosage");
            BigDecimal project_proportion = (BigDecimal) obj.get("project_proportion");
            obj.put("loss_dosage", loss_dosage.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("project_proportion", project_proportion.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> dismantlebaseinstallcompfeeCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "project_proportion", "baseinstallquotaTotal", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, dismantlebaseinstallcompfee, dismantlebaseinstallcompfeeCellList);
        rownum = getHeader(sheet, rownum, "合计：" + disBaseComFeeTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "老房拆除其它综合服务");
        rownum = getCell(sheet, rownum, cellStyle2, dismantlebaseinstallquotaList);
        //查询老房拆除的其他综合费
        List<Map<String, Object>> dismantleothercompfee = new ArrayList<>();
        if (customerContract.getContractStatus().equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
            dismantleothercompfee = projectMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString());
        } else {
            dismantleothercompfee = projectChangeMaterialService.getDismantlebaseinstallquotaByContractCode(contractCode, SelectMaterialTypeEnmu.DISMANTLEOTHERCOMPFEE.toString());
        }
        //拆除工程占比的总价
        BigDecimal comprehensivefee4 = byContractCode.getComprehensivefee4();
        BigDecimal disOtherComFeeTotal = new BigDecimal(0);//老房拆除其他综合服务费合计
        for (Map<String, Object> obj : dismantleothercompfee) {
            //获得合计
            BigDecimal baseinstallquotaTotal = (BigDecimal) obj.get("baseinstallquotaTotal");
            //获得计价方式
            String dosagePricingMode = (String) obj.get("dosage_pricing_mode");
            //获得占比
            BigDecimal projectProportion = (BigDecimal) obj.get("project_proportion");
            //获得单价
            BigDecimal price = (BigDecimal) obj.get("store_sale_price");
            //获得数量
            BigDecimal lossDosage = (BigDecimal) obj.get("loss_dosage");
            if ("拆除基装定额总价占比".equals(dosagePricingMode)) {
                baseinstallquotaTotal = (projectProportion.multiply(baseloadrating3)).divide(new BigDecimal(100));
                disOtherComFeeTotal = disOtherComFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if ("拆除工程总价占比".equals(dosagePricingMode)) {
                baseinstallquotaTotal = (projectProportion.multiply(comprehensivefee4)).divide(new BigDecimal(100));
                disOtherComFeeTotal = disOtherComFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else if ("固定单价".equals(dosagePricingMode)) {
                baseinstallquotaTotal = (price.multiply(lossDosage));
                disOtherComFeeTotal = disOtherComFeeTotal.add(baseinstallquotaTotal);
                obj.put("baseinstallquotaTotal", baseinstallquotaTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            obj.put("loss_dosage", lossDosage.setScale(2, BigDecimal.ROUND_HALF_UP));
            obj.put("project_proportion", projectProportion.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<String> dismantleothercompfeeCellList = Arrays.asList("domain_name", "product_name", "material_unit", "loss_dosage", "dosage_pricing_mode", "project_proportion", "baseinstallquotaTotal", "quota_describe");
        rownum = fillExcelCell(sheet, rownum, cellStyle4, dismantleothercompfee, dismantleothercompfeeCellList);
        rownum = getHeader(sheet, rownum, "合计：" + disOtherComFeeTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        rownum = getHeader(sheet, rownum, cellStyle, "老房拆除工程造价合计");
        rownum = getHeader(sheet, rownum, (dismantlebaseinstallquotaTotal.add(disBaseComFeeTotal).add(disOtherComFeeTotal)).setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
        getHeader(sheet, rownum, "业主签字：            主案设计师签字：            设计部负责人：            审计员签字：            总经理签字：");
        try {
            ServletOutputStream out = null;
            resp.setContentType("application/x-msdownload");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(customerContract.getCustomerName() + customerContract.getContractCode() + "老房拆改.xls", "UTF-8") + "\"");
            out = resp.getOutputStream();
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getOldHouseCellData(HSSFSheet sheet, int rownum, CustomerContract customerContract, String oneCellData, String twoCellData, String threeCellData, String fourCellData) {
        for (int i = 0; i < 8; i++) {
            sheet.setColumnWidth(i, 4000);
        }
        String oneRowData = null;
        String twoRowData = null;
        String threeRowData = null;
        String fourRowData = null;
        if (null != customerContract) {
            if (5 == rownum) {
                if (null != customerContract.getCustomerName()) {
                    oneRowData = customerContract.getCustomerName();
                } else {
                    oneRowData = "";
                }

                if (null != customerContract.getBuildArea()) {
                    twoRowData = customerContract.getBuildArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getDesignerDepName()) {
                    threeRowData = customerContract.getDesigner();
                } else {
                    threeRowData = "";
                }
                if (null != customerContract.getHouseAddr()) {
                    fourRowData = customerContract.getAddressProvince() + customerContract.getAddressCity()
                            + customerContract.getAddressArea() + customerContract.getHouseAddr();
                } else {
                    fourRowData = "";
                }
                addOldHouseData(sheet, rownum, oneCellData, twoCellData, threeCellData, fourCellData, oneRowData, twoRowData, threeRowData, fourRowData);
            }
            if (6 == rownum) {
                if (null != customerContract.getCustomerMobile()) {
                    oneRowData = customerContract.getCustomerMobile();
                } else {
                    oneRowData = "";
                }
                if (null != customerContract.getValuateArea()) {
                    twoRowData = customerContract.getValuateArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getDesignerMobile()) {
                    threeRowData = customerContract.getDesignerMobile();
                } else {
                    threeRowData = "";
                }
                if (null != customerContract.getElevator() && customerContract.getElevator() == 1) {
                    fourRowData = "有";
                } else {
                    fourRowData = "无";
                }
                addOldHouseData(sheet, rownum, oneCellData, twoCellData, threeCellData, fourCellData, oneRowData, twoRowData, threeRowData, fourRowData);
            }
            if (7 == rownum) {
                if (null != customerContract.getLayout()) {
                    oneRowData = customerContract.getLayout();
                } else {
                    oneRowData = "";
                }
                twoRowData = "";
                if (null != customerContract.getHouseType()) {
                    if (customerContract.getHouseType().equals("1")) {
                        threeRowData = "复式";
                    } else if (customerContract.getHouseType().equals("2")) {
                        threeRowData = "别墅";
                    } else {
                        threeRowData = "楼房平层";
                    }
                } else {
                    threeRowData = "";
                }
                if (null != customerContract.getSecondContact() && null != customerContract.getSecondContactMobile()) {
                    fourRowData = customerContract.getSecondContact() + "    " + customerContract.getSecondContactMobile();
                } else {
                    fourRowData = "";
                }
                addOldHouseData(sheet, rownum, oneCellData, twoCellData, threeCellData, fourCellData, oneRowData, twoRowData, threeRowData, fourRowData);
            }
        }
    }

    private void addOldHouseData(HSSFSheet sheet, int rownum, String oneCellData,
                                       String twoCellData, String threeCellData, String fourCellData,
                                       String oneRowData, String twoRowData,
                                       String threeRowData, String fourRowData) {
        HSSFRow row = sheet.createRow(rownum);//从第几行开始插入数据
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(oneCellData);
        row.createCell(1).setCellValue(oneRowData);
        cell = row.createCell((short) 2);
        cell.setCellValue(twoCellData);
        row.createCell(3).setCellValue(twoRowData);
        cell = row.createCell((short) 4);
        cell.setCellValue(threeCellData);
        row.createCell(5).setCellValue(threeRowData);
        cell = row.createCell((short) 6);
        cell.setCellValue(fourCellData);
        row.createCell(7).setCellValue(fourRowData);
        sheet.addMergedRegion(new Region(rownum, (short) 7, rownum, (short) 14));
    }

    private void getRowCellData(HSSFSheet sheet, CustomerContract customerContract, int rownum, String oneCellData, String twoCellData, String threeCellData) {
        for (int i = 0; i < 14; i++) {
            if (i == 0 || i == 2 || i == 4) {
                sheet.setColumnWidth(i, 4500);
            } else {
                sheet.setColumnWidth(i, 3000);
            }
        }
        String oneRowData = null;
        String twoRowData = null;
        String threeRowData = null;
        if (null != customerContract) {
            if (5 == rownum) {
                if (StringUtils.isNotBlank(customerContract.getSecondContact())) {
                    if (null != customerContract.getSecondContact()) {
                        oneRowData = customerContract.getSecondContact();
                    } else {
                        oneRowData = "";
                    }
                } else {
                    if (null != customerContract.getCustomerName()) {
                        oneRowData = customerContract.getCustomerName();
                    } else {
                        oneRowData = "";
                    }
                }
                if (null != customerContract.getDesigner()) {
                    twoRowData = customerContract.getDesigner();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getHouseAddr()) {
                    threeRowData = customerContract.getAddressProvince() + customerContract.getAddressCity()
                            + customerContract.getAddressArea() + customerContract.getHouseAddr();
                } else {
                    threeRowData = "";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (6 == rownum) {
                if (StringUtils.isNotBlank(customerContract.getSecondContact())) {
                    if (null != customerContract.getSecondContactMobile()) {
                        oneRowData = customerContract.getSecondContactMobile();
                    } else {
                        oneRowData = "";
                    }
                } else {
                    if (null != customerContract.getCustomerMobile()) {
                        oneRowData = customerContract.getCustomerMobile();
                    } else {
                        oneRowData = "";
                    }
                }
                if (null != customerContract.getDesignerMobile()) {
                    twoRowData = customerContract.getDesignerMobile();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getLayout()) {
                    threeRowData = customerContract.getLayout();
                } else {
                    threeRowData = "";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (7 == rownum) {
                if (null != customerContract.getSupervisor()) {
                    oneRowData = customerContract.getSupervisor();
                } else {
                    oneRowData = "";
                }

                twoRowData = "";

                if (null != customerContract.getMealPrice()) {
                    threeRowData = customerContract.getMealPrice().toString();
                } else {
                    threeRowData = "";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (8 == rownum) {
                if (null != customerContract.getSupervisorMobile()) {
                    oneRowData = customerContract.getSupervisorMobile();
                } else {
                    oneRowData = "";
                }

                twoRowData = "";

                if (null != customerContract.getHouseCondition()) {
                    if (customerContract.getHouseCondition().equals("1")) {
                        threeRowData = "新房";
                    } else {
                        threeRowData = "旧房";
                    }
                } else {
                    threeRowData = "";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (9 == rownum) {
                oneRowData = "";
                if (null != customerContract.getBuildArea()) {
                    twoRowData = customerContract.getBuildArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }

                if (null != customerContract.getElevator() && customerContract.getElevator() == 1) {
                    threeRowData = "有";
                } else {
                    threeRowData = "无";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (10 == rownum) {
                oneRowData = "";
                if (null != customerContract.getValuateArea()) {
                    twoRowData = customerContract.getValuateArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }

                if (null != customerContract.getHouseType()) {
                    if (customerContract.getHouseType().equals("1")) {
                        threeRowData = "复式";
                    } else if (customerContract.getHouseType().equals("2")) {
                        threeRowData = "别墅";
                    } else {
                        threeRowData = "楼房平层";
                    }
                } else {
                    threeRowData = "";
                }
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (11 == rownum) {

                if (null != customerContract.getContractStartTime()) {
                    oneRowData = DateUtils.parseStrYMD(customerContract.getContractStartTime());
                } else {
                    oneRowData = "";
                }
                if (StringUtils.isNotBlank(customerContract.getSecondContact())) {
                    if (null != customerContract.getCustomerName()) {
                        twoRowData = customerContract.getCustomerName();
                    } else {
                        twoRowData = "";
                    }
                    if (null != customerContract.getCustomerMobile()) {
                        threeRowData = customerContract.getCustomerMobile();
                    } else {
                        threeRowData = "";
                    }
                } else {
                    if (null != customerContract.getSecondContact()) {
                        twoRowData = customerContract.getSecondContact();
                    } else {
                        twoRowData = "";
                    }
                    if (null != customerContract.getSecondContactMobile()) {
                        threeRowData = customerContract.getSecondContactMobile();
                    } else {
                        threeRowData = "";
                    }
                }


                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (12 == rownum) {
                if (null != customerContract.getContractCompleteTime()) {
                    oneRowData = DateUtils.parseStrYMD(customerContract.getContractCompleteTime());
                } else {
                    oneRowData = "";
                }
                twoRowData = "";
                threeRowData = "";
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (13 == rownum) {
                if (null != customerContract.getDesignRemark()) {
                    oneRowData = customerContract.getDesignRemark();
                } else {
                    oneRowData = "";
                }
                twoRowData = "";
                threeRowData = "";
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }
            if (14 == rownum) {
                if (null != customerContract.getAuditRemark()) {
                    oneRowData = customerContract.getAuditRemark();
                } else {
                    oneRowData = "";
                }
                twoRowData = "";
                threeRowData = "";
                addRowData(sheet, rownum, oneCellData, twoCellData, threeCellData, oneRowData, twoRowData, threeRowData);
            }

        }
    }


    private void addRowData(HSSFSheet sheet, int rownum, String oneCellData,
                                  String twoCellData, String threeCellData,
                                  String oneRowData, String twoRowData, String threeRowData) {
        HSSFRow row = sheet.createRow(rownum);//从第几行开始插入数据
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(oneCellData);
        row.createCell(1).setCellValue(oneRowData);
        cell = row.createCell((short) 2);
        cell.setCellValue(twoCellData);
        row.createCell(3).setCellValue(twoRowData);
        cell = row.createCell((short) 4);
        cell.setCellValue(threeCellData);
        row.createCell(5).setCellValue(threeRowData);
        sheet.addMergedRegion(new Region(rownum, (short) 5, rownum, (short) 14));
        if (rownum == 13 || rownum == 14) {
            sheet.addMergedRegion(new Region(rownum, (short) 1, rownum, (short) 14));
        }
    }

    private void getChageRowCellData(HSSFSheet sheet, int rownum, CustomerContract customerContract, String oneCellData, String twoCellData, String threeCellData, String fourCellData, String fiveCellData) {
        for (int i = 0; i < 13; i++) {
            if (i == 0 || i == 8) {
                sheet.setColumnWidth(i, 4000);
            } else {
                sheet.setColumnWidth(i, 3000);
            }
        }
        String oneRowData = null;
        String twoRowData = null;
        String threeRowData = null;
        String fourRowData = null;
        String fiveRowData = null;
        if (null != customerContract) {
            if (3 == rownum) {
                if (null != customerContract.getSecondContact()) {
                    oneRowData = customerContract.getSecondContact();
                } else {
                    oneRowData = "";
                }

                if (null != customerContract.getBuildArea()) {
                    twoRowData = customerContract.getBuildArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getDesigner()) {
                    threeRowData = customerContract.getDesigner();
                } else {
                    threeRowData = "";
                }
                if (null != customerContract.getCustomerName()) {
                    fourRowData = customerContract.getCustomerName();
                } else {
                    fourRowData = "";
                }
                if (null != customerContract.getHouseAddr()) {
                    fiveRowData = customerContract.getAddressProvince() + customerContract.getAddressCity()
                    + customerContract.getAddressArea() + customerContract.getHouseAddr();
                } else {
                    fiveRowData = "";
                }
                addChangeData(sheet, rownum, oneCellData, twoCellData, threeCellData, fourCellData, fiveCellData, oneRowData, twoRowData, threeRowData, fourRowData, fiveRowData);
            }
            if (4 == rownum) {
                if (null != customerContract.getSecondContactMobile()) {
                    oneRowData = customerContract.getSecondContactMobile();
                } else {
                    oneRowData = "";
                }
                if (null != customerContract.getValuateArea()) {
                    twoRowData = customerContract.getValuateArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    twoRowData = "";
                }
                if (null != customerContract.getDesignerMobile()) {
                    threeRowData = customerContract.getDesignerMobile();
                } else {
                    threeRowData = "";
                }
                if (null != customerContract.getCustomerMobile()) {
                    fourRowData = customerContract.getCustomerMobile();
                } else {
                    fourRowData = "";
                }
                if (null != customerContract.getLayout()) {
                    fiveRowData = customerContract.getLayout();
                } else {
                    fiveRowData = "";
                }
                addChangeData(sheet, rownum, oneCellData, twoCellData, threeCellData, fourCellData, fiveCellData, oneRowData, twoRowData, threeRowData, fourRowData, fiveRowData);
            }
        }
    }

   private void addChangeData(HSSFSheet sheet, int rownum, String oneCellData,
                                     String twoCellData, String threeCellData, String fourCellData,
                                     String fiveCellData,
                                     String oneRowData, String twoRowData,
                                     String threeRowData, String fourRowData, String fiveRowData) {
        HSSFRow row = sheet.createRow(rownum);//从第几行开始插入数据
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(oneCellData);
        row.createCell(1).setCellValue(oneRowData);
        cell = row.createCell((short) 2);
        cell.setCellValue(twoCellData);
        row.createCell(3).setCellValue(twoRowData);
        cell = row.createCell((short) 4);
        cell.setCellValue(threeCellData);
        row.createCell(5).setCellValue(threeRowData);
        cell = row.createCell((short) 6);
        cell.setCellValue(fourCellData);
        row.createCell(7).setCellValue(fourRowData);
        cell = row.createCell((short) 8);
        cell.setCellValue(fiveCellData);
        row.createCell(9).setCellValue(fiveRowData);
        if (rownum == 3 || rownum == 4) {
            sheet.addMergedRegion(new Region(rownum, (short) 9, rownum, (short) 14));
        }
    }

    private void getRowCellData(HSSFSheet sheet, int rownum, String oneCellData) {
        HSSFRow row = sheet.createRow(rownum);//从第几行开始插入数据
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(oneCellData);
        row.createCell(1).setCellValue(1);
    }

    public static int getHeader(HSSFSheet sheet, int rownum, String oneCellData) {
        sheet.addMergedRegion(new Region(rownum, (short) 0, rownum, (short) 14));
        HSSFRow row = sheet.createRow(rownum);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(new HSSFRichTextString(oneCellData));
        rownum++;
        return rownum;
    }

    private int getHeader(HSSFSheet sheet, int rownum, HSSFCellStyle cellStyle, String oneCellData) {
        sheet.addMergedRegion(new Region(rownum, (short) 0, rownum, (short) 14));
        HSSFRow row = sheet.createRow(rownum);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(new HSSFRichTextString(oneCellData));
        cell.setCellStyle(cellStyle);
        rownum++;
        return rownum;
    }

    private int getCell(HSSFSheet sheet, int rownum, HSSFCellStyle cellStyle, List<String> cellNameList) {
        HSSFRow row = sheet.createRow(rownum);//创建第一行（表头）
        HSSFCell cell = row.createCell((short) 0);
        for (int i = 0; i < cellNameList.size(); i++) {
            cell.setCellStyle(cellStyle);
            cell.setCellValue(cellNameList.get(i));
            cell = row.createCell((short) i + 1);
            if (i == cellNameList.size() - 1) {
                sheet.addMergedRegion(new Region(rownum, (short) i, rownum, (short) 14));
            }
        }
        rownum++;
        return rownum;
    }


    private int getTotal(HSSFSheet sheet, int rownum, String total) {
        HSSFRow row = sheet.createRow(rownum);
        sheet.addMergedRegion(new Region(rownum, (short) 0, rownum, (short) 14));
        row.createCell(0).setCellValue(total);
        rownum++;
        return rownum;
    }

    private  <T> int fillExcelCell(HSSFSheet sheet, int rownum, HSSFCellStyle cellStyle, List<T> list, List<String> cellNameList) {
        int startRowIndex = rownum;
        for (T obj : list) {
            HSSFRow row = sheet.createRow(startRowIndex);
            int colIdx = 0;
            try {
                for (String fieldName : cellNameList) {
                    Cell cell = row.createCell(colIdx);
                    cell.setCellStyle(cellStyle);
                    Object propValue = null;
                    if (obj instanceof Map) { // Map类型的集合
                        Map<String, Object> map = (Map<String, Object>) obj;
                        propValue = map.get(fieldName);
                    } else {
                        propValue = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(obj, fieldName);
                    }

                    if (null == propValue) {
                        propValue = StringUtils.EMPTY;
                    }
                    if (propValue instanceof Date) {
                        SimpleDateFormat df = new SimpleDateFormat("");
                        String dateStr = df.format(propValue);
                        cell.setCellValue(dateStr);
                    } else if (propValue instanceof Double) {
                        cell.setCellValue((Double) propValue);
                    } else if (propValue instanceof Integer) {
                        cell.setCellValue((Integer) propValue);
                    } else if (propValue instanceof Boolean) {
                        cell.setCellValue((Boolean) propValue);
                    } else {
                        cell.setCellValue(propValue.toString());
                    }
                    if (colIdx == cellNameList.size() - 1) {
                        sheet.addMergedRegion(new Region(startRowIndex, (short) colIdx, startRowIndex, (short) 14));
                    }
                    colIdx++; // 列偏移
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            startRowIndex++; // 行号偏移
        }
        return startRowIndex + 1;
    }


    private CustomerContract getCustomerContract(String contractCode) {
        //查询客户合同
        CustomerContract customerContract = customerContractService.getByCode(contractCode);
        return customerContract;
    }
}
