package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.UserDao;
import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.User;
import org.hibernate.Query;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import static com.onlineshopping.entity.LevelType.*;

/**
 * Created by hehe on 17-6-15.
 */
@Repository("UserDao")
public class UserDaoImpl extends BaseDaoHibernate<User> implements UserDao{

    @Override
    public boolean isUserExist(String user_id){
        String str = "select * from user where user_id='"+user_id+"'";
        List list=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        if(list.size()==0){
            return false;
        }
        return true;
    }

    @Override
    public User findUserByUserId(String user_id){
        User user = get(User.class,user_id);
        if(user==null)return null;
        user.setLevel(getLevelTypeByPoint(user.getPoints()));
        return user;
    }

    @Override
    public List<User> getUsersByLevel(LevelType level) {
        int[] lAndh=getLowerAndHigherByLevel(level);
        String str="SELECT e.* from user e where e.points BETWEEN ?1 AND ?2";
        List<User> users = findUseSql(str,User.class,lAndh[0],lAndh[1]);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByBeginTime(Date beginTime) {
        String str ="select e.* from user e where e.regdate>=?1";
        List<User> users = findUseSql(str,User.class,beginTime);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByEndTime(Date endTime) {
        String str="select e.* FROM user e where e.regdate<?1";
        List<User> users = findUseSql(str,User.class,endTime);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByLevelBegin(LevelType level, Date beginTime) {
        int[] lAndh=getLowerAndHigherByLevel(level);
        String str="select e.* FROM user e where e.regdate>=?1 AND e.points BETWEEN ?2 AND ?3";
        List<User> users = findUseSql(str,User.class,beginTime,lAndh[0],lAndh[1]);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByLevelEnd(LevelType level, Date endTime) {
        int[] lAndh=getLowerAndHigherByLevel(level);
        String str="select e.* FROM user e where e.regdate<?1 AND e.points BETWEEN ?2 AND ?3";
        List<User> users = findUseSql(str,User.class,endTime,lAndh[0],lAndh[1]);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByBeginEnd(Date beginTime, Date endTime) {
        String str="select e.* FROM user e where e.regdate>=?1 and e.regdate<?2";
        List<User> users = findUseSql(str,User.class,beginTime,endTime);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public List<User> getUsersByLevelBeginEnd(LevelType level, Date beginTime, Date endTime) {
        int[] lAndh=getLowerAndHigherByLevel(level);
        String str="select e.* FROM user e where e.regdate>=?1 and e.regdate<?2 and e.points BETWEEN ?3 AND ?4";
        List<User> users = findUseSql(str,User.class,beginTime,endTime,lAndh[0],lAndh[1]);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }

    @Override
    public int deleteUser(User user) {
        try{
            delete(user);
        }catch(Exception e){
            e.printStackTrace();
            return -2;
        }

        return 0;
    }

    private int[] getLowerAndHigherByLevel(LevelType levelType){
        String sql = "{call getLowerAndHigherBoundByLevel(?,?,?)}";
        Integer a = -1 ,b =-1;
        Connection conn=null;
        try {
            conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
            CallableStatement cs =conn.prepareCall(sql);

            switch (levelType) {
                case 普通会员:
                    cs.setObject(1,1);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    a=cs.getInt(2);
                    b=cs.getInt(3);
                    break;
                case 银卡:
                    cs.setObject(1,2);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    a=cs.getInt(2);
                    b=cs.getInt(3);
                    break;
                case 金卡:
                    cs.setObject(1,3);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    a=cs.getInt(2);
                    b=cs.getInt(3);
                    break;
                case 白金卡:
                    cs.setObject(1,4);
                    cs.registerOutParameter(2, Types.INTEGER);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    a=cs.getInt(2);
                    b=cs.getInt(3);
                    break;
                default:
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(conn!=null){
                try {
                    conn.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        int[] arr = new int[2];
        arr[0]=a;
        if(b==null)
        arr[1]=100000000;
        else
        arr[1]=b;
        return arr;
    }

    @Override
    public LevelType getLevelTypeByPoint(int point){
        int[] a1 = getLowerAndHigherByLevel(普通会员);
        int[] a2 = getLowerAndHigherByLevel(银卡);
        int[] a3 = getLowerAndHigherByLevel(金卡);
        int[] a4 = getLowerAndHigherByLevel(白金卡);
        if(point>=a1[0]&&point<a1[1]){
            return 普通会员;
        }
        else if(point>=a2[0]&&point<a2[1]){
            return 银卡;
        }
        else if(point>=a3[0]&&point<a3[1]){
            return 金卡;
        }
        else{
            return 白金卡;
        }
    }

    @Override
    public List<User> findAll(Class<User> entityClazz) {
        List<User> users = super.findAll(entityClazz);
        for(User user : users){
            user.setLevel(getLevelTypeByPoint(user.getPoints()));
        }
        return users;
    }
}
