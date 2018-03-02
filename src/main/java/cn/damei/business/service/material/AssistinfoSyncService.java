package cn.damei.business.service.material;

import cn.damei.business.constants.OutApiEnum;
import cn.damei.business.constants.ResponseEnum;
import cn.damei.business.dao.material.AssistinfoSyncDao;
import cn.damei.business.dto.material.AssistinfoSyncDto;
import cn.damei.business.entity.orderflow.CustomerContract;
import cn.damei.business.entity.material.AssistinfoSync;
import cn.damei.business.service.api.OutApiService;
import cn.mdni.commons.json.JsonUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AssistinfoSyncService extends CrudService<AssistinfoSyncDao,AssistinfoSync> {

    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private OutApiService outApiService;

    public Object syncAssistinfo(String requestKey){
        Object result = new Object();
        try{
            AssistinfoSyncDto assistinfoSyncDto = JsonUtils.fromJson(requestKey, AssistinfoSyncDto.class);
            if( null == assistinfoSyncDto){
                return StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),ResponseEnum.PARAM_ERROR.getMessage());
            }
            //判断项目是否存在
            CustomerContract customerContract = customerContractService.getByCode(assistinfoSyncDto.getOrderNumber());
            if( null == customerContract){
                //项目信息不存在 则向老订单系统推送
                return outApiService.makeOldOrderProject(requestKey, OutApiEnum.SYNC_ASSIST_MATERIAL);
            }

            List<AssistinfoSync> assistinfoSyncList = assistinfoSyncDto.getAssistInfo();
            assistinfoSyncList.forEach(d -> {
                d.setContractCode(assistinfoSyncDto.getOrderNumber());
            });
            //插入辅料明细
            this.entityDao.batchInsertList(assistinfoSyncList);
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            result = StatusDto.buildStatusDtoWithCode(ResponseEnum.DEFEAT.getCode(),ResponseEnum.ERROR.getMessage());
        }
        return result;
    }


}
