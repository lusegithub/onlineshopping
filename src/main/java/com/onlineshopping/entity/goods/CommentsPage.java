package com.onlineshopping.entity.goods;

import com.onlineshopping.entity.Comment;

import java.util.List;

/**
 * Created by W on 2017/6/21.
 */
public class CommentsPage {


    private int currentPageNum;
    private int pagedCommentsNum;
    private int totalPagesNum;
    private int totalCommentsNum;
    private List<Comment> comments;

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getPagedCommentsNum() {
        return pagedCommentsNum;
    }

    public void setPagedCommentsNum(int pagedCommentsNum) {
        this.pagedCommentsNum = pagedCommentsNum;
    }

    public int getTotalPagesNum() {
        return totalPagesNum;
    }

    public void setTotalPagesNum(int totalPagesNum) {
        this.totalPagesNum = totalPagesNum;
    }

    public int getTotalCommentsNum() {
        return totalCommentsNum;
    }

    public void setTotalCommentsNum(int totalCommentsNum) {
        this.totalCommentsNum = totalCommentsNum;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("currentPageNum=").append(currentPageNum).append(", pagedCommentsNum=")
                .append(pagedCommentsNum)
                .append(", totalPagesNum=").append(totalPagesNum).append(", totalCommentsNum=")
                .append(totalCommentsNum).append(", commentsId=[");
        if (getComments() != null && !getComments().isEmpty()) {
            for (int i = 0; i < comments.size() - 1; i++) {
                sb.append(comments.get(i).getCommentId()).append(", ");
            }
            sb.append(comments.get(comments.size()-1).getCommentId());
        }
        sb.append("]");
        return sb.toString();
    }
}
