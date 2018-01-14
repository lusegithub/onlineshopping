package com.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onlineshopping.util.DateSerializerUtil1;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hehe on 17-6-9.
 */
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @Column(name="comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    @ManyToOne(targetEntity = Goods.class)
    @JoinColumn(name="goods_id",referencedColumnName = "goods_id",nullable = false)
    private Goods goods;
    @Column(name="nickname",nullable = false,length = 30)
    private String nickname;
    @Column(name="time",nullable = false,columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;//yyyy-mm-dd hh:mm:ss
    @Column(name="score",columnDefinition = "int default 5",nullable = false)
    private int score=5;
    @Column(name="content")
    private String content;//enable null

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer id) {
        this.commentId = id;
    }

    @JsonIgnore
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JsonSerialize(using = DateSerializerUtil1.class)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", goods=" + goods +
                ", nickname='" + nickname + '\'' +
                ", time=" + time +
                ", score=" + score +
                ", content='" + content + '\'' +
                '}';
    }
}
