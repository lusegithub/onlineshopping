package com.onlineshopping.entity.goods;

import java.util.List;

/**
 * Created by W on 2017/6/13.
 */
public class GoodsPage {

    private int pageNum;
    private int goodsNum;
    private int totalPagesNum;
    private int totalGoodsNum;
    private List<String> goodsIds;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
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

    public List<String> getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(List<String> goodsIds) {
        this.goodsIds = goodsIds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("pageNum=").append(pageNum).append(", goodsNum=").append(goodsNum)
                .append(", totalPagesNum=").append(totalPagesNum).append(", totalGoodsNum=")
                .append(totalGoodsNum).append(", goodsIds=[");
        if (getGoodsIds() != null && !getGoodsIds().isEmpty()) {
            for (int i = 0; i < goodsIds.size() - 1; i++) {
                sb.append(goodsIds.get(i)).append(", ");
            }
            sb.append(goodsIds.get(goodsIds.size()-1));
        }
        sb.append("]");
        return sb.toString();
    }
}
