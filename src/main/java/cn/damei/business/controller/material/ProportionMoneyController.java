package cn.damei.business.controller.material;

import cn.damei.business.service.material.ProportionMoneyService;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/proportionmoney")
public class ProportionMoneyController extends BaseController{
    @Autowired
    private ProportionMoneyService service;

    @RequestMapping("/getbycontractcode/{contractCode}")
    public Object getByContractCode(@PathVariable("contractCode") String contractCode){
        return StatusDto.buildDataSuccessStatusDto(service.getByContractCode(contractCode)) ;
    }
}
