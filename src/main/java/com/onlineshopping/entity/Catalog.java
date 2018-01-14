package com.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="catalog")
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id@ Column(name="catalog_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catalogId;
    @Column(name="catalog_name",length=10,nullable=false)
    private  String name;
    @Column(name="sign")
    private  int sign;
    @OneToMany(targetEntity = Goods.class,mappedBy = "catalog")
    private Set<Goods> goodsSet = new HashSet<>();

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    @JsonIgnore
    public Set<Goods> getGoodsSet() {
        return goodsSet;
    }

    public void setGoodsSet(Set<Goods> goodsSet) {
        this.goodsSet = goodsSet;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "name='" + name + '\'' +
                '}';
    }
    @Override
        public int hashCode(){
            final int prime = 31;
            int result =1;
            result = prime * result + (name == null? 0:name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj){
            if(this==obj)return true;
            if(obj==null)return false;
            if(obj.getClass()!=this.getClass())return false;
            else {
                Catalog other = (Catalog) obj;
                if(other.getName()==this.getName())return true;
            }
            return false;
        }
}
