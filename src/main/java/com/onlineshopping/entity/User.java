package com.onlineshopping.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.util.DateSerializerUtil;

import javax.annotation.Resource;
import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @Column(name="user_id",length = 30)
    private String userId;
    @Column(name="nickname",length = 30,nullable = false)
    private String  nickname;
    @Column(name="password",length = 20,nullable = false)
    private String  password;
    @Column(name="regdate",columnDefinition = "timestamp default CURRENT_TIMESTAMP",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date    regdate;//default current time
    @Column(name="points",columnDefinition = "int default 0")
    private int points=0;
    @Transient
    private transient LevelType level = LevelType.普通会员;  // 不实现持久化
    @Transient
   // @Resource(name="UserDao")
   // private UserDao userDao;
    @ManyToMany(targetEntity = Goods.class)
    @JoinTable(name="shopping_cart",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id"),
    inverseJoinColumns = @JoinColumn(name="goods_id",referencedColumnName = "goods_id"))
    private Set<Goods> shoppingcart = new HashSet<>();
    @ManyToMany(targetEntity = Goods.class)
    @JoinTable(name="favorite",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name="goods_id",referencedColumnName = "goods_id"))
    private Set<Goods> favorite = new HashSet<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonSerialize(using = DateSerializerUtil.class)
    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {

        this.points = points;
        //setLevel(userDao.getLevelTypeByPoint(points));

    }

    public LevelType getLevel() {
        return level;
    }

    public void setLevel(LevelType level) {
        this.level = level;
    }

    @JsonIgnore
    public Set<Goods> getShoppingcart() {
        return shoppingcart;
    }

    public void setShoppingcart(Set<Goods> shoppingcart) {
        this.shoppingcart = shoppingcart;
    }

    @JsonIgnore
    public Set<Goods> getFavorite() {
        return favorite;
    }

    public void setFavorite(Set<Goods> favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", regdate=" + regdate +
                ", points=" + points +
                ", level=" + level +
                '}';
    }
}
