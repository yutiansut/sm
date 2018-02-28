package cn.mdni.business.service.finance;


import cn.mdni.business.constants.PropertyHolder;
import cn.mdni.business.entity.finance.PaymoneyRecord;
import cn.mdni.business.entity.finance.ProjectPayplanStage;
import cn.mdni.commons.excel.export.ExportSingleSheetHelper;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.core.dto.BootstrapPage;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 财务查询Controller
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/22.
 */
@Service
public class FinaAnalyzeService {

    @Autowired
    private PaymoneyRecordService paymoneyRecordService;

    @Autowired
    private FinaCustomerContractService finaCustomerContractService;

    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;

    @Autowired
    private ProjectChangeMoneyService projectChangeMoneyService;

    @Autowired
    private ReparationMoneyService reparationMoneyService;

    @Autowired
    private RefundRecordService refundRecordService;

    /**
     * 查询财务阶段
     * @return
     */
    public List<Map<String,String>> getStageType(String storeCode) {
        if(StringUtils.isNotBlank(storeCode)){
            String[] storeCodeArr = storeCode.split(",");
            if(storeCodeArr[0] != null){
                return this.paymoneyRecordService.getStageType(storeCodeArr[0]);
            }
        }
        return null;
    }

    /**
     * 交款记录查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<PaymoneyRecord> paymoneyRecordFindAll(Map<String, Object> paramMap) {
        return this.paymoneyRecordService.paymoneyRecordFindAll(paramMap);
    }

    /**
     * 综合查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<Map> projectCompositeFindAll(Map<String, Object> paramMap) {
        BootstrapPage<Map> paymoneyRecordBootstrapPage = this.paymoneyRecordService.projectCompositeFindAll(paramMap);
        return paymoneyRecordBootstrapPage;
    }


    /**
     * 财务汇总查询
     * 1.总查询
     * 2.获取所有contractCode集合
     * 3.根据contractCode进行查询
     * 4.获取的数据放入map中
     * @param paramMap
     * @param storeCode
     * @return
     */
    public BootstrapPage<Map> findContractAll(Map<String, Object> paramMap, String storeCode) {

        //1.总查询
        BootstrapPage<Map> contractList= this.finaCustomerContractService.findContractAll(paramMap);
        List<Map> list = contractList.getRows();

        //2.获取所有contractCode集合
        List<String> codeList = new ArrayList<>();
        if( null != list && list.size() > 0 ){
            for (Map map : list){
                //2.1获取contractCode
                String queryContractCode = map.get("contractCode").toString();
                if(StringUtils.isNotBlank(queryContractCode)){
                    codeList.add(queryContractCode);
                }
            }
        }else{
            return contractList;
        }

        //3.根据contractCode进行查询
        //获取账目金额
        List<Map<String, String>> list1 = this.finaCustomerContractService.getAccountByContractCode(codeList);
        //获取赔款金额
        List<Map<String, String>> list2 = this.reparationMoneyService.getFeparationAmountByContractCode(codeList);
        //获取退单扣除费用
        List<Map<String,String>> list3 =this.refundRecordService.getChargebackAmount(codeList);
        //获取阶段金额
        List<ProjectPayplanStage> list4 = this.projectPayplanStageService.getStageByContractCode(codeList);
        //获取增项金额
        List<Map<String, String>> list5 = this.projectChangeMoneyService.getAddAmountByContractCode(codeList);
        //获取减项金额
        List<Map<String, String>> list6 = this.projectChangeMoneyService.getFewAmountByContractCode(codeList);

        Map<String, Map<String, String>> m1 = resultGroupByContractCode(list1);
        Map<String, Map<String, String>> m2 = resultGroupByContractCode(list2);
        Map<String, Map<String, String>> m3 = resultGroupByContractCode(list3);
        Map<String, Map<String, String>> m5 = resultGroupByContractCode(list5);
        Map<String, Map<String, String>> m6 = resultGroupByContractCode(list6);
        //4.获取的数据放入map中
        for(Map map : list){
            String queryContractCode = map.get("contractCode").toString();
            Map<String, String> a1 = m1.get(queryContractCode);
            Map<String, String> a2 = m2.get(queryContractCode);
            Map<String, String> a3 = m3.get(queryContractCode);
            Map<String, String> a5 = m5.get(queryContractCode);
            Map<String, String> a6 = m6.get(queryContractCode);
            resultIsNull("constructExpectAmount",a1,map);
            resultIsNull("constructTotalPayed",a1,map);
            resultIsNull("accumulativeTotal",a1,map);
            resultIsNull("reparationAmount",a2,map);
            resultIsNull("chargebackAmount",a3,map);
            resultIsNull("addChangeAmount",a5,map);
            resultIsNull("fewChangeAmount",a6,map);
            this.fillProjectPayplanStageList(map, list4, queryContractCode);
        }
        return contractList;
    }

