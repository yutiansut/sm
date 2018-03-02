package cn.damei.business.dao.material;

import cn.damei.business.entity.material.SmOrderQuotaBill;
import cn.damei.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmOrderQuotaBillDao extends CrudDao<SmOrderQuotaBill>{
    List<SmOrderQuotaBill> findByCode(String contractCode);
    void deleteByCode(String contractCode);
}
