package com.onlineshopping.service.impl.goods;

import com.onlineshopping.dao.CatalogDao;
import com.onlineshopping.dao.CommentDao;
import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Comment;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.goods.CatalogDetail;
import com.onlineshopping.entity.goods.CommentsPage;
import com.onlineshopping.entity.goods.GoodsPage;
import com.onlineshopping.exception.*;
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
 * Created by W on 2017/6/14.
 */
@Service
public class ReadGoodsServiceImpl implements ReadGoodsService {

    private static Logger logger = LoggerFactory.getLogger(ReadGoodsService.class);

    @Resource
    CatalogDao catalogDao;

    @Resource
    GoodsDao goodsDao;

    @Resource
    CommentDao commentDao;

    @Resource
    ReadGoodsServiceHelper readGoodsServiceHelper;

    //根据种类查询时每页商品数量
    final static private int CATALOG_PAGED_GOODS_NUM = 10;

    //每页商品数量
    final static private int PAGED_GOODS_NUM = 10;

    //每页评论数量
    final static private int PAGED_COMMENT_NUM = 10;

    private boolean isCatalogChange = true;

    @Override
    public void setCatalogChange(boolean isCatalogChange) {
        this.isCatalogChange = isCatalogChange;
    }

    private List<CatalogDetail> catalogDetailsCache = null;

