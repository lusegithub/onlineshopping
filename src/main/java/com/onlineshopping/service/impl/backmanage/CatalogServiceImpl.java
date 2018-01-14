package com.onlineshopping.service.impl.backmanage;

import com.onlineshopping.dao.CatalogDao;
import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.CatalogService;
import com.onlineshopping.service.goods.ReadGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * Created by lsy
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    private Result result = new Result();
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);
    @Resource
    private ReadGoodsService readGoodsService;
    @Resource
    private CatalogDao CatalogDao;
    @Resource
    private GoodsDao GoodsDao;

    /**
     * 增加目录
     *
     * @param name 目录名
     * @return
     */
    @Override
    public Result add(String name) {
        if (illegal(name)) {
            result.setResultCode(-1);
            logger.error(result.getResultInfo());
            return result;
        }

        if (CatalogDao.getByName(name) != null) {
            result.setResultCode(-2);
            result.setResultInfo("目录\"" + name + "\"已存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        // 增加目录
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setSign(1);
        CatalogDao.save(catalog);
        readGoodsService.setCatalogChange(true);

        result.setResultCode(0);
        result.setResultInfo("增加目录成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 更新目录
     *
     * @param old_name 旧目录名
     * @param new_name 新目录名
     * @return
     */
    @Override
    public Result update(String old_name, String new_name) {
        if (illegal(new_name)) {
            result.setResultCode(-1);
            logger.error(result.getResultInfo());
            return result;
        }

        Catalog old_catalog = CatalogDao.getByName(old_name);
        if (old_catalog == null) {
            result.setResultCode(-1);
            result.setResultInfo("目录\"" + old_name + "\"不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        // 更新目录
        old_catalog.setName(new_name);
        CatalogDao.update(old_catalog);
        readGoodsService.setCatalogChange(true);

        result.setResultCode(0);
        result.setResultInfo("更新目录成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    private boolean illegal(String name) {
        if (name.length() < 2) {
            result.setResultInfo("目录过短，不能短于两个字符！");
            return true;
        }
        if (name.length() > 10) {
            result.setResultInfo("目录过长，不能长于十个字符！");
            return true;
        }

        String regex = "[\\u4E00-\\u9FFF_a-zA-Z]+(/[\\u4E00-\\u9FFF_a-zA-Z]+)?";
        if (!Pattern.matches(regex, name)) {
            result.setResultInfo("目录名不合法！");
            return true;
        }

        return false;
    }

    /**
     * 删除目录
     *
     * @param name 目录名
     * @return
     */
    @Override
    public Result delete(String name) {
        Catalog catalog = CatalogDao.getByName(name);

        if (catalog == null) {
            result.setResultCode(-1);
            result.setResultInfo("目录\"" + name + "\"不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        if (GoodsDao.getGoodsNumByCatalog(catalog) > 0) {
            result.setResultCode(-2);
            result.setResultInfo("目录\"" + name + "\"不为空！");
            logger.error(result.getResultInfo());
            return result;
        }

        // 删除目录
        CatalogDao.delete(catalog);
        readGoodsService.setCatalogChange(true);

        result.setResultCode(0);
        result.setResultInfo("删除成功！");
        logger.info(result.getResultInfo());
        return result;
    }
}
