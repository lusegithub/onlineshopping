package com.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hehe on 17-6-9.
 */
@Embeddable
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Parent
    private Order order;
    @ManyToOne(targetEntity = Goods.class)
    @JoinColumn(name="goods_id",nullable = false)
    @Cascade(CascadeType.ALL)
    private Goods goods;
    @Column(name="goods_name",nullable = false,length =250)
    private String goodsName;
    @Column(name="goods_price",columnDefinition = "numeric(6,2)",nullable = false)
    private double goodsPrice;//>0
    @Column(name="goods_number",nullable = false)
    private int goodsNumber;

    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "order=" + order +
                ", goods=" + goods +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsNumber=" + goodsNumber +
                '}';
    }
}
