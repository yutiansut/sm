package cn.damei.business.service.finance;

import cn.damei.business.constants.CommonStatusEnum;
import cn.damei.business.constants.NumberingTypeEnum;
import cn.damei.business.constants.OpreatModeEnum;
import cn.damei.business.dao.finance.ProjectPayplanStageDao;
import cn.damei.business.dao.finance.ReparationMoneyDao;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.ReparationMoney;
import cn.damei.business.service.orderflow.ContractService;
import cn.damei.business.service.orderflow.NumberingRuleService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import cn.mdni.commons.collection.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ReparationMoneyService extends CrudService<ReparationMoneyDao,ReparationMoney>{

    @Autowired
    private ProjectPayplanStageDao projectPayplanStageDao;

    @Autowired
    private FinaOperatLogService finaOperatLogService;

    @Autowired
    private NumberingRuleService numberingRuleService;

    @Autowired
    private ContractService contractService;

    /**
     * 通过项目UUID查询赔款记录
     * @param
     * @return
     */
    public List<Map<String,Object>> claimsRecord(String contractUuid) {
        return entityDao.claimsRecord(contractUuid);
    }


    /**
     * 查询影响某个项目指定阶段的的变更
     * @param contractUuid 项目uuid
     * @param stageCode 阶段编号
     * @param cleared 清算
     * @return
     */
    public List<ReparationMoney> queryEffectStageReparationMoneyList(String contractUuid,String stageCode,Boolean cleared){
        Map<String,Object> paramMap = new HashedMap();
        MapUtils.putNotNull(paramMap,"contractUuid",contractUuid);
        MapUtils.putNotNull(paramMap,"effectStageId",stageCode);
        MapUtils.putNotNull(paramMap,"cleared",cleared);

        return entityDao.search(paramMap);
    }


    /**
     * 求一批赔款的赔款总额
     * @param reparationMoneyList
     * @return
     */
    public BigDecimal sumReparationAmount(List<ReparationMoney> reparationMoneyList){
        BigDecimal totalReparationAmount = BigDecimal.ZERO;
        for(ReparationMoney reparation : reparationMoneyList){
            totalReparationAmount = totalReparationAmount.add(reparation.getReparationAmount());
        }
        return totalReparationAmount;
    }

    /**
     * 求影响指定阶段的未清算的赔款总额
     * @param stageCode
     * @return
     */
    public BigDecimal sumUnClearedReparatAmount(String stageCode){
        return entityDao.sumUnClearedReparatAmount(stageCode);
    }

    /**
     * 新增赔款记录
     * @param reparationMoney
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertReparationMoney(ReparationMoney reparationMoney) {
        reparationMoney.setReparationMode(OpreatModeEnum.MANUAL.toString());
        reparationMoney.setCleared(false);
        reparationMoney.setCreator(WebUtils.getCurrentUserNameWithOrgCode());
        reparationMoney.setReparationStatus(CommonStatusEnum.VALID.toString());
        entityDao.insert(reparationMoney);

        //插入日志
        finaOperatLogService.addReparationMoneyfromMessage(reparationMoney);
    }

    /**
     * 查询项目当前的阶段信息,用于展示赔款
     * @param contractUuid 项目的uuid
     * @return
     */
    public Map findProjectStage(String contractUuid) {
        //查到订单
        CustomerContract project = contractService.getContractByUuid(contractUuid);
        //查询的赔款信息
        Map<String, Object> projectStage = projectPayplanStageDao.findProjectStage(contractUuid);
        if(projectStage != null && projectStage.size() > 0){
            //获取本次页面要用的赔款编号
            String reparation_no = numberingRuleService.getNumber(project.getStoreCode(), NumberingTypeEnum.REPARATIONS_NUMBER);
            projectStage.put("reparationNo",reparation_no);
        }
        return projectStage;

    }

    /**
     * 获取赔款
     * @param codeList
     * @return
     */
    public List<Map<String, String>> getFeparationAmountByContractCode(List<String> codeList) {
        return entityDao.getFeparationAmountByContractCode(codeList);
    }


    /**
     * 清算影响指定阶段的赔款
     * @param reparationMoney
     */
    public void clearReparationMoneyInStage(ReparationMoney reparationMoney){
        entityDao.batchClearReparationMoneyInStage(reparationMoney);
    }
}
