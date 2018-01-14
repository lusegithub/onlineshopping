package com.onlineshopping.service.backmanage;

import com.onlineshopping.entity.Result;

import java.util.Date;

public interface OrderService {
    Result find(Integer order_id, String user_id, Date begin_time, Date end_time);

    Result getDetail(int order_id);

    Result updateStatus(int order_id, String status);

    Result getRevenue(Date begin_time, Date end_time, String catalog_name);
}
