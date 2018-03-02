package cn.damei.business.controller.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.damei.business.service.material.ProdSkuPriceService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.ShiroUser;
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

@RestController
@RequestMapping("/material/prodskuprice")
public class ProdSkuPriceController extends BaseController {
    @Autowired
    private ProdSkuPriceService service;

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
    @RequestMapping(value = "{id}/del")
    public Object delete(@PathVariable Long id) {
        service.deleteById(id);
        return StatusDto.buildSuccessStatusDto("删除操作成功！");
    }

    /**
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

    @RequestMapping(value = "/{id}/downlabel/{type}")
    public void downLabel(@PathVariable("id") Long id,@PathVariable("type") String type, HttpServletResponse resp,HttpServletRequest httpServletRequest) {
        service.downLabel(id,type,resp,httpServletRequest);
    }



}
