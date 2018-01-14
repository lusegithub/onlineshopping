package com.onlineshopping.service.backmanage;

import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.VipLevel;

import java.util.Date;

public interface UserService {
    Result getVipLevel();

    Result setVipLevel(VipLevel vipLevel);

    Result findUser(String user_id, String level, Date begin_time, Date end_time);

    Result deleteUser(String user_id);
}
