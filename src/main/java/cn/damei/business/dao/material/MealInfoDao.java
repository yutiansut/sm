package cn.damei.business.dao.material;


import cn.damei.business.entity.material.MealInfo;
import cn.damei.business.entity.material.Store;
import cn.damei.core.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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