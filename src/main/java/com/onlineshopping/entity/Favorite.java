package com.onlineshopping.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hehe on 17-6-9.
 */
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private Set<String> goodsIdSet = new HashSet<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getGoodsIdSet() {
        return goodsIdSet;
    }

    public void setGoodsIdSet(Set<String> goodsIdSet) {
        this.goodsIdSet = goodsIdSet;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "user=" + userId +
                ", goodsSet=" + goodsIdSet +
                '}';
    }
}
