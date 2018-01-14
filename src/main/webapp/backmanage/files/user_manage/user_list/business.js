/**
 * Created by lsy
 */
$(function () {
    var items_per_page = 6;
    var oldPage = 0;
    var userList = $("#userList");

    var user_id = getUrlParam("user_id");
    var level = getUrlParam("level");
    var begin = getUrlParam("begin");
    var end = getUrlParam("end");

    findUser();

    function findUser() {
        $.ajax({
            url: webRoot + "/user",
            type: "GET",
            data: {
                user_id: user_id,
                level: level,
                begin_time: begin,
                end_time: end
            }
        }).done(function (response) {
            if (response.resultCode === 0) {
                //填充内容
                userList.empty();
                $.each(response.content, function (index, user, arr) {
                    userList.append(
                        $("<tr style='display: none'></tr>")
                            .append("<td>" + user.userId + "</td>")
                            .append("<td>" + user.nickname + "</td>")
                            .append("<td>" + user.level + "</td>")
                            .append("<td>" + user.points + "</td>")
                            .append("<td>" + user.regdate + "</td>")
                            .append(
                                $("<td></td>").append(
                                    $("<input type='button' value='删除'>").click(deleteUser)
                                )
                            )
                    );
                });

                //初始化翻页控件
                $("#Pagination").pagination(response.content.length, {
                    num_edge_entries: 2,
                    num_display_entries: 4, //主体页数
                    prev_text: "上一页",
                    next_text: "下一页",
                    items_per_page: items_per_page,
                    callback: function (page_index) {
                        for (var i = items_per_page * oldPage; i < items_per_page * (oldPage + 1); i++) {
                            userList.children().eq(i).css("display", "none");
                        }
                        for (var i = items_per_page * page_index; i < items_per_page * (page_index + 1); i++) {
                            userList.children().eq(i).css("display", "table-row");
                        }
                        oldPage = page_index;
                    }
                });
            } else {
                alert(response.resultInfo);
                history.back();
            }
        }).error(function () {
            alert("查询失败！");
            history.back();
        });
    }

    function deleteUser() {
        var userId = $(this).parent().siblings().eq(0).html();
        if (window.confirm("确认删除用户" + userId + "吗？")) {
            //将“.”替换成“。”
            userId = userId.replace(/\./g, "。");
            $.ajax({
                url: webRoot + "/user/" + userId,
                type: "DELETE"
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0) {
                    findUser();
                }
            }).error(function () {
                alert("删除用户失败！");
            })
        }
    }
});