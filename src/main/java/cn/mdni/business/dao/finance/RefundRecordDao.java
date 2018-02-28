package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.RefundRecord;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退款记录Dao
 * @Company: 美得你智装科技有限公司
 * @Author wangshuo
 * @Date: 2017/11/22.
 */
@Repository
public interface RefundRecordDao extends CrudDao<RefundRecord>{

    List<Map<String,Object>> refundRecord(String contractUuid);

    List<Map<String,String>> getChargebackAmount(@Param("codeList") List<String> codeList);
}
