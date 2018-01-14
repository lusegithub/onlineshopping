document.write("<script src='files/global.js'></script>");
function getParam(paramName) { 
    paramValue = "", isFound = !1; 
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) { 
        arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0; 
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++ 
    } 
    return paramValue == "" && (paramValue = null), paramValue 
}
$(document).ready(function(){
    var nickname;
    nickname = getParam('nickname');
    nickname = $('div#u1975 p span').text(nickname + ' |');
    $('div#u1975 p').attr('align', 'Right');
    var order=JSON.parse(sessionStorage.getItem("orderData"));
    var beginTime=order.b;
    var endTime=order.e;
    var orderStatus=order.s;
    if(orderStatus!="所有状态") {
        $.ajax({
            type: 'GET',
            url: host + '/user/orders',
            dataType: 'json',
            data: {
                begin_time: beginTime, end_time: endTime,
                status: orderStatus
            },
            success: function (results) {
                var result_code = results.resultCode;
                console.log(result_code);
                if (result_code === 0) {
                    console.log(results.content);
                    $.each(results.content, function (i, item) {
                        var order_Id = item.orderId;
                        var consignee = item.consignee;
                        var order_time = item.orderTime;
                        var status = item.status;
                        var url_order = "javascript:location.href='order_info.html?order_id=" + item.orderId + "'";

                        $('tbody').append('<tr><td></td><td></td><td></td>\
        <td></td><td><input type="button", value="详情"\
        onclick=' + url_order + '></td></tr>');
                        var tr = $('tbody tr').eq(i);
                        var children = tr.children();
                        $(children[0]).text(order_Id);
                        $(children[1]).text(consignee);
                        $(children[2]).text(order_time);
                        $(children[3]).text(status);
                    })
                } else if (result_code === -1) {
                    alert("时间参数不合法");
                    history.go(-1);
                } else if (result_code === -2) {
                    alert("状态参数不合法");
                    history.go(-1);
                } else if (result_code === -3) {
                    alert("开始时间不能大于结束时间");
                    history.go(-1);
                } else if (result_code === -4) {
                    alert("订单数量为零");
                    history.go(-1);
                }
            }
        });
    }else {
        $.ajax({
            type: 'GET',
            url: host + '/user/orders',
            dataType: 'json',
            data: {
                begin_time: beginTime, end_time: endTime
            },
            success: function (results) {
                var result_code = results.resultCode;
                console.log(result_code);
                if (result_code === 0) {
                    console.log(results.content);
                    $.each(results.content, function (i, item) {
                        var order_Id = item.orderId;
                        var consignee = item.consignee;
                        var order_time = item.orderTime;
                        var status = item.status;
                        var url_order = "javascript:location.href='order_info.html?order_Id=" + order_Id + "'";

                        $('tbody').append('<tr><td></td><td></td><td></td>\
        <td></td><td><input type="button", value="详情"\
        onclick=' + url_order + '></td></tr>');
                        var tr = $('tbody tr').eq(i);
                        var children = tr.children();
                        $(children[0]).text(order_Id);
                        $(children[1]).text(consignee);
                        $(children[2]).text(order_time);
                        $(children[3]).text(status);
                    })
                } else if (result_code === -1) {
                    alert("时间参数不合法");
                    history.go(-1);
                } else if (result_code === -2) {
                    alert("状态参数不合法");
                    history.go(-1);
                } else if (result_code === -3) {
                    alert("开始时间不能大于结束时间");
                    history.go(-1);
                } else if (result_code === -4) {
                    alert("订单数量为零");
                    history.go(-1);
                }
            }
        });
    }
    // var orders = JSON.parse(localStorage.getItem("orders"));
    //
    // if(orders.length === 0){
    //     alert("没有输出结果");
    // }
    $('#favorite').css('width', '1000px');


    $("div#u1981").click(function(){
        $.ajax({
            type: 'PUT',
            url: host + '/user',
            dataType: 'json',
            success: function(results){
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                console.log(result_code);
                if(result_code === 0){
                    sessionStorage.setItem('status', -1);
                    location.href = host + "/index.html";
                }else{
                    alert('Error');
                }
            }
        })
        });

    $("div#u1977").click(function(){
        $(function(){
                    location.href = host + "/user_edit.html?" +
                    "nickname=" + nickname;
                })
    })

    $("div#u1979").click(function(){
        $(function(){
                    location.href = host + "/address_daohang.html?" +
                    "nickname=" + nickname;
                })
    })

    $('div#u1893').click(function(){
        $(function(){
            location.href = host + '/credit_query.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1897').click(function(){
        $(function(){
            location.href = host + '/favorite.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1899').click(function(){
        $(function(){
            location.href = host + '/cart_list.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1901').click(function(){
        $(function(){
            location.href = host + '/tradequery.html?' +
            "nickname=" + nickname;
        })
    })

})