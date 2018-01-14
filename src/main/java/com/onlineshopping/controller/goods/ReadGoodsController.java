package com.onlineshopping.controller.goods;

import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.goods.CommentsPage;
import com.onlineshopping.entity.goods.GoodsPage;
import com.onlineshopping.exception.*;


import com.onlineshopping.service.goods.ReadGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by W on 2017/6/9.
 * 该控制器用于处理与浏览商品有关的请求
 */
@RestController
public class ReadGoodsController {

    private static final Logger logger = LoggerFactory.getLogger(ReadGoodsController.class);

    @Resource
    ReadGoodsService readGoodsService;

    @RequestMapping(value = "/catalog", method = RequestMethod.GET)
    Result getCatalog() {
        logger.info("访问: /catalog");
        long startTime = System.currentTimeMillis();
        Result result = new Result();
        List catalogsDetail = readGoodsService.getCatalogsDetail();
        if (catalogsDetail == null || catalogsDetail.isEmpty()) {
            catalogsDetail = null;
            result.setResultCode(-1);
            result.setResultInfo("目录数据为空");
        } else {
            result.setResultCode(0);
            result.setResultInfo("请求成功");
        }
        result.setContent(catalogsDetail);
        long endTime = System.currentTimeMillis();
        logger.info("访问/catalog结束，耗时：" + (endTime - startTime));
        return result;
    }

    @RequestMapping(value = "/catalog/{catalogName}/goods/page-{pageNum}", method = RequestMethod.GET)
    Result getPagedGoodsIdByCatalogName(@PathVariable("catalogName") String catalogName,
                                             @PathVariable("pageNum") int pageNum) {
        try {
            catalogName = URLDecoder.decode(catalogName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("访问: /catalog/" + catalogName + "/goods/page-" + pageNum);
        Result result = new Result();
        try {
            GoodsPage goodsPage = readGoodsService.getPagedGoodsIdsByCatalogName(catalogName, pageNum);
            result.setResultCode(0);
            result.setResultInfo("请求成功");
            result.setContent(goodsPage);
        } catch (NoGoodsException e) {
            result.setResultCode(-3);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (PageNumFaultException e) {
            result.setResultCode(-2);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (NoCatalogException e) {
            result.setResultCode(-1);
            result.setResultInfo(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/goods/{goodsId}/info", method = RequestMethod.GET)
    Result getGoodsById(@PathVariable("goodsId") String goodsId) {
        logger.info("访问: /goods/" + goodsId + "/info");
        Result result = new Result();
        try {
            Goods goods = readGoodsService.getGoodsById(goodsId);
            result.setResultCode(0);
            result.setResultInfo("请求成功");
            result.setContent(goods);
        } catch (NoGoodsException e) {
            result.setResultCode(-1);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        }
        return result;
    }

    @RequestMapping(value = "/goods/{goodsId}/comment", method = RequestMethod.GET)
    Result getCommentByGoodsId(@PathVariable("goodsId") String goodsId) {
        logger.info("访问: /goods/" + goodsId + "/comment");
        return getPagedCommentByGoodsId(goodsId, 1);
    }

    @RequestMapping(value = "/goods/{goodsId}/comment/{commentId}")
    Result deleteCommentById(@PathVariable("goodsId") String goodsId, @PathVariable("commentId") Integer commentId) {
        logger.info("访问:/goods/"+goodsId+"/comment/"+commentId);
        return readGoodsService.deleteCommentById(commentId);
    }

    @RequestMapping(value = "/goods/{goodsId}/comment/page-{pageNum}", method = RequestMethod.GET)
    Result getPagedCommentByGoodsId(@PathVariable("goodsId") String goodsId,
                                    @PathVariable("pageNum") int pageNum) {
        logger.info("访问: /goods/" + goodsId + "/comment/page-" + pageNum);
        Result result = new Result();
        try {
            CommentsPage commentsPage = readGoodsService.getPagedCommentByGoodsId(goodsId, pageNum);
            if (commentsPage == null || commentsPage.getComments().isEmpty()) {
                result.setResultCode(-2);
                result.setResultInfo("评论数量为空");
                result.setContent(null);
            } else {
                result.setResultCode(0);
                result.setResultInfo("请求成功");
                result.setContent(commentsPage);
            }
        } catch (PageNumFaultException e) {
            result.setResultCode(-3);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (NoGoodsException e) {
            result.setResultCode(-1);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (NoCommentsException e) {
            result.setResultCode(-2);
            result.setResultInfo("评论数量为空");
            result.setContent(null);
        }
        return result;
    }

    @RequestMapping(value = "/goods/like-{keyword}/page-{pageNum}", method = RequestMethod.GET)
    Result getPagedGoodsIdsByKeyword(@PathVariable("keyword") String keyword,
                                     @PathVariable("pageNum") int pageNum) {
        logger.info("访问: /goods/like-" + keyword + "/page-" + pageNum);
        Result result = new Result();
        try {
            GoodsPage goodsPage = readGoodsService.getGoodsPage(keyword, pageNum);
            result.setResultCode(0);
            result.setResultInfo("请求成功");
            result.setContent(goodsPage);
        } catch (PageNumFaultException e) {
            result.setResultCode(-2);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (NoGoodsException e) {
            result.setResultCode(-1);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        }
        return result;
    }

    @RequestMapping(value = "/goods/like-{keyword}", method = RequestMethod.GET)
    Result getPagedGoodsIdsByKeyword(@PathVariable("keyword") String keyword) {
        logger.info("访问: /goods/like-" + keyword);
        return getPagedGoodsIdsByKeyword(keyword, 1);
    }

    @RequestMapping(value = "/special-goods/page-{pageNum}", method = RequestMethod.GET)
    Result getPagedSpecialGoodsId(@PathVariable("pageNum") int pageNum,
                                  @RequestParam(value = "catalog_name",
                                          required = false) String catalogName,
                                  @RequestParam(value = "small_price",
                                          required = false) String minPriceStr,
                                  @RequestParam(value = "large_price",
                                          required = false) String maxPriceStr,
                                  @RequestParam(value = "keyword",
                                          required = false) String keyword) {
        logger.info("访问: /special-goods/page-" + pageNum + ", catalog_name = " + catalogName +
                ", small_price = " + minPriceStr + ", large_price = " + maxPriceStr + ", keyword = " +
                keyword);
        if (keyword.equals("")) keyword = null;
        if (catalogName.equals("")) catalogName = null;
        Result result = new Result();
        try {
            Float minPrice = "".equals(minPriceStr) ? null : Float.valueOf(minPriceStr);
            Float maxPrice = "".equals(maxPriceStr) ? null : Float.valueOf(maxPriceStr);
            GoodsPage goodsPage = readGoodsService.getPagedSpecialGoodsIds(keyword,
                    minPrice, maxPrice, catalogName, pageNum);
            if (goodsPage.getGoodsIds() == null) {
                throw new NoGoodsException();
            }
            result.setResultCode(0);
            result.setResultInfo("请求成功");
            result.setContent(goodsPage);
        } catch (NumberFormatException | InvalidParameterException e){
            result.setResultCode(-2);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        }catch (NoGoodsException e) {
            result.setResultCode(-3);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (PageNumFaultException e) {
            result.setResultCode(-4);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        } catch (NoCatalogException e) {
            result.setResultCode(-1);
            result.setResultInfo(e.getMessage());
            result.setContent(null);
        }
        return result;
    }


}
