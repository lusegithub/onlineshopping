package com.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="goods")
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @Column(name="goods_id",length = 20)
    private String goodsId;
    @ManyToOne(targetEntity = Catalog.class)
    @JoinColumn(name="catalog_id",referencedColumnName = "catalog_id",nullable = false)
    private Catalog catalog;
    @Column(name="catalog_name")
    private String catalogName;
    @Column(name="name",nullable = false)
    private String  name;
    @Column(name="description",nullable = false)
    private String  describe;
    @Column(name="photo",nullable = false)
    private String  photo;
    @Column(name="price",columnDefinition = "numeric(6,2) default 0.0",nullable = false)
    private double  price;//>=0
    @Column(name="stock",nullable = false)
    private int stock;//>=0
    @Column(name = "sign")
    private int sign = 1; // 判断商品是否被删除的标识
    @OneToMany(targetEntity = Comment.class,mappedBy = "goods")
    private Set<Comment> commentSet = new HashSet<>();
    @ManyToMany(targetEntity = User.class)
    @JoinTable(name="shopping_cart",joinColumns = @JoinColumn(name="goods_id",referencedColumnName = "goods_id"),
    inverseJoinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id"))
    private Set<User> shoppingCart = new HashSet<>();
    @ManyToMany(targetEntity = User.class)
    @JoinTable(name="favorite",joinColumns = @JoinColumn(name="goods_id",referencedColumnName = "goods_id"), inverseJoinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id"))

    private Set<User> favorite = new HashSet<>();

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getGoodsId() {return goodsId;}

    public void setGoodsId(String id) {
        this.goodsId = id;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    @JsonIgnore
    public Set<User> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Set<User> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @JsonIgnore
    public Set<User> getFavorite() {
        return favorite;
    }

    public void setFavorite(Set<User> favorite) {
        this.favorite = favorite;
    }

    @JsonIgnore
    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", catalog=" + catalog +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", photo='" + photo + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result= 1;
        result = prime*result + (catalog==null?0:catalog.hashCode());
        result = prime*result + (name==null?0:name.hashCode());
        result = prime*result + (describe==null?0:describe.hashCode());
        result = prime*result + (photo==null?0:photo.hashCode());
       // result = prime*result + Double.doubleToLongBits(price)
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Goods) {
            String objId = ((Goods) obj).getGoodsId();
            if (this.getGoodsId().equals(objId)) {
                return true;
            }
        }
        return false;
    }
}
