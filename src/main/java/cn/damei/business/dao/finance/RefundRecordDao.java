package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.RefundRecord;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RefundRecordDao extends CrudDao<RefundRecord>{

    List<Map<String,Object>> refundRecord(String contractUuid);

    List<Map<String,String>> getChargebackAmount(@Param("codeList") List<String> codeList);
}
