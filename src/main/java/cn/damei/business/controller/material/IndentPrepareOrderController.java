package cn.damei.business.controller.material;

import cn.damei.business.entity.material.IndentPrepareOrder;
import cn.damei.business.service.material.IndentPrepareOrderService;
import cn.damei.core.base.controller.BaseComController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/prepareorder")
public class IndentPrepareOrderController extends BaseComController<IndentPrepareOrderService, IndentPrepareOrder> {


}
