package cn.mdni.business.controller.material;

import cn.mdni.business.service.material.ProportionMoneyService;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材添加用量 </dd>
 * <dd>@date：2017/11/6  17:33</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("/material/proportionmoney")
public class ProportionMoneyController extends BaseController{
    @Autowired
    private ProportionMoneyService service;

    /**
     * @Description: 美得你智装 根据合同编号查询金额
     * @date: 2017/12/13  18:45
     * @param contractCode
     * @author: Ryze
     */
    @RequestMapping("/getbycontractcode/{contractCode}")
    public Object getByContractCode(@PathVariable("contractCode") String contractCode){
        return StatusDto.buildDataSuccessStatusDto(service.getByContractCode(contractCode)) ;
    }
}
