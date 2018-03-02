package cn.damei.business.service.orderflow.projectsign;

import cn.damei.business.constants.Constants;
import cn.damei.business.constants.CustomerContractEnum;
import cn.damei.business.dao.orderflow.ProjectSignDao;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.orderflow.ProjectSign;
import cn.damei.business.service.finance.FinanceProjectService;
import cn.damei.business.service.material.ContractOperateTimeSummaryService;
import cn.damei.business.service.material.CustomerContractService;
import cn.damei.business.service.material.IndentPrepareOrderService;
import cn.damei.business.service.api.OutApiService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ProjectSignService extends CrudService<ProjectSignDao,ProjectSign> {

    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private FinanceProjectService financeProjectService;
    @Autowired
    private OutApiService outApiService;
    @Autowired
    private ContractOperateTimeSummaryService contractOperateTimeSummaryService;
    @Autowired
    private IndentPrepareOrderService indentPrepareOrderService;

    /**
     * 根据项目编号获取签约信息
     * @param contractCode 项目编号
     * @return
     */
    public ProjectSign getByCode(String contractCode){
        return this.entityDao.getByCode(contractCode);
    }

    /**
     * 修改
     * @param projectSign
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSign(ProjectSign projectSign){
        CustomerContract customerContract = this.customerContractService.getByCode(projectSign.getContractCode());
        if(projectSign.getId() != null){
            projectSign.setOperator(WebUtils.getLoggedUser().getName());
            projectSign.setOperateTime(new Date());
            this.entityDao.update(projectSign);
        }else {
            projectSign.setOperator(WebUtils.getLoggedUser().getName());
            projectSign.setOperateTime(new Date());
            this.entityDao.insert(projectSign);
        }
        this.financeProjectService.handleFinanceAfterProjectSign(customerContract);
        //产业工人推单
        boolean boo = this.outApiService.pushProjectToMps(projectSign.getContractCode());
        if(boo){
            //修改订单状态为待施工
            customerContract.setOrderFlowStatus(CustomerContractEnum.STAY_CONSTRUCTION);
            //修改选材状态为转单完成
            customerContract.setContractStatus(CustomerContractEnum.TRANSFER_COMPLETE);
            //李照新增 异步向产业工人推送选材单
            outApiService.pushMaterialToMps(customerContract.getContractCode());
            //记录操作时间
            this.contractOperateTimeSummaryService.buildContractOperateTimeSummary(customerContract.getContractCode(),CustomerContractEnum.TRANSFER_COMPLETE);

            this.customerContractService.update(customerContract);
        }else {
            throw new RuntimeException("推单失败！！！");
        }
        //调用转 预备单 方法
        try{
            indentPrepareOrderService.asynCreatePrepareOrder(customerContract.getContractCode(), Constants.PAGE_TYPE_SELECT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
