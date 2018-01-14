package com.onlineshopping.entity;

/**
 * Created by W on 2017/6/12.
 */
public class Result {
    private int resultCode;
    private String resultInfo;
    private Object content;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", resultInfo='" + resultInfo + '\'' +
                ", content=" + content +
                '}';
    }
}