    @Override
    public List<CatalogDetail> getCatalogsDetail() {
        if (!isCatalogChange && catalogDetailsCache != null) {
            return catalogDetailsCache;
        }
        List<Catalog> catalogs = catalogDao.findAll(Catalog.class);
        List<CatalogDetail> catalogDetails = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            int goodsNum = goodsDao.getGoodsNumByCatalog(catalog);
            //确保有余数则加一
            int pagesNum = (goodsNum + CATALOG_PAGED_GOODS_NUM - 1) / CATALOG_PAGED_GOODS_NUM;
            CatalogDetail catalogDetail = new CatalogDetail();
            catalogDetail.setCatalogName(catalog.getName());
            catalogDetail.setTotalGoodsNum(goodsNum);
            catalogDetail.setTotalPagesNum(pagesNum);
            catalogDetails.add(catalogDetail);
        }
        logger.info("种类数为" + catalogDetails.size());
        this.isCatalogChange = false;
        this.catalogDetailsCache = catalogDetails;
        return catalogDetails;
    }

    @Override
    public GoodsPage getPagedGoodsIdsByCatalogName(String catalogName, int pageNum) throws
            NoCatalogException, PageNumFaultException, NoGoodsException{
        Catalog catalog = catalogDao.getByName(catalogName);
        if (catalog == null) {
            throw new NoCatalogException();
        }
        int totalGoodsNum = goodsDao.getGoodsNumByCatalog(catalog);
        if (totalGoodsNum < 1) {
            throw new NoGoodsException();
        } else if (pageNum < 1) {
            throw new PageNumFaultException("页码必须为正数");
        }
        int pagesNum = (totalGoodsNum + CATALOG_PAGED_GOODS_NUM - 1) / CATALOG_PAGED_GOODS_NUM;
        if (pageNum > pagesNum) {
            throw new PageNumFaultException("页码过大");
        }
        int startIndex = (pageNum - 1) * CATALOG_PAGED_GOODS_NUM + 1;
        List<String> ids = goodsDao.getPagedGoodsIdsByCatalogName(catalogName, startIndex,
                CATALOG_PAGED_GOODS_NUM);
        if (ids == null || ids.isEmpty()) {
            throw new NoGoodsException();
        }
        GoodsPage goodsPage =  assembleGoodsPage(ids, pageNum, totalGoodsNum, pagesNum);
        logger.debug(catalogName +  "类第" + pageNum + "页内容为：" + goodsPage.toString());
        return goodsPage;
    }

    @Override
    public GoodsPage getGoodsPage(String keyword, int pageNum)
            throws PageNumFaultException, NoGoodsException {
        if (pageNum < 1) {
            throw new PageNumFaultException("页码必须为正数");
        }
        List<String> ids = readGoodsServiceHelper.getGoodsByKeyword(keyword);
        if (ids == null || ids.isEmpty()) {
            throw new NoGoodsException();
        }
        int pagesNum = (ids.size() + PAGED_GOODS_NUM - 1) / PAGED_GOODS_NUM;
        if (pageNum > pagesNum) {
            throw new PageNumFaultException("页码过大");
        }
        int fromIndex = (pageNum - 1) * PAGED_GOODS_NUM;
        int exclusiveToIndex = (fromIndex + PAGED_GOODS_NUM) > ids.size()
                ? ids.size() : (fromIndex + PAGED_GOODS_NUM);
        List<String> pagedIds = ids.subList(fromIndex, exclusiveToIndex);
        GoodsPage goodsPage = assembleGoodsPage(pagedIds, pageNum, ids.size(), pagedIds.size());
        logger.debug("关键字：" + keyword +  "，第" + pageNum + "页内容为：" + goodsPage.toString());
        return goodsPage;
    }

    @Override
    public Goods getGoodsById(String id) throws NoGoodsException {
        Goods goods = goodsDao.get(Goods.class, id);
        if (goods == null) {
            throw new NoGoodsException("商品id不合法");
        }
        logger.debug("id为" + id + "的商品的信息：" + goods.toString());
        return goods;
    }

    @Override
    public CommentsPage getPagedCommentByGoodsId(String goodsId, int pageNum) throws
            PageNumFaultException, NoGoodsException, NoCommentsException {
        if(pageNum < 1) {
            throw new PageNumFaultException("页码必须为正数");
        }
        Goods goods = goodsDao.get(Goods.class, goodsId);
        if (goods == null) {
            throw new NoGoodsException("商品id不合法");
        }
        int totalCommentsNum = commentDao.getCommentNumByGoods(goods);
        if (totalCommentsNum == 0) {
            throw new NoCommentsException();
        }
        int totalPagesNum = (totalCommentsNum + PAGED_COMMENT_NUM - 1)/PAGED_COMMENT_NUM;
        if (pageNum > totalPagesNum) {
            throw new PageNumFaultException("页码过大");
        }
        CommentsPage commentsPage = new CommentsPage();
        commentsPage.setCurrentPageNum(pageNum);
        int startIndex = (pageNum - 1) * PAGED_COMMENT_NUM + 1;
        List<Comment> comments =  commentDao.getPagedCommentByGoods(goods, startIndex,
                PAGED_COMMENT_NUM);
        int commentsSize = comments == null ? 0 : comments.size();
        commentsPage.setPagedCommentsNum(commentsSize);
        commentsPage.setTotalCommentsNum(totalCommentsNum);
        commentsPage.setTotalPagesNum(totalPagesNum);
        commentsPage.setComments(comments);
        logger.debug("商品id为" + goodsId +  ", " +
                commentsPage.toString());
        return commentsPage;
    }

    @Override
    public GoodsPage getPagedSpecialGoodsIds(String keyword, Float minPrice,
                                             Float maxPrice, String catalogName, int pageNum)
            throws InvalidParameterException, PageNumFaultException, NoGoodsException,
            NoCatalogException {
        if (maxPrice != null && minPrice != null && maxPrice < minPrice) {
            throw new InvalidParameterException("价格区间错误");
        }
        GoodsPage goodsPage;
        if (catalogName == null) {
            if (minPrice == null || maxPrice == null) {
                if (keyword == null) {
                    throw new InvalidParameterException("参数不能全为空");
                } else {
                    //仅凭关键字查询
                    goodsPage = getGoodsPage(keyword, pageNum);
                }
            } else {
                if (keyword == null) {
                    //仅凭价格区间查询
                    int startIndex = (pageNum - 1) * PAGED_GOODS_NUM + 1;
                    List<String> goodsIds = goodsDao.getPagedGoodsByPriceInterval(minPrice, maxPrice,
                            startIndex, PAGED_GOODS_NUM);
                    if (goodsIds == null || goodsIds.isEmpty()) {
                        throw new NoGoodsException();
                    }
                    int totalGoodsNum = goodsDao.getGoodsNumByPriceInterval(minPrice, maxPrice);
                    int totalPageNum = (totalGoodsNum + PAGED_GOODS_NUM - 1) / PAGED_GOODS_NUM;
                    goodsPage = assembleGoodsPage(goodsIds, pageNum, totalGoodsNum, totalPageNum);
                } else {
                    //凭价格区间和关键字查询
                    List<String> goodsIds = readGoodsServiceHelper
                            .getHandledGoodsIdsByPriceIntervalAndKeyword(minPrice,
                            maxPrice, keyword);
                    if (goodsIds == null || goodsIds.isEmpty()) {
                        throw new NoGoodsException();
                    }
                    goodsPage = handleFinalList(goodsIds, pageNum);
                }
            }
        } else {
            if (minPrice == null || maxPrice == null) {
                if (keyword == null) {
                    //仅凭种类查询
                    goodsPage = getPagedGoodsIdsByCatalogName(catalogName, pageNum);
                } else {
                    //凭种类和关键字查
                    List<String> goodsIds = readGoodsServiceHelper
                            .getHandledGoodsIdsByCatalogNameAndKeyword(catalogName,
                            keyword);
                    if (goodsIds == null || goodsIds.isEmpty()) {
                        throw new NoGoodsException();
                    }
                    goodsPage = handleFinalList(goodsIds, pageNum);
                    }
                } else {
                if (keyword == null) {
                    //凭种类和价格区间查询
                    int startIndex = (pageNum - 1) * PAGED_GOODS_NUM + 1;
                    List<String> goodsIds = goodsDao.getPagedGoodsIdsByCatalogNameAndPriceInterval(
                            catalogName, minPrice, maxPrice, startIndex, PAGED_GOODS_NUM);
                    if (goodsIds == null || goodsIds.isEmpty()) {
                        throw new NoGoodsException();
                    }
                    int totalGoodsNum = goodsDao.getGoodsNumByCatalogNameAndPriceInterval(
                            catalogName, minPrice, maxPrice);
                    int totalPageNum = (totalGoodsNum + PAGED_GOODS_NUM - 1) / PAGED_GOODS_NUM;
                    goodsPage = assembleGoodsPage(goodsIds, pageNum, totalGoodsNum, totalPageNum);
                } else {
                    //凭种类、价格区间和关键字查询
                    List<String> goodsIds = readGoodsServiceHelper
                            .getHandledGoodsIdByCatalogNameAndKeywordAndPriceInterval(
                            catalogName, keyword, minPrice, maxPrice);
                    if (goodsIds == null || goodsIds.isEmpty()) {
                        throw new NoGoodsException();
                    }
                    goodsPage = handleFinalList(goodsIds, pageNum);
                }
            }
        }
        logger.debug("关键字" + keyword +  "，价格区间[" + minPrice + "," + maxPrice + "]，种类为" +
                catalogName + ", 第" + pageNum + "页内容为：" + goodsPage.toString());
        return goodsPage;
    }

    @Override
    public Result deleteCommentById(Integer commentId) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        commentDao.deleteComment(comment);
        Result result = new Result();
        result.setResultCode(0);
        result.setResultInfo("删除评论成功");
        return result;
    }

    private GoodsPage handleFinalList(List<String> list, int pageNum) {
        if (list == null) {
            return assembleGoodsPage(null, pageNum, 0, 0);
        }
        int totalGoodsNum = list.size();
        int totalPageNum = (totalGoodsNum + PAGED_GOODS_NUM - 1) / PAGED_GOODS_NUM;
        int fromIndex = (pageNum - 1) * PAGED_GOODS_NUM;
        int exclusiveToIndex = fromIndex + PAGED_GOODS_NUM > list.size()
                ? list.size() : (fromIndex + PAGED_GOODS_NUM);
        list = list.subList(fromIndex, exclusiveToIndex);
        return assembleGoodsPage(list, pageNum, totalGoodsNum, totalPageNum);
    }

    private GoodsPage assembleGoodsPage(List<String> goodsIds,int pageNum,
                                        int totalGoodsNum, int totalPageNum) {
        GoodsPage goodsPage = new GoodsPage();
        int goodsNum = goodsIds == null ? 0 : goodsIds.size();
        goodsPage.setTotalGoodsNum(totalGoodsNum);
        goodsPage.setTotalPagesNum(totalPageNum);
        goodsPage.setGoodsNum(goodsNum);
        goodsPage.setPageNum(pageNum);
        goodsPage.setGoodsIds(goodsIds);
        return goodsPage;
    }
}
