package com.onlineshopping.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by hehe on 17-6-9.
 */
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private Map<String,Integer> goodsIdMap = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Integer> getGoodsIdMap() {
        return goodsIdMap;
    }

    public void setGoodsIdMap(Map<String, Integer> goodsIdMap) {
        this.goodsIdMap = goodsIdMap;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "user=" + userId +
                ", goodsSet=" + goodsIdMap +
                '}';
    }
}
