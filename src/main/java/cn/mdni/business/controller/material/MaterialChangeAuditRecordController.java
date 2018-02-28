package cn.mdni.business.controller.material;

import cn.mdni.business.constants.ChangeDetailAuditEnum;
import cn.mdni.business.constants.RoleNameFromCenter;
import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.business.entity.material.SmMaterialChangeAuditRecord;
import cn.mdni.business.service.material.SmChangeDetailService;
import cn.mdni.business.service.material.SmMaterialChangeAuditRecordService;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import cn.mdni.core.shiro.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by 刘铎 on 2017/11/21.
 */
@RestController
@RequestMapping("/material/smmaterialchangeauditrecord")
public class MaterialChangeAuditRecordController extends BaseController {

    @Autowired
    private SmChangeDetailService smChangeDetailService;
    @Autowired
    private SmMaterialChangeAuditRecordService smMaterialChangeAuditRecordService;

    @RequestMapping("/updatestatus")
    public Object updateStatus(@RequestParam(required = false) String type,
                               @RequestParam(required = false) String changeNo,
                               @RequestParam(required = false) String changeLogId,
                               @RequestParam(required = false) String contractCode,
                               @RequestParam(required = false) String changeCategoryUrl,
                               @RequestParam(required = false) Long id,
                               @RequestParam(required = false) String remark){
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        SmMaterialChangeAuditRecord smMaterialChangeAuditRecord = new SmMaterialChangeAuditRecord();
        smMaterialChangeAuditRecord.setChangeNo(changeNo);
        smMaterialChangeAuditRecord.setAuditUser(loggedUser.getName());
        smMaterialChangeAuditRecord.setAuditDep(loggedUser.getDepCode());
        smMaterialChangeAuditRecord.setAuditTime(new Date());
        smMaterialChangeAuditRecord.setAuditUserType(RoleNameFromCenter.AUDITOR);
        if ("0".equals(type)) {
            smMaterialChangeAuditRecord.setAuditResult(ChangeDetailAuditEnum.AUDITFAILED.toString());
        }else{
            smMaterialChangeAuditRecord.setAuditResult(ChangeDetailAuditEnum.EXAMINATIONPASSED.toString());
        }
        smMaterialChangeAuditRecord.setAuditRemark(remark);
        //1是通过，0是未通过
        if ("0".equals(type)) {
            //不通过，更改变更单状态加日志
            SmChangeDetail smChangeDetail = new SmChangeDetail();
            smChangeDetail.setId(id);
            smChangeDetail.setCurrentStatus(ChangeDetailAuditEnum.AUDITFAILED.toString());
            smChangeDetailService.update(smChangeDetail);
            smMaterialChangeAuditRecordService.insert(smMaterialChangeAuditRecord);
        }else{
            try {
                smChangeDetailService.updateChangeOrderStatus(id, changeNo, changeLogId, contractCode, changeCategoryUrl, ChangeDetailAuditEnum.EXAMINATIONPASSED.toString());
            }catch (Exception e){
                e.printStackTrace();
                return StatusDto.buildFailureStatusDto("操作失败");
            }
            smMaterialChangeAuditRecordService.insert(smMaterialChangeAuditRecord);
        }
        return StatusDto.buildDataSuccessStatusDto("操作成功！");
    }
}
