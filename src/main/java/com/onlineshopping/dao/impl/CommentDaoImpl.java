package com.onlineshopping.dao.impl;

import com.onlineshopping.dao.CommentDao;
import com.onlineshopping.entity.Comment;
import com.onlineshopping.entity.Goods;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by hehe on 17-6-16.
 */
@Repository("CommentDao")
public class CommentDaoImpl extends BaseDaoHibernate<Comment> implements CommentDao{
    @Override
    public List<Comment> getCommentByGoods(Goods goods) {
        String str = "select e.* from comment e where e.goods_id=?1";
        List<Comment> commentList = findUseSql(str,Comment.class,goods.getGoodsId());
        return commentList;
    }

    @Override
    public List<Comment> getPagedCommentByGoods(Goods goods, int startIndex, int pageSize) {
        String str = "SELECT e.* FROM comment e WHERE e.goods_id='"+goods.getGoodsId()+"'";

        return findByStartIndexAndPage(str,Comment.class,startIndex,pageSize);
    }

    @Override
    public void deleteCommentsByGoods(Goods goods) {
         String str= "delete from comment e where e.goods_id='"+goods.getGoodsId()+"'";
        getSessionFactory().getCurrentSession().createSQLQuery(str).executeUpdate();
    }

    @Override
    public int getCommentNumByGoods(Goods goods) {
        String str="select count(*) FROM comment e WHERE e.goods_id='"+goods.getGoodsId()+"'";
        List l=getSessionFactory().getCurrentSession().createSQLQuery(str).list();
        return ((BigInteger)l.get(0)).intValue();
    }

    @Override
    public void deleteComment(Comment comment) {
        delete(comment);
    }
}
