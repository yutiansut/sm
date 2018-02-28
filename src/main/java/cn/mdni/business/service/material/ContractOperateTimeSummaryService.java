package cn.mdni.business.service.material;

import cn.mdni.business.constants.CustomerContractEnum;
import cn.mdni.business.dao.material.ContractOperateTimeSummaryDao;
import cn.mdni.business.entity.material.ContractOperateTimeSummary;
import cn.mdni.core.base.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <dl>
 * <dd>Description: 美得你智装 操作时间汇总service</dd>
 * <dd>@date：2017/12/19</dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
@Service
public class ContractOperateTimeSummaryService extends CrudService<ContractOperateTimeSummaryDao,ContractOperateTimeSummary> {

    /**
     * 根据合同编号查询操作时间汇总
     * @param contractCode 合同编号
     */
    public ContractOperateTimeSummary getByContractCode(String contractCode){
        return this.entityDao.getByContractCode(contractCode);
    }
    /**
     * 构造操作时间汇总
     * @param contractCode 合同编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void buildContractOperateTimeSummary(String contractCode, CustomerContractEnum status){
        ContractOperateTimeSummary contractOperateTimeSummary = this.entityDao.getByContractCode(contractCode);
        //发起变更时间
        if(status.equals(CustomerContractEnum.CHANGING)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setStartChangeTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setStartChangeTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //指派审计时间
        } else if(status.equals(CustomerContractEnum.UNDER_AUDIT)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setMaterialAssignAuditTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setMaterialAssignAuditTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //审计审核时间
        }else if(status.equals(CustomerContractEnum.WAIT_TRANSFER)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setMaterialAuditPassTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setMaterialAuditPassTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //签约时间
        }else if(status.equals(CustomerContractEnum.TRANSFER_COMPLETE)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setMaterialSignTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setMaterialSignTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //变更提交审计时间
        }else if(status.equals(CustomerContractEnum.CHANGE_AUDIT)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setChangeSubmitAuditTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setChangeSubmitAuditTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //变更审计通过时间
        }else if(status.equals(CustomerContractEnum.CHANGE_AUDIT_SUCCESS)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setChangeAuditPassTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setChangeAuditPassTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //变更审计未通过时间
        }else if(status.equals(CustomerContractEnum.CHANGE_AUDIT_NOT_PASS)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setChangeAuditRefuseTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setChangeAuditRefuseTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //选材审计未通过时间
        }else if(status.equals(CustomerContractEnum.NOT_PASS)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setMaterialAuditRefuseTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setMaterialAuditRefuseTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        //选材提交审计时间
        }else if(status.equals(CustomerContractEnum.ASSIGN_AUDIT)){
            if(contractOperateTimeSummary != null){
                contractOperateTimeSummary.setMaterialSubmitAuditTime(new Date());
                this.entityDao.update(contractOperateTimeSummary);
            }else {
                ContractOperateTimeSummary contractOperateTime = new ContractOperateTimeSummary();
                contractOperateTime.setContractCode(contractCode);
                contractOperateTime.setMaterialSubmitAuditTime(new Date());
                this.entityDao.insert(contractOperateTime);
            }
        }
    }
}
