package cn.damei.business.controller.material;

import cn.damei.business.entity.material.PrepareOrderAbnormalLog;
import cn.damei.business.service.material.PrepareOrderAbnormalLogService;
import cn.damei.core.base.controller.BaseComController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/prepareorderabnormallog")
public class PrepareOrderAbnormalLogController extends BaseComController<PrepareOrderAbnormalLogService, PrepareOrderAbnormalLog> {


}
