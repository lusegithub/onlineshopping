/**
 * Created by lsy
 */

$(function () {
    var table = $("#levelTable").find("tbody").eq(1);
    var level1 = $("#level1");
    var level2 = $("#level2");
    getLevel();

    //获取会员等级并显示
    function getLevel() {
        level1.empty();
        level2.empty();
        level2.append("<option value=''></option>");
        table.empty();
        $.get(webRoot + "/vip-level", function (response) {
            if (response.resultCode === 0) {
                $.each(response.content, function (index, item, arr) {
                    level1.append("<option value='" + item.level + "'>" + item.level + "</option>");
                    level2.append("<option value='" + item.level + "'>" + item.level + "</option>");
                    if (item.note == null) {
                        item.note = "";
                    }
                    table.append($("<tr></tr>")
                        .append($("<td></td>").append(item.level))
                        .append($("<td></td>").append(item.lowerLimitCredit))
                        .append($("<td></td>").append(percent(item.rateCredit)))
                        .append($("<td></td>").append(percent(item.discount)))
                        .append($("<td></td>").append(item.note))
                    );
                });
            } else {
                alert(response.resultInfo);
                history.back();
            }
        }).error(function () {
            alert("请求失败！");
            // history.back();
        });
    }

    var lowerLimitCredit = $("#lowerLimitCredit");
    var rateCredit = $("#rateCredit");
    var discount = $("#discount");

    //点击修改
    $("#edit").click(function () {

        if (!isInteger(lowerLimitCredit.val())) {
            alert("积分下限必须为非0整数！");
            lowerLimitCredit.focus();
            return;
        }
        if (!isInteger(rateCredit.val())) {
            alert("积分比例必须填入大不小于100的整数！");
            rateCredit.focus();
            return;
        }
        if (!isInteger(discount.val())) {
            alert("折扣必须填入1-100内的整数！");
            discount.focus();
            return;
        }

        $.post(
            webRoot + "/vip-level/" + level1.val(),
            {
                lower_limit_credit: lowerLimitCredit.val(),
                rate_credit: rateCredit.val() / 100,
                discount: discount.val() / 100
            },
            function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0) {
                    getLevel();
                    resetLevel();
                }
            }
        ).error(function () {
            alert("请求失败！");
        })
    });

    function isInteger(obj) {
        return parseInt(obj, 10) == obj
    }

    //点击重置
    $("#resetLevel").click(function () {
        resetLevel();
    });
    function resetLevel() {
        level1.val("");
        lowerLimitCredit.val("");
        rateCredit.val("");
        discount.val("");
    }

    var userId = $("#userId");
    var beginTime = $("#beginTime");
    var endTime = $("#endTime");

    //点击查询
    $("#find").click(function () {
        var data = "?";
        data += "user_id=" + userId.val();
        data += "&level=" + level2.val();
        data += "&begin=" + beginTime.val();
        data += "&end=" + endTime.val();
        location.href = "user_list.html" + data;
    });

    $("#resetFind").click(function () {
        userId.val("");
        level2.val("");
        beginTime.val("");
        endTime.val("");
    });

    function percent(num) {
        return parseInt(num * 100) + "%";
    }
});