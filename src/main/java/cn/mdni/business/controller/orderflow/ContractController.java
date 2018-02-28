package cn.mdni.business.controller.orderflow;

import cn.mdni.business.constants.*;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.operatelog.OperateLog;
import cn.mdni.business.service.material.ContractChangeService;
import cn.mdni.business.service.operatelog.OperateLogService;
import cn.mdni.business.service.orderflow.ContractService;
import cn.mdni.business.service.upload.UploadService;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseComController;
import cn.mdni.core.base.service.ServiceException;
import cn.mdni.core.dto.BootstrapPage;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.SSOShiroUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

/**
 * @Description: 订单Controller
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/13.
 */
@RestController
@RequestMapping("/order")
public class ContractController extends BaseComController<ContractService, CustomerContract> {

    @Autowired
    private ContractService contractService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ContractChangeService contractChangeService;

    private static String AUDITOR = "审计员";

    private static String AUDITORDEP = "审计经理";

    private static String DESIGNER = "设计师";
    
    private static String DESIGNER_GROUP_LEADER = "设计组长";

    /**
     * 查询
     *
     * @param keyword
     * @param orderFlowStatus
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String orderFlowStatus,
                          @RequestParam(required = false) String operateType,
                          @RequestParam(required = false) String startTime,
                          @RequestParam(required = false) String endTime,
                          @RequestParam(defaultValue = "0") int offset,
                          @RequestParam(defaultValue = "20") int limit,
                          @RequestParam(defaultValue = "id") String orderColumn,
                          @RequestParam(defaultValue = "DESC") String orderSort) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("keyword", keyword);
        paramMap.put("orderFlowStatus", orderFlowStatus);
        //时间的勾选类型
        if ("createTime".equals(operateType)) {
            paramMap.put("startDate", startTime);
            paramMap.put("endDate", endTime);
        } else if ("planHouseTime".equals(operateType)) {
            paramMap.put("planHouseStartTime", startTime);
            paramMap.put("planHouseEndTime", endTime);
        } else if ("bookHouseTime".equals(operateType)) {
            paramMap.put("bookHouseStartTime", startTime);
            paramMap.put("bookHouseEndTime", endTime);
        }
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
        //加门店
        paramMap.put("storeCode", WebUtils.getLoginedUserMainStore());
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        return StatusDto.buildDataSuccessStatusDto(this.contractService.searchCustomerContractScrollPage(paramMap));
    }

    /**
     * 根据合同编码查询
     *
     * @param contractCode 合同编码
     * @return
     */
    @RequestMapping("/getbycode")
    public Object getContractByContractCode(String contractCode) {
        return StatusDto.buildDataSuccessStatusDto(this.contractService.getContractByContractCode(contractCode));
    }

    /**
     * 从综管获取所有的设计组
     */
    @RequestMapping("/findAllDesignerGroup")
    public Object findAllDesignerGroup() {
        HashMap<String, Object> param = Maps.newHashMap();
        String storeCode = WebUtils.getLoginedUserMainStore();
        param.put("storeCode", storeCode);
        String url = PropertyHolder.getDesignGroupUrl();
        String result = HttpUtils.post(url, param);
        return result;
    }

