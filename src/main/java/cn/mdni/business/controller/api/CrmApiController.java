package cn.mdni.business.controller.api;

import cn.mdni.business.constants.ResponseEnum;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.finance.ProjectCloseApply;
import cn.mdni.business.entity.orderflow.SingleTag;
import cn.mdni.business.service.api.CrmApiService;
import cn.mdni.business.service.orderflow.ContractService;
import cn.mdni.business.service.orderflow.SingleTagService;
import cn.mdni.core.dto.StatusDto;
import com.rocoinfo.weixin.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 对外提供接口 CRM
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 16:28 2017/11/06.
 */
@RestController
@RequestMapping("/api/crm")
public class CrmApiController extends ApiController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private SingleTagService singleTagService;

    @Autowired
    private CrmApiService crmApiService;


    /**
     * Crm调用生成项目
     * @param requestKey
     * @return
     */
    @RequestMapping("/createproject")
    public @ResponseBody Object createProject(@RequestParam String requestKey) {
        try{
            String reqJson = getLogicParamJsonStr(requestKey);
            return contractService.createProject(reqJson);
        }catch (Exception e) {
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),e.getMessage());
        }

    }


    /**
     * 串单列表
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/proj/singletag/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getSingleTagList(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        Map<String, Object> params = null;
        if(StringUtils.isNoneBlank(reqJson)){
            params = JsonUtils.fromJson(reqJson, Map.class);
        }
        List<SingleTag> singleTagList = singleTagService.getSingleTagList(params);
        for(int i = 0;i < singleTagList.size();i++){
            Long id = singleTagList.get(i).getId();
            singleTagList.get(i).setTagId(String.valueOf(id));
        }
        return StatusDto.buildDataSuccessStatusDto(singleTagList);
    }

    /**
     * 新增串单
     * @param requestKey
     * @return
     */
    @RequestMapping(value = "/proj/singletag/saveorupdate", method = RequestMethod.POST)
    public Object saveOrUpdate(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        if(StringUtils.isBlank(reqJson)){
            return StatusDto.buildFailureStatusDto("要保存的订单标签信息不完整!");
        }
        Map<String, Object> params = null;
        if(StringUtils.isNoneBlank(reqJson)){
            params = JsonUtils.fromJson(reqJson,Map.class);
        }
        SingleTag singleTag = new SingleTag();
        singleTag.setTagName((String) params.get("tagName"));
        singleTag.setDescribtion((String) params.get("tagContent"));
        singleTag.setStoreCode((String) params.get("storeCode"));
        singleTag.setOperator((String) params.get("userName"));
        singleTagService.insert(singleTag);

        return StatusDto.buildSuccessStatusDto("保存订单标签成功!");
    }


    /**
     * 串单
     * @param
     * @return
     */
    @RequestMapping(value = "/proj/singletag/singlestring", method = RequestMethod.POST)
    public Object singleString(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        if(StringUtils.isBlank(reqJson)){
            return StatusDto.buildFailureStatusDto("订单标签信息不完整!");
        }
        Map<String, Object> params = null;
        if(StringUtils.isNoneBlank(reqJson)){
            params = JsonUtils.fromJson(reqJson, Map.class);
        }
        String orderId = (String) params.get("orderId");
        CustomerContract customerContract = contractService.getContractByUuid(orderId);
        if(customerContract != null){
            String contractCode = customerContract.getContractCode();
            Long tagId = Long.parseLong((String) params.get("tagId"));
            int i = singleTagService.singleString(contractCode,tagId);
            if(i > 0){
                return StatusDto.buildSuccessStatusDto("订单添加标签成功!");
            }else{
                return StatusDto.buildFailureStatusDto("订单添加标签失败!");
            }
        }else{
            return StatusDto.buildFailureStatusDto("订单打标签参数有误!");
        }


    }

    /**
     * 根据项目uuid查询订单简要列表
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/proj/info/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object querySimpleCustomerContractList(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        try{
            if(StringUtils.isBlank(reqJson)){
                return StatusDto.buildFailureStatusDto("请求参数缺失");
            }

            String contractUuidStr = "";
            Map<String, String> reqJsonMap = JsonUtils.fromJson(reqJson, Map.class);
            if(reqJsonMap.containsKey("contractUuidStr")){
                contractUuidStr = reqJsonMap.get("contractUuidStr");
            }
            return StatusDto.buildDataSuccessStatusDto(crmApiService.querySimpleCustomerContractList(contractUuidStr));
        }catch(Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("查询项目简要信息时异常");
        }
    }

    /**
     * 根据项目uuid查询订单详情
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/proj/info/detaillist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object queryCustomerContractDetail(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        try{
            String contractUuid = "";
            Map<String, String> reqJsonMap = JsonUtils.fromJson(reqJson, Map.class);
            if(reqJsonMap.containsKey("contractUuid")){
                contractUuid = reqJsonMap.get("contractUuid");
            }
            if(contractUuid == null && contractUuid.length() <= 0){
                return StatusDto.buildFailureStatusDto("接收参数为空");
            }
			/*查询订单详情*/
            Map<String, Object> customerContractDtlMap = crmApiService.queryCustomerContractDetail(contractUuid);
            return StatusDto.buildDataSuccessStatusDto(customerContractDtlMap);
        }
        catch(Exception exp){
            exp.printStackTrace();
            return StatusDto.buildFailureStatusDto("查询项目详情时失败");
        }
    }

    /**
     *
     * 函数功能描述:批量查询订单的定金情况
     * @param requestKey
     * @return
     */
    @RequestMapping(value = "/proj/info/deposit",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object queryFinanceDepositDetail(String requestKey){
        String reqJson = getLogicParamJsonStr(requestKey);
        try{
            if(StringUtils.isBlank(reqJson)){
                return StatusDto.buildFailureStatusDto("请求参数缺失");
            }

            String contractUuidStr = "";
            Map<String, String> reqJsonMap = JsonUtils.fromJson(reqJson, Map.class);
            if(reqJsonMap.containsKey("contractUuidStr")){
                contractUuidStr = reqJsonMap.get("contractUuidStr");
            }

            return StatusDto.buildDataSuccessStatusDto(crmApiService.queryFinanceDepositDetail(contractUuidStr));
        }catch(Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("查询项目简要信息时异常");
        }
    }



    /**
     * 新增退单申请记录
     * @param requestKey
     * @return
     */
    @RequestMapping(value = "/proj/close/save", method = RequestMethod.POST)
    public Object save(String requestKey) {
        String reqJson = getLogicParamJsonStr(requestKey);
        if(StringUtils.isBlank(reqJson)){
            return StatusDto.buildFailureStatusDto("要保存的退单申请信息不完整!");
        }
        ProjectCloseApply projectCloseApply = null;
        if(StringUtils.isNoneBlank(reqJson)){
            projectCloseApply = JsonUtils.fromJson(reqJson ,ProjectCloseApply.class);
        }
        crmApiService.saveProjectCloseApply(projectCloseApply);

        return StatusDto.buildSuccessStatusDto("保存退单申请记录成功!");
    }


    /**
     * 从request请求中取出业务参数json串
     * @param requestStr
     * @return
     */
    private String getLogicParamJsonStr(String requestStr){
        Map<String, Object> reqMap = null;
        if(StringUtils.isNoneBlank(requestStr)){
            reqMap = JsonUtils.fromJson(requestStr, Map.class);
        }
        String logicParamJsonStr = "";
        if(reqMap.containsKey("reqJson")){
            logicParamJsonStr = reqMap.get("reqJson").toString();
        }
        return logicParamJsonStr;
    }
}
