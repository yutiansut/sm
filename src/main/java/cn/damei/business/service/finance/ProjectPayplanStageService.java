package cn.damei.business.service.finance;

import cn.damei.business.constants.CommonStatusEnum;
import cn.damei.business.dao.finance.ProjectPayplanStageDao;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.*;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.dto.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProjectPayplanStageService extends CrudService<ProjectPayplanStageDao,ProjectPayplanStage>{


    @Autowired
    private PayplanService payplanService;

    @Autowired
    private FinaOperatLogService finaOperatLogService;

    @Autowired
    private ProjectChangeMoneyService changeMoneyService;

    @Autowired
    private ReparationMoneyService reparationMoneyService;



    /**
     * 批量持久化某个项目的多个交款规划阶段
     * @param stageList
     */
    public void addProjectPayplanStageBatch(List<ProjectPayplanStage> stageList){
        entityDao.insertProjectPayplanStageBatch(stageList);
    }


    /**
     * 查询指定项目的指定交款阶段
     * @param contractUuid 项目uuid
     * @param itemTempCodeEnum 阶段模版编号枚举
     * @return
     */
    public ProjectPayplanStage getProjectStageWithItemTempCode(String contractUuid,PayplanItem.PayplanItemCodeEnum itemTempCodeEnum){
        Map<String,Object> queryParamMap = new HashMap();
        MapUtils.putNotNull(queryParamMap,"contractUuid",contractUuid);
        MapUtils.putNotNull(queryParamMap,"itemTempCode",itemTempCodeEnum.toString());
        List<ProjectPayplanStage> aimStageList = entityDao.search(queryParamMap);

        if(aimStageList == null || aimStageList.size()<1){
            return null;
        }
        return aimStageList .get(0);
    }


    /**
     * 通过交款阶段编号查询交款阶段
     * @param stageCode
     * @return
     */
    public ProjectPayplanStage getProjectStageWithCode(String stageCode){
        Map<String,Object> queryParamMap = new HashMap();
        MapUtils.putNotNull(queryParamMap,"stageCode",stageCode);
        List<ProjectPayplanStage> aimStageList = entityDao.search(queryParamMap);

        if(aimStageList == null || aimStageList.size()<1){
            return null;
        }
        return aimStageList .get(0);
    }


    /**
     * 获取指定项目的当前阶段
     * @param contractUuid
     * @return
     */
    public ProjectPayplanStage getProjectCurrentStage(String contractUuid){
        return entityDao.getProjectCurrentStage(contractUuid);
    }




    /**
     * 添加项目的定金规划阶段
     * @param contract
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto addProjectDepositStage(CustomerContract contract){
        //校验该门店是否设置了交款规划
        if(payplanService.queryPayPlanItemFlowWithStore(contract.getStoreCode()).isEmpty()){
            return StatusDto.buildFailureStatusDto("门店未设置交款规划");
        }

        //判断当前是否需要交定金阶段，如果需要则获取该门店当前使用的交款规划中的定金规划节点
        PayplanItem depositPlanTemp = payplanService.getFirstItemOfPlanWithStore(contract.getStoreCode(), CommonStatusEnum.NORMAL.toString());
        if(depositPlanTemp == null){
            return StatusDto.buildSuccessStatusDto("门店当前使用的交款规划中不用交定金");
        }

        //持久化定金阶段
        ProjectPayplanStage depositStage = new ProjectPayplanStage();
        depositStage.setStageCode(UUID.randomUUID().toString());
        depositStage.setContractCode(contract.getContractCode());
        depositStage.setContractUuid(contract.getContractUuid());
        depositStage.setStageidx(1d);
        depositStage.setStageTemplateId(depositPlanTemp.getId().intValue());
        depositStage.setStageTemplateCode(depositPlanTemp.getItemCode());
        depositStage.setStageType(depositPlanTemp.getItemType());
        depositStage.setStageName(depositPlanTemp.getItemName());
        depositStage.setPayPlanTemplateCode(depositPlanTemp.getPlanCode());
        depositStage.setExpectReceived(depositPlanTemp.getFinaTransAmount());
        depositStage.setActualTotalReceived(BigDecimal.ZERO);
        depositStage.setAgoUnpayAmount(BigDecimal.ZERO);
        depositStage.setStartTime(new Date());
        depositStage.setStageFinished(false);
        depositStage.setCurentFlag(true);
        entityDao.insert(depositStage);

        //插入日志
        finaOperatLogService.addStageTransformMessage(depositStage);

        //正常
        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 施工合同签约后补充项目的各个交款阶段
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StatusDto addProjectStagesAfterContructSign(CustomerContract projectContract){
        //校验项目
        if(projectContract == null || StringUtils.isBlank(projectContract.getContractUuid())){
            return StatusDto.buildFailureStatusDto("项目uuid无效");
        }

        //查询定金阶段
        ProjectPayplanStage depositStage = getProjectStageWithItemTempCode(projectContract.getContractUuid(),PayplanItem.PayplanItemCodeEnum.NODE_DEPOSIT);

        //确定要使用的交款规划模板节点序列
        ArrayList<PayplanItem> payPlanflow = null;
        double stageIndexWeight = 1d;
        if(depositStage != null){
            payPlanflow = payplanService.queryPayPlanItemFlowWithPlanCode(depositStage.getPayPlanTemplateCode());
            stageIndexWeight++;
        }
        else{
            payPlanflow = payplanService.queryPayPlanItemFlowWithStore(projectContract.getStoreCode());
        }

        //创建整套交款阶段（除定金阶段）
        List<ProjectPayplanStage> projectPayplanStageList = new ArrayList<ProjectPayplanStage>();
        for(PayplanItem planItem : payPlanflow){
            //忽略定金类型的交款规划节点
            if(PayplanItem.PayplanItemTypeEnum.DEPOSIT.toString().equals(planItem.getItemType())){
                continue;
            }

            ProjectPayplanStage projectPlanStage = new ProjectPayplanStage();
            //区分拆改费和工程款
            BigDecimal expectReceived = BigDecimal.ZERO;
            if(PayplanItem.PayplanItemTypeEnum.MODIFY.toString().equals(planItem.getItemType())){
                expectReceived = projectContract.getModifyAmount().multiply(new BigDecimal(planItem.getFinaTransRate()));
            }
            else if (PayplanItem.PayplanItemTypeEnum.CONSTRUCT.toString().equals(planItem.getItemType())){
                expectReceived = projectContract.getContractMoneyAmount().multiply(new BigDecimal(planItem.getFinaTransRate()));
            }
            //如果阶段应收=0（不包括小于）,不处理
            if(expectReceived.compareTo(BigDecimal.ZERO) == 0 && planItem.getFinaTransAmount().compareTo(BigDecimal.ZERO) == 0 ){
                continue;
            }

            projectPlanStage.setStageCode(UUID.randomUUID().toString());
            projectPlanStage.setContractCode(projectContract.getContractCode());
            projectPlanStage.setContractUuid(projectContract.getContractUuid());
            projectPlanStage.setStageidx(stageIndexWeight);
            projectPlanStage.setStageTemplateId(planItem.getId().intValue());
            projectPlanStage.setStageTemplateCode(planItem.getItemCode());
            projectPlanStage.setStageType(planItem.getItemType());
            projectPlanStage.setStageName(planItem.getItemName());
            projectPlanStage.setPayPlanTemplateCode(planItem.getPlanCode());

            projectPlanStage.setExpectReceived(expectReceived);
            projectPlanStage.setActualTotalReceived(BigDecimal.ZERO);
            projectPlanStage.setAgoUnpayAmount(BigDecimal.ZERO);
            projectPlanStage.setStageFinished(false);
            projectPlanStage.setCurentFlag(false);

            //连接相邻两个规划节点
            if(projectPayplanStageList.size() >0){
                ProjectPayplanStage prevStage = projectPayplanStageList.get(projectPayplanStageList.size()-1);
                prevStage.setNextStageCode(projectPlanStage.getStageCode());
                projectPlanStage.setPrevStageCode(prevStage.getStageCode());
            }
            projectPayplanStageList.add(projectPlanStage);
            stageIndexWeight++;
        }

        if(projectPayplanStageList.size()<1){
            return StatusDto.buildFailureStatusDto("没有为项目生成定金外的交款阶段");
        }
        entityDao.insertProjectPayplanStageBatch(projectPayplanStageList);

        //过一下财务阶段流转，处理一下
        ProjectPayplanStage aimStage = projectPayplanStageList.get(0);
        List<ProjectPayplanStage> stagesAfterTrans = financeStageTransform(depositStage,aimStage);
        //持久化处理后的阶段
        for (ProjectPayplanStage stage : stagesAfterTrans){
            update(stage);
        }

        return StatusDto.buildSuccessStatusDto();
    }


    /**
     * 相邻两个阶段流转，只是处理复制，不做持久化工作
     * @return
     */
    public List<ProjectPayplanStage> financeStageTransform(ProjectPayplanStage oldStage,ProjectPayplanStage aimStage){
        //校验阶段是否为空
        if (aimStage == null){
            return null;
        }
        //存放处理后的阶段
        List<ProjectPayplanStage> stagesAfterTrans = new ArrayList<ProjectPayplanStage>();

        //特殊情况：交款规划中无定金阶段，直接将签约后生成的所有阶段中的第一个作为当前阶段
        if(oldStage == null){
            aimStage.setAgoUnpayAmount(BigDecimal.ZERO);
            aimStage.setCurentFlag(true);
            aimStage.setStartTime(new Date());
        }
        //由定金阶段往下流转时特殊处理
        else if(PayplanItem.PayplanItemTypeEnum.DEPOSIT.toString().equals(oldStage.getStageType())){
            //定金阶段在转大定的时候就设置完成了
            oldStage.setNextStageCode(aimStage.getStageCode());
            oldStage.setCurentFlag(false);
            oldStage.setStageFinished(true);

            aimStage.setCurentFlag(true);
            aimStage.setPrevStageCode(oldStage.getStageCode());
            aimStage.setAgoUnpayAmount(BigDecimal.ZERO);
            aimStage.setStartTime(new Date());
        }
        //其他类型阶段的相邻两个阶段流转
        else{
            //计算阶段在结束时未交金额
            BigDecimal unPayd = calculateStageSummaryUnpay(oldStage);
            oldStage.setCurentFlag(false);

            aimStage.setAgoUnpayAmount(unPayd);
            aimStage.setCurentFlag(true);
            aimStage.setStartTime(new Date());
        }

        //记录财务阶段流转操作日志
        finaOperatLogService.addStageTransformMessage(aimStage);

        //返回经过阶段流转处理后的阶段
        if(oldStage != null) {
            stagesAfterTrans.add(oldStage);
        }
        stagesAfterTrans.add(aimStage);
        return stagesAfterTrans;
    }




    /**
     * 查询交款计划明细
     * @param contractUuid
     * @return
     */
    public List<Map<String,Object>> paymentPlanDetail(String contractUuid) {
        return entityDao.paymentPlanDetail(contractUuid);
    }


    /**
     * 根据contractCode查询阶段明细
     * @param codeList
     * @return
     */
    public List<ProjectPayplanStage> getStageByContractCode(List<String> codeList) {
        return this.entityDao.getStageByContractCode(codeList);
    }

    /**
     * 计算指定阶段当前应交
     * @param stage
     * @return
     */
    public BigDecimal calculateStageSummaryExpectedReceived(ProjectPayplanStage stage){
        BigDecimal summaryExpectedReceived = null;

        //计算阶段未交 = 本阶段应交 + 以往未交 + 影响本阶段的变更 -影响本阶段的赔款 - 本阶段已交
        BigDecimal totalChangeAmountInStage = changeMoneyService.sumUnClearedChangeAmount(stage.getStageCode());
        BigDecimal totalReparationsAmountInStage = reparationMoneyService.sumUnClearedReparatAmount(stage.getStageCode());

        summaryExpectedReceived = stage.getExpectReceived().add(stage.getAgoUnpayAmount()).add(totalChangeAmountInStage)
                .subtract(totalReparationsAmountInStage).subtract(stage.getActualTotalReceived());

        return summaryExpectedReceived;
    }


    /**
     * 计算阶段在结束时未交金额
     * @param stage
     * @return
     */
    public BigDecimal calculateStageSummaryUnpay(ProjectPayplanStage stage){
        BigDecimal summaryExpectedReceived = null;

        //计算阶段未交 = 本阶段应交 + 以往未交 + 所有的影响本阶段的变更 - 所有的影响本阶段的赔款 - 本阶段已交
        List<ProjectchangeMoney> clearedChanges = changeMoneyService.queryEffectStageChangeMoneyList(stage.getContractUuid(),stage.getStageCode(),null);
        List<ReparationMoney> reparationMoneys = reparationMoneyService.queryEffectStageReparationMoneyList(stage.getContractUuid(),stage.getStageCode(),null);

        BigDecimal totalChangeAmountInStage = clearedChanges.stream().map(ProjectchangeMoney::getChangeAmount).reduce(BigDecimal.ZERO,(a,b)->a.add(b));
        BigDecimal totalReparationsAmountInStage = reparationMoneys.stream().map(ReparationMoney::getReparationAmount).reduce(BigDecimal.ZERO,(a,b)->a.add(b));

        summaryExpectedReceived = stage.getExpectReceived().add(stage.getAgoUnpayAmount()).add(totalChangeAmountInStage)
                .subtract(totalReparationsAmountInStage).subtract(stage.getActualTotalReceived());

        return summaryExpectedReceived;
    }

    public List<Map> getStageTemplateCode(String storeCode) {
        return this.entityDao.getStageTemplateCode(storeCode);
    }
}
