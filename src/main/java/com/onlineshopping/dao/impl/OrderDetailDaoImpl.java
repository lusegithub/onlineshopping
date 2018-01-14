package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.dao.OrderDao;
import com.onlineshopping.dao.OrderDetailDao;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.OrderDetail;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hehe on 17-6-16.
 */
@Repository("OrderDetailDao")
public class OrderDetailDaoImpl extends BaseDaoHibernate<OrderDetail> implements OrderDetailDao{
    @Resource(name="GoodsDao")
    private GoodsDao goodsDao;
    @Resource(name="OrderDao")
    private OrderDao orderDao;

    @Override
    public List<OrderDetail> getOrderDetailByOrder(Order order) {
        String str ="select o.* from order_detail o where o.order_id="+order.getOrderId();
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("goods_id", StandardBasicTypes.STRING).addScalar("goods_name",StandardBasicTypes.STRING).addScalar("goods_price",StandardBasicTypes.DOUBLE).addScalar("goods_number",StandardBasicTypes.INTEGER).list();
        if(l.size()!=0){
            List<OrderDetail> orderDetailsList = new ArrayList<>();
            for(Object tmp:l){
                OrderDetail orderDetail = new OrderDetail();
                Object[] ele = (Object[])tmp;
                orderDetail.setOrder(order);
                orderDetail.setGoods(goodsDao.get(Goods.class,(String)ele[0]));
                orderDetail.setGoodsPrice((Double)ele[2]);
                orderDetail.setGoodsName((String)ele[1]);
                orderDetail.setGoodsNumber((Integer)ele[3]);
                orderDetailsList.add(orderDetail);
            }

            return orderDetailsList;
        }
        return null;
    }



    @Override
    public List<OrderDetail> getOrderDetailByCatalog(Catalog catalog) {

        String str = "SELECT o.* from order_detail  o INNER JOIN goods g on g.goods_id = o.goods_id where g.CATALOG_NAME ='"+catalog.getName()+"'";
        return findOrderDetailUseSql(str);
    }



    @Override
    public List<OrderDetail> getOrderDetailByOrderIdList(List<Integer> OrderIdList) {
        StringBuilder str = new StringBuilder("select e.* from order_detail e where e.order_id IN (");
        int sign=0;

        for(int tmp:OrderIdList){
            if(sign==0){
                sign=1;
                str.append(tmp);
            }else {
                str.append("," + tmp);
            }
        }
                str.append(")");

        return findOrderDetailUseSql(str.toString());

    }



    @Override
    public List<OrderDetail> getOrderDetailByCatalogOrderIdList(Catalog catalog, List<Integer> orderIdList) {
        StringBuilder str = new StringBuilder();
        String sql = "SELECT o.* from order_detail  o INNER JOIN goods g on g.goods_id = o.goods_id where g.CATALOG_NAME ='"+catalog.getName()+"' AND o.order_id IN (";
        str.append(sql);
        int sign=0;
        for(int tmp:orderIdList){
            if(sign==0){
                sign=1;
                str.append(tmp);
            }else {
                str.append("," + tmp);
            }
        }
        str.append(")");

        return findOrderDetailUseSql(str.toString());

    }

    private List<OrderDetail> findOrderDetailUseSql(String str){

        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("goods_id", StandardBasicTypes.STRING).addScalar("goods_name",StandardBasicTypes.STRING).addScalar("goods_price",StandardBasicTypes.DOUBLE).addScalar("goods_number",StandardBasicTypes.INTEGER).addScalar("order_id",StandardBasicTypes.INTEGER).list();
        if(l.size()!=0){
            List<OrderDetail> orderDetailsList = new ArrayList<>();
            for(Object tmp:l){
                OrderDetail orderDetail = new OrderDetail();
                Object[] ele = (Object[])tmp;
                orderDetail.setOrder(orderDao.get(Order.class,(Integer)ele[4]));
                System.out.println((String)ele[0]);
                orderDetail.setGoods(goodsDao.get(Goods.class,(String)ele[0]));
                orderDetail.setGoodsPrice((Double)ele[2]);
                orderDetail.setGoodsName((String)ele[1]);
                orderDetail.setGoodsNumber((Integer)ele[3]);
                orderDetailsList.add(orderDetail);
            }

            return orderDetailsList;
        }
        return null;
    }


    @Override
    public List<OrderDetail> getAll() {
        String str = "SELECT * from order_detail";
        List<OrderDetail> l = findOrderDetailUseSql(str);
        return l;
    }
}
