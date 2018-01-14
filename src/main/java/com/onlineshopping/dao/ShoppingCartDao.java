package com.onlineshopping.dao;

import com.onlineshopping.entity.ShoppingCart;
import com.onlineshopping.entity.User;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface ShoppingCartDao {

    /**
     *
     * @param id
     * @return
     */
    ShoppingCart getShoppingCartByUserId(String id);

    void save(ShoppingCart shoppingCart);

    void deleteShoppingCartByUserId(String uid);

    /**
     * @param goodsId 商品id
     * @param id 用户id
     * @return 判断用户是否已添加该商品，如果是，则返回true，否则false
     */
    boolean isGoodsExistInShoppingCart(String goodsId,String id);

    /**
     * @param shoppingCart
     * 通过shoppingCart参数里的goodsId集合和userId进行删除
     */
    void delete(ShoppingCart shoppingCart);

}
