package com.onlineshopping.controller.backmanage;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * created by lsy
 */
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private static final Logger log= LoggerFactory.getLogger(CatalogController.class);

    @Resource
    private CatalogService catalogService;

    /**
     * 添加目录
     * @param catalog_name 目录名
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ManagerAccessRequired
    public Result add(String catalog_name){
        log.info("管理员添加目录" + catalog_name);
        return catalogService.add(catalog_name);
    }

    /**
     * 修改目录
     * @param old_catalog_name 旧目录名
     * @param new_catalog_name 新目录名
     * @return
     */
    @PutMapping
    @ManagerAccessRequired
    public Result update(String old_catalog_name, String new_catalog_name){
        log.info("管理员更新目录" + old_catalog_name + "为" + new_catalog_name);
        return catalogService.update(old_catalog_name, new_catalog_name);
    }

    /**
     * 删除目录
     * @param catalog_name 目录名
     * @return
     */
    @DeleteMapping("/{catalog_name}")
    @ManagerAccessRequired
    public Result delete(@PathVariable String catalog_name) {
        try {
            catalog_name = URLDecoder.decode(catalog_name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("管理员删除目录" + catalog_name);
        return catalogService.delete(catalog_name);
    }
}
