package com.onlineshopping.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehe on 17-6-10.
 */
public interface BaseDao<T> {

    //Get the entity by the id of the entity
    T get(Class<T> entityClazz, Serializable id);
    //save the entity
    Serializable save(T entity);
    //update the entity
    void update(T entity);
    //delete the entity
    void delete(T entity);
    //delete the entity by its ID
    void delete(Class<T> entityClazz,Serializable id);
    //get all the entity
    List<T> findAll (Class<T> entityClazz);
    //get the sum of the entities
    long findCount(Class<T> entityClazz);
}
