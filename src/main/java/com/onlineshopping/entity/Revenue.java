package com.onlineshopping.entity;

/**
 * Created by lsy
 */
public class Revenue {
    private String goodsId;
    private String goodsName;
    private double goodsPrice;
    private int goodsNumber;
    private double revenue;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    public void addGoodsNumber(int goodsNumber){
        this.goodsNumber += goodsNumber;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void addRevenue(double revenue) {
        this.revenue += revenue;
    }
}
