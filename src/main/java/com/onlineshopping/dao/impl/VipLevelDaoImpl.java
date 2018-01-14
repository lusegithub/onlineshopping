package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.VipLevelDao;
import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.VipLevel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hehe on 17-6-17.
 */
@Repository("VipLevelDao")
public class VipLevelDaoImpl extends BaseDaoHibernate<VipLevel> implements VipLevelDao{
    @Override
    public void updateVipLevel(VipLevel vipLevel) {
        update(vipLevel);

    }

    @Override
    public VipLevel getVipLevelByLevelType(LevelType levelType) {
        /*System.out.print(levelType.toString());
        String str="select e.* from vip_level e where e.level=?1";
        List l = getSessionFactory().getCurrentSession().createSQLQuery()*/

        List<VipLevel> vipLevelList =findAll(VipLevel.class);
        if(vipLevelList.size()!=0) {
            for (VipLevel tmp : vipLevelList) {
                if (tmp.getLevel().equals(levelType))
                    return tmp;
            }
        }
        return null;

    }

    @Override
    public List<VipLevel> getAll() {

        return findAll(VipLevel.class);
    }
}
