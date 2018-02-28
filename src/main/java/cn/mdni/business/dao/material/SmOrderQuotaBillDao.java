package cn.mdni.business.dao.material;

import cn.mdni.business.entity.material.SmOrderQuotaBill;
import cn.mdni.core.base.dao.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 美得你智装 选材的定额</dd>
 * <dd>@date：2017/11/22  16:36</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface SmOrderQuotaBillDao extends CrudDao<SmOrderQuotaBill>{
    /**
     * @Description: 美得你智装 根据合同编号获取 定额
     * @date: 2017/11/23  16:32
     * @param contractCode 合同号码
     * @author: Ryze
     */
    List<SmOrderQuotaBill> findByCode(String contractCode);
    /**
     * @Description: 美得你智装 根据合同编号删除定额
     * @date: 2017/11/23  16:32
     * @param contractCode 合同号码
     * @author: Ryze
     */
    void deleteByCode(String contractCode);
}
