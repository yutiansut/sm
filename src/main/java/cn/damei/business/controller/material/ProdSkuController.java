package cn.damei.business.controller.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.commodity.prodsku.ProdSku;
import cn.damei.business.service.material.ProdSkuService;
import cn.damei.business.service.material.ProdSkuPriceService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.view.ViewDownLoad;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseComController;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.BootstrapPage;
import cn.damei.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/material/prodsku")
public class ProdSkuController extends BaseController {
    @Autowired
    private ProdSkuService service;
    public static final int INT = 100;
    @Autowired
    private ProdSkuPriceService prodSkuPriceService;
    @RequestMapping("/list")
    public Object list(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String catalogUrl1,
                            @RequestParam(required = false) String catalogUrl2,
                            @RequestParam(required = false) Long supplierId,
                            @RequestParam(required = false) Long brandId,
                            @RequestParam(required = false) String allSupplierId,
                            @RequestParam(required = false) String allStoreCode,
                            @RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "20") Integer limit,
                            @RequestParam(defaultValue = "id") String orderColumn,
                            @RequestParam(defaultValue = "ASC") String orderSort) {
        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        MapUtils.putNotNull(params, "keyword", keyword);
        //有2级类目先2级类目
        pushCatalog(catalogUrl1, catalogUrl2, params);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        //加门店
        params.put("storeCode", WebUtils.getLoginedUserMainStore());
        List<ProdSku> pageData = Collections.emptyList();
        Long count = service.countSkuPriceTotal(params);
        if (count > 0) {
            pageData = service.findSkuPriceList(params);
        }
        BootstrapPage<ProdSku> productPage = new BootstrapPage(count, pageData);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    private void pushCatalog( String catalogUrl1, String catalogUrl2, Map<String, Object> params) {
        if(StringUtils.isNoneBlank(catalogUrl2)) {
            MapUtils.putNotNull(params, "catalogUrl", catalogUrl2);
        }else if (StringUtils.isNoneBlank(catalogUrl1)){
            MapUtils.putNotNull(params, "catalogUrl", catalogUrl1);
        }else{
            MapUtils.putNotNull(params, "catalogUrl",catalogUrl1);
        }
    }

    /**
     * sku列表获取了不同类型的价格查询
     * @param keyword    sku代码/sku名称
     * @param catalogUrl1 商品1级分类url
     * @param catalogUrl2 商品2级分类url
     * @param brandId    品牌Id
     * @param priceType    价格类型
     *
     * @date 2017-11-2 17:28:37
     */
    @RequestMapping("/pricetypelist")
    public Object findSkuPriceListByType(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String catalogUrl1,
                            @RequestParam(required = false) String catalogUrl2,
                            @RequestParam(required = false) Long brandId,
                            @RequestParam(required = true) String priceType,
                            @RequestParam(required = true) Date date,
                            @RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "20") Integer limit,
                            @RequestParam(defaultValue = "id") String orderColumn,
                            @RequestParam(defaultValue = "ASC") String orderSort) {
        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        MapUtils.putNotNull(params, "keyword", keyword);
        //有2级类目先2级类目
        pushCatalog(catalogUrl1, catalogUrl2, params);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "date",date );
        MapUtils.putNotNull(params, "priceType",priceType);
        //加门店
        params.put("storeCode", WebUtils.getLoginedUserMainStore());
        List<ProdSku> pageData = Collections.emptyList();
        Long count = service.countfindSkuPriceListByTypeTotal(params);
        if (count > 0) {
            pageData = service.findSkuPriceListByType(params);
        }
        BootstrapPage<ProdSku> productPage = new BootstrapPage(count, pageData);
        return StatusDto.buildDataSuccessStatusDto(productPage);
    }

    @RequestMapping("/storelist")
    public Object storeList(){
        return StatusDto.buildDataSuccessStatusDto(service.findStoreList());
    }
    /**
     * @date 2017/11/3 10:31
     * @description 根据门店编号获取区域供应商下拉框
     * @param   code 门店编号
     * @return
     */
    @RequestMapping("/findregionsupplierbystorecode/{code}")
    public Object findRegionSupplierByStoreCode(@PathVariable("code") String code){
        return StatusDto.buildDataSuccessStatusDto(service.findRegionSupplierByStoreCode(code));
    }
    @RequestMapping("/findsupplierbyregionid/{id}")
    public Object findSupplierByRegionId(@PathVariable("id") Integer id){
        return StatusDto.buildDataSuccessStatusDto(service.findSupplierByRegionId(id));
    }
    /**
     *
     * @date 2017/11/3 11:31
     * @description  获取品牌列表
     * @return
     */
    @RequestMapping("/findAllBrand")
    public Object findAllBrand(){
        return StatusDto.buildDataSuccessStatusDto(service.findAllBrand());
    }
    @RequestMapping("/findcatalogfirstlist")
    public Object findCatalogFirstList(){
        return StatusDto.buildDataSuccessStatusDto(service.findCatalogFirstList());
    }
    @RequestMapping("/findcataloglistbyurl/{url}")
    public Object findCatalogListByUrl(@PathVariable("url") String url){
        return StatusDto.buildDataSuccessStatusDto(service.findCatalogListByUrl(url));
    }


    @RequestMapping(value = "/downlabellist/{type}")
    public Object downLabel(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String catalogUrl1,
                          @RequestParam(required = false) String catalogUrl2,
                          @RequestParam(required = false) Long supplierId,
                          @RequestParam(required = false) Long brandId,
                          @RequestParam(required = false) String allSupplierId,
                          @RequestParam(required = false) String allStoreCode,
                          @PathVariable("type") String type, HttpServletResponse resp,HttpServletRequest request) {

        //参数组装
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);
        //有2级类目先2级类目
        pushCatalog(catalogUrl1, catalogUrl2, params);
        MapUtils.putNotNull(params, "supplierId", supplierId);
        MapUtils.putNotNull(params, "brandId", brandId);
        MapUtils.putNotNull(params, "allSupplierId", allSupplierId);
        MapUtils.putNotNull(params, "allStoreCode", allStoreCode);
        MapUtils.putNotNull(params, "type", type);
        //加门店
        params.put("storeCode", WebUtils.getLoginedUserMainStore());
        List<ProdSku> byIdAndType = service.findByIdAndType(params);
        if(byIdAndType==null || byIdAndType.size()==0){
            return  StatusDto.buildFailureStatusDto("没有查询结果");
        }else if(byIdAndType.size()> INT) {
            return  StatusDto.buildFailureStatusDto("超出上限，请分段下载");
        }
        String save=null;
       try {
           save = prodSkuPriceService.downLabelList(byIdAndType, type, resp, request);
        } catch (Exception e) {
            StatusDto.buildFailureStatusDto("删除文件异常");
        }
        return   StatusDto.buildDataSuccessStatusDto(save);
    }
    @RequestMapping(value = "/fileDown")
    public ModelAndView fileDown(String path){
        return new ModelAndView(new ViewDownLoad(new File(path), null));
    }
}
