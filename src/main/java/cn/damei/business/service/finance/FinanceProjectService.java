package cn.damei.business.service.finance;

import cn.damei.business.entity.orderflow.Customer;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.FinaProjectAccount;
import cn.damei.business.service.orderflow.ContractService;
import cn.damei.business.service.orderflow.CustomerService;
import cn.damei.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class FinanceProjectService {

    @Autowired
    private ProjectChangeMoneyService projectChangeMoneyService;

    @Autowired
    private PaymoneyRecordService paymoneyRecordService;

    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;

    @Autowired
    private FinaOperatLogService finaOperatLogService;

    @Autowired
    private ReparationMoneyService reparationMoneyService;

    @Autowired
    private RefundRecordService refundRecordService;

    @Autowired
    private FinaProjectAccountService finaProjectAccountService;

    @Autowired
    private ContractService projectService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProjectCloseApplyService projectCloseApplyService;


    /**
     * 通过项目UUID查询交款信息
     * @param
     * @return
     */
    public List<Map<String,Object>> paymentInformationInquiry(String contractUuid) {
        return paymoneyRecordService.paymentInformationInquiry(contractUuid);
    }

    /**
     * 通过项目UUID查询交款计划（明细）
     * @param
     * @return
     */
    public List<Map<String,Object>> paymentPlanDetail(String contractUuid) {
        return projectPayplanStageService.paymentPlanDetail(contractUuid);
    }

    /**
     * 通过项目UUID查询变更记录
     * @param
     * @return
     */
    public List<Map<String,Object>> changeLog(String contractUuid) {
        return projectChangeMoneyService.changeLog(contractUuid);
    }

    /**
     * 通过项目UUID查询赔款记录
     * @param
     * @return
     */
    public List<Map<String,Object>> claimsRecord(String contractUuid) {
        return reparationMoneyService.claimsRecord(contractUuid);
    }

    /**
     * 通过项目UUID查询退款记录
     * @param
     * @return
     */
    public List<Map<String,Object>> refundRecord(String contractUuid) {
        return refundRecordService.refundRecord(contractUuid);
    }

    /**
     * 通过项目UUID查询退单申请记录
     * @param
     * @return
     */
    public List<Map<String,Object>> backRecord(String contractUuid) {
        return projectCloseApplyService.backRecord(contractUuid);
    }

    /**
     * 通过项目UUID查询操作记录
     * @param
     * @return
     */
    public List<Map<String,Object>> operationRecord(String contractUuid) {
        return finaOperatLogService.operationRecord(contractUuid);
    }


    /**
     * 订单创建后财务处理方法
     * @param project 刚创建的项目
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto handleFinanceAfterProjectCreate(CustomerContract project){
        //根据交款规划创建定金阶段(如果需要定金阶段的话)
        StatusDto stageRt = projectPayplanStageService.addProjectDepositStage(project);
        if(!stageRt.isSuccess()){
            return stageRt;
        }

        //创建项目的财务账户
        FinaProjectAccount projectAccount = new FinaProjectAccount();
        projectAccount.setContractCode(project.getContractCode());
        projectAccount.setContractUuid(project.getContractUuid());
        finaProjectAccountService.insert(projectAccount);

        //成功
        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 项目施工合同签约后财务处理
     * @param project
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto handleFinanceAfterProjectSign(CustomerContract project){

        //根据交款计划将该项目的整个阶段补充完整
        StatusDto stageRt = projectPayplanStageService.addProjectStagesAfterContructSign(project);
        if(!stageRt.isSuccess()){
            return stageRt;
        }

        //更新项目的财务账户
        FinaProjectAccount finaProjectAccount = finaProjectAccountService.getFinaceAccountByContratUuid(project.getContractUuid());
        if(finaProjectAccount == null)
        {
            return StatusDto.buildFailureStatusDto("项目账户不存在");
        }
        finaProjectAccount.setConstructExpectAmount(project.getContractMoneyAmount());
        finaProjectAccount.setConstructTotalPayed(new BigDecimal(0));
        finaProjectAccount.setModifyExpectAmount(project.getModifyAmount());
        finaProjectAccount.setModifyTotalPayed(new BigDecimal(0));
        finaProjectAccountService.update(finaProjectAccount);

        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 财务交定金时修改客户信息
     * @param customerInfo
     * @return
     */
    public StatusDto alterCustomerInfo(Customer customerInfo){
        //校验订单是否可以改用户名
        CustomerContract project = projectService.getContractByUuid(customerInfo.getContractUuid());
        if(project == null){
            return StatusDto.buildFailureStatusDto("订单不存在");
        }
        if((project.getSignDeposit()==null ? Integer.valueOf(0) : project.getSignDeposit()) >0){
            return StatusDto.buildFailureStatusDto("已签订预定合同，无法修改用户名");
        }
        //设置客户的id
        customerInfo.setId(project.getCustomerId());
        //修改客户信息
        try {
            customerService.update(customerInfo);
            //插入修改用户信息日志
            finaOperatLogService.addAlterCustomerInfoLog(customerInfo);
        }
        catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildFailureStatusDto("修改客户信息时出错");
        }


        //修改成功
        return StatusDto.buildSuccessStatusDto("客户信息修改成功");
    }
}
