package cn.mdni.business.controller.api;

import cn.mdni.business.service.material.AssistinfoSyncService;
import cn.mdni.business.service.material.ConstructionChangeService;
import cn.mdni.business.service.material.ProjectIntemService;
import cn.mdni.business.service.orderflow.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 对外提供接口 产业工人同步施工项
 * @Company: 美得你智装科技有限公司
 * @Author Allen
 * @Date: 16:28 2017/11/06.
 */
@RestController
@RequestMapping("/api/mps")
public class MpsApiController extends ApiController {

    @Autowired
    private ProjectIntemService projectIntemService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ConstructionChangeService constructionChangeService;
    @Autowired
    private AssistinfoSyncService assistinfoSyncService;

    /**
     * 添加和修改 施工项信息
     * @param requestKey
     * @return
     */
    @RequestMapping("/saveorupdateprojectintem")
    public @ResponseBody Object saveOrUpdateProjectIntem(@RequestParam String requestKey) {
        return projectIntemService.saveOrUpdateProjectIntem(requestKey);
    }

    /**
     * 添加和修改 施工项价格
     * @param requestKey
     * @return
     */
    @RequestMapping("/saveorupdateprojectintemprice")
    public Object saveOrUpdateProjectIntemPrice(@RequestParam String   requestKey) {
         return projectIntemService.saveOrUpdateProjectIntemPrice(requestKey);
    }

    /**
     * 添加和修改 施工项分类
     * @param requestKey
     * @return
     */
    @RequestMapping("/saveorupdateprojectintemtype")
    public Object saveOrUpdateProjectIntemType(@RequestParam String requestKey) {
        return projectIntemService.saveOrUpdateProjectIntemType(requestKey);
    }


    /**
     * 同步项目信息接口 （项目经理，监理 ）
     * @param requestKey
     * @return
     */
    @RequestMapping("/syncprojectmanagerorsupervisor")
    public Object syncProjectManagerOrSupervisor(@RequestParam String requestKey) {
        return contractService.syncProjectManagerOrSupervisor(requestKey);
    }

    /**
     * 同步项目信息接口 （开工 竣工时间）
     * @param requestKey
     * @return
     */
    @RequestMapping("/syncstartworkorcompleted")
    public Object syncStartWorkOrCompleted(@RequestParam String requestKey) {
        return contractService.syncStartWorkOrCompleted(requestKey);
    }

    /**
     * 产业工人拒绝接单
     * @param requestKey
     * @return
     */
    @RequestMapping("/mpsrejectproject")
    public Object mpsRejectProject(@RequestParam String requestKey) {
        return contractService.mpsRejectProject(requestKey);
    }


    /**
     * 同步施工变更（基装变更接口）
     * @param requestKey
     * @return
     */
    @RequestMapping("/syncconstructionchange")
    public Object syncConstructionChange(@RequestParam String requestKey) {
        return constructionChangeService.syncConstructionChange(requestKey);
    }

    /**
     * 同步辅料信息接口
     * @param requestKey
     * @return
     */
    @RequestMapping("/syncassistmaterials")
    public Object syncAssistMaterials(@RequestParam String requestKey) {
        return assistinfoSyncService.syncAssistinfo(requestKey);
    }





}
