package com.onlineshopping.service.impl.goods;

import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.exception.NoGoodsException;
import com.onlineshopping.service.goods.ReadGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by W on 2017/6/30.
 * 由于Spring Cache基于动态CGLib实现AOP，受到内部了内部调用时代理失效的限制，
 * 该辅助类用于解决这个问题。
 */

@Service
public class ReadGoodsServiceHelper {

    private static Logger logger = LoggerFactory.getLogger(ReadGoodsService.class);

    //根据关键字直接查询时得到的商品结果数如果少于该值，则拆分关键字再次查询
    final static private int MIN_GOODS_NUM = 1;

    //返回搜索商品结果的最大数目
    final static private int MAX_GOODS_NUM = 1000;

    @Resource
    GoodsDao goodsDao;

    //返回根据种类、价格区间和关键字查询到的，且经过筛选和排序的商品，
    //最多不超过 MAX_GOODS_NUM 条
    //@Cacheable("goodsRelatedWithCatalogAndKeywordAndPriceInterval")
    public List<String> getHandledGoodsIdByCatalogNameAndKeywordAndPriceInterval(
            String catalogName, String keyword, float minPrice, float maxPrice)
            throws NoGoodsException {
        List<Goods> goods = goodsDao.getGoodsByCatalogNameAndKeywordAndPriceInterval(
                catalogName, keyword, minPrice, maxPrice);
        if (goods == null || goods.isEmpty()) {
            throw new NoGoodsException();
        }
        return getSortedGoodsIdByKeyword(goods, keyword, MAX_GOODS_NUM);
    }

    //返回根据种类和关键字查询到的，且经过筛选和排序的商品，最多不超过 MAX_GOODS_NUM 条
    //@Cacheable("goodsRelatedWithCatalogAndKeyword")
    public List<String> getHandledGoodsIdsByCatalogNameAndKeyword(String catalogName,
                                                                  String keyword)
            throws NoGoodsException {
        List<Goods> goods = goodsDao.getGoodsByCatalogNameAndKeyword(catalogName, keyword);
        if (goods == null || goods.isEmpty()) {
            throw new NoGoodsException();
        }
        return getSortedGoodsIdByKeyword(goods, keyword, MAX_GOODS_NUM);
    }

    //返回根据关键字和价格区间查询到的，且经过筛选和排序的商品，最多不超过 MAX_GOODS_NUM条
    //@Cacheable("goodsRelatedWithPriceIntervalAndKeyword")
    public List<String> getHandledGoodsIdsByPriceIntervalAndKeyword(Float minPrice, Float maxPrice,
                                                                    String keyword)
            throws NoGoodsException {
        List<Goods> goods =
                goodsDao.getGoodsByPriceIntervalAndKeyword(minPrice, maxPrice, keyword);
        if (goods == null || goods.isEmpty()) {
            throw new NoGoodsException();
        }
        return getSortedGoodsIdByKeyword(goods, keyword, MAX_GOODS_NUM);
    }

    //返回根据关键字查询到的商品，最多不超过 MAX_GOODS_NUM条
    //@Cacheable("goodsRelatedWithKeyword")
    public List<String> getGoodsByKeyword(String keyword) throws NoGoodsException {
        long startTime = System.currentTimeMillis();
        List<String> ids = goodsDao.getGoodIdsByKeyword(keyword);
        long endTime = System.currentTimeMillis();
        logger.info("getGoodsIdsByKeyword 耗时：" + (endTime - startTime) + "毫秒");
        if (ids == null) {
            ids = new ArrayList<>();
        }
        if (ids.size() < MIN_GOODS_NUM) {
            long startTime1 = System.currentTimeMillis();
            List<Goods> goods = goodsDao.getGoodsByKeyword(keyword);
            if (goods == null || goods.isEmpty()) {
                throw new NoGoodsException();
            }
            long endTime1 = System.currentTimeMillis();
            logger.info("getGoodsByKeyword 耗时：" + (endTime1 - startTime1) + "毫秒");
            long startTime2 = System.currentTimeMillis();
            List<String> sortedGoodsIds =
                    getSortedGoodsIdByKeyword(goods, keyword, MAX_GOODS_NUM - ids.size());
            long endTime2 = System.currentTimeMillis();
            logger.info("getSortedGoodsIdByKeyword 耗时：" + (endTime2 - startTime2) + "毫秒");
            ids.addAll(sortedGoodsIds);
        } else if (ids.size() > MAX_GOODS_NUM) {
            ids = ids.subList(0, MAX_GOODS_NUM);
        }
        if (ids.isEmpty()) {
            throw new NoGoodsException();
        }
        return ids;
    }

    //先找出名称含有 keyword 中任意一个字符的商品，然后商品
    //名称每含有一个 keyword中的字符，其权值加 1，得到的权值除以名称长度得到最终权值，
    //返回的商品数量不超过maxSize，根据最终权值从大到小进行排序。
    private List<String> getSortedGoodsIdByKeyword(List<Goods> goods, String keyword, int maxSize) {
        if (goods == null) {
            return null;
        }
        List<Pair> pairList = new ArrayList<>();
        char[] chars = keyword.toCharArray();
        for (Goods aGoods : goods) {
            int num = 0;
            String name = aGoods.getName();
            for (char c : chars) {
                if (aGoods.getName().contains(String.valueOf(c))) {
                    num++;
                }
            }
            String id = aGoods.getGoodsId();
            pairList.add(new Pair(id, num / (float)name.length()));
        }
        Collections.sort(pairList, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o2.compareTo(o1);
            }
        });
        int exclusiveEnd = pairList.size() < maxSize ? pairList.size() : maxSize;
        List<Pair> subList = pairList.subList(0, exclusiveEnd);
        List<String> resultList = new ArrayList<>();
        for (Pair pair : subList) {
            resultList.add(pair.id);
        }
        return resultList;
    }

    private class Pair implements Comparable<Pair> {
        private String id;
        private float value;
        Pair(String id, float value) {
            this.id = id;
            this.value = value;
        }
        @Override
        public int compareTo(Pair o) {
            if (this.value < o.value) {
                return -1;
            } else if (this.value > o.value) {
                return 1;
            }
            return 0;
        }
    }
}
