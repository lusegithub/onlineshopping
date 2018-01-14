package com.onlineshopping.dao;

import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface UserDao extends BaseDao<User>{
    boolean isUserExist(String user_id);
    User findUserByUserId(String user_id);

    List<User> getUsersByLevel(LevelType level);

    List<User> getUsersByBeginTime(Date beginTime);

    List<User> getUsersByEndTime(Date endTime);

    List<User> getUsersByLevelBegin(LevelType level, Date beginTime);

    List<User> getUsersByLevelEnd(LevelType level, Date endTime);

    List<User> getUsersByBeginEnd(Date beginTime, Date endTime);

    List<User> getUsersByLevelBeginEnd(LevelType level, Date beginTime, Date endTime);

    /**
     * 删除user，若存在外键约束则删除失败
     * @param user
     * @return 删除成功返回0，删除失败返回-2
     */
    int deleteUser(User user);

    LevelType getLevelTypeByPoint(int point);


}
