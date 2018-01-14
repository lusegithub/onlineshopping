package com.onlineshopping.entity.goods;

/**
 * Created by W on 2017/6/13.
 */
public class CatalogDetail {

    private String catalogName;
    private int totalPagesNum;
    private int totalGoodsNum;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public int getTotalPagesNum() {
        return totalPagesNum;
    }

    public void setTotalPagesNum(int totalPagesNum) {
        this.totalPagesNum = totalPagesNum;
    }

    public int getTotalGoodsNum() {
        return totalGoodsNum;
    }

    public void setTotalGoodsNum(int totalGoodsNum) {
        this.totalGoodsNum = totalGoodsNum;
    }

    @Override
    public String toString() {
        return "catalogName=" + catalogName + ", totalPagesNum=" + totalPagesNum +
                ", totalGoodsNum="
                + totalGoodsNum;
    }
}
