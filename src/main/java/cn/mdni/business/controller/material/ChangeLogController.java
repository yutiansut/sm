package cn.mdni.business.controller.material;

import cn.mdni.business.entity.material.ChangeLog;
import cn.mdni.business.service.material.ChangeLogService;
import cn.mdni.core.base.controller.BaseComController;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/11/23.
 */
@RestController
@RequestMapping("/material/changelog")
public class ChangeLogController extends BaseComController<ChangeLogService,ChangeLog>{

    @RequestMapping("/findchangeversionno")
    public Object findChangVersionNo(@RequestParam(required = false) String contractCode){
        return StatusDto.buildDataSuccessStatusDto(this.service.findchangeVersionNoByContractNo(contractCode));
    }

    @RequestMapping("/findchangehistory")
    public Object findChangeHistory(@RequestParam(required = false) String changeVersionNo){
        List<ChangeLog> changeLogList = this.service.findChangeLogByChNo(changeVersionNo);
        Map<String,Object> map = Maps.newHashMap();
        map.put("data",changeLogList);
        return StatusDto.buildDataSuccessStatusDto(map);
    }
}
