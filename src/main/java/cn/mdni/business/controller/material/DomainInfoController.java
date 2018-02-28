package cn.mdni.business.controller.material;

import cn.mdni.business.constants.Constants;
import cn.mdni.business.entity.material.DomainInfo;
import cn.mdni.business.service.material.DomainInfoService;
import cn.mdni.core.base.controller.BaseController;
import cn.mdni.core.dto.StatusDto;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: 功能区管理Controller
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/02.
 */
@RestController
@RequestMapping("/domaininfo")
public class DomainInfoController extends BaseController{

    @Autowired
    private DomainInfoService domainInfoService;

    /**
     * 列表搜索
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping("/findAll")
    public Object list( @RequestParam(required = false)String keyword,
                        @RequestParam(required = false)String domainName,
                        @RequestParam(required = false)String includeDomainType,
                        @RequestParam(defaultValue = "0") int offset,
                        @RequestParam(defaultValue = "20") int limit){
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(Constants.PAGE_OFFSET, offset);
        paramMap.put(Constants.PAGE_SIZE, limit);
        paramMap.put("keyword", keyword);
        paramMap.put("domainName", domainName);
        paramMap.put("includeDomainType", includeDomainType);
        return StatusDto.buildDataSuccessStatusDto(this.domainInfoService.searchScrollPage(paramMap));
    }


    /**
     * 根据id查询单条数据
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Object getById(Long id){
        return StatusDto.buildDataSuccessStatusDto(this.domainInfoService.getById(id));
    }

    /**
     * 添加和修改
     * @param domainInfo
     * @return
     */
    @RequestMapping("/insertOrUpdate")
    public Object insertOrUpdate(@RequestBody DomainInfo domainInfo){
        if(domainInfo == null){
            return StatusDto.buildFailureStatusDto("操作失败");
        }else {
            Object obj = this.domainInfoService.insertOrUpdate(domainInfo);
            return obj;
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Object deleteById(@RequestParam("id") Long id){
        this.domainInfoService.deleteById(id);
        return StatusDto.buildDataSuccessStatusDto("删除成功！");
    }

    @RequestMapping("/openById")
    public Object openById(@RequestParam("id") Long id){
        this.domainInfoService.openById(id);
        return StatusDto.buildDataSuccessStatusDto("修改成功！");
    }

    @RequestMapping("/offById")
    public Object offById(@RequestParam("id") Long id){
        this.domainInfoService.offById(id);
        return StatusDto.buildDataSuccessStatusDto("修改成功！");
    }

}
