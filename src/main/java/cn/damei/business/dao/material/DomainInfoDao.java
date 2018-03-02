package cn.damei.business.dao.material;

import cn.damei.business.entity.material.DomainInfo;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DomainInfoDao extends CrudDao<DomainInfo>{

    DomainInfo getByName(Map map);

    void openById(@Param("id")Long id);

    void offById(@Param("id")Long id);

}
