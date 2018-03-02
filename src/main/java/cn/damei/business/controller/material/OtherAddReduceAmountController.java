package cn.damei.business.controller.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.OtherAddReduceAmount;
import cn.damei.business.service.material.CustomerContractService;
import cn.damei.business.service.material.OtherAddRecuceAmountService;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/material/otheraddreduceamount")
public class OtherAddReduceAmountController extends BaseController{
   @Autowired
   private CustomerContractService customerContractService;
   @Autowired
   private OtherAddRecuceAmountService otherAddRecuceAmountService;

    @RequestMapping("/save")
    public Object saveOtherAddReduceAmount(OtherAddReduceAmount otherAddReduceAmount){
        if (otherAddReduceAmount.getPageType().equals(Constants.PAGE_TYPE_CHANGE)) {
            otherAddReduceAmount.setChangeFlag("1");
        }
        otherAddRecuceAmountService.insert(otherAddReduceAmount);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmount);
    }

    @RequestMapping("/findlistbycontractcode")
    public Object findByContractCodeList(@RequestParam(required = false) String contractCode,
                                         @RequestParam(required = false) String pageType){
        Map<String,Object> map = Maps.newHashMap();
        map.put("contractCode",contractCode);
        if (pageType.equals(Constants.PAGE_TYPE_SELECT)) {
            map.put("changeFlag","0");
        }else{
            map.put("changeFlag","");
        }
        List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.findByContractCodeList(map);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }
    @RequestMapping("/getChangeByContractCode/{contractCode}")
    public Object getChangeByContractCode(@PathVariable("contractCode") String contractCode){
        List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.getChangeByContractCode(contractCode);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }
    @RequestMapping("/findOtherAddReduceAmountOnlyByContrCode/{changevision}/{contractCode}")
    public Object findOtherAddReduceAmountOnlyByContrCode(@PathVariable("changevision") String changevision,@PathVariable("contractCode") String contractCode){
        List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.findOtherAddReduceAmountOnlyBychangevision(changevision,contractCode);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }

    @RequestMapping("/findotheraddreduceamount")
    public Object findOtherAddReduceAmount(@RequestParam(required = false) String contractCode,
                                           @RequestParam(required = false) String changeNo){
        List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.findOtherAddReduceAmount(contractCode,changeNo);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }


    @RequestMapping("/getChangeBychangeVison/{changeVision}")
    public Object getChangeBychangeVison(@PathVariable("changeVision") String changeVision){
        String substring = changeVision.substring(0, changeVision.length() - 3);
        String substring1 = changeVision.substring(0, changeVision.length() - 5);
        List<OtherAddReduceAmount> otherAddReduceAmountList=null;
        //当前变更版本号 是的当前的变更的去查
        CustomerContract byCode = customerContractService.getByCode(substring1);
        if(byCode.getCurrentChangeVersion().equals(substring)) {
             otherAddReduceAmountList = otherAddRecuceAmountService.getChangeByContractCode(substring1);
        }
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }
}
