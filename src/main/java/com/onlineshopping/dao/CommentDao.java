package com.onlineshopping.dao;

import com.onlineshopping.entity.Comment;
import com.onlineshopping.entity.Goods;

import java.util.List;

/**
 * Created by hehe on 17-6-9.
 */
public interface CommentDao extends BaseDao<Comment>{


    /**
     *@Param:  The goods object
     *@return: The Comment objects you get by the goods object.In addition, you need to determine whether the result is null.
     */
    List<Comment> getCommentByGoods(Goods goods);

    int getCommentNumByGoods(Goods goods);

    /**
     * 根据 goods 查询得到结果集中取
     * 第 startIndex 条到第 startIndex + pageSize 条结果作为最终结果集返回，
     * 如果从 startIndex 开始，剩下的记录数不够 pageSize 条，则只返回剩下的记录。
     * @param goods 返回的 goods 对象代表的商品的评论
     * @param startIndex 第一条数据记录下标视为 1
     * @param pageSize 至多返回 pageSize 条结果
     * @return 返回商品评论集
     */
    List<Comment> getPagedCommentByGoods(Goods goods, int startIndex, int pageSize);


    /**
     *@Param: The goods object
     *@return:
     */
    void  deleteCommentsByGoods(Goods goods);

    /**
     * 根据id删除评论
     * @param comment 评论
     */
    void deleteComment(Comment comment);
}
