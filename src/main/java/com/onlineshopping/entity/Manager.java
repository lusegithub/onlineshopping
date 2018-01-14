package com.onlineshopping.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name="manager")
public class Manager implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="manager_id",length = 20,nullable = false)
    private String manager_id;
    @Column(name="password",length =20,nullable = false)
    private String password;

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "manager_id='" + manager_id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
