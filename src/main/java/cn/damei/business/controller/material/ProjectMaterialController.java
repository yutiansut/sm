package cn.damei.business.controller.material;

import cn.damei.business.constants.Constants;
import cn.damei.business.entity.material.ProjectMaterial;
import cn.damei.business.service.material.ProjectChangeMaterialService;
import cn.damei.business.service.material.ProjectMaterialService;
import cn.damei.business.service.material.SmSkuDosageService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.base.controller.BaseComUUIDController;
import cn.damei.core.dto.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/material/projectmaterial")
public class ProjectMaterialController extends BaseComUUIDController<ProjectMaterialService, ProjectMaterial>{

    @Autowired
    private ProjectMaterialService projectMaterialService;
    @Autowired
    private SmSkuDosageService smSkuDosageService;
    @Autowired
    private ProjectChangeMaterialService projectChangeMaterialService;

    /**
     * 移除商品, 并将其对应的用量信息 清空
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/delwithdosage")
    public Object deletewithSkudosage(String id){
        if(id != null){
            smSkuDosageService.deleteByMaterialId(id);
            projectMaterialService.deleteById(id);
        }
        return StatusDto.buildSuccessStatusDto("操作成功!");
    }

    /**
     * 增加或修改
     *  新增时,返回主键id
     */
    @RequestMapping(value = "/saveandreturnid")
    public Object saveAndReturnId(ProjectMaterial projectMaterial) {
        return projectMaterialService.saveAndReturnId(projectMaterial);
    }

    /**
     * 通过条件 查找 projectMaterial集合
     *  该方法目前是为查询定额部分而用,查的都是原表,不是变更表!
     * @param categoryCode
     * @param pageType
     * @return
     */
    @RequestMapping(value = "/findmateriallist")
    public Object findmaterialList(String contractCode, String categoryCode, String categoryDetailCode,
                                   String pageType) {
        if(StringUtils.isBlank(pageType)){
            return StatusDto.buildFailureStatusDto("pageType不能为空!");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        MapUtils.putNotNull(params, "categoryCode", categoryCode);
        MapUtils.putNotNull(params, "contractCode", contractCode);
        MapUtils.putNotNull(params, "categoryDetailCode", categoryDetailCode);
        if(Constants.PAGE_TYPE_CHANGE.equals(pageType)){
            //变更查临时表
            return StatusDto.buildDataSuccessStatusDto(
                    projectChangeMaterialService.findWithSubListByMaterialParams(params));
        }else{
            //其他查原表
            return StatusDto.buildDataSuccessStatusDto(
                    projectMaterialService.findWithSubListByMaterialParams(params));
        }

    }

    /**
     * 保存定额
     */
    @RequestMapping("/saveprojectintem")
    public Object saveprojectintem(@RequestBody ProjectMaterial projectMaterial){
        Map<String, Object> ids = this.service.save(projectMaterial);
        return StatusDto.buildDataSuccessStatusDto(ids);
    }
}
