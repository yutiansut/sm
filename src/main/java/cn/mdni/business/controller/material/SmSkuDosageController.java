package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.business.constants.PriceTypeEnum;
import cn.mdni.business.constants.PricingMethodEnum;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.business.service.material.SmSkuChangeDosageService;
import cn.mdni.business.service.material.SmSkuDosageService;
import cn.mdni.commons.bean.BeanUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材添加用量 </dd>
 * <dd>@date：2017/11/6  17:33</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@RestController
@RequestMapping("/material/smskudosage")
public class SmSkuDosageController extends BaseController{
    @Autowired
    private SmSkuDosageService service;
    @Autowired
    private SmSkuChangeDosageService smSkuChangeDosageService;

    /**
     * 根据1级分类获取功能区列表
     *
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:37
     */
    @RequestMapping("/domainlist/{catalogUrl}")
    public Object domainList(@PathVariable("catalogUrl") String catalogUrl) {
        return StatusDto.buildDataSuccessStatusDto(service.findDomainList(catalogUrl));
    }

    /**
     * 根据2级分类获取功能区列表
     *
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:37
     */
    @RequestMapping("/convertunit/{catalogUrl}")
    public Object convertUnitList(@PathVariable("catalogUrl") String catalogUrl) {
        return StatusDto.buildDataSuccessStatusDto(service.findConvertUnitList(catalogUrl));
    }

    /**
     * 根据2级分类获取耗损系数
     *
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 17:28:37
     */
    @RequestMapping("/getlossfactor/{catalogUrl}")
    public Object getLossFactor(@PathVariable("catalogUrl") String catalogUrl) {
        return StatusDto.buildDataSuccessStatusDto(service.getLossFactor(catalogUrl));
    }

    /**
     * 根据2级分类获取是否可以输入小数
     *
     * @param catalogUrl 分类
     * @author Ryze
     * @date 2017-11-2 15点55分
     */
    @RequestMapping("/getflg/{catalogUrl}")
    public Object getflg(@PathVariable("catalogUrl") String catalogUrl) {
        String getflg = service.getflg(catalogUrl);
        String s = "0";
        if (getflg != null && s.equals(getflg)) {
            return StatusDto.buildDataSuccessStatusDto(false);
        } else {
            return StatusDto.buildDataSuccessStatusDto(true);
        }

    }

    /**
     * 增加或修改
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/save")
    public Object saveOrUpdate(@RequestBody SmSkuDosage entity) {
        Date date = new Date();
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String jobNum = loggedUser.getOrgCode();
        String name = loggedUser.getName();
        String pageType = entity.getPageType();
        if (StringUtils.isNotBlank(entity.getId())) {
            entity.setUpdateTime(date);
            entity.setUpdateUser(name + "(" + jobNum + ")");
            //选材
            if (Constants.PAGE_TYPE_SELECT.equals(pageType)) {
                service.update(entity);
            }
        } else {
            //组装数据
            entity.setCreateTime(date);
            entity.setCreateUser(name + "(" + jobNum + ")");
            String skuCode = entity.getSkuCode();
            if (StringUtils.isBlank(skuCode)) {
                return StatusDto.buildFailureStatusDto("skuCode 不存在");
            }
            Map<String, Object> params = Maps.newHashMap();
            params.put("priceType", PriceTypeEnum.STORE.toString());
            params.put("skuCode", skuCode);
            params.put("date", entity.getDate());
            entity.setStorePurchasePrice(service.getPriceBySkuCode(params));
            params.put("priceType", PriceTypeEnum.SALE.toString());
            entity.setStoreSalePrice(service.getPriceBySkuCode(params));
            String priceType = entity.getPriceType();
            BigDecimal price = entity.getPrice();
            switch (priceType) {
                case "UPGRADE":
                    entity.setStoreUpgradeDifferencePrice(price);
                    break;
                case "SALE":
                    entity.setStorePurchasePrice(price);
                    break;
                case "INCREASED":
                    entity.setStoreIncreasePrice(price);
                    break;
                case "MINUS":
                    entity.setStoreReducePrice(price);
                    break;
            }
            //固定单价 计价方式
            entity.setDosagePricingMode(PricingMethodEnum.fixedUnitPrice.toString());
            //选材
            if (Constants.PAGE_TYPE_SELECT.equals(pageType)) {
                entity.setId(UUID.randomUUID().toString());
                service.insert(entity);
                //变更
            } else if (Constants.PAGE_TYPE_CHANGE.equals(pageType)) {
                Map<String, Object> stringObjectMap = BeanUtils.beanTransMap(entity, false);
                SmSkuChangeDosage entity1 = BeanUtils.mapTransBean(stringObjectMap, SmSkuChangeDosage.class);
                entity1.setId(UUID.randomUUID().toString());
                smSkuChangeDosageService.insert(entity1);
                entity.setId(entity1.getId());
            }

        }
        return StatusDto.buildDataSuccessStatusDto(entity);
    }

    /**
     * @param contractCode
     * @Description: 美得你智装  根据合同编号 统计金额
     * @date: 2017/11/10  16:06
     * @author: Ryze
     */
    @RequestMapping(value = "/statisticsamount/{contractCode}/{pageType}")
    public Object findDosageByContractCodeList(@PathVariable("contractCode") String contractCode, @PathVariable("pageType") String pageType) {
        try {
            if (Constants.PAGE_TYPE_SELECT.equals(pageType)) {
                return StatusDto.buildDataSuccessStatusDto(service.findDosageByContractCodeList(contractCode));
            } else {
                return StatusDto.buildDataSuccessStatusDto(smSkuChangeDosageService.findDosageByContractCodeList(contractCode));
            }
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto("统计错误");
        }

    }

    @RequestMapping("/getSkuDoSageByContractCode")
    public Object getSkuDoSageByContractCode(String contractCode) {
        List<SmSkuDosage> skuDosages = this.service.getByContractCode(contractCode);
        return StatusDto.buildDataSuccessStatusDto(skuDosages);
    }

    /**
     * @param contractCode
     * @Description: 美得你智装  根据合同编号 统计金额
     * @date: 2017/11/10  16:06
     * @author: Ryze
     */
    @RequestMapping("/changepreview")
    public Object changePreview(@RequestParam String contractCode, @RequestParam CustomerContractEnum contractStatus) {
        try {
            if (contractStatus.equals(CustomerContractEnum.TRANSFER_COMPLETE)) {
                return StatusDto.buildDataSuccessStatusDto(smSkuChangeDosageService.findDosageByContractCodeList2(contractCode));
            } else {
                return StatusDto.buildDataSuccessStatusDto(smSkuChangeDosageService.findDosageByContractCodeList(contractCode));
            }
        } catch (Exception e) {
            return StatusDto.buildFailureStatusDto("统计错误");
        }

    }
    /**
     * @Description: 美得你智装 删除
     * @date: 2018/1/25  11:40
     * @param id
     * @author: Ryze
     */
    @RequestMapping(value = "{id}/del")
    public Object delete(@PathVariable String id) {
        service.deleteById(id);
        return StatusDto.buildSuccessStatusDto("删除操作成功！");
    }
}
