package com.onlineshopping.service.purchase;

import com.onlineshopping.entity.Comment;
import com.onlineshopping.entity.Order;
import com.onlineshopping.entity.Result;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jacky on 17-6-12.
 */
public interface PurchaseService {
    public Result saveOrder(Map<String ,Object> map, String userId);
    public Result getShoppingCart(String userId);
    public Result saveShoppingCart(List<String> goodsIdList,String userId);
    public Result deleteShoppingCart(List<String> goodsIdList,String userId);
    public Result saveFavorite(List<String> goodsIdList,String userId);
    public Result deleteFavorite(List<String> goodsIdList,String userId);
    public Result getFavorite(String userId);

    Result confirmReceipt(Integer order_id, String user_id);

    Result cancelOrder(String user_id, Integer order_id);

    Result saveComment(String goodsId, String nickname, Date time, String content, int score);
}