    private boolean resultIsNull(String key,Map<String, String> sourceMap,Map<String, String> tagerMap){
        boolean flag = false;
        tagerMap.put(key, "0");
        if( null == sourceMap || null == sourceMap.get(key)){
            flag = true;
        }else{
            tagerMap.put(key, sourceMap.get(key));
        }
        return flag;
    }

    private Map<String, Map<String, String>> resultGroupByContractCode(List<Map<String, String>> list1){
        Map<String, Map<String, String>> m1 = Maps.newHashMap();
        for(Map m:list1){
            if(null == m || null == m.get("contractCode")){
                continue;
            }
            m1.put(m.get("contractCode").toString(),m);
        }
        return m1;
    }

    private void fillProjectPayplanStageList(Map map,List<ProjectPayplanStage> data1,String queryContractCode ){
        if( null != data1 && data1.size() > 0 ) {
            for (ProjectPayplanStage data : data1) {
                String contractCode = data.getContractCode();
                if(queryContractCode.equals(contractCode)) {
                    String stageTemplateCode = data.getStageTemplateCode();
                    //定金
                    BigDecimal depositActualTotalReceived = data.getActualTotalReceived();
                    //首期款
                    BigDecimal firstExpectReceived = data.getExpectReceived();
                    BigDecimal firstActualTotalReceived = data.getActualTotalReceived();
                    double oweFirst = firstExpectReceived.subtract(firstActualTotalReceived).doubleValue();

                    //中期款
                    BigDecimal mediumExpectReceived = data.getExpectReceived();
                    BigDecimal mediumActualTotalReceived = data.getActualTotalReceived();
                    double oweMedium = mediumExpectReceived.subtract(mediumActualTotalReceived).doubleValue();

                    //尾款
                    BigDecimal finalExpectReceived = data.getExpectReceived();
                    BigDecimal finalActualTotalReceived = data.getActualTotalReceived();
                    double oweFinal = finalExpectReceived.subtract(finalActualTotalReceived).doubleValue();

                    if (stageTemplateCode.equals("NODE_DEPOSIT")) {
                        map.put("depositActualTotalReceived", depositActualTotalReceived);
                    }
                    if (stageTemplateCode.equals("NODE_FIRST")) {
                        map.put("firstExpectReceived", firstExpectReceived);
                        map.put("firstActualTotalReceived", firstActualTotalReceived);
                        map.put("oweFirst", oweFirst);
                    }
                    if (stageTemplateCode.equals("NODE_MEDIUM")) {
                        map.put("mediumExpectReceived", mediumExpectReceived);
                        map.put("mediumActualTotalReceived", mediumActualTotalReceived);
                        map.put("oweMedium", oweMedium);
                    }
                    if (stageTemplateCode.equals("NODE_FINAL")) {
                        map.put("finalExpectReceived", finalExpectReceived);
                        map.put("finalActualTotalReceived", finalActualTotalReceived);
                        map.put("oweFinal", oweFinal);
                    }
                }
            }
        }
    }

