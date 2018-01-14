package com.onlineshopping.dao;

/**
 * Created by hehe on 17-7-8.
 */
public interface Watcher {

     void updateFollowWatchedCatalogName(String catalog_name,int id);

     void addFollowWatchedCatalogName(String catalog_name,String id);
}
