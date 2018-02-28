package cn.mdni.business.controller.orderflow;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.business.constants.OperateLogEnum;
import cn.mdni.business.constants.PropertyHolder;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.business.service.material.MealInfoService;
import cn.mdni.business.service.operatelog.OperateLogService;
import cn.mdni.business.service.userservice.UserService;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.commons.http.HttpUtils;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 客户合同信息Controller
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/02.
 */
@RestController
@RequestMapping("/customercontract/contract")
public class CustomerContractController extends BaseComController<CustomerContractService, CustomerContract> {
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private MealInfoService mealInfoService;

    private static String system_type = "1";    //选材

    private static String AUDITOR = "审计员";

    private static String DESIGNER = "设计师";

    private static String AUDITORDEP = "审计经理";

    /**
     * 查询所有的项目
     * @param keyword 关键字
     * @param contractStatus 项目状态
     * @param designerName 设计师
     * @param auditorName 审计员
     * @param operateType 时间类型(操作类型)
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) CustomerContractEnum contractStatus,
                          @RequestParam(required = false) String designerName,
                          @RequestParam(required = false) String auditorName,
                          @RequestParam(required = false) String operateType,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate,
                          @RequestParam(defaultValue = "0") int offset,
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
        MapUtils.putNotNull(paramMap, "operateType", operateType);
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
        if(StringUtils.isEmpty(WebUtils.getLoginedUserMainStore())){
            return StatusDto.buildDataSuccessStatusDto(BootstrapPage.emptyPage());
        }
        MapUtils.putNotNull(paramMap, "storeCode", WebUtils.getLoginedUserMainStore());
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(this.customerContractService.searchCustomerContractScrollPage(paramMap));
    }

    /**
     * 通过项目id获取项目信息
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Object getById(@RequestParam Long id) {
        return StatusDto.buildDataSuccessStatusDto(this.customerContractService.getById(id));
    }

    /**
     * 修改项目
     * @param customerContract
     * @return
     */
    @RequestMapping("/update")
    public Object update(CustomerContract customerContract) {
        try {
            this.customerContractService.updateContract(customerContract);
            return StatusDto.buildSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 修改项目状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/{id}/updateStatus", method = RequestMethod.GET)
    public Object updateStatus(@PathVariable Long id, @RequestParam(required = false) CustomerContractEnum status) {
        try{
            this.customerContractService.updateContractStatus(id,status);
            return StatusDto.buildSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }

    }


    /**
     * 指派审计员
     * @param id
     * @param auditorOrgCode
     * @param auditorName
     * @param auditorPhone
     * @return
     */
    @RequestMapping(value = "/updateAuditor", method = RequestMethod.GET)
    public Object updateAuditor(@RequestParam(required = true) Long id,
                                @RequestParam(required = true) String auditorOrgCode,
                                @RequestParam(required = true) String auditorName,
                                @RequestParam(required = true) String auditorPhone) {
        try {
            this.customerContractService.updateAudit(id,auditorOrgCode,auditorName,auditorPhone);
            return StatusDto.buildSuccessStatusDto("指派成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto();
        }
    }

    /**
     * 通过code查找合同对象
     *
     * @param contractCode
     * @return
     */
    @RequestMapping("/get/{contractCode}")
    public Object getById(@PathVariable("contractCode") String contractCode) {
        return StatusDto.buildDataSuccessStatusDto(customerContractService.getByCode(contractCode));
    }

    /**
     * 获取设计师
     * @return
     */
    @RequestMapping("/findDesigner")
    public Object findDesignerFromAuth() {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("orgCode", WebUtils.getLoginedUserMainStore());
        String url = PropertyHolder.getDesignerUrl();
        String result = HttpUtils.post(url, param);
        return result;
    }

    /**
     * 获取历史指派审计员
     * @return
     */
    @RequestMapping("/findHistoryAuditor")
    public Object findHistoryAuditor(@RequestParam("contractCode") String contractCode) {
        Map<String, Object> paramMap = Maps.newHashMap();
        MapUtils.putNotNull(paramMap, "contractCode", contractCode);
        MapUtils.putNotNull(paramMap, "operateType", OperateLogEnum.ASSIGN_AUDIT);
        MapUtils.putNotNull(paramMap, "systemType", system_type);
        return StatusDto.buildDataSuccessStatusDto( operateLogService.findByConditions(paramMap) );
    }


    /**
     * 获取审计员
     * @return
     */
    @RequestMapping("/findAuditor")
    public Object findAuditorFromAuth() {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("storeCode", WebUtils.getLoginedUserMainStore());
        String url = PropertyHolder.getAuditorUrl();
        String result = HttpUtils.post(url, param);
        return result;
    }

    /**
     * 获取所有状态为开启的套餐
     * @return
     */
    @RequestMapping("/findMeal")
    public Object findMealByContractCode(){
        return StatusDto.buildDataSuccessStatusDto(this.mealInfoService.findAllMeal());
    }
    /**
     * 开始设计
     * @param customerContract
     * @return
     */
    @RequestMapping("/startdesign")
    public Object startDesign(CustomerContract customerContract) {
        return customerContractService.startDesign(customerContract);
    }
}
