/**
 * Created by jacky on 17-6-30.
 */
document.write("<script src='../files/all/global.js'></script>");
$(function () {
    var url = location.search;
    var str = url.substr(1);
    var arr = str.split('&');
    var num = arr[0].indexOf('=');
    var orderid = arr[0].substr(num + 1);

    var pass = $("#pass");
    var cancel = $("#cancel");
    $.ajax({
        url: server_host + "/orders/" + orderid,
        type: 'GET',
        success: function (data) {
            var content = data.content;
            $("#orderid").after($("<th class='secondth'></th>").html(content.orderId));
            $("#userid").after($("<th class='secondth'></th>").html(content.user.userId));
            $("#ordertime").after($("<th class='secondth'></th>").html(content.orderTime));
            $("#price").after($("<th class='secondth'></th>").html("￥" + content.price));
            $("#consignee").after($("<th class='secondth'></th>").html(content.consignee));
            $("#address").after($("<th class='secondth'></th>").html(content.address));
            if (content.status === '待审核') {
                $("#orderstatus").after($("<th class='secondth bigRed'></th>").html(content.status));
                pass.css("display", "inline");
                cancel.css("display", "inline");
            } else if (content.status === '审核通过') {
                $("#orderstatus").after($("<th class='secondth bigGreen'></th>").html(content.status));
            } else if (content.status === '已取消') {
                $("#orderstatus").after($("<th class='secondth bigHui'></th>").html(content.status));
            } else if (content.status === "已收货") {
                $("#orderstatus").after($("<th class='secondth bigBlue'></th>").html(content.status));
            }
            $("#phone").after($("<th class='secondth'></th>").html(content.phone));

            $.each(data.content.orderDetailList, function (index, val) {
                var $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + val.goodsName + "</td>");
                $trTemp.append("<td>" + val.goodsPrice + "</td>");
                $trTemp.append("<td>" + val.goodsNumber + "</td>");
                $trTemp.appendTo("#goods");
            });
        }
    });
    pass.click(function () {
        $.ajax({
            url: server_host + "/orders/" + orderid,
            type: 'post',
            data: {
                status: "审核通过"
            },
            success: function (data) {
                location.reload();
            }
        })
    });
    cancel.click(function () {
        $.ajax({
            url: server_host + "/orders/" + orderid,
            type: "post",
            data: {
                status: "已取消"
            },
            success: function (data) {
                location.reload();
            }
        })
    });
});