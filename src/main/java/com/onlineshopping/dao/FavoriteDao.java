package com.onlineshopping.dao;

import com.onlineshopping.entity.Favorite;
import com.onlineshopping.entity.ShoppingCart;
import com.onlineshopping.entity.User;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface FavoriteDao {

    /**
     *
     * @param
     * @return
     */
    Favorite getFavoriteByUserId(String uid);



    void save(Favorite favorite);
    /**
     *
     * @param uid
     */
    void  deleteFavoriteByUserId(String uid);

    /**
     * @param goodsId 商品id
     * @param id 用户id
     * @return 判断用户是否已收藏该商品，如果是，则返回true，否则false
     */
    boolean isGoodsExistInFavorite(String goodsId, String id);

    /**
     * @param favorite
     * 通过favorite参数里的goodsIdSet和userId进行删除
     */
    void delete(Favorite favorite);


}
