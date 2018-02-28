package cn.mdni.business.controller.finance;

import cn.mdni.business.entity.finance.ReparationMoney;
import cn.mdni.business.service.finance.ReparationMoneyService;
import cn.mdni.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 赔款
 * @Company: 美得你智装科技有限公司
 * @Author zhangh
 * @Date: 2017/11/24
 */
@RestController
@RequestMapping("/finance/reparationmoney")
public class ReparationMoneyController {

    @Autowired
    private ReparationMoneyService reparationMoneyService;


    /**
     * 查询项目当前的阶段信息,用于展示赔款
     * @param contractUuid 项目的uuid
     * @return
     */
    @RequestMapping("/findprojectstage")
    public Object projectInformation(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(reparationMoneyService.findProjectStage(contractUuid));
    }

    /**
     * 新增赔款
     * @param reparationMoney
     * @return
     */
    @RequestMapping("/save")
    public Object insert(@RequestBody ReparationMoney reparationMoney){
        reparationMoneyService.insertReparationMoney(reparationMoney);
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }
}
