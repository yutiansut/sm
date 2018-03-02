package cn.damei.business.controller.material;

import cn.damei.business.constants.ChangeDetailAuditEnum;
import cn.damei.business.constants.Constants;
import cn.damei.business.constants.RoleNameFromCenter;
import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.SmChangeDetail;
import cn.damei.business.service.material.SmChangeDetailService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.BootstrapPage;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.SSOShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/material/changedetail")
public class SmChangeDetailController extends BaseController{
    @Autowired
    private SmChangeDetailService service;
    @Autowired
    private SmChangeDetailService smChangeDetailService;

    @RequestMapping("/list")
    public Object list(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String status,
                          @RequestParam(required = false) String catalogUrl,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "20") int limit,
                          @RequestParam(defaultValue = "id") String orderColumn,
                          @RequestParam(defaultValue = "DESC") String orderSort) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyword", keyword);
        params.put("catalogUrl", catalogUrl);
        ArrayList<String> currentStatusList = new ArrayList<>();
        //状态判断 角色判断 材料部
        SSOShiroUser ssoShiroUser = WebUtils.getSSOShiroUser();
        String s = ssoShiroUser.getRoleNameList().toString();
        //加门店
        params.put("storeCode", WebUtils.getLoginedUserMainStore());
        if (s.contains(RoleNameFromCenter.MATERIAL_DEPARTMENT_AUDITOR)) {
            params.put("passFlag", "1");
            if (StringUtils.isNotBlank(status)) {
                currentStatusList.add(status);
            } else {
                currentStatusList.add(ChangeDetailAuditEnum.MATERIALDEPARTMENTAUDIT.toString());
                currentStatusList.add(ChangeDetailAuditEnum.DESIGNDIRECTORINTHEAUDIT.toString());
                currentStatusList.add(ChangeDetailAuditEnum.AUDITREVIEW.toString());
                currentStatusList.add(ChangeDetailAuditEnum.EXAMINATIONPASSED.toString());
            }
            //设计总监
        } else if (s.contains(RoleNameFromCenter.DESIGN_DIRECTOR)) {
            currentStatusList.add(ChangeDetailAuditEnum.DESIGNDIRECTORINTHEAUDIT.toString());
            //审计员
        } else if (s.contains(RoleNameFromCenter.AUDITOR)) {
            currentStatusList.add(ChangeDetailAuditEnum.AUDITREVIEW.toString());
            params.put("orgCode", WebUtils.getLoggedUser().getOrgCode());
            //没权限的
        }else{
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        params.put("currentStatusList", currentStatusList);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchScrollPage(params));
    }

    /**
     *  变更单管理 列表
     * @param keyword 关键字
     * @return
     */
    @RequestMapping("/findchangeorderlist")
    public Object findChangeOrderList(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String currentStatus,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "20") int limit,
                          @RequestParam(defaultValue = "id") String orderColumn,
                          @RequestParam(defaultValue = "DESC") String orderSort) {
        String createUser = WebUtils.getLoggedUser().getName() + "(" + WebUtils.getLoggedUser().getOrgCode() + ")";
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "currentStatus", currentStatus);
        if (WebUtils.getSSOShiroUser().getRoleNameList().toString().contains("设计师")) {
            MapUtils.putNotNull(params,"createUser",createUser);
        }
        if (WebUtils.getSSOShiroUser().getRoleNameList().toString().contains(RoleNameFromCenter.AUDITOR) && !WebUtils.getSSOShiroUser().getRoleNameList().toString().contains(RoleNameFromCenter.AUDITORDEP)) {
            MapUtils.putNotNull(params,"orgCode",WebUtils.getLoggedUser().getOrgCode());
        }
        if(StringUtils.isEmpty(WebUtils.getSSOShiroUser().getStoreCode())){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        MapUtils.putNotNull(params, "storeCode", WebUtils.getLoginedUserMainStore());
        MapUtils.putNotNull(params, Constants.PAGE_OFFSET, offset);
        MapUtils.putNotNull(params, Constants.PAGE_SIZE, limit);
        MapUtils.putNotNull(params, Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(service.searchChangeOrderListPage(params));
    }

    @RequestMapping("/updatestatus")
    public Object updateStatus(@RequestParam(required = false) String id,
                               @RequestParam(required = false) String changeLogId,
                               @RequestParam(required = false) String changeCategoryUrl,
                               @RequestParam(required = false) String contractCode){
        if (null == id) {
            StatusDto.buildFailureStatusDto("该变更单不存在！");
        }
        this.service.updateStatus(Long.parseLong(id),changeLogId,contractCode,changeCategoryUrl);
        return StatusDto.buildDataSuccessStatusDto("撤回变更单成功！");
    }

    /**
     * 根据项目code和变更号查询
     * @param contractCode
     * @param changeNo
     * @return
     */
    @RequestMapping("/findpromatrlbycontrcode")
    public Object findProMatrlByContrCode(@RequestParam(required = false) String contractCode,
                                          @RequestParam(required = false) String changeNo){
        List<ProjectChangeMaterial> proMatrlByContrCode = smChangeDetailService.findProMatrlByContrCode(contractCode, changeNo);
        return StatusDto.buildDataSuccessStatusDto(proMatrlByContrCode);
    }


    @RequestMapping("/findchangedetail")
    public Object findChangeDetail(@RequestParam(required = false) String contractCode,
                                   @RequestParam(required = false) String changeVersionNo){
       List<SmChangeDetail> smChangeDetailList =  smChangeDetailService.findChangeDetail(contractCode,changeVersionNo);
       Map<String,Object> map = Maps.newHashMap();
       map.put("data",smChangeDetailList);
       return StatusDto.buildDataSuccessStatusDto(map);
    }

    /**
     * 变更提交
     * 提交变更单
     * @param smChangeDetail 变更单对象
     * @return
     */
    @RequestMapping("/submit")
    public Object submitChangeDetail(SmChangeDetail smChangeDetail){
        if(smChangeDetail.getId() == null || StringUtils.isBlank(smChangeDetail.getContractCode())){
            return StatusDto.buildFailureStatusDto("参数丢失,请重新尝试!");
        }
        return smChangeDetailService.submitChangeDetail(smChangeDetail);
    }

    /**
     * @Description: 大美智装 根据变更号查出备注
     * @date: 2017/12/21  16:09
     * @param changeNo
     *
     */
    @RequestMapping("/getremark/{changeNo}")
    public Object getRemakk(@PathVariable("changeNo") String changeNo){
        return StatusDto.buildDataSuccessStatusDto(smChangeDetailService.getRemakk(changeNo)) ;
    }

}