    /**
     * 财务汇总 ----导出
     * 1.总查询
     * 2.获取所有contractCode集合
     * 3.根据contractCode进行查询
     * 4.获取的数据放入map中
     * @param
     * @return
     */
    public List<Map> exportProjectSummariz(Map<String, Object> paramMap) {
        List<Map> list = this.finaCustomerContractService.exportProjectSummariz(paramMap);

        //2.获取所有contractCode集合
        List<String> codeList = new ArrayList<>();
        if( null != list && list.size() > 0 ){
            for (Map map : list){
                //2.1获取contractCode
                String queryContractCode = map.get("contractCode").toString();
                if(StringUtils.isNotBlank(queryContractCode)){
                    codeList.add(queryContractCode);
                }
            }
        }else{
            return list;
        }

        //3.根据contractCode进行查询
        //获取账目金额
        List<Map<String, String>> list1 = this.finaCustomerContractService.getAccountByContractCode(codeList);
        //获取赔款金额
        List<Map<String, String>> list2 = this.reparationMoneyService.getFeparationAmountByContractCode(codeList);
        //获取退单扣除费用
        List<Map<String,String>> list3 =this.refundRecordService.getChargebackAmount(codeList);
        //获取阶段金额
        List<ProjectPayplanStage> list4 = this.projectPayplanStageService.getStageByContractCode(codeList);
        //获取增项金额
        List<Map<String, String>> list5 = this.projectChangeMoneyService.getAddAmountByContractCode(codeList);
        //获取减项金额
        List<Map<String, String>> list6 = this.projectChangeMoneyService.getFewAmountByContractCode(codeList);

        Map<String, Map<String, String>> m1 = resultGroupByContractCode(list1);
        Map<String, Map<String, String>> m2 = resultGroupByContractCode(list2);
        Map<String, Map<String, String>> m3 = resultGroupByContractCode(list3);
        Map<String, Map<String, String>> m5 = resultGroupByContractCode(list5);
        Map<String, Map<String, String>> m6 = resultGroupByContractCode(list6);
        //4.获取的数据放入map中
        for(Map map : list){
            String queryContractCode = map.get("contractCode").toString();
            Map<String, String> a1 = m1.get(queryContractCode);
            Map<String, String> a2 = m2.get(queryContractCode);
            Map<String, String> a3 = m3.get(queryContractCode);
            Map<String, String> a5 = m5.get(queryContractCode);
            Map<String, String> a6 = m6.get(queryContractCode);
            resultIsNull("constructExpectAmount",a1,map);
            resultIsNull("constructTotalPayed",a1,map);
            resultIsNull("accumulativeTotal",a1,map);
            resultIsNull("reparationAmount",a2,map);
            resultIsNull("chargebackAmount",a3,map);
            resultIsNull("addChangeAmount",a5,map);
            resultIsNull("fewChangeAmount",a6,map);
            this.fillProjectPayplanStageList(map, list4, queryContractCode);
        }

        return list;
    }

    /**
     * 综合数据 ----导出
     * @param paramMap
     * @return
     */
    public String exportProjectComposite(Map<String, Object> paramMap) {
        UploadCategory uploadCategory = UploadCategory.EXCLE;
        String basePath = PropertyHolder.getUploadBaseUrl();
        String fileName = "综合统计.xls";
        String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
        List<Map> list = this.paymoneyRecordService.exportProjectComposite(paramMap);
        if(list != null || list.size() > 0) {
            for (Map map : list) {
                //工程地址
                StringBuilder address = new StringBuilder();
                String addressProvince = StringUtils.isNotBlank((String) map.get("addressProvince")) ? map.get("addressProvince").toString() : "";
                String addressCity = StringUtils.isNotBlank((String) map.get("addressCity")) ? map.get("addressCity").toString() : "";
                String addressArea = StringUtils.isNotBlank((String) map.get("addressArea")) ? map.get("addressArea").toString() : "";
                String houseAddr = StringUtils.isNotBlank((String) map.get("houseAddr")) ? map.get("houseAddr").toString() : "";
                address.append(addressProvince).append(addressCity).append(addressArea).append(houseAddr);
                map.put("address", address);
            }
        }
        LinkedHashMap<String, String> headerMapper = Maps.newLinkedHashMap();
        headerMapper.put("contractCode", "项目编号");
        headerMapper.put("customerName", "客户");
        headerMapper.put("customeMobile", "客户电话");
        headerMapper.put("address", "工程地址");
        headerMapper.put("buildArea", "面积(m²)");
        headerMapper.put("orderFlowStatus", "订单状态");
        headerMapper.put("itemName", "财务阶段");
        headerMapper.put("stageFinished", "已签定金合同");
        headerMapper.put("actualReceived", "收款金额");
        headerMapper.put("payTime", "收款日期");
        headerMapper.put("paymethodName", "收款方式");
        ExportSingleSheetHelper exportSingleSheetHelper = new ExportSingleSheetHelper(fileFullPath, headerMapper, list);
        exportSingleSheetHelper.build();
        return fileFullPath;
    }

