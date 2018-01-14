package com.onlineshopping.dao;

import com.onlineshopping.entity.Address;
import com.onlineshopping.entity.User;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface AddressDao extends BaseDao<Address>{



    /**
     *@Param:  The user
     *@return: The address objects you get by the user.In addition, you need to determine whether the result is null.
     *
     */
    List<Address> getAddressByUser(User user);


    /**
     *@Param: The user object
     *@return:
     */
    void  deleteAddressByUser(User user);

    /**
     * 查询指定用户地址的数量
     * @param user 用户
     * @return
     */
    int countAddressByUser(User user);
}
