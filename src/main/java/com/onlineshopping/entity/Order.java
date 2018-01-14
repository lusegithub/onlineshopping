package com.onlineshopping.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onlineshopping.util.DateSerializerUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.onlineshopping.entity.StatusType.待审核;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="orders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    @Column(name="order_time",columnDefinition = "timestamp default CURRENT_TIMESTAMP",nullable = false)
    private Date orderTime;//defualt current time
    @Column(name="price",columnDefinition = "numeric(6,2) default 0.0",nullable = false)
    private Double price;
    @Column(name="consignee",length = 20,nullable = false)
    private String consignee;
    @Column(name="address",length = 50,nullable = false)
    private String address;
    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status=待审核;
    @Column(name="discount",nullable = false,columnDefinition = "numeric(3,2)")
    private double discount;//>0 <=1
    @Column(name="phone",nullable = false,length=20)
    private String phone;
    @Column(name="points",nullable = false)
    private int points;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable = false)
    private User user;

    @ElementCollection(targetClass = OrderDetail.class)
    @CollectionTable(name="order_detail",joinColumns = @JoinColumn(name="order_id",nullable=false))
    @OrderColumn(name="list_order")
    private List<OrderDetail> orderDetailList;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer id) {
        this.orderId = id;
    }

    @JsonSerialize(using = DateSerializerUtil.class)
    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderTime=" + orderTime +
                ", price=" + price +
                ", consignee='" + consignee + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", discount=" + discount +
                ", phone='" + phone + '\'' +
                ", user=" + user +
          //      ", orderDetailSet=" + orderDetailList +
                '}';
    }
}
