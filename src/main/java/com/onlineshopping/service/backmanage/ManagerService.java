package com.onlineshopping.service.backmanage;

import com.onlineshopping.entity.Result;

/**
 * Created by lsy
 */
public interface ManagerService {
    Result login(String manager_id, String password);

    Result logout();
}
