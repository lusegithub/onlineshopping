package com.onlineshopping.service.backmanage;

import com.onlineshopping.entity.Goods;
import com.onlineshopping.entity.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lsy
 */
public interface GoodsService {
    Result add(Goods goods, MultipartFile file);

    Result update(Goods goods, MultipartFile file);

    Result delete(String goods_id);
}
