if (sessionStorage.getItem("status") != 0) {
    location.href = "index.html";
}

document.write("<script src='files/global.js'></script>");

var select='select';
var request = {};
var goods={};//为遍历而设置
function setItem(index, val) {
    request[index] = val;
}
function setItem2(index,val){
    goods[index]=val;
}

var discount;
var discountStr;
var cost = 0;
var ori_cost = 0;
var points_rate;

$(document).ready(function(){
    var nickname = sessionStorage.getItem("nickname");
    $('div#u1797 p span').text(nickname + ' |');
    $('div#u1797 p').attr('align', 'Right');

    $('div#u1691 p span').text(nickname + ' |');
    $('div#u1691 p').attr('align', 'Right');

    var goodsId;
    var name;
    var price;
    var id;

    $.ajax({
        type: 'GET',
        url: host + '/user/discount',
        dataType: 'json',
        success: function (results) {
            var result_code = results.resultCode;
            var result_info = results.resultInfo;
            if (result_code === 0) {
                discount = parseFloat(results.content);
                $.ajax({
                    type: 'GET',
                    url: host + '/user/shoppingcart',
                    dataType: 'json',
                    success: function (results) {
                        var result_code = results.resultCode;
                        var result_info = results.resultInfo;
                        if (result_code === 0) {
                            var content = results.content;

                            $.each(content, function (i, item) {
                                goodsId = item.goodsId;
                                setItem(goodsId, 1);
                                setItem2(goodsId,1);
                                // request[goodsId] = 1;
                                name = item.name;
                                price = parseFloat(item.price);
                                id = i + 1;
                                ori_cost += price;

                                discountStr = discount * 100 + "%";

                                $("tbody").eq(0)
                                $("tbody").eq(0).append($("<tr></tr>")
                                    .append('<td align="center"><span>'
                                        + id + '</span></td><td align="center"><a href="item_info.html?goodsId=' + goodsId + '&&resultCode=0'
                                        + '">' + name + '</a></td><td align="center">' + price + '</td>')
                                    .append($("<td></td>")
                                        .append($("<input type='button' value='-'>")
                                            .css("width", "20px")
                                            .click(sub)
                                        )
                                        .append($("<input type='number' value='1' >")
                                            .css("width", "35px")
                                            .css("text-align", "center")
                                            .attr("goodsid", item.goodsId)
                                            .attr("price", price)
                                            .attr("name",name)
                                            .attr("max", item.stock)
                                            .attr("prevValue",1)
                                            .keyup(just)
                                        )
                                        .append($("<input type='button' value='+'>")
                                            .css("width", "20px")
                                            .attr("max", item.stock)
                                            .click(add))
                                    )
                                    .append($('<td align="center" >'+parseInt(item.stock-1)
                                        +'</td>')
                                        .attr("stock",item.stock)
                                    )
                                );
                            });

                            $.ajax({
                                type: 'GET',
                                url: host + '/user/vip-level/point-rate',
                                dataType: 'json',
                                success: function (results) {
                                    var result_code = results.resultCode;
                                    var result_info = results.resultInfo;
                                    var total=ori_cost*discount;
                                    if (result_code === 0) {
                                        points_rate = results.content;
                                        $("tbody").eq(0)
                                            .append($("<tr></tr>")
                                                .append($("<td>总价</td>"))
                                                .append($("<td colspan='4'></td>")
                                                    .attr("id", "cost")
                                                    .append("商品价格￥")
                                                    .append(ori_cost)
                                                    .append(" * 折扣")
                                                    .append(discountStr)
                                                    .append(" + 送货费￥5.00 = ￥")
                                                    .append((total + 5).toFixed(1))
                                                )
                                            )
                                            .append($("<tr></tr>")
                                                .append($("<td>本单将产生积分：</td>"))
                                                .append($("<td id='points' colspan='4'></td>")
                                                    .append(parseInt(ori_cost * points_rate))
                                                )
                                            )
                                    }
                                }
                            })
                        } else {
                            alert(result_info);
                        }
                    }
                }).error(function () {
                    alert("请求失败！");
                });


                var aid = getParam('aid');
                if (aid === null) {
                    aid = 0;
                }else {
                    aid = parseInt(aid);
                }

                var addressId;
                $.ajax({
                    type: "GET",
                    url: host + '/user/addresses',
                    dataType: 'json',
                    success: function (results) {
                        var result_code = results.resultCode;
                        var result_info = results.resultInfo;
                        if (result_code === 0) {
                            var content = results.content;
                            $.each(content, function (i, item) {
                                if (i === aid) {
                                    var name = item.name;
                                    var address = item.address;
                                    var phone = item.phoneNumber;
                                    addressId = item.addressId;
                                    $('#itemsearchtd1').text(name);
                                    $('#itemsearchtd2').text(address);
                                    $('#itemsearchtd3').text(phone);

                                    setItem("addressId", item.addressId);
                                    // request.address = item.addressId;
                                }
                            })
                        } else {
                            // $('.itemsearchth').remove();
                        }
                    }
                });
            }
        }
    })


    $("div#u1697").click(function () {
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
    });

    $("div#u1693").click(function () {
        $(function () {
            location.href = host + "/user_edit.html";
        })
    });

    $("div#u1695").click(function () {
        $(function () {
            location.href = host + "/address_daohang.html";
        })
    });

    $("div#u1699").click(function () {
        $(function () {
            location.href = host + "/order_success.html";
        })
    });

    $('div#u1510').click(function () {
        $(function () {
            location.href = host + '/credit_query.html';
        })
    });

    $('div#u1514').click(function () {
        $(function () {
            location.href = host + '/favorite.html';
        })
    });

    $('div#u1516').click(function () {
        $(function () {
            location.href = host + '/cart_list.html';
        })
    });

    $('div#u1518').click(function () {
        $(function () {
            location.href = host + '/tradequery.html';
        })
    });


    $("#order_submit").click(function () {
        //通过遍历数量的和来确定是否全为零，则提示后返回
        var totalNum=0;
        $.each(goods,function(index,value){
            totalNum+=value;
        })
        if(totalNum==0){
            alert("商品数量不能全为零");
            return;
        }
        if(request.addressId == null) {
            alert("请选择地址！");
            return;
        }
        if(request.length < 2){
            alert("商品为空！");
            return;
        }
        // alert(JSON.stringify(request));
        $.ajax({
            type: "POST",
            url: host + "/user/orders",
            contentType: "application/json",
            data: JSON.stringify(request)
            // dataType: "json"
        }).done(function (response) {
            if (response.resultCode == 0) {
                alert("提交订单成功！");
                location.href = "index.html";
            }else{
                alert(response.resultInfo);
            }
        }).error(function () {
            alert("提交订单失败！");
        });


        // $.post(
        //     host+"/user/orders",
        //     JSON.stringify(request),
        //     function (response) {
        //         if (response.resultCode == 0) {
        //             alert("提交订单成功！");
        //             location.href = "index.html";
        //         }else{
        //             alert(response.resultInfo);
        //         }
        //     }
        // ).error(function () {
        //     alert("提交订单失败！");
        // })
    })
})

