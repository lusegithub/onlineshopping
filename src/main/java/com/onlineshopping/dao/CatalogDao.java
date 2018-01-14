package com.onlineshopping.dao;

import com.onlineshopping.entity.Catalog;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface CatalogDao extends BaseDao<Catalog>{

    Catalog getByName(String catalog_name);

}
