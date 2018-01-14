document.write("<script src='files/global.js'></script>");
function getParam(paramName) {
    paramValue = "", isFound = !1;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}
var nickname=sessionStorage.getItem("nickname");

var discount ;
$(document).ready(function () {
    // var nickname;
    // nickname = sessionStorage.getItem("nickname");
    $('#u1797 p span').empty();
    $('#u1797 p span').html(sessionStorage.getItem("nickname"));

    var orderId = location.search.substr(10);
    $.ajax({
        type: 'GET',
        url: host + '/user/discount',
        dataType: 'json',
        success:function (results) {
            discount=results.content
            $.ajax({
                type: 'GET',
                url: host + '/user/order/' + orderId,
                dataType: 'json',
                success: function (results) {
                    var result_code = results.resultCode;
                    var result_info = results.resultInfo;
                    if (result_code === 0) {
                        var content = results.content;
                        var orderTime = content.orderTime;
                        var price = content.price;
                        var status = content.status;
                        var phone = content.phone;
                        var consignee = content.consignee;
                        var address = content.address;
                        var discount2;

                        $('#orderId').text(orderId);
                        $('#orderTime').text(orderTime);
                        $('#discount').text(discount);
                        $('#price').text(price);
                        $('#pay').text('货到付款');
                        $('#status').text(status);
                        $('#name').text(consignee);
                        $('#address').text(address);
                        $('#phone').text(phone);


                        if (status === "审核通过") {
                            $("#button").append('<button id="confirm">确认收货</button>')

                            $("#confirm").click(function(){
                                $.ajax({
                                    type: 'PUT',
                                    url: host + "/user/orders/" + orderId + "/receipt",
                                    dataType: 'json',
                                    success: function(results){
                                        var result_code = results.resultCode;
                                        var result_info = results.resultInfo;
                                        if (result_code === 0) {
                                            $('#status').text("已收货");
                                            $("#button").remove();
                                        }else{
                                            alert('Error');
                                        }
                                    }
                                })
                            })
                        }else if (status === "待审核"){
                            $("#button").append('<button id="cancel">取消订单</button>')

                            $("#cancel").click(function(){
                                $.ajax({
                                    type: 'PUT',
                                    url: host + "/user/orders/" + orderId,
                                    dataType: 'json',
                                    success: function(results){
                                        var result_code = results.resultCode;
                                        var result_info = results.resultInfo;
                                        if (result_code === 0) {
                                            $('#status').text("已取消");
                                            $("#button").remove();
                                        }else{
                                            alert('Error');
                                        }
                                    }
                                })
                            })
                        }


                        var count;
                        var goods_id;
                        var goods;
                        var price;
                        var name;
                        var dis_price;
                        var discount2;
                        $.each(content.orderDetailList, function (i, item) {
                            name = item.goodsName;
                            price = item.goodsPrice;
                            goods = item.goods;
                            goods_id = goods.goodsId;
                            count = item.goodsNumber;


                            id = goods_id;
                            dis_price = price * discount;
                            discount2 = discount * 10 + "折";

                            $("tbody").eq(1).append('<tr><td align="center"><span>'
                                + id + '</span></td><td align="center">'+name+'</td><td align="center">' + price + '</td><td align="center">'
                                + discount2 + '</td><td align="center">' + dis_price + '</td><td align="center">' + count + '</td></tr>');
                        })


                    }
                }
            })


        }
    })

    $("div#u1803").click(function () {
        $.ajax({
            type: 'PUT',
            url: host + '/user',
            dataType: 'json',
            success: function (results) {
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                console.log(result_code);
                if (result_code === 0) {
                    sessionStorage.setItem('status', -1);
                    location.href = host + "/index.html";
                } else {
                    alert('Error');
                }
            }
        })
    })

    $("div#u1799").click(function () {
        $(function () {
            location.href = host + "/user_edit.html?" +
                "nickname=" + nickname;
        })
    })

    $("div#u1801").click(function () {
        $(function () {
            location.href = host + "/address_daohang.html?" +
                "nickname=" + nickname;
        })
    })


    $('div#u1710').click(function () {
        $(function () {
            location.href = host + '/credit_query.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u1714').click(function () {
        $(function () {
            location.href = host + '/favorite.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u1716').click(function () {
        $(function () {
            location.href = host + '/cart_list.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u1718').click(function () {
        $(function () {
            location.href = host + '/tradequery.html?' +
                "nickname=" + nickname;
        })
    })

})