    /**
     * 从综管获取当前设计组下的所有设计师
     */
    @RequestMapping("/findDesignByGroup")
    public Object findDesignByGroup() {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("orgCode", WebUtils.getLoggedUser().getDepCode());
        String url = PropertyHolder.getDesignerUrl();
        String result = HttpUtils.post(url, param);
        return result;
    }
    /**
     * 从综管获取所有的审计员
     */
    @RequestMapping("/findAllAudit")
    public Object findAllAudit() {
        HashMap<String, Object> param = Maps.newHashMap();
        String storeCode = WebUtils.getLoginedUserMainStore();
        param.put("storeCode", storeCode);
        String url = PropertyHolder.getAuditorUrl();
        String result = HttpUtils.post(url, param);
        return result;
    }
    /**
     * 督导组长分配设计组
     *
     * @param customerContract
     */
    @RequestMapping("/updateDesignerGroup")
    public Object update(@RequestBody CustomerContract customerContract) {
        CustomerContract contract = this.contractService.getContractByContractCode(customerContract.getContractCode());
        if(!StringUtils.isEmpty( contract.getReturnReason() )){
            customerContract.setReturnReason(null);
            customerContract.setReturnReasonDescribe(null);
        }
        customerContract.setOrderFlowStatus(CustomerContractEnum.DESIGN_STAY_ASSIGNED);
        customerContract.setDesigner("");
        customerContract.setDesignerMobile("");
        customerContract.setDesignerCode("");
        try{
            this.contractService.updateDesignerGroup(customerContract);
            OperateLog operateLog = this.buildOperateLog(customerContract, CustomerContractEnum.DESIGN_STAY_ASSIGNED);
            this.operateLogService.insert(operateLog);
            return StatusDto.buildDataSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 督导组长分配设计组
     *
     * @param customerContract
     */
    @RequestMapping("/updateDesigner")
    public Object updateDesigner(@RequestBody CustomerContract customerContract) {
        try{
            customerContract.setOrderFlowStatus(CustomerContractEnum.STAY_DESIGN);
            customerContract.setContractStatus(CustomerContractEnum.NOT_SELECT_MATERIAL);
            this.contractService.update(customerContract);
            OperateLog operateLog = this.buildOperateLog(customerContract, CustomerContractEnum.STAY_DESIGN);
            this.operateLogService.insert(operateLog);
            return StatusDto.buildDataSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 根据id查找
     *
     * @param id
     */
    @RequestMapping("/getById")
    public Object getById(Long id) {
        CustomerContract customerContract = this.contractService.getById(id);
        if(StringUtils.isBlank(customerContract.getOutMapExecutor())){
            customerContract.setOutMapExecutor(WebUtils.getCurrentUserNameWithOrgCode());
        }
        if(StringUtils.isBlank(customerContract.getBookHouseExecutor())){
            customerContract.setBookHouseExecutor(WebUtils.getCurrentUserNameWithOrgCode());
        }
        return StatusDto.buildDataSuccessStatusDto(customerContract);
    }

    /**
     * 修改量房信息
     *
     * @return
     */
    @RequestMapping("/updateBookHouse")
    public Object updateBookHouse(CustomerContract customerContract) {
        try {
            this.contractService.updateBookHouse(customerContract);
            return StatusDto.buildDataSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 修改出图信息
     *
     * @param customerContract
     * @return
     */
    @RequestMapping("/updateOutMap")
    public Object updateOutMap(CustomerContract customerContract) {
        try {
            this.contractService.updateOutMap(customerContract);
            return StatusDto.buildDataSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 发起变更
     *
     * @param id
     * @return
     */
    @RequestMapping("/startChange")
    public Object startChange(@RequestParam Long id) {
        try {
            this.contractChangeService.startChange(id);
            return StatusDto.buildSuccessStatusDto("操作成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("操作失败！！！");
        }
    }

    /**
     * 根据项目编号查询串单信息
     * @param contractCode
     * @return
     */
    @RequestMapping("/singledetail")
    public Object findSingleDetailBySingleId(@RequestParam String contractCode){
        return StatusDto.buildDataSuccessStatusDto(this.contractService.findSingleDetailBySingleId(contractCode));
    }

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam(value = "file") CommonsMultipartFile file,
                         @RequestParam UploadCategory category) {
        try {
            String saveTmpPath = uploadService.upload(file, category);
            Map<String, String> data = Maps.newHashMap();
            data.put("fileName", file.getOriginalFilename());
            data.put("path", WebUtils.getUploadFilePath(saveTmpPath)); // 图片的相对路径
            data.put("fullPath", WebUtils.getFullUploadFilePath(saveTmpPath)); // 图片的绝对路径
            return StatusDto.buildDataSuccessStatusDto(data);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "上传文件失败";
            if (e instanceof ServiceException) {
                msg = e.getMessage();
            }
            return StatusDto.buildFailureStatusDto(msg);
        }
    }

    /**
     * 从mongo中查询原始选材信息
     *
     * @param contractCode 合同编号
     * @return
     */
    @RequestMapping("/findmaterialbycodeandflag")
    public Object findMaterialByCodeAndFlag(@RequestParam String contractCode) {
        return StatusDto.buildDataSuccessStatusDto(this.contractChangeService.findMaterialByCodeAndFlag(contractCode));
    }

    /**
     * 原始其他综合
     * @param contractCode 单号
     * @return
     */
    @RequestMapping("/originaladdreduceamount")
    public Object findOtherAddReduceAmount(@RequestParam String contractCode){
        return StatusDto.buildDataSuccessStatusDto(this.contractChangeService.findOtherAddReduceAmount(contractCode));
    }

    /**
     * 查询原始的总金额
     * @param contractCode 合同编号
     * @return
     */
    @RequestMapping("/originaltotalamount")
    public Object findOriginalTotalAmount(@RequestParam String contractCode){
        return StatusDto.buildDataSuccessStatusDto(this.contractChangeService.findDosageByContractCodeList(contractCode));
    }

    /**
     * 设计师或者设计组长退回
     * @param customerContract
     * @return
     */
    @RequestMapping("/retreat")
    public Object assignRetreat(@RequestBody CustomerContract customerContract){
        try{
            this.contractService.assignRetreat(customerContract);
            return StatusDto.buildSuccessStatusDto("退回成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("退回失败！！！");
        }
    }

    /**
     * 督导组长收回
     * @param customerContract
     * @return
     */
    @RequestMapping("/recovery")
    public Object recovery(@RequestBody CustomerContract customerContract){
        try{
            this.contractService.recovery(customerContract);
            return StatusDto.buildSuccessStatusDto("收回成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("收回失败！！！");
        }
    }
    /**
     * 构建操作日志:每一个状态对应一个操作类型，
     * 根据不同的状态构建不同的操作类型
     *
     * @param customerContract 项目信息
     * @param status           当前状态
     */
    private OperateLog buildOperateLog(CustomerContract customerContract, CustomerContractEnum status) {
        OperateLog operateLog = new OperateLog();
        operateLog.setContractCode(customerContract.getContractCode());
        operateLog.setSystemType("2");
        if (CustomerContractEnum.STAY_TURN_DETERMINE.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.CREATE_TIME);
            operateLog.setOperateDescription(status.getLabel());
        } else if (CustomerContractEnum.SUPERVISOR_STAY_ASSIGNED.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.SUPERVISOR_STAY_ASSIGNED);
            operateLog.setOperateDescription(status.getLabel());
        } else if (CustomerContractEnum.DESIGN_STAY_ASSIGNED.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.DESIGN_STAY_ASSIGNED);
            operateLog.setOperateDescription(status.getLabel());
        } else if (CustomerContractEnum.STAY_DESIGN.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.ASSIGN_DESIGNER);
            operateLog.setOperateDescription(status.getLabel());
        } else if (CustomerContractEnum.STAY_SIGN.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.AUDIT_MANAGER_RETRACT);
            operateLog.setOperateDescription(status.getLabel());
        } else if (CustomerContractEnum.STAY_CONSTRUCTION.equals(status)) {
            operateLog.setOperateType(OperateLogEnum.SIGN_COMPLETE);
            operateLog.setOperateDescription(status.getLabel());
        }
        String operator = WebUtils.getLoggedUser().getName() + "(" + WebUtils.getLoggedUser().getOrgCode() + ")";
        operateLog.setOperator(operator);
        operateLog.setOperateTime(new Date());
        return operateLog;
    }
}
