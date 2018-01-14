package com.onlineshopping.exception;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by hehe on 17-6-20.
 */
public class ForeignKeyException extends Exception{
    private static Logger loger = Logger.getLogger(ForeignKeyException.class);
    public ForeignKeyException(){
    }
    public ForeignKeyException(String msg){
        loger.info(msg);
    }

}
