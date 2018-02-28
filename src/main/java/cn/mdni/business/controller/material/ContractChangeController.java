package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.material.ChangeAuditCollect;
import cn.mdni.business.service.material.ContractChangeService;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.material.ChangeAuditCollectService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseComController;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.SSOShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: 客户合同信息变更Controller
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
@RestController
@RequestMapping("/contract/contractchange")
public class ContractChangeController extends BaseComController<CustomerContractService, CustomerContract> {
    @Autowired
    private ContractChangeService contractChangeService;
    @Autowired
    private ChangeAuditCollectService changeAuditCollectService;

    private static String AUDITOR = "审计员";

    private static String AUDITORDEP = "审计经理";

    private static String DESIGNER = "设计师";

    private static String DESIGNER_GROUP_LEADER = "设计组长";


    /**
     * 查询所有的项目
     *
     * @param keyword        关键字
     * @param contractStatus 项目状态
     * @param designerName   设计师
     * @param auditorName    审计员
     * @param changeTime   时间类型
     * @param startDate      开始时间
     * @param endDate        结束时间
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) CustomerContractEnum contractStatus,
                          @RequestParam(required = false) String designerName,
                          @RequestParam(required = false) String auditorName,
                          @RequestParam(required = false) String changeTime,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(required = false) Long id,
                          @RequestParam(defaultValue = "20") int limit,
                          @RequestParam(defaultValue = "id") String orderColumn,
                          @RequestParam(defaultValue = "DESC") String orderSort) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "keyword", keyword);
        MapUtils.putNotNull(paramMap, "contractStatus", contractStatus);
        MapUtils.putNotNull(paramMap, "designer", designerName);
        MapUtils.putNotNull(paramMap, "auditor", auditorName);
        MapUtils.putNotNull(paramMap, "startDate", startDate);
        MapUtils.putNotNull(paramMap, "endDate", endDate);
        MapUtils.putNotNull(paramMap, "changeTime", changeTime);
        MapUtils.putNotNull(paramMap, "id", id);
        //获取当前登录人
        SSOShiroUser ssoShiroUser = WebUtils.getSSOShiroUser();
        //获取当前登录人角色
        String users = ssoShiroUser.getRoleNameList().toString();
        if(users.contains(AUDITOR) && !users.contains(AUDITORDEP)){
            paramMap.put("auditorOrgCode", ssoShiroUser.getOrgCode());
        }
        if(users.contains(DESIGNER)){
            paramMap.put("designerCode",ssoShiroUser.getOrgCode());
        }
        if(users.contains(DESIGNER_GROUP_LEADER)){
            paramMap.put("designerDepCode",ssoShiroUser.getDepCode());
        }
        if(StringUtils.isEmpty(WebUtils.getLoginedUserMainStore())){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        MapUtils.putNotNull(paramMap, "storeCode", WebUtils.getLoginedUserMainStore());
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(this.contractChangeService.searchCustomerContractScrollPage(paramMap));
    }
    /**
     * 变更审核状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/{id}/updateStatus", method = RequestMethod.GET)
    public Object updateStatus(@PathVariable Long id, @RequestParam(required = false) CustomerContractEnum status) {
        try{
            this.contractChangeService.updateChangeStatus(id,status);
            return StatusDto.buildSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }

    }


    /**
     * 获取审计汇总信息
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/auditcollect")
    public Object findAuditCollectInfo(@RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate) {
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("storeCode",WebUtils.getLoginedUserMainStore());
        paramMap.put("startDate",startDate);
        paramMap.put("endDate",endDate);
        BootstrapPage<ChangeAuditCollect> result = this.changeAuditCollectService.searchScrollPage(paramMap);
        return StatusDto.buildDataSuccessStatusDto(result);
    }

    /**
     * 获取审计员 审核变更单信息
     * @param auditUser
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/findauditchangeorderinfo")
    public Object findAuditChangeOrderInfo(@RequestParam(required = false) String auditUser,
                                           @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("storeCode",WebUtils.getLoginedUserMainStore());
        paramMap.put("startDate",startDate);
        paramMap.put("endDate",endDate);
        paramMap.put("auditUser",auditUser);

        return StatusDto.buildDataSuccessStatusDto(this.changeAuditCollectService.findAuditChangeOrderInfo(paramMap));
    }


}
