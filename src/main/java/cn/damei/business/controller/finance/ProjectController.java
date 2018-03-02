package cn.damei.business.controller.finance;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.orderflow.Customer;
import cn.damei.business.entity.finance.RefundRecord;
import cn.damei.business.service.finance.FinaCustomerContractService;
import cn.damei.business.service.finance.FinaProjectAccountService;
import cn.damei.business.service.finance.FinanceProjectService;
import cn.damei.business.service.finance.PaymentService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/finance/project")
public class ProjectController {

    @Autowired
    private FinanceProjectService finaProjectService;

    @Autowired
    private FinaCustomerContractService finaCustomerContractService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private FinaProjectAccountService finaProjectAccountService;

    /**
     * 财务搜索订单接口
     * @param keyword 关键词
     * @param isFlag 是否首次进入页面
     * @param offset
     * @param limit
     * @param orderColumn 排序列名
     * @param orderSort 排序顺序
     * @return
     */
    @RequestMapping("/search")
    public Object search(@RequestParam(required = false) String keyword, @RequestParam(required = false) Boolean isFlag,@RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "id") String orderColumn,
                         @RequestParam(defaultValue = "DESC") String orderSort) {
        //校验
        if(WebUtils.getLoggedUser()==null){
            return StatusDto.buildFailureStatusDto("缺失登录用户信息");
        }
        //校验门店，没有门店返回空数据
        String[] storeCodeArr = WebUtils.getLoginedUserStores();
        if(storeCodeArr == null){
            return StatusDto.buildDataSuccessStatusDto(Arrays.asList());
        }

        String[] storeCode = WebUtils.getLoginedUserStores();
        if(storeCode[0] == null || storeCode[0] == ""){
            return StatusDto.buildDataSuccessStatusDto(Arrays.asList());
        }
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "keyword", keyword);
        //强制限制门店
        params.put("storeCode", storeCode[0]);
        params.put("storeCodeArray", storeCodeArr);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        //根据项目的创建时间倒序排序
        orderColumn = " cc.create_time ";
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        if(isFlag){
            return StatusDto.buildDataSuccessStatusDto(Arrays.asList());
        }
        else{
            return StatusDto.buildDataSuccessStatusDto(finaCustomerContractService.searchScrollPage(params));
        }
    }

    /**
     * 查询项目信息
     * @param
     * @return
     */
    @RequestMapping("/projectinformation")
    public Object projectInformation(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaCustomerContractService.projectInformation(contractUuid));
    }

    /**
     * 通过contractUuid查找对应的各种款项
     *
     * @param
     * @return
     */
    @RequestMapping("/getfinaprojectaccountbyuuid")
    public Object getFinaProjectAccountByUuid(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectAccountService.getFinaProjectAccountByUuid(contractUuid));
    }

    /**
     * 通过项目UUID查询交款信息
     * @param
     * @return
     */
    @RequestMapping("/paymentinformationinquiry")
    public Object paymentInformationInquiry(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.paymentInformationInquiry(contractUuid));
    }

    /**
     * 通过项目UUID查询交款计划（明细）
     * @param
     * @return
     */
    @RequestMapping("/paymentplandetail")
    public Object paymentPlanDetail(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.paymentPlanDetail(contractUuid));
    }

    /**
     * 通过项目UUID查询变更记录
     * @param
     * @return
     */
    @RequestMapping("/changelog")
    public Object changeLog(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.changeLog(contractUuid));
    }

    /**
     * 通过项目UUID查询赔款记录
     * @param
     * @return
     */
    @RequestMapping("/claimrecord")
    public Object claimsRecords(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.claimsRecord(contractUuid));
    }

    /**
     * 通过项目UUID查询退款记录
     * @param
     * @return
     */
    @RequestMapping("/refundrecord")
    public Object refundRecord(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.refundRecord(contractUuid));
    }

    /**
     * 通过项目UUID查询退单申请记录
     * @param
     * @return
     */
    @RequestMapping("/backrecord")
    public Object backRecord(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.backRecord(contractUuid));
    }

    /**
     * 通过项目UUID查询操作记录
     * @param
     * @return
     */
    @RequestMapping("/operationrecord")
    public Object operationRecord(String contractUuid) {
        return StatusDto.buildDataSuccessStatusDto(finaProjectService.operationRecord(contractUuid));
    }



    /**
     * 在财务交定金时修改客户信息
     * @return
     */
    @RequestMapping("/altcusinfo")
    public Object alterCustomerInfo(@RequestBody Customer customer){
        return finaProjectService.alterCustomerInfo(customer);
    }


    /**
     * 通过项目UUID修改有无预定合同
     * @param
     * @return
     */
    @RequestMapping("/checkcontract")
    public Object checkContract(String contractUuid) {
        return paymentService.finishDepositStageManual(contractUuid);
    }

    /**
     * 通过项目UUID修改有无可退字样
     * @param
     * @return
     */
    @RequestMapping("/checkrefundable")
    public Object checkRefundable(String contractUuid,int returnWord) {
        return paymentService.checkRefundable(contractUuid,returnWord);
    }

    /**
     * 项目退单
     * @param refundRecord
     * @return
     */
    @RequestMapping("/closeproject")
    public Object closeProject(@RequestBody RefundRecord refundRecord){
        return paymentService.closeProjectAfterRefund(refundRecord);
    }
}
