package com.onlineshopping.service.customer;

import com.onlineshopping.entity.*;

/**
 * Created by loong on 17-6-12.
 */
public interface CustomerService {

    Result addCustomer(User user);

    Result Login(String user_id, String password);

    Result getUserInfo(String user_id);

    Result updateNickname(String user_id, String nickname);

    Result updatePassword(String user_id, String old_password, String new_password);

    Result getAddressList(String user_id);

    Result updateAddress(Integer address_id, Address address);

    Result deleteAddress(String user_id, Integer address_id);

    Result addAddress(String user_id, Address address);

    Result getOrders(String begin_time, String end_time, String status, String user_id);

    Result getOrderById(Integer order_id, String user_id);

    Result getDiscount(String userId);

    Result getPointsDetail(String user_id);

    Result getPointRate(String userId);
}
