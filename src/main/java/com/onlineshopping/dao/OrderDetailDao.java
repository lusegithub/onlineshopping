package com.onlineshopping.dao;

import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.OrderDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface OrderDetailDao extends BaseDao<OrderDetail>{

    /**
    *
    * @param order
    * @return
    */
    List<OrderDetail> getOrderDetailByOrder(Order order);



    List<OrderDetail> getAll();



    List<OrderDetail> getOrderDetailByCatalog(Catalog catalog);



    List<OrderDetail> getOrderDetailByOrderIdList(List<Integer> OrderIdList);



    List<OrderDetail> getOrderDetailByCatalogOrderIdList(Catalog catalog, List<Integer> orderIdList);
}
