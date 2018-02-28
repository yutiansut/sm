package cn.mdni.business.service.material;

import cn.mdni.business.constants.ResponseEnum;
import cn.mdni.business.dao.material.ProjectIntemDao;
import cn.mdni.business.dao.material.ProjectIntemPriceDao;
import cn.mdni.business.dao.material.ProjectIntemTypeDao;
import cn.mdni.business.dao.material.StoreRelationMappingDao;
import cn.mdni.business.entity.material.ProjectIntem;
import cn.mdni.business.entity.material.ProjectIntemPrice;
import cn.mdni.business.entity.material.ProjectIntemType;
import cn.mdni.business.entity.material.StoreRelationMapping;
import cn.mdni.commons.json.JsonUtils;
import cn.mdni.core.base.service.CrudService;
import cn.mdni.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



/**
 * @Description: 施工项service
 * @Company: 美得你智装科技有限公司
 * @Author: Allen
 * @Date: 2017/11/6 15:48.
 */
@Service
public class ProjectIntemService extends CrudService<ProjectIntemDao,ProjectIntem> {
    @Autowired
    private ProjectIntemPriceDao projectIntemPriceDao;
    @Autowired
    private ProjectIntemTypeDao projectIntemTypeDao;
    @Autowired
    private StoreRelationMappingDao storeRelationMappingDao;
    /**
     * 添加定额（施工项）
     * @param resquestKey
     * @return
     */
    public Object saveOrUpdateProjectIntem(String resquestKey) {
        try {
            ProjectIntem entity=JsonUtils.fromJson(resquestKey,ProjectIntem.class);
            if(null == entity){
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.PARAM_ERROR.getCode(),ResponseEnum.PARAM_ERROR.getMessage());
            }
            ProjectIntem projectIntem= entityDao.getById(entity.getId());
            if(projectIntem != null ) {
                entityDao.update(entity);
            }else {
                entityDao.insert(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.ERROR.getCode(),ResponseEnum.ERROR.getMessage());
        }
        return StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
    }
    /**
     * 添加定额价格（施工项价格）
     * @param resquestKey
     * @return
     */
    public Object saveOrUpdateProjectIntemPrice(String resquestKey) {
        try {
            ProjectIntemPrice entity=JsonUtils.fromJson(resquestKey,ProjectIntemPrice.class);
            if(null == entity){
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.PARAM_ERROR.getCode(),ResponseEnum.PARAM_ERROR.getMessage());
            }
            ProjectIntemPrice projectIntemPrice= projectIntemPriceDao.getById(entity.getId());
            //获取门店code
            String storeCode = this.getSmStoreCode(entity.getStoreId().longValue());
            entity.setStoreCode(storeCode);
            if(projectIntemPrice != null) {
                projectIntemPriceDao.update(entity);
            } else {
                projectIntemPriceDao.insert(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.ERROR.getCode(),ResponseEnum.ERROR.getMessage());
        }
        return StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
    }

    /**
     * 添加定额分类（施工项分类）
     * @param resquestKey
     * @return
     */
    public Object saveOrUpdateProjectIntemType(String resquestKey) {
        try {
            ProjectIntemType entity=JsonUtils.fromJson(resquestKey,ProjectIntemType.class);
            if(null == entity){
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.PARAM_ERROR.getCode(),ResponseEnum.PARAM_ERROR.getMessage());
            }
            ProjectIntemType projectIntemType= projectIntemTypeDao.getById(entity.getId());
            if(projectIntemType != null ) {
                projectIntemTypeDao.update(entity);
            }else {
                projectIntemTypeDao.insert(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusDto.buildStatusDtoWithCode(ResponseEnum.ERROR.getCode(),ResponseEnum.ERROR.getMessage());
        }
        return StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
    }

    /**
     * 获取门店Code
     * @param mpsStoreId 产业工人系统门店id
     * @return
     */
    private String getSmStoreCode(Long mpsStoreId) {
        StoreRelationMapping storeInfo = storeRelationMappingDao.getByMpsStoreId(mpsStoreId);
        if(storeInfo != null) {
            return storeInfo.getSmStoreCode();
        }
        return "";
    }


    /**
     * 查询增项的基装定额
     * @param contractCode
     * @param
     * @return
     */
    public List<Map<String,Object>> getProIntemByContractCode(String contractCode,String categoryCode, String categoryDetailCode) {
        return this.entityDao.getProIntemByContractCode(contractCode,categoryCode,categoryDetailCode);
    }
}
