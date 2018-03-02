package cn.damei.business.controller.finance;

import cn.damei.business.constants.Constants;
import cn.damei.business.constants.PropertyHolder;
import cn.damei.business.service.finance.FinaAnalyzeService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.BootstrapPage;
import cn.damei.core.dto.StatusDto;
import cn.mdni.commons.excel.export.ExportSingleSheetHelper;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.view.ViewDownLoad;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finance/analyze")
public class AnalyzeController extends BaseController {

    @Autowired
    private FinaAnalyzeService finaAnalyzeService;

    /**
     * 交款记录查询
     * @param keyword               关键字
     * @param contractCode          项目编号
     * @param payerName             客户姓名
     * @param payerMobile           客户电话
     * @param receiptNum            单据号
     * @param paystartTime          收款开始时间
     * @param payendTime            收款结束时间
     * @param paymethodName         收款方式
     * @param ifRcw                 是否红冲
     * @param templateStageId       财务阶段 Id
     * @return
     */
    @RequestMapping("/paymoneyrecordfindall")
    public Object paymoneyRecordFindAll(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String contractCode,
                          @RequestParam(required = false) String payerName,
                          @RequestParam(required = false) String payerMobile,
                          @RequestParam(required = false) String receiptNum,
                          @RequestParam(required = false) String paystartTime,
                          @RequestParam(required = false) String payendTime,
                          @RequestParam(required = false) String paymethodName,
                          @RequestParam(required = false) String ifRcw,
                          @RequestParam(required = false) String templateStageId,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "20") int limit,
                          @RequestParam(defaultValue = "id") String paymoneyRecordColumn,
                          @RequestParam(defaultValue = "DESC") String orderSort) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "payendTime", payendTime);
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "payerName", payerName);
        MapUtils.putNotNull(paramMap, "payerMobile", payerMobile);
        MapUtils.putNotNull(paramMap, "receiptNum", receiptNum);
        MapUtils.putNotNull(paramMap, "paystartTime", paystartTime);
        MapUtils.putNotNull(paramMap, "paymethodName", paymethodName);
        MapUtils.putNotNull(paramMap, "ifRcw", ifRcw);
        MapUtils.putNotNull(paramMap, "templateStageId", templateStageId);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), paymoneyRecordColumn));
        return StatusDto.buildDataSuccessStatusDto(finaAnalyzeService.paymoneyRecordFindAll(paramMap));
    }


    /**
     * 查询财务阶段
     * @return
     */
    @RequestMapping("/getstagetype")
    public Object getStageType(String storeCode){
        List<Map<String,String>> stageTypeList = this.finaAnalyzeService.getStageType(storeCode);
        return StatusDto.buildDataSuccessStatusDto(stageTypeList);
    }

    /**
     * 综合查询
     * @param keyword           关键字
     * @param contractCode      项目编号
     * @param customerName      客户姓名
     * @param secondContact     第二联系人姓名
     * @param designer          设计师
     * @param serviceName       客服姓名
     * @param serviceMobile     客服电话
     * @param paystartTime      收款开始时间
     * @param payendTime        收款结束时间
     * @param templateStageId   财务阶段 Id
     * @param orderFlowStatus   订单流转状态
     * @param paymethodName     收款方式
     * @param stageFinished     是否签订金合同
     * @return
     */
    @RequestMapping("/projectcompositefindall")
    public Object projectCompositeFindAll(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String contractCode,
                          @RequestParam(required = false) String customerName,
                          @RequestParam(required = false) String secondContact,
                          @RequestParam(required = false) String designer,
                          @RequestParam(required = false) String serviceName,
                          @RequestParam(required = false) String serviceMobile,
                          @RequestParam(required = false) String paystartTime,
                          @RequestParam(required = false) String payendTime,
                          @RequestParam(required = false) String templateStageId,
                          @RequestParam(required = false) String orderFlowStatus,
                          @RequestParam(required = false) String paymethodName,
                          @RequestParam(required = false) String stageFinished,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "payendTime", payendTime);
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "customerName", customerName);
        MapUtils.putNotNull(paramMap, "secondContact", secondContact);
        MapUtils.putNotNull(paramMap, "designer", designer);
        MapUtils.putNotNull(paramMap, "serviceName", serviceName);
        MapUtils.putNotNull(paramMap, "serviceMobile", serviceMobile);
        MapUtils.putNotNull(paramMap, "paystartTime", paystartTime);
        MapUtils.putNotNull(paramMap, "templateStageId", templateStageId);
        MapUtils.putNotNull(paramMap, "orderFlowStatus", orderFlowStatus);
        MapUtils.putNotNull(paramMap, "stageFinished", stageFinished);
        MapUtils.putNotNull(paramMap, "paymethodName", paymethodName);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        BootstrapPage<Map> mapBootstrapPage = this.finaAnalyzeService.projectCompositeFindAll(paramMap);
        return StatusDto.buildDataSuccessStatusDto(mapBootstrapPage);
    }

    /**
     * 财务汇总查询
     * @param keyword           关键字
     * @param contractCode      项目编号
     * @param customerName      客户姓名
     * @param customerMobile    客户电话
     * @param orderFlowStatus   订单流转状态
     * @param stratDate         生单开始时间
     * @param endDate           生单结束时间
     * @return
     */
    @RequestMapping("/projectsummarizfindall")
    public Object projectSummarizFindAll(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String contractCode,
                             @RequestParam(required = false) String customerName,
                             @RequestParam(required = false) String customerMobile,
                             @RequestParam(required = false) String orderFlowStatus,
                             @RequestParam(required = false) String stratDate,
                             @RequestParam(required = false) String endDate,
                             @RequestParam(required = false) String contractCodeMany,
                             @RequestParam(defaultValue = "0") int offset,
                             @RequestParam(defaultValue = "20") int limit){
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "customerName", customerName);
        MapUtils.putNotNull(paramMap, "customerMobile", customerMobile);
        MapUtils.putNotNull(paramMap, "orderFlowStatus", orderFlowStatus);
        MapUtils.putNotNull(paramMap, "stratDate", stratDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        if(StringUtils.isNotBlank(contractCodeMany)) {
            String[] stringsArr = contractCodeMany.split("\n");
            List<String> strings = Arrays.asList(stringsArr);
            MapUtils.putNotNull(paramMap, "contractCodeMany", strings);
        }
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        BootstrapPage<Map> contractList= this.finaAnalyzeService.findContractAll(paramMap,storeCode);
        return StatusDto.buildDataSuccessStatusDto(contractList);
    }

    @RequestMapping("/getstagetemplatecode")
    public Object getStageTemplateCode(String storeCode){
        List<Map> list = this.finaAnalyzeService.getStageTemplateCode(storeCode);
        return StatusDto.buildDataSuccessStatusDto(list);
    }

    /**
     * 财务汇总 ----导出
     * @param keyword           关键字
     * @param contractCode      项目编号
     * @param customerName      客户姓名
     * @param customerMobile    客户电话
     * @param orderFlowStatus   订单流转状态
     * @param stratDate         生单开始时间
     * @param endDate           生单结束时间
     */
    @RequestMapping("/exportprojectsummariz")
    public ModelAndView exportProjectSummariz(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String contractCode,
                                  @RequestParam(required = false) String customerName,
                                  @RequestParam(required = false) String customerMobile,
                                  @RequestParam(required = false) String orderFlowStatus,
                                  @RequestParam(required = false) String stratDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(required = false) String contractCodeMany) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "customerName", customerName);
        MapUtils.putNotNull(paramMap, "customerMobile", customerMobile);
        MapUtils.putNotNull(paramMap, "orderFlowStatus", orderFlowStatus);
        MapUtils.putNotNull(paramMap, "stratDate", stratDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);
        if(StringUtils.isNotBlank(contractCodeMany)) {
            String[] stringsArr = contractCodeMany.split(",");
            List<String> strings = Arrays.asList(stringsArr);
            MapUtils.putNotNull(paramMap, "contractCodeMany", strings);
        }
        UploadCategory uploadCategory = UploadCategory.EXCLE;
        String basePath = PropertyHolder.getUploadBaseUrl();
        String fileName = "项目汇总统计.xls";
        String fileFullPath = FileUtils.saveFilePath(uploadCategory, basePath, fileName);
        List<Map> list = this.finaAnalyzeService.exportProjectSummariz(paramMap);
        LinkedHashMap<String, String> headerMapper = this.finaAnalyzeService.getMapData(list,storeCode);
        ExportSingleSheetHelper exportSingleSheetHelper = new ExportSingleSheetHelper(fileFullPath, headerMapper, list);
        exportSingleSheetHelper.build();
        ViewDownLoad viewDownLoad = new ViewDownLoad(new File(fileFullPath),null);
        return new ModelAndView(viewDownLoad);
    }

    /**
     * 综合数据 ----导出
     * @param keyword           关键字
     * @param contractCode      项目编号
     * @param customerName      客户姓名
     * @param secondContact     第二联系人姓名
     * @param designer          设计师
     * @param serviceName       客服姓名
     * @param serviceMobile     客服电话
     * @param paystartTime      收款开始时间
     * @param payendTime        收款结束时间
     * @param templateStageId   财务阶段 Id
     * @param orderFlowStatus   订单流转状态
     * @param paymethodName     收款方式
     * @param stageFinished     是否签订金合同
     * @return
     */
    @RequestMapping("/exportprojectcomposite")
    public ModelAndView exportProjectComposite(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String contractCode,
                               @RequestParam(required = false) String customerName,
                               @RequestParam(required = false) String secondContact,
                               @RequestParam(required = false) String designer,
                               @RequestParam(required = false) String serviceName,
                               @RequestParam(required = false) String serviceMobile,
                               @RequestParam(required = false) String paystartTime,
                               @RequestParam(required = false) String payendTime,
                               @RequestParam(required = false) String templateStageId,
                               @RequestParam(required = false) String orderFlowStatus,
                               @RequestParam(required = false) String paymethodName,
                               @RequestParam(required = false) String stageFinished){
        Map<String,Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "payendTime", payendTime);
        MapUtils.putNotNull(paramMap,"keyword",keyword);
        MapUtils.putNotNull(paramMap,"contractCode",contractCode);
        MapUtils.putNotNull(paramMap,"paystartTime",paystartTime);
        MapUtils.putNotNull(paramMap,"payendTime",payendTime);
        MapUtils.putNotNull(paramMap,"customerName",customerName);
        MapUtils.putNotNull(paramMap,"secondContact",secondContact);
        MapUtils.putNotNull(paramMap,"serviceName",serviceName);
        MapUtils.putNotNull(paramMap,"serviceMobile",serviceMobile);
        MapUtils.putNotNull(paramMap,"orderFlowStatus",orderFlowStatus);
        MapUtils.putNotNull(paramMap,"stageFinished",stageFinished);
        MapUtils.putNotNull(paramMap,"paymethodName",paymethodName);
        MapUtils.putNotNull(paramMap,"templateStageId",templateStageId);
        MapUtils.putNotNull(paramMap,"designer",designer);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);

        String fileFullPath = this.finaAnalyzeService.exportProjectComposite(paramMap);

        ViewDownLoad viewDownLoad = new ViewDownLoad(new File(fileFullPath),null);
        return new ModelAndView(viewDownLoad);
    }

    /**
     * 交款记录 ----导出
     * @param keyword           关键字
     * @param paystartTime      收款开始时间
     * @param payendTime        收款结束时间
     * @param contractCode      项目编号
     * @param payerName         交款人姓名
     * @param payerMobile       交款人电话
     * @param ifRcw             是否红冲
     * @param paymethodName     交款方式
     * @param templateStageId   财务阶段 Id
     * @return
     */
    @RequestMapping("/exportpaymoneyrecord")
    public ModelAndView exportPaymoneyRecord(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) String contractCode,
                                     @RequestParam(required = false) String payerName,
                                     @RequestParam(required = false) String payerMobile,
                                     @RequestParam(required = false) String receiptNum,
                                     @RequestParam(required = false) String paystartTime,
                                     @RequestParam(required = false) String payendTime,
                                     @RequestParam(required = false) String paymethodName,
                                     @RequestParam(required = false) String ifRcw,
                                     @RequestParam(required = false) String templateStageId){
        Map<String,Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap,"keyword",keyword);
        MapUtils.putNotNull(paramMap,"paystartTime",paystartTime);
        MapUtils.putNotNull(paramMap, "payendTime", payendTime);
        MapUtils.putNotNull(paramMap,"contractCode",contractCode);
        MapUtils.putNotNull(paramMap,"payerName",payerName);
        MapUtils.putNotNull(paramMap,"receiptNum",receiptNum);
        MapUtils.putNotNull(paramMap,"payerMobile",payerMobile);
        MapUtils.putNotNull(paramMap,"ifRcw",ifRcw);
        MapUtils.putNotNull(paramMap,"paymethodName",paymethodName);
        MapUtils.putNotNull(paramMap,"templateStageId",templateStageId);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);

        String fileFullPath= this.finaAnalyzeService.exportPaymoneyRecord(paramMap);

        ViewDownLoad viewDownLoad = new ViewDownLoad(new File(fileFullPath),null);
        return new ModelAndView(viewDownLoad);
    }
}

