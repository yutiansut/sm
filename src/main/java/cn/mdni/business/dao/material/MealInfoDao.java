package cn.mdni.business.dao.material;


import cn.mdni.business.entity.material.MealInfo;
import cn.mdni.business.entity.material.Store;
import cn.mdni.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @describe 套餐dao
 * @author 张晗
 * @date 2017-11-2 11:30:32
 *
 */
@Repository
public interface MealInfoDao extends CrudDao<MealInfo> {


    List<Store> getStoreList();

    /**
     * 查询所有状态为开启的套餐
     * @return
     */
    List<MealInfo> findAllMeal();

    /**
     * 获取当前门店的可用套餐
     *@param storeCode 门店code
     * @return
     */
    List<MealInfo> findMealByStoreCode(@Param("storeCode") String storeCode);

}