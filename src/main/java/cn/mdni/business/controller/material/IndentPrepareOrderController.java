package cn.mdni.business.controller.material;

import cn.mdni.business.entity.material.IndentPrepareOrder;
import cn.mdni.business.service.material.IndentPrepareOrderService;
import cn.mdni.core.base.controller.BaseComController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 订货预备单controller
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 19:01.
 */
@RestController
@RequestMapping("/material/prepareorder")
public class IndentPrepareOrderController extends BaseComController<IndentPrepareOrderService, IndentPrepareOrder> {


}
