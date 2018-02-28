package cn.mdni.business.service.material;

import cn.mdni.business.dao.material.SmChangeDetailDao;
import cn.mdni.business.dao.material.ProjectChangeMaterialDao;
import cn.mdni.business.entity.material.SmChangeDetail;
import cn.mdni.business.entity.material.ProjectChangeMaterial;
import cn.mdni.core.base.service.CrudUUIDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <dl>
 * <dd>Description: 美得你智装 变更的项目主材</dd>
 * <dd>@date：2017/11/7 11:20</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class ProjectChangeMaterialService extends CrudUUIDService<ProjectChangeMaterialDao, ProjectChangeMaterial> {
    @Autowired
    private SmChangeDetailDao smChangeDetailDao;
    @Autowired
    private ProjectChangeMaterialDao projectChangeMaterialDao;

    /**
     * @Description: 美得你智装 通过订单编号和类别 获取审核信息
     * @date: 2017/11/15  15:15
     * @param categoryUrl 类别
     * @param contractCode 编号
     * @author: Ryze
     */
    public List<ProjectChangeMaterial> getAuditList(String categoryUrl, String contractCode){
        List<ProjectChangeMaterial> listByCode = entityDao.getAuditList(categoryUrl,contractCode);
        List<ProjectChangeMaterial> returnList = new ArrayList<>();
        return getProjectChangeMaterials(listByCode, returnList,true);
    }
    /**
     * @Description: 美得你智装 批量修改
     * @date: 2017/11/16  15:14
     * @param list
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(ArrayList<ProjectChangeMaterial> list) {
        for (ProjectChangeMaterial item :list) {
            entityDao.update(item);
        }
    }
    /**
     * @Description: 美得你智装 审批提交修改状态
     * @date: 2017/11/16  16:39
     * @param smChangeDetail
     * @param list
     * @author: Ryze
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitAuidt(SmChangeDetail smChangeDetail, ArrayList<ProjectChangeMaterial> list) {
        smChangeDetailDao.update(smChangeDetail);
        if(list!=null && list.size()>0) {
            batchUpdate(list);
        }
    }

    /**
     * 通过合同编码查询  所有 项目主材sku 及 其下面的所有 sku用量信息
     * @param materialParams
     * @return
     */
    public List<ProjectChangeMaterial> findWithSubListByMaterialParams(Map<String, Object> materialParams) {
        return projectChangeMaterialDao.findWithSubListByMaterialParams(materialParams);
    }

    /**
     * 根据类目查询本次变更单的数据
     * @param changeCategoryUrl
     * @return
     */
    public List<ProjectChangeMaterial> getBychangeCategoryUrl(String changeCategoryUrl,String contractCode) {
        return this.entityDao.getBychangeCategoryUrl(changeCategoryUrl,contractCode);
    }

    public void updateMaterialStatus(String contractCode, String changeCategoryUrl, String status) {
        this.entityDao.updateMaterialStatus(contractCode,changeCategoryUrl,status);
    }

    /**
     * 删除临时主材表数据
     * @param contractCode
     */
    public void delete(String contractCode) {
        this.entityDao.delete(contractCode);
    }

    /**
     * @Description: 美得你智装 通过订单编号 获取变化列表(类别 都是1级的 截取过的)
     * @date: 2017/11/15  15:15
     * @param contractCode 编号
     * @author: Ryze
     */
    public List<ProjectChangeMaterial> getListByCode(String contractCode){
        List<ProjectChangeMaterial> listByCode = entityDao.getListByCode(contractCode);
        List<ProjectChangeMaterial> returnList = new ArrayList<>();
        return getProjectChangeMaterials(listByCode, returnList,false);
    }

    private List<ProjectChangeMaterial> getProjectChangeMaterials(List<ProjectChangeMaterial> listByCode, List<ProjectChangeMaterial> returnList, Boolean isAll) {
        if(listByCode!=null && listByCode.size()>0) {
            //1个材料对应多个用量 所有会有问题 在这里进行汇总
            Map<String, List<ProjectChangeMaterial>> collect = listByCode.stream().collect(Collectors.groupingBy(a -> a.getId()));
            for (Map.Entry<String, List<ProjectChangeMaterial>> entry:collect.entrySet()) {
                List<ProjectChangeMaterial> value = entry.getValue();
               if(value!=null && value.size()>0) {
                   value = value.stream().filter(a -> a.getLossDosage() != null && a.getOriginalDosage() != null).collect(Collectors.toList());
                   if(value!=null && value.size()>0){
                       ProjectChangeMaterial projectChangeMaterial = value.get(0);
                       Long lossDosage = value.stream().map(a -> a.getLossDosage()).reduce(0L, Long::sum);
                       Long originalDosage = value.stream().map(a -> a.getOriginalDosage()).reduce(0L, Long::sum);
                       BigDecimal num = BigDecimal.valueOf(lossDosage-originalDosage);
                       projectChangeMaterial.setLossDosage(lossDosage);
                       projectChangeMaterial.setOriginalDosage(originalDosage);
                       projectChangeMaterial.setNum(num);
                       if(isAll){
                           String productCategoryUrl = projectChangeMaterial.getProductCategoryUrl();
                           if(productCategoryUrl!=null){
                               productCategoryUrl=productCategoryUrl.substring(0,productCategoryUrl.indexOf("-",productCategoryUrl.indexOf("-")+1)+1);
                           }
                           Integer materialIsCheckScale = entityDao.getMaterialIsCheckScale(productCategoryUrl);
                           //是定制则 全部
                           if(materialIsCheckScale!=null && materialIsCheckScale.intValue()==1){
                               returnList.add(projectChangeMaterial);
                           }else {
                               //不是定制
                               //去掉数量没变化的
                               if (num.compareTo(BigDecimal.ZERO) == 1 || num.compareTo(BigDecimal.ZERO) == -1) {
                                   returnList.add(projectChangeMaterial);
                               }
                           }
                       }else{
                           //去掉数量没变化的
                           if (num.compareTo(BigDecimal.ZERO) == 1 || num.compareTo(BigDecimal.ZERO) == -1) {
                               returnList.add(projectChangeMaterial);
                           }
                       }
                   }
               }
            }
            return returnList ;
        }else{
            return listByCode;
        }
    }

    /**
     * 查询套餐标配
     * @param contractCode
     * @return
     */
    public List<Map<String,Object>> getPackagestandardByContractCode(String contractCode) {
        return this.entityDao.getPackagestandardByContractCode(contractCode);
    }

    /**
     * 查询升级项的主材
     * @param contractCode
     * @return
     */
    public List<Map<String,Object>> getUpMaterialByContractCode(String contractCode) {
        return this.entityDao.getUpMaterialByContractCode(contractCode);
    }

    /**
     * 查询增项
     * @param contractCode
     * @return
     */
    public List<Map<String,Object>> getAddItemByContractCode(String contractCode,String addItemType) {
        return this.entityDao.getAddItemByContractCode(contractCode,addItemType);
    }

    /**
     * 查询增项的基装定额
     * @param contractCode
     * @param
     * @return
     */
    public List<Map<String,Object>> getProIntemByContractCode(String contractCode,String categoryCode, String categoryDetailCode) {
        return this.entityDao.getProIntemByContractCode(contractCode,categoryCode,categoryDetailCode);
    }

    /**
     * 查询减项
     * @param contractCode
     * @return
     */
    public List<Map<String,Object>> getReduceitemByContractCode(String contractCode,String reduceItem) {
        return this.entityDao.getReduceitemByContractCode(contractCode,reduceItem);
    }

    /**
     * 查询其他综合费
     * @param contractCode
     * @return
     */
    public List<Map<String,Object>> getOthercateFeesByContractCode(String contractCode) {
        return this.entityDao.getOthercateFeesByContractCode(contractCode);
    }

    /**
     * 查询老房拆除基装定额
     *
     * @param contractCode
     * @return
     */
    public List<Map<String, Object>> getDismantlebaseinstallquotaByContractCode(String contractCode, String oldHouseType) {
        return this.entityDao.getDismantlebaseinstallquotaByContractCode(contractCode, oldHouseType);
    }

    public List<ProjectChangeMaterial> getByContractCode(String contractCode) {
        return this.entityDao.getByContractCode(contractCode);
    }

    /**
     * 查询临时表的变更主材
     * @param contractCode
     * @return
     */
    public List<ProjectChangeMaterial> getMaterial(String contractCode,String changeCategoryUrl) {
        return this.entityDao.getMaterial(contractCode, changeCategoryUrl);
    }

    public void deleteByIds(List<String> ids) {
        this.entityDao.deleteByIds(ids);
    }

    public void deleteByConAndUrl(String contractCode, String changeCategoryUrl) {
         this.entityDao.deleteByConAndUrl(contractCode,changeCategoryUrl);
    }

    public static void main(String[] args) {
        String productCategoryUrl="28-90-90-";

        System.out.println( productCategoryUrl.substring(0,productCategoryUrl.indexOf("-",productCategoryUrl.indexOf("-")+1)+1));

    }
}
