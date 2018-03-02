package cn.damei.business.controller.orderflow;

import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.finance.PayplanItem;
import cn.damei.business.entity.finance.ProjectPayplanStage;
import cn.damei.business.entity.orderflow.ProjectSign;
import cn.damei.business.service.finance.ProjectPayplanStageService;
import cn.damei.business.service.material.CustomerContractService;
import cn.damei.business.service.orderflow.projectsign.ProjectSignService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseComController;
import cn.damei.core.dto.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/projectSign")
public class ProjectSignController extends BaseComController<ProjectSignService, ProjectSign> {

    @Autowired
    private ProjectSignService projectSignService;
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private ProjectPayplanStageService projectPayplanStageService;


    /**
     * 修改
     * @param projectSign
     */
    @RequestMapping("/update")
    public Object update(ProjectSign projectSign){
        try {
            this.projectSignService.updateSign(projectSign);
            return StatusDto.buildDataSuccessStatusDto("操作成功！！！");
        }catch (Exception e){
            return StatusDto.buildFailureStatusDto(e.getMessage());
        }
    }

    /**
     * 根据项目编号获取签约信息
     * @param contractCode 项目编号
     * @return
     */
    @RequestMapping("/getByCode")
    public Object getByCode(String contractCode){
        ProjectSign projectSign = this.projectSignService.getByCode(contractCode);
        if(StringUtils.isBlank(projectSign.getSignExecutor())){
            projectSign.setSignExecutor(WebUtils.getCurrentUserNameWithOrgCode());
        }
        return StatusDto.buildDataSuccessStatusDto(projectSign);
    }
    @RequestMapping("/getById")
    public Object getById(Long id){
        return StatusDto.buildDataSuccessStatusDto(this.projectSignService.getById(id));
    }

    /**
     * 根据项目编号查询首期款结束时间
     * @param contractCode 项目编号
     */
    @RequestMapping("/getFinaFinishTime")
    public Object getFinaFinishTime(String contractCode){
        CustomerContract contract = this.customerContractService.getByCode(contractCode);
        ProjectPayplanStage payplanStage = this.projectPayplanStageService.getProjectStageWithItemTempCode(contract.getContractUuid(), PayplanItem.PayplanItemCodeEnum.NODE_FIRST);
        return StatusDto.buildDataSuccessStatusDto(payplanStage);
    }
}
