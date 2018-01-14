package com.onlineshopping.controller.backmanage;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by lsy
 */
@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Resource
    private OrderService orderService;

    /**
     * 自动转化日期参数
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查找订单
     * @param order_id      订单ID
     * @param user_id       用户ID
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @return
     */
    @GetMapping("/orders")
    @ManagerAccessRequired
    public Result find(
            Integer order_id,
            String user_id,
            Date begin_time,
            Date end_time) {
        logger.info("管理员查询订单");
        if (end_time == null) {
            return orderService.find(order_id, user_id, begin_time, null);
        }else {
            // 日期默认取时间0点，将结束日期+1，以查询包括结束日期的数据
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end_time);
            calendar.add(Calendar.DATE, 1);
            return orderService.find(order_id, user_id, begin_time, calendar.getTime());
        }
    }

    /**
     * 管理员查看订单详情
     * @param order_id  订单ID
     * @return
     */
    @GetMapping("/orders/{order_id}")
    @ManagerAccessRequired
    public Result getDetail(@PathVariable int order_id) {
        logger.info("管理员查看订单" + order_id + "详情");
        return orderService.getDetail(order_id);
    }

    /**
     * 管理员更新订单状态
     * @param order_id  订单ID
     * @param status    状态
     * @return
     */
    @PostMapping("/orders/{order_id}")
    @ManagerAccessRequired
    public Result updateStatus(@PathVariable int order_id, String status) {
        logger.info("管理员更新订单" + order_id + "状态为" + status);
        return orderService.updateStatus(order_id, status);
    }

    /**
     * 统计销售量
     * @param begin_time    下单时间下限
     * @param end_time      下单时间上限
     * @param catalog_name  目录名
     * @return
     */
    @GetMapping("/revenue")
    @ManagerAccessRequired
    public Result revenue(
            Date begin_time,
            Date end_time,
            String catalog_name) {
        logger.info("管理员查看销售量统计");
        if (end_time == null) {
            return orderService.getRevenue(begin_time, null, catalog_name);
        }else {
            // 日期默认取时间0点，将结束日期+1，以查询包括结束日期的数据
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end_time);
            calendar.add(Calendar.DATE, 1);
            return orderService.getRevenue(begin_time, calendar.getTime(), catalog_name);
        }
    }
}
