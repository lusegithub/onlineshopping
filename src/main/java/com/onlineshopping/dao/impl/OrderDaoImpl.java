package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.OrderDao;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.User;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by hehe on 17-6-16.
 */
@Repository("OrderDao")
public class OrderDaoImpl extends BaseDaoHibernate<Order> implements OrderDao{

    public List<Order> getOrdersByUser(User user){

        String str = "select e.* from orders e where e.user_id=?1";
        return findUseSql(str,Order.class,user.getUserId());
    }

    @Override
    public List<Order> getOrdersByBegin(Date beginTime) {
        String str = "select e.* from orders e where e.order_time >= ?1";
        List<Order> orderList = findUseSql(str,Order.class,beginTime);
        return orderList;
    }

    @Override
    public List<Order> getOrdersByEnd(Date endTime) {
        String str = "select e.* from orders e where e.order_time < ?1";
        List<Order> orderList = findUseSql(str,Order.class,endTime);
        return orderList;
    }

    @Override
    public List<Order> getOrdersByUserBegin(User user, Date beginTime) {
        String str = "SELECT e.* from orders e,user u WHERE  e.user_id = u.user_id AND e.order_time>=?1 AND e.user_id=?2";
        List<Order> orderList = findUseSql(str,Order.class,beginTime,user.getUserId());
        return orderList;
    }

    @Override
    public List<Order> getOrdersByUserEnd(User user, Date endTime) {
        String str = "SELECT e.* from orders e,user u WHERE  e.user_id = u.user_id AND e.order_time<?1 AND e.user_id=?2";
        List<Order> orderList = findUseSql(str,Order.class,endTime,user.getUserId());
        return orderList;
    }

    @Override
    public List<Order> getOrdersByBeginEnd(Date beginTime, Date endTime) {
        String str = "select e.* from orders e where e.order_time>=?1 AND e.order_time<?2";
        List<Order> orderList = findUseSql(str,Order.class,beginTime,endTime);
        return orderList;
    }

    @Override
    public List<Order> getOrdersByUserBeginEnd(User user, Date beginTime, Date endTime) {
        String str = "SELECT e.* from orders e,user u WHERE  e.user_id = u.user_id AND e.order_time>=?1 AND e.order_time<?2 AND e.user_id=?3";
        List<Order> orderList = findUseSql(str,Order.class,beginTime,endTime,user.getUserId());
        return orderList;
    }

    @Override
    public List<Integer> getOrderIdByBegin(Date beginTime) {
        String str = "SELECT e.order_id from orders e where e.order_time >= ?1";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("order_id", StandardBasicTypes.INTEGER).setParameter(1+"",beginTime).list();
        return (List<Integer>)l;
    }

    @Override
    public List<Integer> getOrderIdByEnd(Date endTime) {
        String str = "SELECT e.order_id from orders e where e.order_time < ?1";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("order_id", StandardBasicTypes.INTEGER).setParameter(1+"",endTime).list();
        return (List<Integer>)l;
    }

    @Override
    public List<Integer> getOrderIdByBeginEnd(Date beginTime, Date endTime) {
        String str = "SELECT e.order_id from orders e,user u WHERE  e.user_id = u.user_id AND e.order_time>=?1 AND e.order_time<?2";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("order_id", StandardBasicTypes.INTEGER).setParameter(1+"",beginTime).setParameter(2+"",endTime).list();
        return (List<Integer>)l;
    }
}
