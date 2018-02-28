package cn.mdni.business.controller.finance;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.finance.PaymoneyRecord;
import cn.mdni.business.service.finance.FinaPrintService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * @Description: 财务打印Controller
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/12/04.
 */
@RestController
@RequestMapping("/finance/print")
public class PrintController {

    @Autowired
    private FinaPrintService finaPrintService;

    /**
     * 收据查询
     * @param keyword
     * @param paystartTime
     * @param payendTime
     * @param contractCode
     * @param secondContact
     * @param secondContactMobile
     * @param creator
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping("/findreceiptall")
    public Object findReceiptAll(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String paystartTime,
                         @RequestParam(required = false) String payendTime,
                         @RequestParam(required = false) String contractCode,
                         @RequestParam(required = false) String secondContact,
                         @RequestParam(required = false) String secondContactMobile,
                         @RequestParam(required = false) String creator,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit){
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "payendTime", payendTime);
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "paystartTime", paystartTime);
        MapUtils.putNotNull(paramMap, "secondContact", secondContact);
        MapUtils.putNotNull(paramMap, "secondContactMobile", secondContactMobile);
        MapUtils.putNotNull(paramMap, "creator", creator);
        String storeCode = WebUtils.getLoginedUserMainStore();
        MapUtils.putNotNull(paramMap, "storeCode", storeCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        BootstrapPage<PaymoneyRecord> findReceiptList = this.finaPrintService.findReceiptAll(paramMap);
        return StatusDto.buildDataSuccessStatusDto(findReceiptList);
    }

    /**
     * 收据打印 -- 单个
     * @param id
     * @return
     */
    @RequestMapping("/singleprint")
    public Object onePrint(@RequestParam Long id,HttpServletRequest res) throws IOException {
        String templatePdfFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/dj.pdf";
        String imgerFullFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/home.png";
        String fileFullPath = this.finaPrintService.pdfTemplate(id,templatePdfFile);
        if(fileFullPath != null){
            String addWater = this.finaPrintService.addWater(imgerFullFile, fileFullPath);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            return new ResponseEntity<byte[]>(readFileToByteArray(new File(addWater)), httpHeaders,
                    HttpStatus.OK);
        }
        return StatusDto.buildFailureStatusDto("打印失败");
    }

    /**
     * 批量打印
     * @param ids
     * @return
     */
    @RequestMapping("/multiprint")
    public Object manyPrint(@RequestParam String ids, HttpServletRequest res) throws IOException {
        String templatePdfFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/dj.pdf";
        String imgerFullFile = res.getSession().getServletContext().getRealPath("/")+"/static/business/template/home.png";
        String fileFullPath = this.finaPrintService.manyPdfTemplate(ids, templatePdfFile);
        if(fileFullPath != null){
            String addWater = this.finaPrintService.addWater(imgerFullFile, fileFullPath);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            return new ResponseEntity<byte[]>(readFileToByteArray(new File(addWater)), httpHeaders,
                    HttpStatus.OK);
        }
        return StatusDto.buildFailureStatusDto("打印失败");
    }

    /**
     * 没有获取到门店信息
     * @param storeCode
     * @return
     */
    @RequestMapping("/getstorecode/{storeCode}")
    public Object getStoreCode(@PathVariable String storeCode) {
        if (storeCode != null) {
            String[] storeCodeArr = storeCode.split(",");
            if (storeCodeArr[0] != null) {
                return  StatusDto.buildDataSuccessStatusDto(this.finaPrintService.getStoreCode(storeCodeArr[0]));
            }
        }
        return StatusDto.buildFailureStatusDto("没有获取到门店信息");
    }
}
