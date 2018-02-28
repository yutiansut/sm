package cn.mdni.business.controller.material;

import cn.mdni.business.entity.material.PrepareOrderAbnormalLog;
import cn.mdni.business.service.material.PrepareOrderAbnormalLogService;
import cn.mdni.core.base.controller.BaseComController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 选材数据转换预备订单异常日志 controller
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 19:01.
 */
@RestController
@RequestMapping("/material/prepareorderabnormallog")
public class PrepareOrderAbnormalLogController extends BaseComController<PrepareOrderAbnormalLogService, PrepareOrderAbnormalLog> {


}
