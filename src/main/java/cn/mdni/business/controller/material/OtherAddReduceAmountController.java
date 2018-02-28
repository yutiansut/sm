package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.material.OtherAddRecuceAmountService;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/11/13.
 */
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
    /**
     * @Description: 美得你智装 据合同编号获取其他金额增减列表
     * @date: 2017/11/17  18:42
     * @param contractCode
     * @author: Ryze
     */
    @RequestMapping("/getChangeByContractCode/{contractCode}")
    public Object getChangeByContractCode(@PathVariable("contractCode") String contractCode){
        List<OtherAddReduceAmount> otherAddReduceAmountList = otherAddRecuceAmountService.getChangeByContractCode(contractCode);
        return StatusDto.buildDataSuccessStatusDto(otherAddReduceAmountList);
    }
    /**
     * @Description: 美得你智装 据合同编号获取其他金额增减列表 差芒果
     * @date: 2017/11/17  18:42
     * @param contractCode
     * @author: Ryze
     */
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


    /**
     * @Description: 美得你智装 据便更号编号获取其他金额增减列表
     * @date: 2017/11/17  18:42
     * @param changeVision
     * @author: Ryze
     */
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