    /**
     * 交款记录 ----导出
     * @param paramMap
     * @return
     */
    public String exportPaymoneyRecord(Map<String, Object> paramMap) {
        UploadCategory uploadCategory = UploadCategory.EXCLE;
        String basePath = PropertyHolder.getUploadBaseUrl();
        String fileName = "交款记录统计.xls";
        String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
        List<PaymoneyRecord> paymoneyRecordList = this.paymoneyRecordService.exportPaymoneyRecord(paramMap);
        if(paymoneyRecordList != null || paymoneyRecordList.size() > 0 ){
            for(PaymoneyRecord paymoney : paymoneyRecordList){
                //收 -- 支
                if("RETURN_CONSTRUCT".equals(paymoney.getPayType())
                        || "RETURN_MODIFY".equals(paymoney.getPayType()) || "RETURN_DEPOSIT".equals(paymoney.getPayType())){
                    BigDecimal decimal = BigDecimal.valueOf(-1);
                    paymoney.setBranch(paymoney.getActualReceived().multiply(decimal));
                }else {
                    paymoney.setActual(paymoney.getActualReceived());
                }
                //会计科目
                if("首期款".equals(paymoney.getPayManualFlag()) || "中期款".equals(paymoney.getPayManualFlag())
                        || "尾款".equals(paymoney.getPayManualFlag()) || "补交首期款".equals(paymoney.getPayManualFlag())
                        || "首期款补交".equals(paymoney.getPayManualFlag()) || "中期款补交".equals(paymoney.getPayManualFlag())
                        || "补交中期款".equals(paymoney.getPayManualFlag()) || "拆改费".equals(paymoney.getPayManualFlag())){
                    paymoney.setAccountSubject("预收账款-住宅装修");
                }else if("小定".equals(paymoney.getPayManualFlag())){
                    paymoney.setAccountSubject("其他应付款-客户定金-小订");
                }else if("大定".equals(paymoney.getPayManualFlag())){
                    paymoney.setAccountSubject("其他应付款-客户定金-大定");
                }
            }
        }
        LinkedHashMap<String, String> headerMapper = Maps.newLinkedHashMap();
        headerMapper.put("createTime","日期");
        headerMapper.put("contractCode","项目编号");
        headerMapper.put("payerName","客户姓名");
        headerMapper.put("remark","摘要");
        headerMapper.put("actual","收");
        headerMapper.put("costfeeAmount","收款手续费");
        headerMapper.put("branch","支");
        headerMapper.put("receiptNum","收据号");
        headerMapper.put("paymethodName","收付款方式");
        headerMapper.put("paymethodAttrFullname","银行名称");
        headerMapper.put("orderFlowStatus","订单状态");
        headerMapper.put("accountSubject","会计科目");
        headerMapper.put("payManualFlag","款项类别");
        headerMapper.put("buildArea","面积(m²)");
        headerMapper.put("contractStartTime","合同开工时间");
        headerMapper.put("ifRcwName","是否红冲");
        ExportSingleSheetHelper exportSingleSheetHelper = new ExportSingleSheetHelper(fileFullPath, headerMapper, paymoneyRecordList);
        exportSingleSheetHelper.build();
        return fileFullPath;
    }

