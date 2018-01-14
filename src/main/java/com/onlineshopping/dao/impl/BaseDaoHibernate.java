package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.*;
import com.onlineshopping.entity.Comment;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hehe on 17-6-14.
 */
@Component
public class BaseDaoHibernate<T> implements BaseDao<T>{
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory  = sessionFactory;
    }
    public SessionFactory getSessionFactory(){
        return this.sessionFactory;
    }
    @SuppressWarnings("uncheck")
    public T get(Class<T> entityClazz, Serializable id){
        return (T)getSessionFactory().getCurrentSession().get(entityClazz,id);
    }
    public Serializable save(T entity){
        return getSessionFactory().getCurrentSession().save(entity);
    }
    public void update(T entity){
        getSessionFactory().getCurrentSession().saveOrUpdate(entity);
    }
    public void delete(T entity){
        getSessionFactory().getCurrentSession().delete(entity);
    }
    public void delete(Class<T> entityClazz,Serializable id){
        getSessionFactory().getCurrentSession().createQuery("delete from "+entityClazz.getSimpleName()+" en where en.id=?1").setParameter("1",id).executeUpdate();
    }
    public List<T> findAll(Class<T> entityClazz){
        return  find("select en from "+entityClazz.getSimpleName()+" en");
    }
    public long findCount(Class<T> entityClazz){
        List<?> l = find("select count(*) from "+entityClazz.getSimpleName());
        if(l!=null&&l.size()==1){
            return (Long)l.get(0);
        }
        return 0;
    }
    @SuppressWarnings("unchecked")
    protected List<T> find(String sql){
        return (List<T>)getSessionFactory().getCurrentSession().createQuery(sql).list();
    }
    @SuppressWarnings("unchecked")
    protected List<T> find(String sql,Object... params){
        Query query = getSessionFactory().getCurrentSession().createQuery(sql);
        for(int i=0,len=params.length;i<len;i++){
            query.setParameter(i+"",params[i]);
        }
        return (List<T>)query.list();
    }
    protected List<T> findUseSql(String sql){
        return (List<T>)getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
    }
    //you need to use the e as the alias for the table name in sql string ,but you need to determine whether the result is null
    protected List<T> findUseSql(String sql,Class<T> entityClazz,Object... params){
        Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(entityClazz);
        int i=1;
        for(Object o:params){
            query.setParameter(i+"",params[i-1]);
            i++;
        }
        List<T> list = new ArrayList<>();
        if(query.list().size()==0)return null;
        for(Object tmp:query.list()){
            list.add((T)tmp);
        }
        return list;
    }
    protected List<T> findByPage(String sql,int pageNo,int pageSize){
        return getSessionFactory().getCurrentSession().createQuery(sql).setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list();
    }

    protected List<T> findByPage(String sql,int pageNo,int pageSize,Object... params){
        Query query = getSessionFactory().getCurrentSession().createQuery(sql);
        for(int i=0,len=params.length;i<len;i++){
            query.setParameter(i+"",params[i]);
        }
        return query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list();
    }
    protected  List<T> findByStartIndexAndPage(String sql,Class<T> entityClazz,int startIndex,int pageSize){
        if(startIndex<=0)startIndex=1;
        List l=getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity("e",entityClazz).setFirstResult(startIndex-1).setMaxResults(pageSize).list();
        if(l.size()==0)return  null;
        return (List<T>)l;

    }

}
