package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.CatalogDao;
import com.onlineshopping.dao.Watched;
import com.onlineshopping.dao.Watcher;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Goods;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

/**
 * Created by hehe on 17-6-15.
 */
@Repository("CatalogDao")
public class CatalogDaoImpl extends BaseDaoHibernate<Catalog> implements CatalogDao,Watched{

    @Resource(name="GoodsDao")
    private Watcher watcher;

    @Override
    public Catalog getByName(String catalog_name) {
        String sql = "select e.* from catalog e where e.catalog_name='"+catalog_name+"' AND e.sign=1";
        List<Catalog> l=getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity("e",Catalog.class).list();
        if(l.size()==0)return null;
        return  (Catalog)l.get(0);
    }



    @Override
    public void update(Catalog entity) {

        List<Catalog> catalogs = findAll(Catalog.class);
        for(Catalog tmp:catalogs){
            if(tmp.getCatalogId()==entity.getCatalogId()&&tmp.getSign()==1){
                String sql = "update catalog set catalog_name ='"+entity.getName()+"' where catalog_id = "+entity.getCatalogId();
                getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
                notifyWathcersUpdateCatalogname(entity.getName(),entity.getCatalogId());
            }
        }


    }

    @Override
    public void notifyWatchersAddCatalogname(String str,String id) {
       watcher.addFollowWatchedCatalogName(str,id);
    }

    @Override
    public void notifyWathcersUpdateCatalogname(String str,int id) {
       watcher.updateFollowWatchedCatalogName(str,id);
    }

    @Override
    public Serializable save(Catalog catalog){
        //      getSessionFactory().getCurrentSession().getTransaction().begin();

        //Catalog catalog = goods.getCatalog();
        //      Serializable id = super.save(goods);
        //addFollowWatchedCatalogName(catalog.getName(),(String)super.save(goods));
        //      getSessionFactory().getCurrentSession().getTransaction().commit();
        // String str= "INSERT into goods (goods_id, catalog_name, name, description, photo, price, stock, catalog_id) VALUE (?1,?2,?3,?4,?5,?6,?7,?8)";
        // String str= "INSERT into goods (goods_id, catalog_name, name, description, photo, price, stock, catalog_id) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)";
        // getSessionFactory().getCurrentSession().createSQLQuery(str).setParameter(1,goods.getGoodsId()).setParameter(2,catalog.getName()).setParameter(3,goods.getName()).setParameter(4,goods.getDescribe()).setParameter(5,goods.getPhoto()).setParameter(6,goods.getPrice()).setParameter(7,goods.getStock()).setParameter(8,catalog.getCatalogId()).executeUpdate();
        //goods.setCatalogName(catalog.getName());
        if(getByName(catalog.getName())!=null){

            String str = "update catalog SET catalog.sign = "+0+" where catalog_id ='"+catalog.getCatalogId()+"'";
            getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();

        }
        return super.save(catalog);


    }
    @Override
    public void delete(Catalog entity){
        String str = "update catalog SET catalog.sign = "+0+" where catalog_id ='"+entity.getCatalogId()+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }
    @Override
    public void delete(Class<Catalog> entityClazz, Serializable id){
        String str = "update catalog SET catalog.sign = "+0+" where catalog_id ='"+id+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }
    @Override
    public List<Catalog> findAll(Class<Catalog> entityClazz){
        String sql = "select * from catalog where sign = 1";
        return  getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(Catalog.class).list();
    }
    @Override
    public long findCount(Class<Catalog> entityClazz){
        String sql = "select * from catalog where sign = 1";
        List l =  getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(Catalog.class).list();
        if(l!=null&&l.size()==1){
            return (Long)l.get(0);
        }
        return 0;
    }

    @Override
    public Catalog get(Class<Catalog> entityClazz, Serializable id){
        List<Catalog> catalogs = findAll(entityClazz);
        for(Catalog tmp:catalogs){
            if(tmp.getCatalogId()==id&&tmp.getSign()==1){
                return tmp;
            }
        }
        return null;
    }

}
