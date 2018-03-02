package cn.damei.business.controller.material;

import cn.damei.business.service.material.SmSkuChangeDosageService;
import cn.damei.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/smskuchangedosage")
public class SmSkuChangeDosageController extends BaseController {

    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;

    @RequestMapping(value = "/delete/{id}")
    public Object deleteById(@PathVariable String id) {
        return smSkuChangeDosageService.deleteOrUpdate(id);
    }

}
