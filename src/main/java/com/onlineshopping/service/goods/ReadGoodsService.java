package com.onlineshopping.service.goods;

import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.goods.CatalogDetail;
import com.onlineshopping.entity.goods.CommentsPage;
import com.onlineshopping.entity.goods.GoodsPage;
import com.onlineshopping.exception.*;

import java.util.List;


/**
 * Created by W on 2017/6/14.
 */
public interface ReadGoodsService {

    /**
     *
     * @return 返回种类目录
     */
    List<CatalogDetail> getCatalogsDetail();

    /**
     *
     * @param catalogName
     * @param pageNum
     * @return
     * @throws NoCatalogException 当目录中没有该种类时抛出该异常
     * @throws PageNumFaultException 页码错误时抛出该异常
     * @throws NoGoodsException 搜索结果为空时抛出该异常
     */
    GoodsPage getPagedGoodsIdsByCatalogName(String catalogName, int pageNum) throws
            NoCatalogException, PageNumFaultException, NoGoodsException;

    /**
     *
     * @param keyword 不能为null，也不能为空串
     * @param pageNum 不能小于1
     * @return
     * @throws PageNumFaultException
     * @throws NoGoodsException
     */
    GoodsPage getGoodsPage(String keyword, int pageNum) throws PageNumFaultException, NoGoodsException;

    /**
     *
     * @param id
     * @return
     * @throws NoGoodsException
     */
    Goods getGoodsById(String id) throws NoGoodsException;

    /**
     *
     * @param goodsId
     * @param pageNum
     * @return
     * @throws PageNumFaultException
     * @throws NoGoodsException
     */
    CommentsPage getPagedCommentByGoodsId(String goodsId, int pageNum)
            throws PageNumFaultException, NoGoodsException, NoCommentsException;

    GoodsPage getPagedSpecialGoodsIds(String keyword, Float minPrice, Float maxPrice,
                                      String catalogName, int pageNum)
            throws InvalidParameterException, PageNumFaultException, NoGoodsException, NoCatalogException;

    Result deleteCommentById(Integer commentId);

    void setCatalogChange(boolean isCatalogChange);
}
