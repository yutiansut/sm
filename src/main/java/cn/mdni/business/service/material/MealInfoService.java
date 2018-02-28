package cn.mdni.business.service.material;



import cn.mdni.business.dao.material.MealInfoDao;
import cn.mdni.business.entity.material.MealInfo;
import cn.mdni.business.entity.material.Store;
import cn.mdni.core.WebUtils;
import cn.mdni.core.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @describe 套餐 Service
 * @author 张晗
 * @date 2017-11-2 11:22:00
 *
 */
@Service
public class MealInfoService extends CrudService<MealInfoDao, MealInfo> {

    @Autowired
    private MealInfoDao mealInfoDao;


    /**
     * 增加
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(MealInfo entity) {
        if (entity == null)
            return;
        String userName = WebUtils.getCurrentUserNameWithOrgCode();
        entity.setCreateUser(userName);
        entity.setCreateTime(new Date());
        entityDao.insert(entity);

    }

    /**
     * 修改
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MealInfo entity) {
        if (entity == null){
            return;
        }
        String userName = WebUtils.getCurrentUserNameWithOrgCode();
        entity.setUpdateUser(userName);
        entity.setUpdateTime(new Date());
        entityDao.update(entity);
    }


    /**
     * 获取门店
     * @return
     */
    public List<Store> getStoreList() {

        return mealInfoDao.getStoreList();
    }

    /**
     * 关闭-开启
     * @param entity
     */
    public void changeStatus(MealInfo entity) {
        if("1".equals(entity.getMealStatus())){
            entity.setMealStatus("0");
        }else{
            entity.setMealStatus("1");
        }
        String userName = WebUtils.getCurrentUserNameWithOrgCode();
        entity.setUpdateUser(userName);
        entity.setUpdateTime(new Date());
        this.entityDao.update(entity);
    }

    /**
     * 查询所有的状态为开启的套餐
     * @return
     */
    public List<MealInfo> findAllMeal(){
        return this.entityDao.findAllMeal();
    }

    /**
     * 根据门店查询 可选套餐
     * @return
     */
    public List<MealInfo> findMealByStoreCode(String storeCode) {
        return this.entityDao.findMealByStoreCode(storeCode);
    }

}
