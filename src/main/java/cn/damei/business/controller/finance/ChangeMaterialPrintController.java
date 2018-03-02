package cn.damei.business.controller.finance;

import cn.damei.business.constants.Constants;
import cn.damei.business.service.finance.ChangeMaterialPrintService;
import cn.damei.business.service.material.SmChangeDetailService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/finance/changematerialprint")
public class ChangeMaterialPrintController{

    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private ChangeMaterialPrintService changeMaterialPrintService;

    @RequestMapping("/changemateriallist")
    public Object changeMaterialList(@RequestParam(required = false) String keyWord,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     @RequestParam(required = false) String printCount,
                                     @RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id") String paymoneyRecordColumn,
                                     @RequestParam(defaultValue = "DESC") String orderSort){
        Map<String,Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params,"keyword",keyWord);
        MapUtils.putNotNull(params,"startDate",startDate);
        MapUtils.putNotNull(params,"endDate",endDate);
        if(StringUtils.isNotBlank(printCount)){
            MapUtils.putNotNull(params,"printCount", Integer.parseInt(printCount));
        }
        MapUtils.putNotNull(params,"storeCode", WebUtils.getLoginedUserMainStore());
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), paymoneyRecordColumn));
        return StatusDto.buildDataSuccessStatusDto(smChangeDetailService.changeMaterialList(params));
    }

    @RequestMapping(value = "/materialchangeprint")
    public Object onePrint(Long[] ids, Boolean isPrint, HttpServletRequest res){
        if(ids == null || ids.length == 0){
            return StatusDto.buildFailureStatusDto("参数id不能为空!");
        }
        return changeMaterialPrintService.viewOrPrint(ids, isPrint,res);

    }
}
