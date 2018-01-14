package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.ManagerDao;
import com.onlineshopping.entity.Manager;
import org.springframework.stereotype.Repository;

/**
 * Created by hehe on 17-6-17.
 */
@Repository("ManagerDao")
public class ManagerDaoImpl extends BaseDaoHibernate<Manager> implements ManagerDao {

}
