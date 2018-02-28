package cn.mdni.business.service.finance;

import cn.mdni.business.constants.PropertyHolder;
import cn.mdni.business.constants.RoleNameFromCenter;
import cn.mdni.business.constants.SelectMaterialTypeEnmu;
import cn.mdni.business.dao.material.SmChangeDetailDao;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.material.ProjectMaterial;
import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmMaterialChangeAuditRecord;
import cn.mdni.business.service.material.SmChangeDetailService;
import cn.mdni.business.service.material.SmMaterialChangeAuditRecordService;
import cn.mdni.business.service.material.ConstructionChangeService;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.material.MongoCustomerContractService;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.pdf.PDFUtils;
import cn.mdni.commons.pdf.PdfDrawCell;
import cn.mdni.commons.pdf.PdfTablePrint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 刘铎 on 2017/12/12.
 */
@Service
public class ChangeMaterialPrintService {

    @Autowired
    private MongoCustomerContractService mongoCustomerContractService;

    private Logger logger = LoggerFactory.getLogger(ConstructionChangeService.class);
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private SmChangeDetailDao smChangeDetailDao;
    @Autowired
    private SmMaterialChangeAuditRecordService smMaterialChangeAuditRecordService;
    @Autowired
    private FinaPrintService finaPrintService;

