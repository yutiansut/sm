package cn.mdni.business.service.orderflow;



import cn.mdni.business.dao.material.CustomerContractDao;
import cn.mdni.business.dao.orderflow.SingleTagDao;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.orderflow.ContractSingle;
import cn.mdni.business.entity.orderflow.SingleTag;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @describe 串单 Service
 * @author zhangh
 * @date 2017-11-14 11:22:00
 *
 */
@Service
public class SingleTagService extends CrudService<SingleTagDao, SingleTag> {

    @Autowired
    private SingleTagDao singleTagDao;

    @Autowired
    private CustomerContractDao customerContractDao;

    /**
     * 增加
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SingleTag entity) {
        if (entity == null)
            return;
        if(entity.getOperator() == null){
            String userName = WebUtils.getCurrentUserNameWithOrgCode();
            entity.setOperator(userName);
        }
        if(entity.getStoreCode() == null){
            String storeCode = WebUtils.getLoginedUserMainStore();
            entity.setStoreCode(storeCode);
        }
        entity.setOperateTime(new Date());
        entityDao.insert(entity);

    }

    /**
     * 修改
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SingleTag entity) {
        if (entity == null){
            return;
        }
        String userName = WebUtils.getCurrentUserNameWithOrgCode();
        entity.setOperator(userName);
        entity.setOperateTime(new Date());
        entityDao.update(entity);
    }

    /**
     * 根据id查询项目信息
     * @param id
     * @return
     */
    public List<SingleTag> getProjectManageById(Long id,String storeCode) {
        return singleTagDao.getProjectManageById(id,storeCode);
    }

    /**
     * 根据contractCode查询项目信息
     * @param contractCode
     * @return
     */
    public List<SingleTag> getProjectManageByCode(String contractCode,String storeCode) {
        return singleTagDao.getProjectManageByCode(contractCode,storeCode);
    }

    /**
     * 移除
     * @param contractCode
     */
    @Transactional(rollbackFor = Exception.class)
    public int removeSingleTag(String contractCode) {
        ContractSingle contractSingle = singleTagDao.getContractSingleByContractCode(contractCode);
        contractSingle.setDeleted("1");
        int i = singleTagDao.updateContractSingle(contractSingle);
        if(i > 0){
            CustomerContract customerContract = customerContractDao.getByCode(contractCode);
            customerContract.setSingleOrderInfo("");
            customerContract.setSingleId(null);
            customerContractDao.update(customerContract);
            customerContractDao.updateSingleId(customerContract);
        }
        return i;
    }

    /**
     * 串单
     * @param contractCode,id
     */
    @Transactional(rollbackFor = Exception.class)
    public int singleString(String contractCode, Long id) {
        ContractSingle contractSingle = singleTagDao.getContractSingleByContractCode(contractCode);
        CustomerContract customerContract = customerContractDao.getByCode(contractCode);

        SingleTag singleTag = singleTagDao.getById(id);
        if(contractSingle != null){
            contractSingle.setSingleTagId(id.intValue());
            contractSingle.setContractCode(contractCode);
            contractSingle.setDeleted("0");
            customerContract.setSingleOrderInfo(singleTag.getTagName());
            customerContract.setSingleId(id);
            int i = singleTagDao.updateContractSingle(contractSingle);
            if(i > 0){
                customerContractDao.update(customerContract);
            }
            return i;
        }else{
            contractSingle = new ContractSingle();
            contractSingle.setSingleTagId(id.intValue());
            contractSingle.setContractCode(contractCode);
            contractSingle.setDeleted("0");
            customerContract.setSingleOrderInfo(singleTag.getTagName());
            customerContract.setSingleId(id);
            int i = singleTagDao.insertContractSingle(contractSingle);
            if(i > 0){
                customerContractDao.update(customerContract);
            }
            return i;
        }
    }

    /**
     * 查询串单列表
     * @param params
     * @return
     */
    public List<SingleTag> getSingleTagList(Map<String, Object> params) {
        return singleTagDao.getSingleTagList(params);
    }
}
