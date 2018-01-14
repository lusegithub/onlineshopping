package com.onlineshopping.dao;

import com.onlineshopping.entity.*;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface GoodsDao extends BaseDao<Goods>{

    /**
     *
     * @param shoppingCart
     * @return
     */
    List<Goods> getGoodsByShoppingCart(ShoppingCart shoppingCart);

    /**
     *
     * @param favorite
     * @return
     */
    List<Goods> getGoodsByFavorite(Favorite favorite);

    /**
     *
     * @param order
     * @return
     */
    List<Goods> getGoodsByOrder(Order order);

    /**
     *
     * @param orderDetail
     * @return
     */
    Goods getGoodsByOrderDetail(OrderDetail orderDetail);

    /**
     *
     * @param catalog
     * @return 返回类型为 catalog 的商品的数量
     */
    int getGoodsNumByCatalog(Catalog catalog);

//    /**
//     * 根据 keyword 和页码返回商品，根据商品的名称应包含 keyword 查询数据库表，
//     * 返回的结果集按商品名称的长度从短到长排列，最终返回结果集中
//     * 第 startIndex 条到第 startIndex + pageSize 条结果。
//     * @param keyword 返回的商品的名称应包含该 keyword
//     * @param startIndex 第一条数据记录下标视为 1
//     * @param pageSize 一共返回 pageSize 条结果
//     * @return 返回商品id集
//     */
//    List<Integer> getPagedGoodsIdsByKeyword(String keyword, int startIndex, int pageSize);


    /**
     * 根据 keyword 查询商品，将商品名称含有 keyword 的商品id加入结果集，
     * 返回的结果集按商品名称的长度从短到长排列。
     * @param keyword
     * @return 返回商品id集,按商品名称的长度从短到长排列
     */
    List<String> getGoodIdsByKeyword(String keyword);



    /**
     * 根据 keyword 查询商品，只要商品名称含有 keyword 中至少一个字符都加入结果集。
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByKeyword(String keyword);

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的，且名称含有 keyword 中至少一个字符的商品
     * @param minPrice
     * @param maxPrice
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByPriceIntervalAndKeyword(float minPrice, float maxPrice, String keyword);

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的商品
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByPriceInterval(float minPrice, float maxPrice);

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的商品的数量
     * @param minPrice
     * @param maxPrice
     * @return
     */
    int getGoodsNumByPriceInterval(float minPrice, float maxPrice);

    /**
     * 查询价格在区间[minPrice, maxPrice]的商品的id，
     * 在查询结果中返回第 startIndex 条到 startIndex + pageSize 条结果，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param minPrice
     * @param maxPrice
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 至多返回 pageSize 条结果
     * @return 返回商品的id，无顺序要求
     */
    List<String> getPagedGoodsByPriceInterval(float minPrice, float maxPrice, int startIndex, int pageSize);

    /**
     * 根据 catalogName查询得到结果集中取
     * 第 startIndex 条到第 startIndex + pageSize 条结果作为最终结果集返回，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param catalogName 返回的商品的名称应包含该 catalogName
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 一共返回 pageSize 条结果
     * @return 返回商品id集，无顺序要求
     */
    List<String> getPagedGoodsIdsByCatalogName(String catalogName, int startIndex, int pageSize);


//    /**
//     * 返回名称包含 keyword 的商品的数量
//     * @param keyword
//     * @return
//     */
//    int getGoodsNumByKeyword(String keyword);

    /**
     * 返回类型为 catalogName的，且名称含有 keyword 中至少一个字符的商品
     * @param catalogName
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByCatalogNameAndKeyword(String catalogName, String keyword);

    /**
     * 返回类型为 catalogName的商品
     * @param catalogName
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByCatalogName(String catalogName);

    /**
     * 根据 catalogName 和价格闭区间[minPrice, maxPrice]查询得到结果集中取
     * 第 startIndex 条到第 startIndex + pageSize 条结果作为最终结果集返回，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param catalogName 返回的商品的名称应包含该 catalogName
     * @param minPrice
     * @param maxPrice
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 一共返回 pageSize 条结果
     * @return 返回商品id集，无顺序要求
     */
    List<String> getPagedGoodsIdsByCatalogNameAndPriceInterval(String catalogName, float minPrice,
                                                float maxPrice, int startIndex, int pageSize);

    /**
     * 返回种类为catalogName, 价格在闭区间[minPrice, maxPrice]的商品的数量
     * @param catalogName
     * @param minPrice
     * @param maxPrice
     * @return
     */
    int getGoodsNumByCatalogNameAndPriceInterval(String catalogName, float minPrice, float maxPrice);

    /**
     * 返回类型为 catalogName 的，价格在闭区间[minPrice, maxPrice]的，
     * 且名称含有 keyword 中至少一个字符的商品
     * @param catalogName
     * @param keyword
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByCatalogNameAndKeywordAndPriceInterval(String catalogName, String keyword,
                                                float minPrice, float maxPrice);

    /**
     * 返回类型为 catalogName 的，价格在闭区间[minPrice, maxPrice]的商品
     * @param catalogName
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    List<Goods> getGoodsByCatalogNameAndPriceInterval(String catalogName,
                                                      float minPrice, float maxPrice);
}
