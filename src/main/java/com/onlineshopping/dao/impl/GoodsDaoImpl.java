package com.onlineshopping.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.onlineshopping.dao.GoodsDao;
import com.onlineshopping.dao.Watcher;
import com.onlineshopping.entity.*;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hehe on 17-6-15.
 */
@Repository("GoodsDao")
public class GoodsDaoImpl extends BaseDaoHibernate<Goods> implements GoodsDao,Watcher {


    public List<Goods> getGoodsByShoppingCart(ShoppingCart shoppingCart){

        return  null;
    }


    public List<Goods> getGoodsByFavorite(Favorite favorite){
        return null;
    }




    public List<Goods> getGoodsByOrder(Order order){
        String str = "select g.* from goods g,order_detail od,orders o where g.goods_id=od.goods_id and od.order_id=o.order_id and o.order_id="+order.getOrderId();
        List list = getSessionFactory().getCurrentSession().createSQLQuery(str).addEntity("g",Goods.class).list();
        if(list.size()!=0){
            List<Goods> goodsList = new ArrayList<>();
            for(Object tmp:list){
                goodsList.add((Goods)tmp);
            }
            return goodsList;
        }
        return null;
    }


    public Goods getGoodsByOrderDetail(OrderDetail orderDetail){
        String str = "select g.* from goods g where g.goods_id="+orderDetail.getGoods().getGoodsId();
        List list = getSessionFactory().getCurrentSession().createSQLQuery(str).addEntity("g",Goods.class).list();
        if(list!=null){
            return (Goods)list.get(0);
        }
        return null;
    }

    /**
     *
     * @param catalog
     * @return 返回类型为 catalog 的商品的数量
     */
    public int getGoodsNumByCatalog(Catalog catalog){
        String str = "select count(*) from goods g where g.catalog_name='"+catalog.getName()+"'"+" And g.sign=1";
        List list = getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        if(list.size()!=0){
            return ((BigInteger) list.get(0)).intValue();
        }
        return 0;
    }

//    /**
//     * 根据 keyword 和页码返回商品，根据商品的名称应包含 keyword 查询数据库表，
//     * 返回的结果集按商品名称的长度从短到长排列，最终返回结果集中
//     * 第 startIndex 条到第 startIndex + pageSize 条结果。
//     * @param keyword 返回的商品的名称应包含该 keyword
//     * @param startIndex 第一条数据记录下标视为 1
//     * @param pageSize 一共返回 pageSize 条结果
//     * @return 返回商品id集
//     */
//    List<Integer> getPagedGoodsIdsByKeyword(String keyword, int startIndex, int pageSize);


    /**
     * 根据 keyword 查询商品，将商品名称含有 keyword 的商品id加入结果集，
     * 返回的结果集按商品名称的长度从短到长排列。
     * @param keyword
     * @return 返回商品id集,按商品名称的长度从短到长排列
     */
    public List<String> getGoodIdsByKeyword(String keyword){
        String str="select g.goods_id from goods g where g.sign=1 And g.name like '%"+keyword+"%' order by LENGTH(g.name)";
        List list=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
//        if(list.size()!=0){
//            List<String> integerList = new ArrayList<>();
//            for(Object tmp:list){
//               integerList.add((String)tmp);
//            }
//            return integerList;
//        }
//        return  null;
        List<String> integerList = new ArrayList<>();
        for(Object tmp:list){
            integerList.add((String)tmp);
        }
        return integerList;
    }



    /**
     * 根据 keyword 查询商品，只要商品名称含有 keyword 中至少一个字符都加入结果集。
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByKeyword(String keyword){
        String str="select g.* from goods g where g.name REGEXP " + getRegexp(keyword)+" And g.sign=1"+" order by LENGTH (g.name)";
        List list=getSessionFactory().getCurrentSession().createSQLQuery(str).addEntity("g",Goods.class).list();
//        if(list.size()!=0){
//            List<Goods> goodsList = new ArrayList<>();
//            for(Object tmp:list){
//                goodsList.add((Goods)tmp);
//            }
//            return  goodsList;
//        }
//        return null;
        List<Goods> goodsList = new ArrayList<>();
        for(Object tmp:list){
            goodsList.add((Goods)tmp);
        }
        return goodsList;
    }

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的，且名称含有 keyword 中至少一个字符的商品
     * @param minPrice
     * @param maxPrice
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByPriceIntervalAndKeyword(float minPrice, float maxPrice, String keyword){
        String str ="select e.* from goods  e where e.name regexp "+getRegexp(keyword)
                +" and price>=?1 and price <=?2"+" And e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,minPrice,maxPrice);
        return goodsList;
    }

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的商品
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByPriceInterval(float minPrice, float maxPrice){
        String str ="select e.* from goods  e where price>=?1 and price <=?2"+" And e.sign=1"+" And e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,minPrice,maxPrice);
        return goodsList;
    }

    /**
     * 返回价格在闭区间[minPrice, maxPrice]的商品的数量
     * @param minPrice
     * @param maxPrice
     * @return
     */
    public int getGoodsNumByPriceInterval(float minPrice, float maxPrice){
        String str ="select count(*) from goods  e where e. price>="+minPrice+" and price <"+maxPrice+" And e.sign=1";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        return ((BigInteger)l.get(0)).intValue();
    }

