package com.onlineshopping.exception;

/**
 * Created by W on 2017/6/15.
 */
public class NoCatalogException extends Exception {

    public NoCatalogException() {
        this("该种类不存在");
    }

    public NoCatalogException(String msg) {
        super("该种类不存在");
    }
}
