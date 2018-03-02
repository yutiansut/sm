package cn.damei.business.controller.material;

import cn.damei.business.constants.CustomerContractEnum;
import cn.damei.business.entity.material.ProdCatalog;
import cn.damei.business.service.material.ProdCatalogService;
import cn.damei.core.base.controller.BaseComController;
import cn.damei.core.dto.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/prodcatalog")
public class ProdCatalogController extends BaseComController<ProdCatalogService, ProdCatalog> {

    @Autowired
    private ProdCatalogService prodCatalogService;


    /**
     * 查询所有可用的 一级和其二级子分类;
     *  同时: 1.去查询该合同编码下 每个二级分类对应的 项目主材sku集合
     *        2.每个项目主材sku下面对应的 sku用量信息集合
     * @param url 商品一级分类url
     * @param contractCode  合同编码
     * @param categoryCode  选材类型（套餐标配、增项、减项等）枚举
     * @return
     */
    @RequestMapping("/findtwostagewithpromaterial")
    public Object findTwoStageWithProMaterialData(String url, String contractCode, String categoryCode,
                                                  String pageType){
        if(StringUtils.isAnyBlank(contractCode, pageType)){
            return StatusDto.buildFailureStatusDto();
        }
        return StatusDto.buildDataSuccessStatusDto(prodCatalogService.findTwoStageWithProMaterialData(url, contractCode, categoryCode, pageType));
    }

    /**
     * 查询一级分类并且含有二级分类的集合
     * @return
     */
    @RequestMapping("/findfirsthaschildren")
    public Object findFirstHasChildren(){
        return StatusDto.buildDataSuccessStatusDto(prodCatalogService.findFirstHasChildren());
    }

    /**
     * 查询所有可用的 一级和其二级子分类;
     *  同时: 1.去查询该合同编码下 每个二级分类对应的 项目主材sku集合
     *        2.每个项目主材sku下面对应的 sku用量信息集合
     * @param url 商品一级分类url
     * @param contractCode  合同编码
     * @param categoryCode  选材类型（套餐标配、增项、减项等）枚举
     * @return
     */
    @RequestMapping("/materialpreview")
    public Object materialPreview(String url, String contractCode, String categoryCode,
                                  String pageType, @RequestParam CustomerContractEnum contractStatus){
        if(StringUtils.isAnyBlank(contractCode, pageType)){
            return StatusDto.buildFailureStatusDto();
        }
        if(contractStatus.equals(CustomerContractEnum.TRANSFER_COMPLETE)){
            pageType = "select";
        }
        return StatusDto.buildDataSuccessStatusDto(prodCatalogService.findTwoStageWithProMaterialData(url, contractCode, categoryCode, pageType));
    }

}