    /**
     * 打印或查看
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object viewOrPrint(Long[] ids, Boolean isPrint, HttpServletRequest res) {
        String fileFullPath = findByConCodeAndChNo(ids);
        String imgerFullFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/home.png";
        String addWater = this.finaPrintService.addWater(imgerFullFile, fileFullPath);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));


        //isPrint是true,是打印,更新打印次数
        if (isPrint) {
            UpdatePrintCount(ids);
        }

        ResponseEntity<byte[]> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(new File(addWater)), httpHeaders,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("主材变更打印错误!" + e);
        }
        return responseEntity;
    }


    public void UpdatePrintCount(Long[] ids) {
        smChangeDetailDao.UpdatePrintCount(ids);
    }

    public String findByConCodeAndChNo(Long[] ids) {
        //根据id查询要打印的变更单
        List<SmChangeDetail> smChangeDetailLis = smChangeDetailService.getByIds(ids);
        List<String> filePath = new ArrayList<String>();
        String tempfilePath = "";
        try {
            if (smChangeDetailLis != null && smChangeDetailLis.size() > 0) {
                for (SmChangeDetail smChangeDetail : smChangeDetailLis) {
                    tempfilePath = FileUtils.saveFilePath(UploadCategory.PDF,
                            PropertyHolder.getUploadBaseUrl(), UUID.randomUUID().toString() + "." + UploadCategory.PDF.getPath());
                    CustomerContract customerContract = customerContractService.getByCode(smChangeDetail.getContractCode());
                    List<ProjectMaterial> projectMaterialList = mongoCustomerContractService.findMaterialByConcodeAndChno(smChangeDetail.getContractCode(), smChangeDetail.getChangeNo());
                    List<SmSkuChangeDosage> smSkuChangeDosageList = mongoCustomerContractService.findDosageByContrCode(smChangeDetail.getContractCode(), smChangeDetail.getChangeNo());
                    List<OtherAddReduceAmount> OtherAddReduceAmountList = mongoCustomerContractService.findOtherAddReduceAmountByContrCode(smChangeDetail.getContractCode(), smChangeDetail.getChangeNo());
                    File outFile = new File(tempfilePath);
                    float[] relativeWidths = {0.1F, 0.2F, 0.1F, 0.15F, 0.15F, 0.1F, 0.1F, 0.1F};
                    PdfTablePrint pdfTablePrint = new PdfTablePrint(8, outFile, relativeWidths);
                    //pdfTablePrint.drawTableRowNoboderFlag("主材变更单");
                    pdfTablePrint.drawTableRow("主材变更单", PdfDrawCell.getTitleFont(), false);
                    pdfTablePrint.drawTableRowEmpty();
                    String[] rowTwo = {"变更单号", smChangeDetail.getChangeNo(), "合同编号", smChangeDetail.getContractCode()};
                    pdfTablePrint.drawTableRowCellFillEmpty(rowTwo);
                    //第二联系人不为空时去第二联系人，否则取客户
                    if (StringUtils.isNotBlank(customerContract.getSecondContact())) {
                        String[] rowThree = {"客户姓名", customerContract.getSecondContact(), "工程地址", customerContract.getHouseAddr(),
                                "建筑面积", customerContract.getBuildArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "㎡", "设计师姓名", customerContract.getDesigner()};
                        pdfTablePrint.drawTableRow(rowThree);
                        String[] rowfour = {"客户电话", customerContract.getSecondContactMobile(), "户型", customerContract.getLayout(),
                                "计价面积", customerContract.getValuateArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "㎡", "设计师电话", customerContract.getDesignerMobile()};
                        pdfTablePrint.drawTableRow(rowfour);
                    }else{
                        String[] rowThree = {"客户姓名", customerContract.getCustomerName(), "工程地址", customerContract.getHouseAddr(),
                                "建筑面积", customerContract.getBuildArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "㎡", "设计师姓名", customerContract.getDesigner()};
                        pdfTablePrint.drawTableRow(rowThree);
                        String[] rowfour = {"客户电话", customerContract.getCustomerMobile(), "户型", customerContract.getLayout(),
                                "计价面积", customerContract.getValuateArea().setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "㎡", "设计师电话", customerContract.getDesignerMobile()};
                        pdfTablePrint.drawTableRow(rowfour);
                    }


                    pdfTablePrint.drawTableRow("主材增加项目", true);
                    String[] rowsix = {"类型", "类目", "品牌", "型号", "属性", "单位", "用量增加", "价格"};
                    pdfTablePrint.drawTableRow(rowsix);

                    //增加的合计
                    BigDecimal addPriceTotal = new BigDecimal(0);
                    //减少的合计
                    BigDecimal reducePriceTotal = new BigDecimal(0);
                    //其他金额的合计
                    BigDecimal otherPriceTotal = new BigDecimal(0);


                    for (ProjectMaterial projectMaterial : projectMaterialList) {
                        List<SmSkuChangeDosage> smSkuDosages = new ArrayList<>();
                        String id = projectMaterial.getId();
                        if (StringUtils.isNotBlank(id)) {
                            List<SmSkuChangeDosage> collect2 = smSkuChangeDosageList.stream().filter(a -> a.getProjectMaterialId().equals(projectMaterial.getId())).collect(Collectors.toList());
                            if (collect2 != null && collect2.size() > 0) {
                                for (SmSkuChangeDosage smSkuChangeDosage : collect2) {
                                    //获取原用量
                                    BigDecimal originalDosage = smSkuChangeDosage.getOriginalDosage();
                                    //获现用量
                                    BigDecimal lossDosage = smSkuChangeDosage.getLossDosage();
                                    //增加的用量
                                    BigDecimal addDosage = lossDosage.subtract(originalDosage);
                                    //减少的用量
                                    BigDecimal reduceDosage = originalDosage.subtract(lossDosage);
                                    smSkuChangeDosage.setAddDosage(addDosage);
                                    smSkuChangeDosage.setReduceDosage(reduceDosage);
                                    smSkuDosages.add(smSkuChangeDosage);
                                }
                                projectMaterial.setSkuChangeDosageList(smSkuDosages);
                            }
                        }
                    }


                    BigDecimal unitPrice = BigDecimal.ZERO;
                    for (ProjectMaterial projectMaterial : projectMaterialList) {
                        List<SmSkuChangeDosage> skuChangeDosageList = projectMaterial.getSkuChangeDosageList();
                        BigDecimal lossDosage = skuChangeDosageList.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal originalDosage = skuChangeDosageList.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal addDosage = lossDosage.subtract(originalDosage);
                        if (lossDosage.compareTo(originalDosage) == 1) {
                            SmSkuChangeDosage smSkuChangeDosage = skuChangeDosageList.get(0);
                            unitPrice = getUnitPrice(unitPrice, projectMaterial, smSkuChangeDosage);
                            //把主材增加项目添加
                            String catagoryName = getCatagoryName(projectMaterial);
                            String[] rowAdd = {String.valueOf(catagoryName), String.valueOf(projectMaterial.getCataLogName()),
                                    String.valueOf(projectMaterial.getBrand()), String.valueOf(projectMaterial.getSkuModel()),
                                    String.valueOf(projectMaterial.getAttribute1()), String.valueOf(projectMaterial.getMaterialUnit()),
                                    String.valueOf(addDosage.setScale(2,BigDecimal.ROUND_HALF_UP)), String.valueOf(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP))};
                            pdfTablePrint.drawTableRowCellColSpanEmpty(rowAdd);
                            if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
                                addPriceTotal = addPriceTotal.subtract(addDosage.multiply(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP)));
                            } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString())) {

                            }else{
                                addPriceTotal = addPriceTotal.add(addDosage.multiply(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP)));
                            }

                        }
                    }


                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"合计", String.valueOf(addPriceTotal.setScale(2,BigDecimal.ROUND_HALF_UP))});
                    pdfTablePrint.drawTableRow("主材减少项目", true);
                    String[] roweight = {"类型", "类目", "品牌", "型号", "属性", "单位", "用量减少", "价格"};
                    pdfTablePrint.drawTableRow(roweight);



                    for (ProjectMaterial projectMaterial : projectMaterialList) {
                        List<SmSkuChangeDosage> skuChangeDosageList = projectMaterial.getSkuChangeDosageList();
                        BigDecimal lossDosage = skuChangeDosageList.stream().filter(b -> b.getLossDosage() != null).map(a -> a.getLossDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal originalDosage = skuChangeDosageList.stream().filter(b -> b.getOriginalDosage() != null).map(a -> a.getOriginalDosage()).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal addDosage = originalDosage.subtract(lossDosage);
                        if (lossDosage.compareTo(originalDosage) == -1) {
                            SmSkuChangeDosage smSkuChangeDosage = skuChangeDosageList.get(0);
                            unitPrice = getUnitPrice(unitPrice, projectMaterial, smSkuChangeDosage);
                            //把主材增加项目添加
                            String catagoryName = getCatagoryName(projectMaterial);
                            String[] rowAdd = {String.valueOf(catagoryName), String.valueOf(projectMaterial.getCataLogName()),
                                    String.valueOf(projectMaterial.getBrand()), String.valueOf(projectMaterial.getSkuModel()),
                                    String.valueOf(projectMaterial.getAttribute1()), String.valueOf(projectMaterial.getMaterialUnit()),
                                    String.valueOf(addDosage.setScale(2,BigDecimal.ROUND_HALF_UP)), String.valueOf(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP))};
                            pdfTablePrint.drawTableRowCellColSpanEmpty(rowAdd);
                            if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
                                reducePriceTotal = reducePriceTotal.subtract(addDosage.multiply(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP)));
                            } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString())) {

                            }else{
                                reducePriceTotal = reducePriceTotal.add(addDosage.multiply(unitPrice.setScale(2,BigDecimal.ROUND_HALF_UP)));
                            }

                        }
                    }

                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"合计", String.valueOf(reducePriceTotal.abs().setScale(2,BigDecimal.ROUND_HALF_UP))});
                    pdfTablePrint.drawTableRow("优惠及其它金额增减项", true);
                    String[] rownine = {"类型", "增减事由", "增减金额"};
                    pdfTablePrint.drawTableRowCellColSpanEmpty(rownine);

                    for (OtherAddReduceAmount otherAddReduceAmount : OtherAddReduceAmountList) {
                        //增加的金额
                        String price = "";
                        //获取金额
                        BigDecimal quota = otherAddReduceAmount.getQuota();
                        if (StringUtils.isNotBlank(otherAddReduceAmount.getAddReduceType())) {
                            if ("1".equals(otherAddReduceAmount.getAddReduceType())) {
                                price = String.valueOf("+" + otherAddReduceAmount.getQuota());
                                otherPriceTotal = otherPriceTotal.add(quota);
                            } else {
                                price = String.valueOf("-" + otherAddReduceAmount.getQuota());
                                otherPriceTotal = otherPriceTotal.subtract(quota);
                            }
                        }
                        String[] rowAdd = {String.valueOf(otherAddReduceAmount.getItemName()),
                                String.valueOf(otherAddReduceAmount.getAddReduceReason()),
                                String.valueOf(price)};
                        pdfTablePrint.drawTableRowCellColSpanEmpty(rowAdd);
                    }
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"合计", String.valueOf(otherPriceTotal.setScale(2,BigDecimal.ROUND_HALF_UP))});
                    BigDecimal bigDecimal = (addPriceTotal.subtract(reducePriceTotal).add(otherPriceTotal)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pdfTablePrint.drawTableRow("主材变更合计", true);
                    if (bigDecimal.compareTo(BigDecimal.ZERO) == -1) {
                        pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{String.valueOf(bigDecimal)});
                    } else {
                        pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{String.valueOf("+" + bigDecimal)});
                    }

                    //材料部审核人员
                    //审计审核人员
                    //设计总监审核
                    Set<String> str1 = new HashSet<>();
                    Set<String> str2 = new HashSet<>();
                    Set<String> str3 = new HashSet<>();
                    List<SmMaterialChangeAuditRecord> smMaterialChangeAuditRecordList = smMaterialChangeAuditRecordService.getNameByChangeNo(smChangeDetail.getChangeNo());
                    for (SmMaterialChangeAuditRecord auditRecord : smMaterialChangeAuditRecordList) {
                        if (StringUtils.isNotBlank(auditRecord.getAuditUserType())) {
                            if (auditRecord.getAuditUserType().equals(RoleNameFromCenter.MATERIAL_DEPARTMENT_AUDITOR)) {//材料部审核人员
                                str1.add(auditRecord.getAuditUser());
                            } else if (auditRecord.getAuditUserType().equals("审计员")) {//审计审核
                                str2.add(auditRecord.getAuditUser());
                            } else if (auditRecord.getAuditUserType().equals("设计总监")) {//审计总监审核
                                str3.add(auditRecord.getAuditUser());
                            }
                        }
                    }
                    Date changeTime = smMaterialChangeAuditRecordService.getByChangeNo(smChangeDetail.getChangeNo());
                    String materialName = StringUtils.join(str1, ",");
                    String auditName = StringUtils.join(str2, ",");
                    String designName = StringUtils.join(str3, ",");
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"材料部审核", String.valueOf(materialName)});
                    pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"设计总监", String.valueOf(designName)});
                    if (changeTime != null) {
                        pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"审计员审核", String.valueOf(auditName),
                                "变更时间", String.valueOf(DateUtils.parseStrYMDHMS(changeTime))});
                    }else{
                        pdfTablePrint.drawTableRowCellColSpanEmpty(new String[]{"审计员审核", String.valueOf(auditName),
                                "变更时间", String.valueOf("")});
                    }


                    pdfTablePrint.build();
                    filePath.add(tempfilePath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建pdf失败! " + e);
        }
        if (filePath.size() > 1) {
            //合并
            String result = FileUtils.saveFilePath(UploadCategory.PDF, PropertyHolder.getUploadBaseUrl(),
                    UUID.randomUUID().toString() + "." + UploadCategory.PDF.getPath());
            PDFUtils.pdfMergeFiles(filePath.toArray(new String[]{}), result);
            return result;
        } else if (filePath.size() == 1) {
            return filePath.get(0);
        } else {
            return "";
        }
    }

    private BigDecimal getUnitPrice(BigDecimal unitPrice, ProjectMaterial projectMaterial, SmSkuChangeDosage smSkuChangeDosage) {
        if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.ADDITEM.toString())) {
            unitPrice = smSkuChangeDosage.getStoreIncreasePrice();
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.UPGRADEITEM.toString())) {
            unitPrice = smSkuChangeDosage.getStoreUpgradeDifferencePrice();
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
            unitPrice = smSkuChangeDosage.getStoreReducePrice();
        }
        return unitPrice;
    }

    private String getCatagoryName(ProjectMaterial projectMaterial) {
        String materialName = "";
        if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString())) {
            materialName = "套餐标配";
            return materialName;
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.UPGRADEITEM.toString())) {
            materialName = "升级项";
            return materialName;
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.ADDITEM.toString())) {
            materialName = "增项";
            return materialName;
        } else if (projectMaterial.getCategoryCode().equals(SelectMaterialTypeEnmu.REDUCEITEM.toString())) {
            materialName = "减项";
            return materialName;
        }
        return materialName;
    }
}

