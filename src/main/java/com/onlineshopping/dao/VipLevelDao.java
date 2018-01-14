package com.onlineshopping.dao;

import com.onlineshopping.entity.VipLevel;
import com.onlineshopping.entity.LevelType;

import java.util.List;
import java.util.logging.Level;

/**
 * Created by hehe on 17-6-9.
 */
public interface VipLevelDao {

    /**
     *
     * @param vipLevel
     * @return
     */
    void  updateVipLevel(VipLevel vipLevel);

    /**
     *
     * @param levelType : the enumeration type
     * @return
     */
    VipLevel getVipLevelByLevelType(LevelType levelType);

    /**
     * 获取所有等级信息
     * @return
     */
    List<VipLevel> getAll();
}
