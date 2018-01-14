package com.onlineshopping.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loong on 2017/6/28.
 */
public class Points {
    private int totalPoints;
    List<PointsItem> pointsItems;

    public Points() {
        pointsItems = new ArrayList<>();
    }

    public Points(int totalPoints, List<PointsItem> pointsItems) {
        this.totalPoints = totalPoints;
        if (pointsItems==null)
            this.pointsItems = new ArrayList<>();
        else
            this.pointsItems = pointsItems;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<PointsItem> getPointsItems() {
        return pointsItems;
    }

    public void setPointsItems(List<PointsItem> pointsItems) {
        this.pointsItems = pointsItems;
    }

    public void addPointsItem(PointsItem pointsItem) {
        pointsItems.add(pointsItem);
    }

    public void removePointsItem(PointsItem pointsItem) {
        pointsItems.remove(pointsItem);
    }
}
