package com.onlineshopping.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.onlineshopping.dao.FavoriteDao;
import com.onlineshopping.entity.Favorite;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.ShoppingCart;
import com.onlineshopping.exception.ForeignKeyException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hehe on 17-6-18.
 */
@Repository("FavoriteDao")
public class FavoriteDaoImpl implements FavoriteDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Favorite getFavoriteByUserId(String uid) {
        String str = "select * from favorite where user_id='"+uid+"'";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).addScalar("goods_id", StandardBasicTypes.STRING).list();
        if(l.size()==0)return null;
        Favorite favorite = new Favorite();
        Set<String> idSet = new HashSet<>();
        for(Object ele:l){
            idSet.add((String) ele);
        }
        favorite.setGoodsIdSet(idSet);
        favorite.setUserId(uid);
        return favorite;
    }

    @Override
    public void save(Favorite favorite) {
        String uid = favorite.getUserId();
        Set<String> idSet = favorite.getGoodsIdSet();
        StringBuilder str = new StringBuilder();
        str.append("insert into favorite (user_id,goods_id) values");
        int sign=0;
        for(String id:idSet) {
            if (sign == 0) {
                sign = 1;
                str.append("('" + uid + "','" + id + "')");
            } else {
                str.append(",('" + uid + "','" + id + "')");
            }
        }
        try {
            getSessionFactory().getCurrentSession().createSQLQuery(str.toString()).executeUpdate();
        }
        catch(ConstraintViolationException e){
            System.out.print(new ForeignKeyException("You need to add the userId and goodsId first!"));
        }
        }

    @Override
    public void deleteFavoriteByUserId(String uid) {
        String str ="delete from favorite where user_id='"+uid+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }

    @Override
    public boolean isGoodsExistInFavorite(String goodsId, String id) {
        String str = "select * from favorite e where e.goods_id="+goodsId+" AND e.user_id='"+id+"'";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        if(l.size()==0)
        return false;
        else{
            return true;
        }
    }

    @Override
    public void delete(Favorite favorite) {

        StringBuilder str =new StringBuilder("delete from favorite where user_id='"+favorite.getUserId()+"'"+" AND goods_id IN (");
        Set<String> goodsSet = favorite.getGoodsIdSet();
        int sign=0;
        for(String i:goodsSet){
            if(sign==0){
                sign=1;
                str.append("'"+i+"'");
            }
            else {
                str.append("," +"'"+i+"'");
            }
        }
        str.append(")");
        getSessionFactory().getCurrentSession().createSQLQuery(str.toString()).executeUpdate();
    }
}
