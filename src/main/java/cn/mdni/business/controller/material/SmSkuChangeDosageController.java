package cn.mdni.business.controller.material;

import cn.mdni.business.service.material.SmSkuChangeDosageService;
import cn.mdni.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更添加用量 </dd>
 * <dd>@date：2017/11/6  17:33</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("/material/smskuchangedosage")
public class SmSkuChangeDosageController extends BaseController {

    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;

    /**
     * @param id
     * @Description: 美得你智装 变更 的用量删除（ 原来有用量删除 -》修改 后添加的用量删除）
     * @date: 2017/11/21  14:46
     * @author: Ryze
     */
    @RequestMapping(value = "/delete/{id}")
    public Object deleteById(@PathVariable String id) {
        return smSkuChangeDosageService.deleteOrUpdate(id);
    }

}
