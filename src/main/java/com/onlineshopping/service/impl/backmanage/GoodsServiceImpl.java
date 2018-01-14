package com.onlineshopping.service.impl.backmanage;

import com.onlineshopping.dao.CatalogDao;
import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.entity.Catalog;
import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by lsy
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    private Result result = new Result();
    private static final String PHOTO_DIR = "goods-image/";
    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    @Resource
    private GoodsDao GoodsDao;
    @Resource
    private CatalogDao CatalogDao;

    /**
     * 添加商品
     * @param goods 商品信息
     * @param file  图片文件
     * @return
     */
    @Override
    public Result add(Goods goods, MultipartFile file) {
        if (goods.getGoodsId().length() > 20) {
            result.setResultCode(-1);
            result.setResultInfo("ISBN长度超过20！");
            logger.error(result.getResultInfo());
            return result;
        }
        Goods myGoods = GoodsDao.get(Goods.class, goods.getGoodsId());
        if (myGoods != null) {
            if (myGoods.getSign() == 1) {
                result.setResultCode(-1);
                result.setResultInfo("商品" + goods.getGoodsId() + "已存在");
                logger.error(result.getResultInfo());
                return result;
            }
            else {
                // 商品被删除后再添加时，直接更新
//                goods.setSign(1);
                result = update(goods, file);
                if (result.getResultInfo().equals("更新商品信息成功！")) {
                    result.setResultInfo("添加商品成功");
                }
                return update(goods, file);
            }
        }

        //商品信息不合法
        if (illegal(goods)) {
            logger.error(result.getResultInfo());
            return result;
        }

        if (file == null || file.isEmpty()) {
            result.setResultCode(-4);
            result.setResultInfo("请上传商品图片");
            logger.error(result.getResultInfo());
            return result;
        } else if (illegal(file)) {
            result.setResultCode(-4);
            logger.error(result.getResultInfo());
            return result;
        }

        // 上传图片
        goods.setPhoto(addPhoto(file));
        if (goods.getPhoto() == null) {
            result.setResultCode(-8);
            result.setResultInfo("上传图片失败！");
            logger.error(result.getResultInfo());
        } else {
            // 添加商品
            GoodsDao.save(goods);
            result.setResultCode(0);
            result.setResultInfo("添加商品成功！");
            logger.info(result.getResultInfo());
        }
        return result;
    }

    /**
     * 更新商品信息
     * @param goods 商品信息
     * @param file  图片文件
     * @return
     */
    @Override
    public Result update(Goods goods, MultipartFile file) {
        //检查商品信息
        if (illegal(goods)) {
            logger.error(result.getResultInfo());
            return result;
        }

        //若上传了图片，则检查图片信息
        boolean upload = file != null && !file.isEmpty();
        if (upload) {
            if (illegal(file)) {
                result.setResultCode(-4);
                logger.error(result.getResultInfo());
                return result;
            }else {
                //图片合法，更新图片
                goods.setPhoto(addPhoto(file));
                if (goods.getPhoto() != null) {
                    // 上传图片成功，删除旧图片
                    deletePhoto(goods.getPhoto());
                }
            }
        }

        Goods old_goods = GoodsDao.get(Goods.class, goods.getGoodsId());
        if (old_goods == null) {
            result.setResultCode(-2);
            result.setResultInfo("该商品" + goods.getGoodsId() + "不存在！");
            logger.error(result.getResultInfo());
            return result;
        } else {
            old_goods.setName(goods.getName());
            old_goods.setDescribe(goods.getDescribe());
            if (goods.getPhoto() != null) {
                old_goods.setPhoto(goods.getPhoto());
            }
            old_goods.setPrice(goods.getPrice());
            old_goods.setStock(goods.getStock());
            old_goods.setSign(goods.getSign());
            GoodsDao.update(old_goods);
            result.setResultCode(0);
            if (upload && goods.getPhoto() == null) {
                result.setResultInfo("图片上传失败，更新商品的其他信息！");
            } else {
                result.setResultInfo("更新商品信息成功！");
            }
            logger.info(result.getResultInfo());
            return result;
        }
    }

    /**
     * 删除商品
     * @param goods_id 商品ID
     * @return
     */
    @Override
    public Result delete(String goods_id) {
        Goods goods = GoodsDao.get(Goods.class, goods_id);
        if (goods == null) {
            result.setResultCode(-1);
            result.setResultInfo("该商品不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        //先删除图片，再删除商品
        deletePhoto(goods.getPhoto());
        GoodsDao.delete(goods);

        result.setResultCode(0);
        result.setResultInfo("删除商品成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 生成图片URL
     * @param filename 图片文件名
     * @return
     */
    private String getUrl(String filename) {
        return "http://localhost:8080/onlineshopping/" + PHOTO_DIR + filename;
    }

    /**
     * 上传图片
     * @param file 图片文件
     * @return
     */
    private String addPhoto(MultipartFile file) {
        //获取上传图片的路径
        String uploadPath = System.getProperty("webapp.root") + PHOTO_DIR;
        String filename;

        // 上传图片
        filename = System.currentTimeMillis() +"."+ file.getContentType().split("/")[1];
        File dest = new File(uploadPath + filename);
        try {
            dest.createNewFile();
            file.transferTo(dest);
            logger.info("上传图片" + filename + "成功");
            return getUrl(filename);
        } catch (IOException e) {
            logger.error("上传图片" + filename + "失败");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除图片
     * @param url 图片URL
     */
    private void deletePhoto(String url) {
        String uploadPath = System.getProperty("webapp.root") + "goods_image/";
        String[] urls = url.split("/");
        String filename = urls[urls.length - 1];
        File oldFile = new File(uploadPath + filename);
        if (oldFile.exists()) {
            oldFile.delete();
            logger.info("删除图片" + filename);
        }
    }

    /**
     * 检查图片信息
     * @param file 图片文件
     * @return
     */
    private boolean illegal(MultipartFile file) {
        if (!file.getContentType().split("/")[0].equals("image") ){
            result.setResultInfo("上传的文件不是图片格式！");
            return true;
        }
        return false;
    }
    /**
     * 检查商品信息
     * @param goods 商品信息
     * @return 如果商品信息不合法，返回true，否则返回false
     */
    private boolean illegal(Goods goods) {
        Catalog catalog = CatalogDao.getByName(goods.getCatalog().getName());
        if (catalog == null) {
            result.setResultCode(-1);
            result.setResultInfo("目录" + goods.getCatalog().getName() + "不存在！");
            return true;
        } else {
            goods.setCatalog(catalog);
        }

        if (goods.getName().length() > 150) {
            result.setResultCode(-3);
            result.setResultInfo("商品名长度超过150！");
            return true;
        }

        if (goods.getDescribe().length() > 5000) {
            result.setResultCode(-5);
            result.setResultInfo("商品描述长度超过5000！");
            return true;
        }

        if (goods.getPrice() <= 0) {
            result.setResultCode(-6);
            result.setResultInfo("市场价小于或等于0！");
            return true;
        } else if(goods.getPrice() >= 10000){
            result.setResultCode(-6);
            result.setResultInfo("市场价大于或等于10000！");
            return true;
        }else {
            DecimalFormat df = new DecimalFormat("#.00");
            goods.setPrice(Double.valueOf(df.format(goods.getPrice())));
        }

        if (goods.getStock() < 0) {
            result.setResultCode(-7);
            result.setResultInfo("库存量小于0！");
            return true;
        }

        return false;
    }
}
