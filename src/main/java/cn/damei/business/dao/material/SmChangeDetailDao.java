package cn.damei.business.dao.material;


import cn.damei.business.entity.material.SmChangeDetail;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface SmChangeDetailDao extends CrudDao<SmChangeDetail> {

    Long getStatusByChangeLogId(@Param("changeLogId") String changeLogId);

    void updateChangeOrderStatus(@Param("id") Long id, @Param("status") String status);
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
    SmChangeDetail getByChangeNo(String changeNo);
    String getRocordByChangeNo(String changeNo);
}