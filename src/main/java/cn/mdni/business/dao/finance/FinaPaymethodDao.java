package cn.mdni.business.dao.finance;

import cn.mdni.business.entity.finance.FinaPaymethodStore;
import cn.mdni.business.entity.finance.FinaPaymethodTree;
import cn.mdni.business.entity.finance.TreeNode;
import cn.mdni.business.entity.finance.FinaPaymethod;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ztw on 2017/11/27.
 */
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
