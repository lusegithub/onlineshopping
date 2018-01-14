package com.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="address")
public class Address implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id@ Column(name="address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable=false)
    private User user;
    @Column(name="name",nullable = false,length =20)
    private String  name;//the name of the receiver
    @Column(name="address",nullable = false,length = 50)
    private String  address;
    @Column(name="zip",length =6)
    private String  zip;//enable null
    @Column(name="phone",nullable = false,length=20)
    private String  phoneNumber;


    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer id) {
        this.addressId = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
        public int hashCode(){
            final int prime = 31;
            int result = 1;

            result = prime * result + (name == null? 0:name.hashCode());
            result = prime * result + (address == null?0:address.hashCode());
            result = prime * result + (zip==null?0:zip.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj){
            if(this==obj)return true;
            if(obj==null)return false;
            if(obj.getClass()!=this.getClass())return false;
            else{
                Address other = (Address)obj;
                if(zip==null){
                    if(other!=null)return false;
                }
                else if(!this.zip.equals(other.zip))return false;
                else{
                    if(this.name==other.name&&this.address==other.address)return true;

                }

            }
            return false;
        }
}