    public LinkedHashMap<String,String> getMapData(List<Map> list,String storeCode) {
        for(Map map : list){
            String modifyAmount = null;
            String addChangeAmount = null;
            String fewChangeAmount = null;
            String constructExpectAmount = null;
            String accumulativeTotal = null;
            if(map.get("modifyAmount") != null){
                modifyAmount = map.get("modifyAmount").toString();
            }
            if(map.get("addChangeAmount") != null){
                addChangeAmount = map.get("addChangeAmount").toString();
            }
            if(map.get("fewChangeAmount") != null){
                fewChangeAmount = map.get("fewChangeAmount").toString();
            }
            if(map.get("constructExpectAmount") != null){
                constructExpectAmount = map.get("constructExpectAmount").toString();
            }
            if(map.get("accumulativeTotal") != null){
                accumulativeTotal = map.get("accumulativeTotal").toString();
            }

            BigDecimal modifyAmounts = StringUtils.isNotBlank(modifyAmount) ? new BigDecimal(modifyAmount) : new BigDecimal(0);
            BigDecimal addChangeAmounts = StringUtils.isNotBlank(addChangeAmount) ? new BigDecimal(addChangeAmount) : new BigDecimal(0);
            BigDecimal fewChangeAmounts = StringUtils.isNotBlank(fewChangeAmount) ? new BigDecimal(fewChangeAmount) : new BigDecimal(0);
            BigDecimal constructExpectAmounts = StringUtils.isNotBlank(constructExpectAmount) ? new BigDecimal(constructExpectAmount) : new BigDecimal(0);
            BigDecimal accumulativeTotals = StringUtils.isNotBlank(accumulativeTotal) ? new BigDecimal(accumulativeTotal) : new BigDecimal(0);

            BigDecimal multiply = fewChangeAmounts.multiply(new BigDecimal(-1));

            BigDecimal accountsTotal = constructExpectAmounts.add(modifyAmounts).add(addChangeAmounts).subtract(multiply);
            double notAmount = accountsTotal.subtract(accumulativeTotals).doubleValue();

            map.put("accountsTotal",accountsTotal.toString());
            map.put("notAmount",Double.toString(notAmount));
        }
        LinkedHashMap<String, String> headerMapper = Maps.newLinkedHashMap();
        headerMapper.put("contractCode", "项目编号");
        headerMapper.put("customerName", "客户");
        headerMapper.put("constructExpectAmount", "合同金额");
        headerMapper.put("modifyAmount", "拆改费");
        headerMapper.put("addChangeAmount", "增项");
        headerMapper.put("fewChangeAmount", "减项");
        headerMapper.put("reparationAmount", "赔款");
        headerMapper.put("accountsTotal", "应收款合计");
        headerMapper.put("accumulativeTotal", "累计收款金额");
        headerMapper.put("notAmount", "未收款金额");
        headerMapper.put("orderFlowStatus", "订单状态");
        headerMapper.put("depositActualTotalReceived", "定金");
        List<Map> stageTemplateCodes = this.getStageTemplateCode(storeCode);
        if(stageTemplateCodes != null || stageTemplateCodes.size() > 0){
            for(Map templateCode : stageTemplateCodes){
               if(templateCode.get("stageTemplateCode").equals("NODE_FIRST")){
                   headerMapper.put("firstExpectReceived", "应收首期");
                   headerMapper.put("firstActualTotalReceived", "实收首期");
                   headerMapper.put("oweFirst", "欠首期款");
               }else if(templateCode.get("stageTemplateCode").equals("NODE_MEDIUM")){
                   headerMapper.put("mediumExpectReceived", "应收中期");
                   headerMapper.put("mediumActualTotalReceived", "实收中期");
                   headerMapper.put("oweMedium", "欠中期款");
               }else if(templateCode.get("stageTemplateCode").equals("NODE_FINAL")){
                   headerMapper.put("finalExpectReceived", "应收尾期");
                   headerMapper.put("finalActualTotalReceived", "实收尾期");
                   headerMapper.put("oweFinal", "欠尾期款");
               }
            }
        }
        headerMapper.put("chargebackAmount", "退单扣除费用");
        headerMapper.put("buildArea", "面积");
        headerMapper.put("serviceName", "客服姓名");
        headerMapper.put("designer", "设计师姓名");
        headerMapper.put("projectManager", "项目经理姓名");
        headerMapper.put("contractStartTime", "合同开工日期");
        headerMapper.put("contractCompleteTime", "合同竣工日期");
        headerMapper.put("startConstructionTime", "实际开工时间");
        headerMapper.put("completeConstructionTime", "实际竣工日期");
        headerMapper.put("mealName", "套餐");
        return headerMapper;
    }

    public List<Map> getStageTemplateCode(String storeCode) {
        if (storeCode != null) {
            String[] storeCodeArr = storeCode.split(",");
            if (storeCodeArr[0] != null) {
                return this.projectPayplanStageService.getStageTemplateCode(storeCodeArr[0]);
            }
        }
        return null;
    }
}
