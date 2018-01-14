package com.onlineshopping.service.backmanage;

import com.onlineshopping.entity.Result;

/**
 * Created by lsy
 */
public interface CatalogService {
    Result add(String name);

    Result update(String old_name, String new_name);

    Result delete(String name);
}
