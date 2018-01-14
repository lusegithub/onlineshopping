package com.onlineshopping.entity;

import javax.persistence.*;
import java.io.Serializable;

import static com.onlineshopping.entity.LevelType.普通会员;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="vip_level")
public class VipLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @Column(name="level")
    @Enumerated(EnumType.STRING)
    private LevelType level=普通会员;
    @Column(name="lower_limit_credit",columnDefinition = "int unsigned default 0",nullable = false)
    private int lowerLimitCredit=0;
    @Column(name="rate_credit",columnDefinition = "numeric(3,2) default 1.00",nullable = false)
    private double rateCredit=1.0;
    @Column(name="discount",columnDefinition = "numeric(3,2) default 1.00",nullable = false)
    private double discount=1.0;
    @Column(name="note",length = 50)
    private String note;


    public LevelType getLevel() {
        return level;
    }

    public void setLevel(LevelType level) {
        this.level = level;
    }

    public int getLowerLimitCredit() {
        return lowerLimitCredit;
    }

    public void setLowerLimitCredit(int lowerLimitCredit) {
        this.lowerLimitCredit = lowerLimitCredit;
    }

    public double getRateCredit() {
        return rateCredit;
    }

    public void setRateCredit(double rateCredit) {
        this.rateCredit = rateCredit;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "VipLevel{" +
                "level=" + level +
                ", lowerLimitCredit=" + lowerLimitCredit +
                ", rateCredit=" + rateCredit +
                ", discount=" + discount +
                ", note='" + note + '\'' +
                '}';
    }
}
