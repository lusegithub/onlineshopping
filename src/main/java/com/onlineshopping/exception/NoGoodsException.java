package com.onlineshopping.exception;

/**
 * Created by W on 2017/6/14.
 */
public class NoGoodsException extends Exception {

    public NoGoodsException() {
        this("商品数量为空");
    }

    public NoGoodsException(String msg) {
        super(msg);
    }
}
