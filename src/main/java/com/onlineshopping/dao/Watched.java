package com.onlineshopping.dao;

/**
 * Created by hehe on 17-7-8.
 */
public interface Watched {

     void notifyWatchersAddCatalogname(String str,String id);

     void notifyWathcersUpdateCatalogname(String str,int id);
}
