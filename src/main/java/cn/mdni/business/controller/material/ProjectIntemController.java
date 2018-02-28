package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.material.ProjectIntem;
import cn.mdni.business.entity.material.ProjectIntemType;
import cn.mdni.business.service.material.ProjectIntemService;
import cn.mdni.business.service.material.ProjectIntemTypeService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘铎 on 2017/11/7.
 */
@RestController
@RequestMapping("/material/projectintem")
public class ProjectIntemController extends BaseController{

    @Autowired
    private ProjectIntemTypeService projectIntemTypeService;
    @Autowired
    private ProjectIntemService projectIntemService;

    /**
     * 定额分类列表
     * @param projectIntemTypeId 分类id
     * @param keyword 定额名称
     * @return
     */
    @RequestMapping("/list")
    public Object projectIntemList(@RequestParam(required = false) Long projectIntemTypeId,
                                   @RequestParam(required = false) String projectIntemMold,
                                   @RequestParam(required = false) String subordinateCategory,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String paymentTime,
                                   @RequestParam(defaultValue = "0") Integer offset,
                                   @RequestParam(defaultValue = "20") Integer limit,
                                   @RequestParam(defaultValue = "id") String orderColumn,
                                   @RequestParam(defaultValue = "ASC") String orderSort){

        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "projectIntemTypeId", projectIntemTypeId);
        MapUtils.putNotNull(params, "projectIntemMold", projectIntemMold);
        MapUtils.putNotNull(params, "subordinateCategory", subordinateCategory);
        MapUtils.putNotNull(params, "date", paymentTime);
        MapUtils.putNotNull(params, "storeCode", WebUtils.getLoginedUserMainStore());
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        BootstrapPage<ProjectIntem> projectIntemBootstrapPage = projectIntemService.searchScrollPage(params);
        return StatusDto.buildDataSuccessStatusDto(projectIntemBootstrapPage);
    }

    /**
     * 根据类型查询分类
     */
    @RequestMapping("/getbytype")
    public Object getProjectIntemByType(){
        List<ProjectIntemType>  projectIntemList =  projectIntemTypeService.getProjectIntemType();
        return StatusDto.buildDataSuccessStatusDto(projectIntemList);
    }
}
