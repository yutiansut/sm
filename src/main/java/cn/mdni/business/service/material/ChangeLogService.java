package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.ChangeLogDao;
import cn.mdni.business.entity.material.ChangeLog;
import cn.mdni.core.base.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更轨迹 service</dd>
 * <dd>@date：2017/11/7 </dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
@Service
public class ChangeLogService extends CrudService<ChangeLogDao,ChangeLog> {
    /**
     * @Description: 美得你智装 根据合同编号获取批次号
     * @date: 2017/11/21  15:37
     * @param contractCode 合同编号
     * @author: Ryze
     */
    public String getChangeVersion(String contractCode){
        StringBuilder returnVersion=new StringBuilder(contractCode);
        String str = entityDao.getChangeVersionTwo(contractCode);
        if(StringUtils.isBlank(str)){
                 returnVersion.append("01") ;
        }else {
            Long changeVersionTwo = Long.parseLong(str)+1L;
            long l = 10L;
            if (changeVersionTwo < l) {
                returnVersion.append('0').append(changeVersionTwo);
            } else {
                returnVersion.append(changeVersionTwo);
            }
        }
        return  returnVersion.toString();
    }

    /**
     * 根据合同编号 获取 变更版本号
     * @param contractCode 合同编号
     * @return
     * @author: Allen
     */
    public List<ChangeLog> findchangeVersionNoByContractNo(String contractCode) {
        return this.entityDao.findChangeVersionNoByContractCode(contractCode);
    }
    /**
     * 根据变更版本号查询变更详情
     * @param changeVersionNo
     * @return
     */
    public List<ChangeLog> findChangeLogByChNo(String changeVersionNo) {
        return this.entityDao.findChangeLogByChNo(changeVersionNo);
    }
    /**
     * 根据变更版本号查询变更单
     * @param changeVersionNo
     */
    public ChangeLog getChangeLogByChNo(String changeVersionNo) {
        return this.entityDao.getChangeLogByChNo(changeVersionNo);
    }
}
