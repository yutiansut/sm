package cn.mdni.business.service.finance;

import cn.mdni.business.constants.CommonStatusEnum;
import cn.mdni.business.constants.Constants;
import cn.mdni.business.constants.FinancePayTypeEnum;
import cn.mdni.business.dao.finance.PayplanDao;
import cn.mdni.business.dao.finance.PayplanItemDao;
import cn.mdni.business.entity.finance.Payplan;
import cn.mdni.business.entity.finance.PayplanItem;
import cn.mdni.commons.collection.MapUtils;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liupengfei
 * @Description 交款规划模板service类
 * @Date Created in 2017/11/18 22:48
 */
@Service
public class PayplanService extends CrudService<PayplanDao,Payplan> {

    @Autowired
    private PayplanItemDao payplanItemDao;


    /**
     * 判断当前门店使用的交款规划是否需要交定金
     * @param storeCode 门店编号
     * @return
     */
    public boolean ifProjectNeedDeposit(String storeCode){
        PayplanItem depositItem = payplanItemDao.getFirstItemOfPlanWithStore(storeCode, CommonStatusEnum.NORMAL.toString());
        return depositItem != null && FinancePayTypeEnum.DEPOSIT.toString().equals(depositItem.getItemType());
    }


    /**
     * 获取该门店当前使用的交款规划中的定金规划节点
     * @param storeCode 门店编号
     * @param status 节点的状态
     * @return
     */
    public PayplanItem getFirstItemOfPlanWithStore(String storeCode,String status){
        return payplanItemDao.getFirstItemOfPlanWithStore(storeCode,status);
    }


    /**
     * 根据交款规划阶段模版编号查询阶段模版
     * @param itemId
     * @return
     */
    public PayplanItem getPlanItemWithItemId(Long itemId) {
        return payplanItemDao.getById(itemId);
    }


    /**
     * 通过交款规划模板编号构造具体节点顺序流程
     * @return
     */
    public ArrayList<PayplanItem> queryPayPlanItemFlowWithPlanCode(String tmpPayPlanCode){
        Map<String,Object> queryMap = new HashMap();
        MapUtils.putNotNull(queryMap,"planCode",tmpPayPlanCode);
        MapUtils.putNotNull(queryMap,"itemStatus",CommonStatusEnum.NORMAL.toString());
        //得到排好序的规划节点
        MapUtils.putNotNull(queryMap,Constants.PAGE_SORT, new Sort(Sort.Direction.ASC,"item_index"));
        List<PayplanItem> sortedPlanItemFlow = payplanItemDao.search(queryMap);
        return new ArrayList<PayplanItem>(sortedPlanItemFlow);
    }


    /**
     * 通过门店编号构造该门店当前使用的交款规划具体节点顺序流程
     * @param storeCode
     * @return
     */
    public ArrayList<PayplanItem> queryPayPlanItemFlowWithStore(String storeCode){
        List<PayplanItem> sortedPlanItemFlow = payplanItemDao.querySortedPlanItemListWithStore(storeCode,CommonStatusEnum.NORMAL.toString());
        return new ArrayList<PayplanItem>(sortedPlanItemFlow);
    }

    /**
     * 获取 交款阶段 是否需要推送到产业工人
     * @param id 阶段id
     * @return
     */
    public boolean getNeedPushToMps(Long id){
        return this.payplanItemDao.getNeedPushToMps(id);
    }

}
