package com.onlineshopping.exception;

/**
 * Created by W on 2017/6/15.
 */
public class InvalidParameterException extends Exception {

    private static String defaultMsg = "参数缺失";

    public InvalidParameterException() {
        this(defaultMsg);
    }

    public InvalidParameterException(String msg) {
        super(msg);
    }
}
