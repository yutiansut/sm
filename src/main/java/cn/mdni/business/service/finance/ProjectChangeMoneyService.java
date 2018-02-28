package cn.mdni.business.service.finance;

import cn.mdni.business.constants.ChangeTypeEnum;
import cn.mdni.business.constants.OpreatModeEnum;
import cn.mdni.business.dao.finance.ProjectchangeMoneyDao;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.finance.ProjectPayplanStage;
import cn.mdni.business.entity.finance.ProjectchangeMoney;
import cn.mdni.business.service.material.CustomerContractService;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.commons.collection.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by WS on 2017/11/17/017.
 */
@Service
public class ProjectChangeMoneyService extends CrudService<ProjectchangeMoneyDao,ProjectchangeMoney>{

    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;
    /**
     * 通过项目UUID查询变更记录
     * @param
     * @return
     */
    public List<Map<String,Object>> changeLog(String contractUuid) {

        return entityDao.changeLog(contractUuid);
    }

    /**
     * 获取增项
     * @param codeList
     * @return
     */
    public List<Map<String,String>> getAddAmountByContractCode(List<String> codeList) {
        return entityDao.getAddAmountByContractCode(codeList);
    }

    /**
     * 获取减项
     * @param codeList
     * @return
     */
    public List<Map<String,String>> getFewAmountByContractCode(List<String> codeList) {
        return entityDao.getFewAmountByContractCode(codeList);
    }

    /**
     * 查询影响某个项目指定阶段的的变更
     * @param contractUuid 项目uuid
     * @param stageCode 阶段编号
     * @param cleared 清算
     * @return
     */
    public List<ProjectchangeMoney> queryEffectStageChangeMoneyList(String contractUuid,String stageCode,Boolean cleared){

        Map<String,Object> paramMap = new HashedMap();
        MapUtils.putNotNull(paramMap,"contractUuid",contractUuid);
        MapUtils.putNotNull(paramMap,"effectStageId",stageCode);
        MapUtils.putNotNull(paramMap,"cleared",cleared);

        return entityDao.search(paramMap);
    }


    /**
     * 求一批变更的总变更额
     * @param changeMoneyList
     * @return
     */
    public BigDecimal sumChangeAmount(List<ProjectchangeMoney> changeMoneyList){
        BigDecimal totalChangeAmount = BigDecimal.ZERO;
        for(ProjectchangeMoney changeMoney :changeMoneyList ){
            totalChangeAmount = totalChangeAmount.add(changeMoney.getChangeAmount());
        }
        return totalChangeAmount;
    }


    /**
     * 求影响指定阶段的未清算的变更总额
     * @param stageCode
     * @return
     */
    public BigDecimal sumUnClearedChangeAmount(String stageCode){
        return entityDao.sumUnClearedChangeAmount(stageCode);
    }

    /**
     * 清算影响指定阶段的变更
     * @param updParam
     */
    public void clearChangeMoneyInStage(ProjectchangeMoney updParam){
        entityDao.batchClearChangeMoneyInStage(updParam);
    }

    /**
     * 新增变更款
     * @param  contractCode 项目编号
     * @param  changeNo   变更号
     * @param  changeAccount  变更金额
     */
    public void insertNewChangeMoney(String contractCode,String changeNo,BigDecimal changeAccount,ChangeTypeEnum changeType){
        CustomerContract customerContract = customerContractService.getByCode(contractCode);
        if(null != customerContract){
            ProjectPayplanStage projectPayplanStage = projectPayplanStageService.getProjectCurrentStage(customerContract.getContractUuid());
            if( null == projectPayplanStage ){

            }
            ProjectchangeMoney projectchangeMoney = new ProjectchangeMoney();
            projectchangeMoney.setContractCode(contractCode);
            projectchangeMoney.setChangeNo(changeNo);
            projectchangeMoney.setChangeAmount(changeAccount);
            projectchangeMoney.setContractUuid(customerContract.getContractUuid());
            projectchangeMoney.setChangeMode(OpreatModeEnum.SYSTEM.toString());
            projectchangeMoney.setCreateTime(new Date());
            projectchangeMoney.setEffectStageId(projectPayplanStage.getStageCode());
            projectchangeMoney.setEffectStageName(projectPayplanStage.getStageName());
            projectchangeMoney.setCleared(false);
            projectchangeMoney.setPrintCount(0);
            projectchangeMoney.setCreateStageId(projectPayplanStage.getStageCode());
            projectchangeMoney.setCreator(OpreatModeEnum.SYSTEM.toString());
            projectchangeMoney.setChangeType(changeType.toString());
            this.insert(projectchangeMoney);
        }
    }
}
