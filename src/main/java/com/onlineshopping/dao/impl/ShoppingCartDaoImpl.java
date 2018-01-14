package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.ShoppingCartDao;
import com.onlineshopping.entity.ShoppingCart;
import com.onlineshopping.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * Created by hehe on 17-6-18.
 */
@Repository("ShoppingCartDao")
public class ShoppingCartDaoImpl implements ShoppingCartDao{
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ShoppingCart getShoppingCartByUserId(String uid) {
        String str = "select * from shopping_cart where user_id='"+uid+"'";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("goods_id", StandardBasicTypes.STRING).addScalar("number",StandardBasicTypes.INTEGER).list();
        if(l.size()==0)return null;
        ShoppingCart shoppingCart = new ShoppingCart();
        Map<String,Integer> idMap = new HashMap<>();
        for(Object ele:l){
            Object[] eleArr = (Object[])ele;
            idMap.put((String)eleArr[0],(Integer)eleArr[1]);
        }
        shoppingCart.setGoodsIdMap(idMap);
        shoppingCart.setUserId(uid);
        return shoppingCart;
    }

    @Override
    public void save(ShoppingCart shoppingCart) {
        String uid = shoppingCart.getUserId();
        Map<String,Integer> idMap = shoppingCart.getGoodsIdMap();
        StringBuilder str = new StringBuilder();
        str.append("insert into shopping_cart (user_id,goods_id,number) values");
        int sign=0;
        for(Map.Entry<String,Integer> entry:idMap.entrySet()){
            if(sign==0){
                sign=1;
                str.append("('"+uid+"','"+entry.getKey()+"',"+entry.getValue()+")");
            }
            else {
                str.append(",('" + uid + "','" + entry.getKey() + "',"+entry.getValue()+")");
            }
        }
        getSessionFactory().getCurrentSession().createSQLQuery(str.toString()).executeUpdate();

    }


    @Override
    public void deleteShoppingCartByUserId(String id) {
       String str ="delete from shopping_cart where user_id='"+id+"'";
       getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();

    }

    @Override
    public boolean isGoodsExistInShoppingCart(String goodsId, String id) {
        String str = "select * from shopping_cart e where e.goods_id='"+goodsId+"' AND e.user_id='"+id+"'";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        if(l.size()==0)
            return false;
        else{
            return true;
        }
    }

    @Override
    public void delete(ShoppingCart shoppingCart) {
        StringBuilder str =new StringBuilder("delete from shopping_cart where user_id='"+shoppingCart.getUserId()+"'"+" AND goods_id IN (");
        Map<String,Integer> goodsMap = shoppingCart.getGoodsIdMap();
        int sign=0;
        for(String i:goodsMap.keySet()){
            if(sign==0){
                sign=1;
                str.append("'"+i+"'");
            }
            else {
                str.append(",'" + i+"'");
            }
        }
        str.append(")");
        getSessionFactory().getCurrentSession().createSQLQuery(str.toString()).executeUpdate();
    }
}

