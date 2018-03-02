package cn.damei.business.service.finance;

import cn.damei.business.constants.CommonStatusEnum;
import cn.damei.business.constants.Constants;
import cn.damei.business.dao.finance.FinaPaymethodDao;
import cn.damei.business.dto.finance.FinaPaymethodDto;
import cn.damei.business.entity.finance.*;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.dto.BootstrapPage;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FinaPaymethodService extends CrudService<FinaPaymethodDao, FinaPaymethod> {
    @Autowired
    private FinaPaymethodDao finaPaymethodDao;

    /**
     * 修改的逻辑
     *
     * @param entity 前台手动输入的实体数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateFinaPaymethod(FinaPaymethod entity) {
        if (entity == null) {
            return 1;
        }


        //先查看关系表中是否有这个属性的门店 有则删除
        List<FinaPaymethodStore> finaPaymethodStores = finaPaymethodDao.getFinaPaymethodStoreById(entity.getMethodCode());
        if (null != finaPaymethodStores && finaPaymethodStores.size() > 0) {
            int flg = entityDao.deletePaymethodStoreById(entity.getMethodCode());
        }

        //获取前台修改过的门店数组
        String[] storeIds = entity.getStoreIds();
        List<FinaPaymethodStore> finaPaymethodStoreList = getFinaPaymethodStoreList(entity);
        if (null != finaPaymethodStoreList && finaPaymethodStoreList.size() > 0)
            entityDao.addTrainRecordBatch(finaPaymethodStoreList);



        //修改通用相关字段属性表
        String userName = WebUtils.getCurrentUserNameWithOrgCode();
        entity.setCreator(userName);
        entity.setCreateTime(new Date());
        entityDao.update(entity);

        return 0;
    }

    /**
     * 根据前台传的门店集合组装成List集合用于批量插入关系表(fina_paymethod_store)方法
     *
     * @param entity 前台手动输入的实体
     * @return
     */
    private List<FinaPaymethodStore> getFinaPaymethodStoreList(FinaPaymethod entity) {
        String[] storeIds = entity.getStoreIds();
        List<FinaPaymethodStore> finaPaymethodStoreList = new ArrayList<FinaPaymethodStore>();
        if (null != storeIds && storeIds.length > 0) {
            for (int i = 0; i < storeIds.length; i++) {
                FinaPaymethodStore finaPaymethodStore = new FinaPaymethodStore();
                if (!StringUtils.isEmpty(entity.getMethodCode()) && !StringUtils.isEmpty(storeIds[i])) {
                    finaPaymethodStore.setPaymethodCode(entity.getMethodCode());
                    finaPaymethodStore.setPaymethodId(null == entity.getId() ? "" : entity.getId().toString());
                    finaPaymethodStore.setStoreCode(storeIds[i]);
                    finaPaymethodStoreList.add(finaPaymethodStore);
                }
            }
        }
        return finaPaymethodStoreList;
    }

    /**
     * 新增的逻辑
     *
     * @param entity 前台收送输入的实体数据
     * @return
     */
    public int insertFinaPaymethod(FinaPaymethod entity) {
        if (entity == null)
            return 1;
        //插入的时候为了保证编码的唯一每次插入前都会效验该编码是否已经存在如果存在返回重复标示,不存在这插入通用表和关系表
        if (null == entity || finaPaymethodDao.isEmptyCode(entity.getMethodCode()) != null) {
            return 2;
        } else {
            String userName = WebUtils.getCurrentUserNameWithOrgCode();
            entity.setCreator(userName);
            entity.setCreateTime(new Date());
            entityDao.insert(entity);
            List<FinaPaymethodStore> finaPaymethodStoreList = getFinaPaymethodStoreList(entity);
            entityDao.addTrainRecordBatch(finaPaymethodStoreList);
            return 0;

        }
    }

    public List<TreeNode> getTreeNodes(String parentId) {
        if (null == parentId || parentId.equals("")) {
            parentId = "0";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", parentId);
        List<TreeNode> treeNodes = finaPaymethodDao.getTreeNodes(parentId);
        if (treeNodes != null) {
            TreeNode treeNode = null;
            for (int i = 0; i < treeNodes.size(); i++) {
                treeNode = treeNodes.get(i);
                List<TreeNode> treeNodemus = finaPaymethodDao.getTreeNodes(treeNode.getId());
                if (null == treeNodemus || treeNodemus.size() <= 0) {
                    treeNode.setIsParent(false);
                } else {
                    treeNode.setIsParent(true);
                }
            }
        }
        return treeNodes;
    }


    /**
     * 查询指定门店、指定阶段允许使用的交款方式
     *
     * @param storeCode
     * @param stageCode
     * @return
     */
    public List<FinaPaymethod> queryPayMethodListWithStoreAndStage(String storeCode, String stageCode) {
        if (StringUtils.isNotBlank(stageCode)) {
            StringBuffer stageCodeSb = new StringBuffer();
            stageCodeSb.append("%,").append(stageCode).append(",%");
            stageCode = stageCodeSb.toString();
        }
        return entityDao.queryPayMethodWithStoreAndStage(storeCode, stageCode);
    }


    public List<FinaPaymethodTree> findAllWithDeleted(Long id) {
        return finaPaymethodDao.findAllWithDeleted(id);
    }

    /**
     * 效验编码是否重复的方法
     *
     * @param finaPaymethodTree 前台手动输入的实体数据
     * @return
     */
    public boolean validateCodeAvailable(FinaPaymethodTree finaPaymethodTree) {
        //首先判断实体和code是否为空,然后判断id是否为空,id为代表是修改 直接放回false最后再根据code查询是否重复
        if (finaPaymethodTree == null || StringUtils.isBlank(finaPaymethodTree.getCode())) {
            return false;
        } else if (null != finaPaymethodTree.getId()) {
            return false;
        } else {
            return finaPaymethodDao.getByCode(finaPaymethodTree.getCode()) != null;
        }
    }

    /**
     * 修改或者插入特殊配置(表名:fina_paymethod_attr)
     *
     * @param finaPaymethodTree 前台手动输入的实体数据()
     */
    public void saveOrUpdate(FinaPaymethodTree finaPaymethodTree) {
        //首先判断实体是否为空,其次判断id是否为空不为空为修改,反之为插入
        if (finaPaymethodTree == null) {
            return;
        } else if (null != finaPaymethodTree.getId()) {
            finaPaymethodDao.updateFinaPaymethodTree(finaPaymethodTree);
        } else {
            String userName = WebUtils.getCurrentUserNameWithOrgCode();
            finaPaymethodTree.setCreator(userName);
            finaPaymethodTree.setCreateTime(new Date());
            finaPaymethodDao.insertFinaPaymethodTree(finaPaymethodTree);
        }
    }

    /**
     * 获取单个节点的数据
     *
     * @param id
     * @return
     */
    public FinaPaymethodTree getByIdTree(Long id) {
        return finaPaymethodDao.getByIdTree(id);
    }

    /**
     * 根据ID查询当前ID下是否有子节点
     *
     * @param id
     * @return
     */
    public boolean checkHasChildNode(Long id) {
        return finaPaymethodDao.getTreeNodes(id + "").size() == 0 ? false : true;
    }

    public int deleteFinaPaymethod(FinaPaymethodTree finaPaymethodTree) {
        return finaPaymethodDao.deleteFinaPaymethod(finaPaymethodTree.getId());
    }

    /**
     * 组装数据 组装成前台下拉框你能识别的数据
     *
     * @return
     */
    public List<Map<String, String>> queryStoreAll() {

        List<Map<String, String>> lists = new ArrayList<>();

        List<FinaPaymethodStore> finaPaymethodStores = finaPaymethodDao.queryStoreAll();

        for (FinaPaymethodStore e : finaPaymethodStores) {
            Map<String, String> maps = new HashMap<>();
            maps.put("value", e.getStoreCode());
            maps.put("text", e.getStoreName());
            lists.add(maps);
        }

        return lists;
    }
    /**
     * 组装数据 组装成前台下拉框你能识别的数据
     *
     * @return
     */
    public List<Map<String, String>> fetchMethodType() {
        List<Map<String, String>> lists = new ArrayList<>();
        for (FinaPaymethod.PaymethodTypeEnum e : FinaPaymethod.PaymethodTypeEnum.values()) {
            Map<String, String> maps = new HashMap<>();
            maps.put("value", e.toString());
            String methodStatus = getMethodType(e.toString());
            if (!StringUtils.isEmpty(methodStatus)) {
                maps.put("text", methodStatus);
                lists.add(maps);
            }
        }
        return lists;
    }

    /**
     * 组装通用状态数据
     *
     * @return
     */
    public List<Map<String, String>> fetchMethodStatus() {
        List<Map<String, String>> lists = new ArrayList<>();
        for (CommonStatusEnum e : CommonStatusEnum.values()) {
            Map<String, String> maps = new HashMap<>();
            maps.put("value", e.toString());
            String methodStatus = getMethodStatus(e.toString());
            if (!StringUtils.isEmpty(methodStatus)) {
                maps.put("text", methodStatus);
                lists.add(maps);
            }
        }
        return lists;
    }

    /**
     * 获取通用配的状态
     * 根据传过来的类型返回对应类型的汉字 (  此方法可用于添加,修改,查询,导出等数据转换)
     *
     * @param src
     * @return
     */
    private String getMethodStatus(String src) {
        if (src.toString().equals("ENABLE")) {
            return "启用";
        } else if (src.toString().equals("DISABLE")) {
            return "禁用";
        } else {
            return null;
        }
        //注释的部分是枚举中全部变量 说不定以后会用就没有删除
/*        else if(src.toString ().equals ("VALID")){
           return"有效";
        }else if(src.toString ().equals ("NVALID")){
           return"无效";
        }else if(src.toString ().equals ("NORMAL")){
           return"正常";
        }else if(src.toString ().equals ("DELETED")){
            return"已删除";
        }*/

    }


    /**
     * 获取特殊配的状态
     * 根据传过来的类型返回对应类型的汉字 (  此方法可用于添加,修改,查询,导出等数据转换)
     *
     * @param src
     * @return
     */
    private String getAttrStatusString(String src) {

        if (src.toString().equals("NORMAL")) {
            return "正常";
        } else if (src.toString().equals("DELETED")) {
            return "已删除";
        } else {
            return null;
        }

    }

    /**
     * 根据传过来的类型返回对应类型的汉字 (  此方法可用于添加,修改,查询,导出等数据转换)
     *
     * @param src
     * @return
     */
    private String getMethodType(String src) {

        if (src.toString().equals("BANKTRANS")) {
            return "银行转账";
        } else if (src.toString().equals("CASH")) {
            return "现金";
        } else if (src.toString().equals("THIRDPAY")) {
            return "第三方支付";
        } else if (src.toString().equals("POS")) {
            return "POS机";
        } else if (src.toString().equals("DEDUCT")) {
            return "抵扣";
        } else if (src.toString().equals("DEPOSITDEDUCT")) {
            return "预付款抵扣";
        } else if (src.toString().equals("CHECK")) {
            return "支票";
        } else if (src.toString().equals("OTHER")) {
            return "其他";
        } else {
            return null;
        }
    }

    /**
     * 根据ID查询通用表(fina_paymethod)数据
     * @param id
     * @return
     */
    public FinaPaymethod getFinaPaymethodById(Long id) {



        //查询通用表的数据
        FinaPaymethod finaPaymethod = finaPaymethodDao.getById(id);
        //根据通用表的code(唯一标示)查询当前code下对应的门店然后遍历组装到查询到的实体中用于回显
        List<FinaPaymethodStore> finaPaymethodStores = finaPaymethodDao.getFinaPaymethodStoreById(finaPaymethod.getMethodCode());
        if (null != finaPaymethodStores && finaPaymethodStores.size() > 0) {
            String[] arrstoreIds = new String[20];
            for (int i = 0; i < finaPaymethodStores.size(); i++) {
                FinaPaymethodStore finaPaymethodStore = finaPaymethodStores.get(i);
                arrstoreIds[i] = finaPaymethodStore.getStoreCode();
            }
            finaPaymethod.setStoreIds(arrstoreIds);
        }
        return finaPaymethod;
    }

    /**
     * 查询通用配置 (表名fina_paymethod)
     *
     * @param storeCode   前台传的门店ID
     * @param offset      分页的当前页
     * @param limit       每页多少条数据
     * @param orderColumn 排序的字段
     * @param orderSort   倒叙或者顺序排序
     * @return
     */
    public BootstrapPage<FinaPaymethod> queryFinaPaymethodAll(String storeCode, int offset, int limit, String orderColumn, String orderSort) {
        Map<String, Object> params = Maps.newHashMap();
        MapUtils.putNotNull(params, "storeCode", storeCode);
        params.put(Constants.PAGE_OFFSET, offset);
        params.put(Constants.PAGE_SIZE, limit);
        params.put(Constants.PAGE_SORT, new Sort(Sort.Direction.valueOf(orderSort.toUpperCase()), orderColumn));
        List<FinaPaymethod> pageData = Collections.emptyList();
        //查询所有的数据用于前台显示数据总数
        Long count = this.entityDao.searchTotal(params);
        if (count > 0) {
            //根据分页或者查询条件查出的数据
            pageData = entityDao.search(params);
            //这个循环式为了转换类型和状态两个字段
            for (FinaPaymethod pageDatum : pageData) {
                pageDatum.setMethodStatus(getMethodStatus(pageDatum.getMethodStatus()));
                pageDatum.setMethodType(getMethodType(pageDatum.getMethodType()));
            }
        }
        return new BootstrapPage(count, pageData);
    }

    /**
     * 保存或者修改的逻辑
     *
     * @param entity 前台手动输入的字段使用实体接收
     * @return
     */
    public int getResult(FinaPaymethod entity) {
        int resultInt = 1;
        //如果id为空就是新增反之为修改
        if (null != entity && null != entity.getId() && entity.getId() > 0) {
            resultInt = this.updateFinaPaymethod(entity);
        } else {
            resultInt = this.insertFinaPaymethod(entity);
        }
        return resultInt;
    }

    /**
     * 查询通用表(fina_paymethod)下特殊配置的逻辑
     *
     * @param id
     * @return
     */
    public List<FinaPaymethodDto> queyTree(Long id) {
        //查询出当前支付方式下树的全量
        List<FinaPaymethodTree> list = this.findAllWithDeleted(id);
        //将0或者为空作为父id,构建树
        FinaPaymethodDto tree = FinaPaymethodTree.buildDictTree(new FinaPaymethodDto(0L), list);
        List<FinaPaymethodDto> childrenList = tree.getChildren();
        //一级树是否展开的逻辑
        if (childrenList != null && childrenList.size() > 0) {
            for (FinaPaymethodDto dict : childrenList) {
                dict.setState(new FinaPaymethoStatus(true, false));
            }
        }
        return childrenList;
    }

    /**
     * 组装特殊状态的数据
     *
     * @return
     */
    public List<Map<String, String>> getAttrStatus() {
        List<Map<String, String>> lists = new ArrayList<>();
        for (CommonStatusEnum e : CommonStatusEnum.values()) {
            Map<String, String> maps = new HashMap<>();
            maps.put("value", e.toString());
            String methodStatus = getAttrStatusString(e.toString());
            if (!StringUtils.isEmpty(methodStatus)) {
                maps.put("text", methodStatus);
                lists.add(maps);
            }
        }
        return lists;
    }
}