    /**
     * 查询价格在区间[minPrice, maxPrice]的商品的id，
     * 在查询结果中返回第 startIndex 条到 startIndex + pageSize 条结果，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param minPrice
     * @param maxPrice
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 至多返回 pageSize 条结果
     * @return 返回商品的id，无顺序要求
     */
    public List<String> getPagedGoodsByPriceInterval(float minPrice, float maxPrice, int startIndex, int pageSize){
        if(startIndex<=0)startIndex=1;
        String str ="select e.goods_id from goods  e where e. price>="+minPrice+" and price <"+maxPrice+" And e.sign=1";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).setFirstResult(startIndex-1).setMaxResults(pageSize).list();
        if(l.size()==0)return null;
        return (List<String>)l;
    }

    /**
     * 根据 catalogName查询得到结果集中取
     * 第 startIndex 条到第 startIndex + pageSize 条结果作为最终结果集返回，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param catalogName 返回的商品的名称应包含该 catalogName
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 一共返回 pageSize 条结果
     * @return 返回商品id集，无顺序要求
     */
    public List<String> getPagedGoodsIdsByCatalogName(String catalogName, int startIndex, int pageSize){
        String str = "select g.goods_id from goods g where g.catalog_name='"+catalogName+"'"+" and g.sign=1"+" order by LENGTH(g.name)";
        List list = getSessionFactory().getCurrentSession().createSQLQuery(str).setFirstResult(startIndex-1).setMaxResults(pageSize).list();
        return (List<String>)list;
    }


