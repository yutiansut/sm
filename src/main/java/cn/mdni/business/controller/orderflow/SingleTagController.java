package cn.mdni.business.controller.orderflow;


import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.orderflow.SingleTag;
import cn.mdni.business.service.orderflow.SingleTagService;
import cn.mdni.core.WebUtils;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.commons.collection.MapUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你sm  串单管理</dd>
 * <dd>@date：2017/11/14  11:25</dd>
 * <dd>@author：zhangh</dd>
 * </dl>
 */

@RestController
@RequestMapping("/material/singletag")
public class SingleTagController{

    @Autowired
    private SingleTagService singleTagService;

    /**
     * 串单列表
     * @param keyword
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list")
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit,
                         @RequestParam(defaultValue = "id") String orderColumn,
                         @RequestParam(defaultValue = "DESC") String orderSort) {
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);
        String storeCode = WebUtils.getLoginedUserMainStore();
        params.put("storeCode", storeCode);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(singleTagService.searchScrollPage(params));
    }

    /**
     * 根据id回显
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}/getsingletagbyid")
    public Object getSingleTagById(@PathVariable Long id) {
        SingleTag singleTag = singleTagService.getById(id);

        return StatusDto.buildDataSuccessStatusDto(singleTag);
    }
    /**
     * 新增/修改
     * @param entity
     * @return
     */
    @RequestMapping(value = "/saveorupdate", method = RequestMethod.POST)
    public Object saveOrUpdate(@RequestBody SingleTag entity) {

        if (entity.getId() != null && entity.getId() > 0) {
            singleTagService.update(entity);
        } else {
            singleTagService.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     * 根据id查询项目信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getprojectmanagebyid")
    public Object getProjectManageById(@RequestParam Long id) {
        String storeCode = WebUtils.getLoginedUserMainStore();
        List<SingleTag> projectManageList = singleTagService.getProjectManageById(id,storeCode);

        return StatusDto.buildDataSuccessStatusDto(projectManageList);
    }

    /**
     * 根据contractCode查询项目信息
     * @param contractCode
     * @return
     */
    @RequestMapping(value = "/getprojectmanagebycode")
    public Object getProjectManageById(@RequestParam String contractCode) {
        String storeCode = WebUtils.getLoginedUserMainStore();
        List<SingleTag> projectManageList = singleTagService.getProjectManageByCode(contractCode,storeCode);

        return StatusDto.buildDataSuccessStatusDto(projectManageList);
    }

    /**
     * 移除串单信息
     * @param contractCode
     * @return
     */
    @RequestMapping(value = "/remove")
    public Object removeSingleTag(@RequestParam String contractCode) {
        int i = singleTagService.removeSingleTag(contractCode);
        if(i > 0){
            return StatusDto.buildDataSuccessStatusDto("移除成功");
        }else{
            return StatusDto.buildFailureStatusDto("移除失败");
        }
    }

    /**
     * 串单
     * @param contractCode
     * @return
     */
    @RequestMapping(value = "/singlestring")
    public Object singleString(@RequestParam String contractCode,@RequestParam Long id) {
        int i = singleTagService.singleString(contractCode,id);
        if(i > 0){
            return StatusDto.buildDataSuccessStatusDto("串单成功");
        }else{
            return StatusDto.buildFailureStatusDto("串单失败");
        }

    }
}
