package cn.mdni.business.dao.material;


import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * <dl>
 * <dd>Description: 美得你智装 变更详细 Dao</dd>
 * <dd>@date：2017/11/16  17:18</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Repository
public interface SmChangeDetailDao extends CrudDao<SmChangeDetail> {

    Long getStatusByChangeLogId(@Param("changeLogId") String changeLogId);

    void updateChangeOrderStatus(@Param("id") Long id, @Param("status") String status);
    /**
     * @Description: 美得你智装 批次号后三位+1
     * @date: 2017/11/21  16:17
     * @param changeVersionNo 批次号
     * @author: Ryze
     */
    String getChangeNoThree(String changeVersionNo);

    List<SmChangeDetail> findChangeDetail(@Param("contractCode") String contractCode,@Param("changeVersionNo") String changeVersionNo);

    List<SmChangeDetail> findChangeOrderList(Map<String, Object> params);

    Long searchChangeOrderListTotal(Map<String, Object> params);

    void updateDownloadStatus(String changeNo);

    SmChangeDetail findDownloadTimesByChangeNo(String changeNo);

    Long listCount(Map<String, Object> params);

    List<SmChangeDetail> changeMaterialList(Map<String, Object> params);

    List<SmChangeDetail> getByIds(Long[] ids);

    void UpdatePrintCount(Long[] ids);
    /**
     * @Description: 美得你智装 根据变更号查出 备注
     * @date: 2017/12/21  16:08
     * @param changeNo
     * @author: Ryze
     */
    SmChangeDetail getByChangeNo(String changeNo);
    /**
     * @Description: 美得你智装 根据变更号查出 审计最新备注
     * @date: 2017/12/21  16:08
     * @param changeNo
     * @author: Ryze
     */
    String getRocordByChangeNo(String changeNo);
}