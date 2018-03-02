package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.ProjectPayplanStage;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectPayplanStageDao extends CrudDao<ProjectPayplanStage>{

    /**
     * 批量插入项目的交款阶段
     * @param stageList
     */
    void insertProjectPayplanStageBatch(@Param("stageList") List<ProjectPayplanStage> stageList);


    /**
     * 查询某个订单的第一个阶段
     * @param contractUuid
     * @return
     */
    ProjectPayplanStage getProjectFirstStage(String contractUuid);


    /**
     * 查询某个项目的指定交款阶段
     * @param contractUuid 项目uuid
     * @param stageCode 阶段编号
     * @return
     */
    ProjectPayplanStage getProjectStageWithStageCode(@Param("contractUuid") String contractUuid,@Param("stageCode") String stageCode);


    /**
     * 查询指定项目的当前阶段
     * @param contractUuid 项目uuid
     * @return
     */
    ProjectPayplanStage getProjectCurrentStage(String contractUuid);


    /**
     * 查询交款计划明细
     * @param contractUuid
     * @return
     */
    List<Map<String,Object>> paymentPlanDetail(String contractUuid);

    /**
     * 根据contractCode查询阶段明细
     * @param codeList
     * @return
     */
    List<ProjectPayplanStage> getStageByContractCode(@Param("codeList")List<String> codeList);
    /**
     * 查询项目当前的阶段信息,用于展示赔款
     * @param contractUuid 项目的uuid
     * @return
     */
    Map<String,Object> findProjectStage(String contractUuid);

    List<Map> getStageTemplateCode(String storeCode);
}
