package cn.damei.business.dao.finance;

import cn.damei.business.entity.finance.FinaPaymethodStore;
import cn.damei.business.entity.finance.FinaPaymethodTree;
import cn.damei.business.entity.finance.TreeNode;
import cn.damei.business.entity.finance.FinaPaymethod;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinaPaymethodDao extends CrudDao<FinaPaymethod> {


    List<TreeNode> getTreeNodes(String parentId);
    /**
     * 查询指定门店、指定阶段允许使用的交款方式
     * @param storeCode 门店编号
     * @param stageCode 阶段编码
     * @return
     */
    List<FinaPaymethod> queryPayMethodWithStoreAndStage(@Param("storeCode") String storeCode,@Param("stageCode") String stageCode);

    List<FinaPaymethodTree> findAllWithDeleted(Long id);

    Object getByCode(String code);

    void insertFinaPaymethodTree(FinaPaymethodTree finaPaymethodTree);

    void updateFinaPaymethodTree(FinaPaymethodTree finaPaymethodTree);

    FinaPaymethodTree getByIdTree(Long id);

    int deleteFinaPaymethod(Long id);

    void addTrainRecordBatch(List<FinaPaymethodStore> finaPaymethodStoreList);

    FinaPaymethod getFinaPaymethodById(Long id);

    List<FinaPaymethodStore> getFinaPaymethodStoreById(String methodCode);

    int deletePaymethodStoreById(String methodCode);

    Object isEmptyCode(String methodCode);

    List<FinaPaymethodStore> queryStoreAll();
}