//    /**
//     * 返回名称包含 keyword 的商品的数量
//     * @param keyword
//     * @return
//     */
//    int getGoodsNumByKeyword(String keyword);

    /**
     * 返回类型为 catalogName的，且名称含有 keyword 中至少一个字符的商品
     * @param catalogName
     * @param keyword
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByCatalogNameAndKeyword(String catalogName, String keyword){
        String str = "SELECT e.* from goods e where e.name regexp "+getRegexp(keyword)+" and e.catalog_name=?1"+" And e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,catalogName);
        return goodsList;
    }

    /**
     * 返回类型为 catalogName的商品
     * @param catalogName
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByCatalogName(String catalogName){
        String str = "select e.* from goods e where e.catalog_name=?1"+" and e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,catalogName);
        return goodsList;
    }

    /**
     * 根据 catalogName 和价格闭区间[minPrice, maxPrice]查询得到结果集中取
     * 第 startIndex 条到第 startIndex + pageSize 条结果作为最终结果集返回，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param catalogName 返回的商品的名称应包含该 catalogName
     * @param minPrice
     * @param maxPrice
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 一共返回 pageSize 条结果
     * @return 返回商品id集，无顺序要求
     */
    public List<String> getPagedGoodsIdsByCatalogNameAndPriceInterval(String catalogName, float minPrice, float maxPrice, int startIndex, int pageSize){
        String str = "select e.goods_id from goods e where e.catalog_name=?1 and e.price>=?2 and e.price<=?3"+" order by LENGTH(e.name)";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).setParameter(1+"",catalogName).setParameter("2",minPrice).setParameter("3",maxPrice).setFirstResult(startIndex-1).setMaxResults(pageSize).list();
        return (List<String>)l;
    }

    /**
     * 返回种类为catalogName, 价格在闭区间[minPrice, maxPrice]的商品的数量
     * @param catalogName
     * @param minPrice
     * @param maxPrice
     * @return
     */
   public  int getGoodsNumByCatalogNameAndPriceInterval(String catalogName, float minPrice, float maxPrice){
       String str = "select count(*) from goods e where e.catalog_name=?1 and e.price>=?2 and e.price<=?3"+" And e.sign=1";
       List l=getSessionFactory().getCurrentSession().createSQLQuery(str).setParameter(1+"",catalogName).setParameter("2",minPrice).setParameter("3",maxPrice).list();
       return ((BigInteger)l.get(0)).intValue();
   }

    /**
     * 返回类型为 catalogName 的，价格在闭区间[minPrice, maxPrice]的，
     * 且名称含有 keyword 中至少一个字符的商品
     * @param catalogName
     * @param keyword
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByCatalogNameAndKeywordAndPriceInterval(String catalogName, String keyword, float minPrice, float maxPrice){
        String str = "select e.* from goods e where e.catalog_name=?1 and e.name regexp "+getRegexp(keyword)
                +" AND e.price >=?2 AND e.price<=?3"+" And e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,catalogName,minPrice,maxPrice);
        return goodsList;
    }

    /**
     * 返回类型为 catalogName 的，价格在闭区间[minPrice, maxPrice]的商品
     * @param catalogName
     * @param minPrice
     * @param maxPrice
     * @return 返回商品集，无顺序要求
     */
    public List<Goods> getGoodsByCatalogNameAndPriceInterval(String catalogName, float minPrice, float maxPrice){
        String str = "select e.* from goods e where e.catalog_name=?1 and e.price >=?2 AND e.price<=?3"+" And e.sign=1"+" order by LENGTH(e.name)";
        List<Goods> goodsList = findUseSql(str,Goods.class,catalogName,minPrice,maxPrice);
        return goodsList;
    }

    private String getRegexp(String keyword) {
        keyword.replaceAll("[\\pP‘’“”]", "");
        keyword.replaceAll("\\s", "");
        if (keyword.length() > 3) {
            keyword = keyword.substring(0, 3);
        }
        if (keyword == null || keyword.equals("")) {
            return keyword;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        for (int i = 0; i < keyword.length() - 1; i++) {
            sb.append(keyword.charAt(i)).append('|');
        }
        sb.append(keyword.charAt(keyword.length()-1));
        sb.append('\'');
        return sb.toString();
    }

    @Override
    public void updateFollowWatchedCatalogName(String catalog_name,int id) {
        String str = "update goods SET goods.catalog_name = '"+catalog_name+"' where catalog_id ="+id;
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();

    }

    @Override
    public void addFollowWatchedCatalogName(String catalog_name,String  goodsid) {
        String str = "update goods SET  goods.catalog_name = '"+catalog_name+"' where goods_id ='"+goodsid+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }

    @Override
    public Serializable save(Goods goods){
  //      getSessionFactory().getCurrentSession().getTransaction().begin();

            Catalog catalog = goods.getCatalog();
            //      Serializable id = super.save(goods);
            //addFollowWatchedCatalogName(catalog.getName(),(String)super.save(goods));
            //      getSessionFactory().getCurrentSession().getTransaction().commit();
            // String str= "INSERT into goods (goods_id, catalog_name, name, description, photo, price, stock, catalog_id) VALUE (?1,?2,?3,?4,?5,?6,?7,?8)";
            // String str= "INSERT into goods (goods_id, catalog_name, name, description, photo, price, stock, catalog_id) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)";
            // getSessionFactory().getCurrentSession().createSQLQuery(str).setParameter(1,goods.getGoodsId()).setParameter(2,catalog.getName()).setParameter(3,goods.getName()).setParameter(4,goods.getDescribe()).setParameter(5,goods.getPhoto()).setParameter(6,goods.getPrice()).setParameter(7,goods.getStock()).setParameter(8,catalog.getCatalogId()).executeUpdate();
            goods.setCatalogName(catalog.getName());
            if(super.get(Goods.class,goods.getGoodsId())!=null){

               super.delete(Goods.class,goods.getGoodsId());

           }
            return super.save(goods);


    }
    @Override
    public void delete(Goods entity){
        String str = "update goods SET goods.sign = "+0+" where goods_id ='"+entity.getGoodsId()+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }
    @Override
    public void delete(Class<Goods> entityClazz, Serializable id){
        String str = "update goods SET goods.sign = "+0+" where goods_id ='"+id+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }
    @Override
    public List<Goods> findAll(Class<Goods> entityClazz){
        return  find("select en from goods en where goods.sign = 1");
    }
    @Override
    public long findCount(Class<Goods> entityClazz){
        List<?> l = find("select count(*) from goods where goods.sign = 1");
        if(l!=null&&l.size()==1){
            return (Long)l.get(0);
        }
        return 0;
    }
}
