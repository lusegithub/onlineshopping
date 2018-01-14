var webRoot='/onlineshopping';
var init = true;
var page = 1;
var pagination = false;
var goodsId=location.search.substr(6);
$(function () {
    var comments = $("#comments");
    getData();
    function getData() {
        $.get(webRoot+"/goods/"+goodsId+"/comment/page-"+page, function (response) {
            if(response.resultCode == 0) {
                //初始化翻页插件
                    if(init) {
                    $("#pagination").pagination(response.content.totalCommentsNum, {
                        current_page: page - 1,
                        num_edge_entries: 2,
                        num_display_entries: 4, //主体页数
                        prev_text: "上一页",
                        next_text: "下一页",
                        items_per_page: 10,
                        callback: function (page_index) {
                            if(!init) {
                                page = page_index + 1;
                                getData();
                                pagination = true;
                            }
                        }
                    });
                    init = false;
                }
                //先添加table，保证顺序
                comments.empty();
                $.each(response.content.comments, function (index) {
                    comments.append($("<table></table>").attr("id", index));
                });
                $.each(response.content.comments, function (index, comment) {
                    $("#" + index)
                        .append($("<tr></tr>")
                            .append($("<td width='150'>用户：</td>"))
                            .append($("<td width='150'></td>").append(comment.nickname))
                            .append($("<td width='150'>评论时间：</td>"))
                            .append($("<td width='150'></td>").append(comment.time))
                            .append($("<td width='100'>打分：</td>"))
                            .append($("<td width='100'></td>").append(comment.score))
                        )
                        .append($("<tr></tr>")
                            .append($("<td></td>").append("评论内容："))
                            .append($("<td colspan='5'></td>").append(comment.content).attr("class", "content"))
                        )
                })
            }else {
                alert(response.resultInfo);
            }
        }).error(function () {
            alert("请求失败！");
        })
    }

    $("#back").click(function () {
        if(pagination) {
            history.back();
        }
        history.back();
    });
});
