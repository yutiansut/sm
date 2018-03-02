package cn.damei.business.controller.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.material.ConstructionChange;
import cn.damei.business.service.material.ConstructionChangeService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseComController;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.ShiroUser;
import com.google.common.collect.Maps;
import com.lowagie.text.DocumentException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/material/contractchange")
public class ConstructionChangeController  extends BaseComController<ConstructionChangeService, ConstructionChange> {

    @Autowired
    private ConstructionChangeService constructionChangeService;

    /**
     *  基装变更单列表
     * @param keyword               关键字
     * @param changeApplyStartDate  变更开始时间
     * @param changeApplyEndDate    变更结束时间
     * @param printCount            打印次数
     * @return
     */
    @RequestMapping("/changelist")
    public Object list(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) String changeApplyStartDate,
                                 @RequestParam(required = false) String changeApplyEndDate,
                                 @RequestParam(required = false) String printCount,
                                 @RequestParam(defaultValue = "0") int offset,
                                 @RequestParam(defaultValue = "20") int limit){
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if(loggedUser == null){
            return StatusDto.buildFailureStatusDto("回话失效,请重新登录!");
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "changeApplyStartDate", changeApplyStartDate);
        MapUtils.putNotNull(paramMap, "changeApplyEndDate", changeApplyEndDate);
        if(StringUtils.isNotBlank(printCount)){
            paramMap.put("printCount", Integer.parseInt(printCount));
        }
        String storeCode = WebUtils.getLoginedUserMainStore();
        paramMap.put("storeCode", storeCode);
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        return StatusDto.buildDataSuccessStatusDto(constructionChangeService.searchConstructionChangeScrollPage(paramMap));
    }


    /**
     * 批量打印/单个打印或查看
     *
     * @param ids 变更单id
     * @param isPrint  是否打印
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "/vieworprint")
    public Object viewOrPrint(Long[] ids,Boolean isPrint, HttpServletRequest res) throws IOException, DocumentException {
        if(ids == null || ids.length == 0){
            return StatusDto.buildFailureStatusDto("参数id不能为空!");
        }
        return constructionChangeService.viewOrPrint(ids, isPrint,res);
    }

}
