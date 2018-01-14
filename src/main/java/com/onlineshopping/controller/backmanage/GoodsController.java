package com.onlineshopping.controller.backmanage;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * created by lsy
 */
@RestController
@RequestMapping("/catalog/{catalog_name}/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    /**
     * 添加商品
     * @param catalog_name  目录名
     * @param ISBN          ISBN作为商品ID
     * @param goods_name    商品名称
     * @param file          图片文件
     * @param describe      商品描述
     * @param goods_price   商品价格
     * @param stock         商品数量
     * @return
     */
    @PostMapping
    @ManagerAccessRequired
    public Result add(
            @PathVariable String catalog_name,
            String ISBN,
            String goods_name,
            MultipartFile file,
            String describe,
            double goods_price,
            int stock) {
        // create catalog entity and set attribute
        try {
            catalog_name = URLDecoder.decode(catalog_name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Catalog catalog = new Catalog();
        catalog.setName(catalog_name);

        // create goods entity and set attributes
        Goods goods = new Goods();
        goods.setGoodsId(ISBN);
        goods.setCatalog(catalog);
        goods.setName(goods_name);
        goods.setDescribe(describe);
        goods.setPrice(goods_price);
        goods.setStock(stock);

        logger.info("管理员添加商品" + goods.toString());
        return goodsService.add(goods, file);
    }

    /**
     * 更新商品信息
     * @param goods_id      商品ID
     * @param catalog_name  目录名
     * @param goods_name    商品名称
     * @param file          图片文件
     * @param describe      商品描述
     * @param goods_price   商品价格
     * @param stock         商品数量
     * @return
     */
    @PostMapping("/{goods_id}")
    @ManagerAccessRequired
    public Result update(
            @PathVariable String goods_id,
            @PathVariable String catalog_name,
            String goods_name,
            MultipartFile file,
            String describe,
            double goods_price,
            int stock) {
        // create catalog entity and set attribute
        try {
            catalog_name = URLDecoder.decode(catalog_name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("目录名解码错误");
            e.printStackTrace();
        }
        Catalog catalog = new Catalog();
        catalog.setName(catalog_name);

        // create goods entity and set attributes
        Goods goods = new Goods();
        goods.setGoodsId(goods_id);
        goods.setCatalog(catalog);
        goods.setName(goods_name);
        goods.setDescribe(describe);
        goods.setPrice(goods_price);
        goods.setStock(stock);

        logger.info("管理员更新商品" + goods.toString());
        return goodsService.update(goods, file);
    }

    /**
     * 删除商品
     * @param goods_id 商品ID
     * @return
     */
    @DeleteMapping("/{goods_id}")
    @ManagerAccessRequired
    public Result delete(@PathVariable String goods_id) {
        logger.info("管理员删除商品" + goods_id);
        return goodsService.delete(goods_id);
    }
}
