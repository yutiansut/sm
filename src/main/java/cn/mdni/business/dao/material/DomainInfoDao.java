package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.DomainInfo;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Description: 功能区管理dao
 * @Company: 美得你智装科技有限公司
 * @Author wangdan
 * @Date: 2017/11/02.
 */
@Repository
public interface DomainInfoDao extends CrudDao<DomainInfo>{

    DomainInfo getByName(Map map);

    void openById(@Param("id")Long id);

    void offById(@Param("id")Long id);

}
