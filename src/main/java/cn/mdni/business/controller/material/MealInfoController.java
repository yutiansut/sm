package cn.mdni.business.controller.material;


import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.material.MealInfo;
import cn.mdni.business.entity.material.Store;
import cn.mdni.business.service.material.MealInfoService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你sm  套餐管理</dd>
 * <dd>@date：2017/11/2  11:25</dd>
 * <dd>@author：张晗</dd>
 * </dl>
 */

@RestController
@RequestMapping("/material/mealinfo")
public class MealInfoController extends BaseController {

    @Autowired
    private MealInfoService mealInfoService;

    /**
     * 套餐列表
     * @param keyword
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list")
    public Object search(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "sort") String orderColumn,
                         @RequestParam(defaultValue = "ASC") String orderSort) {
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);

        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        BootstrapPage<MealInfo> mealInfoBootstrapPage = mealInfoService.searchScrollPage(params);
        return StatusDto.buildDataSuccessStatusDto(mealInfoBootstrapPage);
    }
    /**
     * 套餐列表
     *
     * @return
     */
    @RequestMapping(value = "/findmealbystorecode")
    public Object findMealByStoreCode() {
        List<MealInfo> mealList = mealInfoService.findMealByStoreCode(WebUtils.getLoginedUserMainStore());
        return StatusDto.buildDataSuccessStatusDto(mealList);
    }
    /**
     * 根据id回显
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}/getmealinfobyid")
    public Object getMealInfoById(@PathVariable Long id) {
        MealInfo mealInfo = mealInfoService.getById(id);

        return StatusDto.buildDataSuccessStatusDto(mealInfo);
    }
    /**
     * 新增/修改
     * @param entity
     * @return
     */
    @RequestMapping(value = "/saveorupdate", method = RequestMethod.POST)
    public Object saveOrUpdate(@RequestBody MealInfo entity) {

        if (entity.getId() != null && entity.getId() > 0) {
            mealInfoService.update(entity);
        } else {
            mealInfoService.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }
    /**
     * 获取门店列表
     *
     * @return
     */
    @RequestMapping(value = "/getstorelist", method = RequestMethod.GET)
    public Object getStoreList() {
        List<Store> storeList = mealInfoService.getStoreList();
        return StatusDto.buildDataSuccessStatusDto(storeList);
    }
    /**
     * 关闭-开启
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}/changestatus", method = RequestMethod.GET)
    public Object changeStatus(@PathVariable Long id){
        MealInfo entity = mealInfoService.getById(id);
        mealInfoService.changeStatus(entity);
        return StatusDto.buildSuccessStatusDto();
    }
}
