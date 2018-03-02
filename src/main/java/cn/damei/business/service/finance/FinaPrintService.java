package cn.damei.business.service.finance;

import cn.damei.business.constants.PropertyHolder;
import cn.damei.business.entity.finance.PaymoneyRecord;
import cn.mdni.commons.date.DateUtils;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.number.NumberToCN;
import cn.mdni.commons.pdf.PDFUtils;
import cn.mdni.commons.pdf.PdfSimpleTemplatePrint;
import cn.mdni.commons.pdf.WatermarkInfo;
import cn.damei.core.WebUtils;
import cn.damei.core.dto.BootstrapPage;
import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Lists;
import com.lowagie.text.DocumentException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FinaPrintService {

    @Autowired
    private PaymoneyRecordService paymoneyRecordService;

    /**
     * 收据查询
     * @param paramMap
     * @return
     */
    public BootstrapPage<PaymoneyRecord> findReceiptAll(Map<String, Object> paramMap) {
        BootstrapPage<PaymoneyRecord> receiptAll = this.paymoneyRecordService.findReceiptAll(paramMap);
        receiptAll.setRows(this.replaceAllMobile(receiptAll.getRows()));
        return receiptAll;
    }

    /**
     * 隐藏手机号中间四位
     * @param paymoneyRecordList
     * @return
     */
    private List<PaymoneyRecord> replaceAllMobile(List<PaymoneyRecord> paymoneyRecordList) {
        paymoneyRecordList.forEach(p->{
            String mobile = p.getCustomerMobile();
            String secondMobile = p.getSecondContactMobile();
            String payerMobile = p.getPayerMobile();
            if( null != mobile && mobile.length() > 7 ){
                p.setCustomerMobile(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
            if( null != secondMobile && secondMobile.length() > 7 ){
                p.setSecondContactMobile(secondMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
            if( null != payerMobile && payerMobile.length() > 7 ){
                p.setPayerMobile(payerMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }
        });
        return paymoneyRecordList;
    }

    /**
     * 根据id获取多条数据---批量打印
     * @param ids
     * @return
     */
    public List<PaymoneyRecord> getManyPaymoneyRecordById(String ids) {
        String storeCode = WebUtils.getLoginedUserMainStore();
        List<PaymoneyRecord> paymoneyRecord = Lists.newArrayList();
        if (null != ids || "" != ids) {
            List<String> list = Arrays.asList(StringUtils.split(ids,","));
            paymoneyRecord.addAll(this.paymoneyRecordService.getManyPaymoneyRecordById(list,storeCode));
        }
        if(paymoneyRecord == null || paymoneyRecord.size() == 0){
            return null;
        }
        return paymoneyRecord;
    }

    /**
     * 收据打印
     * @param paymoneyRecord
     * @param templatePdfFile
     * @return
     */
    public String printPdf(PaymoneyRecord paymoneyRecord,String templatePdfFile){
        if(paymoneyRecord != null){
            UploadCategory uploadCategory = UploadCategory.PDF;
            String basePath = PropertyHolder.getUploadBaseUrl();
            String fileName = UUID.randomUUID().toString()+"收据.pdf";
            String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);

            Map<String,String> map = new HashMap<>();
            //公司名称、地址和电话
            String content = paymoneyRecord.getConfigContent();
            Object o = JSONUtils.parse(content);
            Map parse = (Map) o;
            String title = parse.get("title").toString();
            String adress = parse.get("adress").toString();
            String phoneNum = parse.get("phoneNum").toString();

            //工程地址
            StringBuffer buffer = new StringBuffer(paymoneyRecord.getAddressProvince());
            StringBuffer append = null;
            if(paymoneyRecord.getAddressProvince().equals(paymoneyRecord.getAddressCity())){
                append = buffer.append(paymoneyRecord.getAddressArea()).append(paymoneyRecord.getHouseAddr());
            }else{
                append = buffer.append(paymoneyRecord.getAddressCity()).append(paymoneyRecord.getAddressArea()).append(paymoneyRecord.getHouseAddr());
            }

            //获取打印次数
            Long id = paymoneyRecord.getId();
            this.paymoneyRecordService.updatePrintCount(id);
            Integer printCounts = this.paymoneyRecordService.getPrintCount(id);

            map.put("fill_0",title);
            map.put("fill_1", paymoneyRecord.getReceiptNum());
            //客户姓名
            if(StringUtils.isNotBlank(paymoneyRecord.getSecondContact())){
                map.put("fill_2", paymoneyRecord.getSecondContact());
            }else{
                map.put("fill_2", paymoneyRecord.getCustomerName());
            }
            if(StringUtils.isNotBlank(paymoneyRecord.getSecondContactMobile())){
                map.put("fill_3", paymoneyRecord.getSecondContactMobile());
            }else{
                map.put("fill_3", paymoneyRecord.getCustomerMobile());
            }
            map.put("fill_4", paymoneyRecord.getContractCode());
            map.put("fill_5", paymoneyRecord.getMealName());
            map.put("fill_6", append.toString());
            map.put("fill_7", paymoneyRecord.getPayManualFlag());
            map.put("fill_8", paymoneyRecord.getPaymethodName());
            map.put("fill_9", paymoneyRecord.getActualReceived().toString());
            map.put("fill_10", NumberToCN.number2CNMontrayUnit(paymoneyRecord.getActualReceived()));
            map.put("fill_12", DateUtils.parseStrYMD(paymoneyRecord.getPayTime()));
            map.put("fill_13", adress);
            map.put("fill_14", phoneNum);
            map.put("fill_15", Integer.toString(printCounts));
            PdfSimpleTemplatePrint<Map<String, String>> pdfSimpleTemplatePrint = new PdfSimpleTemplatePrint<>(
                    templatePdfFile, fileFullPath, map);
            pdfSimpleTemplatePrint.drawTableCell();
            return fileFullPath;
        }
        return null;
    }

    // 添加水印
    public String addWater(String imgerFullFile,String souceFilePath) {
        UploadCategory uploadCategory = UploadCategory.PDF;
        String basePath = PropertyHolder.getUploadBaseUrl();
        String fileName = UUID.randomUUID().toString()+"收据.pdf";
        String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
        List<WatermarkInfo> watermarkInfoList = Lists.newArrayList();
        watermarkInfoList.add(new WatermarkInfo("大美集团", 60, 620, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo("大美集团", 360, 480, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo("大美集团", 60, 280, 1, 0.1f));
        watermarkInfoList.add(new WatermarkInfo(imgerFullFile , 40, 760, 0, 1f));
        try {
            PDFUtils.addWatermark(souceFilePath,fileFullPath, watermarkInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return fileFullPath;
    }

    /**
     * 根据id查询数据 --- 单个打印
     * @param id
     * @param templatePdfFile
     * @return
     */
    public String pdfTemplate(Long id,String templatePdfFile){
        String storeCode = WebUtils.getLoginedUserMainStore();
        PaymoneyRecord paymoneyRecord = this.paymoneyRecordService.getPaymoneyRecordById(id,storeCode);
        return this.printPdf(paymoneyRecord,templatePdfFile);
    }


    /**
     * 批量打印
     * @param ids
     * @param templatePdfFile
     * @return
     */
    public String manyPdfTemplate(String ids,String templatePdfFile){
        List<PaymoneyRecord> paymoneyRecord = this.getManyPaymoneyRecordById(ids);
        if(paymoneyRecord != null){
            List<String> files = new ArrayList<>();
            for(PaymoneyRecord list:paymoneyRecord){
                String s = this.printPdf(list, templatePdfFile);
                files.add(s);
            }
            UploadCategory uploadCategory = UploadCategory.PDF;
            String basePath = PropertyHolder.getUploadBaseUrl();
            String fileName = "收据.pdf";
            String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
            String[] strings = files.toArray(new String[]{});
            PDFUtils.pdfMergeFiles(strings,fileFullPath);
            return fileFullPath;
        }
        return null;
    }


    public Map getStoreCode(String storeCode) {
        return this.paymoneyRecordService.getStoreCode(storeCode);
    }
}
