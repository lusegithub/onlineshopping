package com.onlineshopping.dao;

import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface OrderDao extends BaseDao<Order> {

    /**
     * @param user
     * @return
     */
    List<Order> getOrdersByUser(User user);

    List<Order> getOrdersByBegin(Date beginTime);

    List<Order> getOrdersByEnd(Date endTime);

    List<Order> getOrdersByUserBegin(User user, Date beginTime);

    List<Order> getOrdersByUserEnd(User user, Date endTime);

    List<Order> getOrdersByBeginEnd(Date beginTime, Date endTime);

    List<Order> getOrdersByUserBeginEnd(User user, Date beginTime, Date endTime);

    List<Integer> getOrderIdByBegin(Date beginTime);

    List<Integer> getOrderIdByEnd(Date endTime);

    List<Integer> getOrderIdByBeginEnd(Date beginTime, Date endTime);
}
