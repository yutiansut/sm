package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.mdni.business.service.material.ProdSkuPriceService;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你智装 </dd>
 * <dd>@date：2017/11/2  17:25</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("/material/prodskuprice")
public class ProdSkuPriceController extends BaseController {
    @Autowired
    private ProdSkuPriceService service;
    /**
     * 获取不同sku的不同类型价格列表
     *
     * @param priceType 价格类型
     * @author Ryze
     * @date 2017-11-2 17:28:37
     */

    @RequestMapping("/listbytype")
    public Object listByType(@RequestParam(required = false) String priceType,
                             @RequestParam(required = false) Integer skuId,
                             @RequestParam(defaultValue = "0") Integer offset,
                             @RequestParam(defaultValue = "20") Integer limit,
                             @RequestParam(defaultValue = "id") String orderColumn,
                             @RequestParam(defaultValue = "ASC") String orderSort) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("priceType", priceType);
        params.put("skuId", skuId);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
    }
    /**
     * @Description: 美得你智装 删除
     * @date: 2018/1/25  11:28
     * @param id
     * @author: Ryze
     */
    @RequestMapping(value = "{id}/del")
    public Object delete(@PathVariable Long id) {
        service.deleteById(id);
        return StatusDto.buildSuccessStatusDto("删除操作成功！");
    }

    /**
     * @author Ryze
     * @date 2017/11/3 17:28
     * @description
     * @param
     * @return
     */
    @RequestMapping(value = "/save")
    public Object saveOrUpdate(ProdSkuPrice entity) {
        //验重 根据日期
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        entity.setEditor(loggedUser.getId());
        entity.setEditTime(new Date());
        Map<String, Object> params = Maps.newHashMap();
        params.put("priceType", entity.getPriceType());
        params.put("skuId", entity.getSkuId());
        List<ProdSkuPrice> search = service.findByTypeAndSkuId(params);
        Long id = entity.getId();
        Date priceStartDate = entity.getPriceStartDate();
        boolean b = search.stream().filter(a -> !a.getId().equals(id)).anyMatch(a -> a.getPriceStartDate().equals(priceStartDate));
        if (b) {
            return StatusDto.buildFailureStatusDto("日期重复");
        }
        if (id != null && id > 0) {
            service.update(entity);
        } else {
            service.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }

    /**
     * @Description: 美得你智装 下载单个标签
     * @date: 2018/1/4  10:49
     * @param id sku id
     * @param type 价格类型
     * @author: Ryze
     */
    @RequestMapping(value = "/{id}/downlabel/{type}")
    public void downLabel(@PathVariable("id") Long id,@PathVariable("type") String type, HttpServletResponse resp,HttpServletRequest httpServletRequest) {
        service.downLabel(id,type,resp,httpServletRequest);
    }



}