//判定输出框里的.input中type为number时max和min属性可以用
function just(){
    var val=parseInt($(this).val());
    var name=$(this).attr("name");
    var r=/^(0|[1-9]\d*)$/;

    //判定
    if(!r.test(val)){
        var stock=$(this).parent().next().attr("stock");
        $(this).parent().next().empty()
            .append(parseInt(stock));
        val=0;
        $(this).attr("value", 0);
        setItem($(this).attr("goodsid"), 0);
        setItem2($(this).attr("goodsid"), 0);
        var prevValue=$(this).attr("prevValue");
        ori_cost += $(this).attr("price")*(val-parseInt(prevValue));
        $("#cost").empty()
            .append("商品价格￥")
            .append((ori_cost).toFixed(1))
            .append(" * 折扣")
            .append(discountStr)
            .append(" + 送货费￥5.00 = ￥")
            .append((ori_cost * discount + 5).toFixed(1));

        $("#points").empty().append(parseInt(ori_cost * points_rate));
        $(this).attr("prevValue",0);

        alert("请输入非负整数");
    }else{
        if(val>$(this).attr("max")){
            $("#order_submit").attr("disabled",true);
            alert(name+'超出库存');
        }else{
            $(this).attr("value", val);
            setItem($(this).attr("goodsid"), val);
            setItem2($(this).attr("goodsid"), val);
            var prevValue=$(this).attr("prevValue");
            ori_cost += $(this).attr("price")*(val-parseInt(prevValue));
            $("#cost").empty()
                .append("商品价格￥")
                .append((ori_cost).toFixed(1))
                .append(" * 折扣")
                .append(discountStr)
                .append(" + 送货费￥5.00 = ￥")
                .append((ori_cost * discount + 5).toFixed(1));
            var stock=$(this).parent().next().attr("stock");
            $(this).parent().next().empty()
                .append(parseInt(stock)-val);

            $("#points").empty().append(parseInt(ori_cost * points_rate));
            $(this).attr("prevValue",val);

            $("#order_submit").attr("disabled",false);
        }
    }
}
function add() {
    var num = $(this).prev();
    var value = parseInt(num.val());
    if (value == $(this).attr("max")) {
        alert("不能超过库存量！");
    } else {
        num.attr("value", value + 1);
        $(this).prev().attr("prevValue",value+1);
        setItem(num.attr("goodsid"), value + 1);
        // setItem2($(this).attr("goodsid"), value+1);
        setItem2(num.attr("goodsid"), value+1);
        ori_cost += parseFloat(num.attr("price"));
        $("#cost").empty()
            .append("商品价格￥")
            .append((ori_cost).toFixed(1))
            .append(" * 折扣")
            .append(discountStr)
            .append(" + 送货费￥5.00 = ￥")
            .append((ori_cost * discount + 5).toFixed(1));
        var stock=$(this).parent().next().attr("stock");
        $(this).parent().next().empty()
            .append(parseInt(stock)-num.val());
        $("#points").empty().append(parseInt(ori_cost * points_rate));
    }
}
function sub() {
    var num = $(this).next();
    var value = num.val();
    if(value >= 1){
        num.val(value-1);
        $(this).next().attr("prevValue",value-1);
        setItem(num.attr("goodsid"), value-1);
        setItem2(num.attr("goodsid"), value-1);
        // request[num.attr("goodsid")] = value - 1;
        ori_cost -= parseFloat(num.attr("price"));
        $("#cost").empty()
            .append("商品价格￥")
            .append((ori_cost).toFixed(1))
            .append(" * 折扣")
            .append(discountStr)
            .append(" + 送货费￥5.00 = ￥")
            .append((ori_cost * discount + 5).toFixed(1));
        var stock=$(this).parent().next().attr("stock");
        $(this).parent().next().empty()
            .append(parseInt(stock)-num.val());
        $("#points").empty().append(parseInt(ori_cost*points_rate));
    }
}

function getParam(paramName) {
    paramValue = "", isFound = !1;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}

function address() {
    location.href = "address_daohang.html?"+select;
}
function cancle(){
    select='';
    history.back();
}