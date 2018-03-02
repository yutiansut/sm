package cn.damei.business.controller.material;

import cn.damei.business.constants.RoleNameFromCenter;
import cn.damei.business.entity.material.SmChangeDetail;
import cn.damei.business.entity.material.ProjectChangeMaterial;
import cn.damei.business.entity.material.SmMaterialChangeAuditRecord;
import cn.damei.business.service.material.ProjectChangeMaterialService;
import cn.damei.business.service.material.SmMaterialChangeAuditRecordService;
import cn.damei.core.WebUtils;
import cn.damei.core.base.controller.BaseController;
import cn.damei.core.dto.StatusDto;
import cn.damei.core.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/material/projectchangematerial")
public class ProjectChangeMaterialController extends BaseController{
    @Autowired
    private ProjectChangeMaterialService  service;
    @Autowired
    private SmMaterialChangeAuditRecordService smMaterialChangeAuditRecordService;
    public static final String STRING_O = "0";
    public static final String STRING_1 = "1";
    public static final String STRING_2 = "2";
    @RequestMapping("/getauditlist")
    public Object getAuditList(@RequestParam("categoryUrl") String categoryUrl,
                               @RequestParam("contractCode") String contractCode) {
        return StatusDto.buildDataSuccessStatusDto(service.getAuditList(categoryUrl, contractCode));
    }
    @RequestMapping(value = "/save")
    public Object saveOrUpdate(ProjectChangeMaterial entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            service.update(entity);
        } else {
            service.insert(entity);
        }
        return StatusDto.buildSuccessStatusDto("保存成功！");
    }


    @RequestMapping("/detail")
    public Object detail(@RequestParam("id") Long id,
                         @RequestParam("remark") String remark,
                         @RequestParam("current") String current,
                         @RequestParam("status") String status,
                         @RequestParam("changeNo") String changeNo,
                         @RequestParam(value = "ids[]",required = false) String[] ids,
                         @RequestParam(value="statusList[]",required = false) String[] statusList) {
        if(StringUtils.isNotBlank(current) && StringUtils.isNotBlank(status)){
            String sta=null;
            SmChangeDetail smChangeDetail = new SmChangeDetail();
            SmMaterialChangeAuditRecord smMaterialChangeAuditRecord = new SmMaterialChangeAuditRecord();
            ShiroUser loggedUser = WebUtils.getLoggedUser();
            smMaterialChangeAuditRecord.setChangeNo(changeNo);
            smMaterialChangeAuditRecord.setAuditUser(loggedUser.getName());
            smMaterialChangeAuditRecord.setAuditDep(loggedUser.getDepCode());
            smMaterialChangeAuditRecord.setAuditTime(new Date());
            smMaterialChangeAuditRecord.setAuditRemark(remark);
            switch (current){
                //材料部审核中
                case "MATERIALDEPARTMENTAUDIT":
                    // 0 拒绝 AUDITFAILED 审核未通过
                    // 1 通过 AUDITREVIEW("审计审核中")
                    // 2 驳回  DESIGNDIRECTORINTHEAUDIT("设计总监审核中"),
                    if(STRING_O.equals(status)){
                        sta="AUDITFAILED";
                    }else if(STRING_1.equals(status)){
                        sta="AUDITREVIEW";
                    }else if(STRING_2.equals(status)){
                        sta="DESIGNDIRECTORINTHEAUDIT";
                    }
                    smMaterialChangeAuditRecord.setAuditUserType(RoleNameFromCenter.MATERIAL_DEPARTMENT_AUDITOR);
                    smChangeDetail.setMaterialRemarks(remark);
                    break;
                 //设计总监审核中
                case "DESIGNDIRECTORINTHEAUDIT":
                    // 1 通过 AUDITREVIEW("审计审核中")
                    // 2 驳回 AUDITFAILED("审核未通过");
                   if(STRING_1.equals(status)){
                        sta="AUDITREVIEW";
                    }else if(STRING_2.equals(status)){
                        sta="AUDITFAILED";
                    }
                    smMaterialChangeAuditRecord.setAuditUserType(RoleNameFromCenter.DESIGN_DIRECTOR);
                    smChangeDetail.setDesignDirectorRemarks(remark);
                    break;
                 //审计审核中
                case  "AUDITREVIEW":
                    // 1 通过 EXAMINATIONPASSED("审核通过")
                    // 2 驳回 AUDITFAILED("审核未通过");
                    if(STRING_1.equals(status)){
                        sta="EXAMINATIONPASSED";
                    }else if(STRING_2.equals(status)){
                        sta="AUDITFAILED";
                    }
                    smMaterialChangeAuditRecord.setAuditUserType(RoleNameFromCenter.AUDITOR);
                    break;
            }
            smMaterialChangeAuditRecord.setAuditResult(sta);
            smMaterialChangeAuditRecordService.insert(smMaterialChangeAuditRecord);
            smChangeDetail.setId(id);
            smChangeDetail.setCurrentStatus(sta);
            ArrayList<ProjectChangeMaterial> list = new ArrayList<>();
            if( ids!=null && statusList!=null ){
                int length = ids.length;
                for (int i = 0; i < length; i++) {
                    ProjectChangeMaterial projectChangeMaterial = new ProjectChangeMaterial();
                    projectChangeMaterial.setId(ids[i]);
                    projectChangeMaterial.setMaterialsStatus(statusList[i]);
                    projectChangeMaterial.setUpdateTime(new Date());
                    projectChangeMaterial.setUpdateUser(loggedUser.getName() + "(" + loggedUser.getOrgCode() +")");
                    list.add(projectChangeMaterial);
                }
            }
            service.submitAuidt(smChangeDetail,list);
            return StatusDto.buildDataSuccessStatusDto("提交成功");
        }else{
            return StatusDto.buildFailureStatusDto("参数缺少");
        }
    }

}
