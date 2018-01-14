package com.onlineshopping.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onlineshopping.util.DateSerializerUtil;

import java.util.Date;

/**
 * Created by loong on 2017/6/28.
 */
public class PointsItem {
    private Integer order_id;
    private Date orderTime;
    private double price;
    private Integer points;

    public PointsItem() {
    }

    public PointsItem(Integer order_id, Date orderTime, double price, Integer points) {
        this.order_id = order_id;
        this.orderTime = orderTime;
        this.price = price;
        this.points = points;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    @JsonSerialize(using = DateSerializerUtil.class)
    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
