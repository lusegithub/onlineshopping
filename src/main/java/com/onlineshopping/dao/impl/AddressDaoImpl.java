package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.AddressDao;
import com.onlineshopping.entity.Address;
import com.onlineshopping.entity.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by hehe on 17-6-17.
 */
@Repository("AddressDao")
public class AddressDaoImpl extends BaseDaoHibernate<Address> implements AddressDao {
    @Override
    public List<Address> getAddressByUser(User user) {
        String str = "select e.* from address e where e.user_id=?1";

        return findUseSql(str,Address.class,user.getUserId());
    }

    @Override
    public void deleteAddressByUser(User user) {
        String str = "delete from address where user_id='"+user.getUserId()+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }

    @Override
    public int countAddressByUser(User user) {
        String str = "select COUNT(*) from address e where e.user_id='"+user.getUserId()+"'";
        List list = getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        return ((BigInteger)(list.get(0))).intValue();
    }
}
