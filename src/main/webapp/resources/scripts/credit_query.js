if (sessionStorage.getItem("status") != 0) {
    location.href = "login.html";
}

$(document).ready(function () {
    var webRoot = '/onlineshopping'


    $('#u779 p span').empty();
    $('#u779 p span').html(sessionStorage.getItem("nickname"));

    $('#u781 p span').empty();
    $('#u781 p span').html('个人信息');
    $('#u781 p span').css('cursor', 'pointer').click(function (event) {
        /* Act on the event */
        location.href = webRoot + '/user_edit.html'
    });
    $('#u783 p span').empty();
    $('#u783 p span').html('地址信息');
    $('#u783 p span').css('cursor', 'pointer').click(function (event) {
        /* Act on the event */
        location.href = webRoot + '/address_daohang.html'
    });
    $('#u785 p span').empty();
    $('#u785 p span').html('退出');
    $('#u785 p span').css('cursor', 'pointer').click(function (event) {
        /* Act on the event */
        $.ajax({
            url: webRoot + '/user',
            type: 'PUT',
            success: function (data) {
                if (data.resultCode == 0) {
                    sessionStorage.setItem('status', -1);
                    alert('已成功退出');
                    location.reload();
                } else {
                    alert(data.resultInfo);
                }
            }
        })
        .done(function () {
            console.log('exit success')
        })
        .fail(function () {
            console.log('exit fail')
        })
        .always(function () {
            console.log('complete')
        });
    });
    //若是用户登录则四个页面可以跳转。添加跳转事件
    $('#u687 p span').css('cursor', 'pointer').click(function (event) {
        /* Act on the event */
        location.href = '/onlineshopping/credit_query.html';
    });
    $('#u691 p span').css('cursor', 'pointer').click(function (event) {
        /* Act on the event */
        location.href = '/onlineshopping/favorite.html';
    });
    $('#u693 p span').css('cursor', 'pointer').click(function (event) {
        location.href = '/onlineshopping/cart_list.html';
    });
    $('#u695 p span').css('cursor', 'pointer').click(function (event) {
        location.href = '/onlineshopping/tradequery.html';
    });

    //获取积分数据.json中按照数组显示的订单信息。可一次性获取订单数量并在前端分页显示
    //全局变量订单总数和页数
    var item_num;
    var pages_num;
    var psize = 3;//每页显示条数
    $.ajax({
        url: webRoot + "/user/points",
        type: 'GET',
        success: function (data) {
            /* iterate through array or object */
            //判定操作是否合法以及是否成功
            if (data.resultCode == 0) {
                $("#u722 p span").html("当前积分为：" + data.content.totalPoints);
                //添加getdiscount的函数，在ajax中的回调函数中设置局部变量使用
                $.get(webRoot + '/user/discount', function (result) {
                    /*optional stuff to do after success */
                    if (result.resultCode == 0) {
                        sessionStorage.setItem('discount', data.content);
                    } else {
                        alert(result.resultInfo);
                    }
                });

                // var li = "";
                $.each(data.content.pointsItems, function (index, val) {
                    //获取订单的总数，将其存储在session中以便全局获取该变量
                    sessionStorage.setItem('item_num', parseInt(index) + 1);
                    // li =
                    //     "<tr class='show'>" +
                    //     "<td>" + "<a href='javascript:void(0);' onclick='turnToOrderInfo(" + val.order_id + ")'>" + val.order_id + "</a>" + "</td>" +
                    //     "<td>" + val.orderTime + "</td>" +
                    //     "<td>" + val.price + "</td>" +
                    //     "<td>" + val.points + "</td>" +
                    //     "</tr>";
                    $('#hiddenresult').append("<tr class='show'>" +
                        "<td>" + "<a href='javascript:void(0);' onclick='turnToOrderInfo(" + val.order_id + ")'>" + val.order_id + "</a>" + "</td>" +
                        "<td>" + val.orderTime + "</td>" +
                        "<td>" + val.price + "</td>" +
                        "<td>" + val.points + "</td>" +
                        "</tr>");
                });
                //each之后可获得item的数量。进行总页数得获取.若不能整除则加一
                item_num = sessionStorage.getItem('item_num');
                if (item_num / psize > parseInt(item_num / psize)) {
                    page_num = Math.ceil(item_num / psize);
                } else {
                    page_num = item_num / psize;
                }

                var initPagination = function () {
                    var num_entries = sessionStorage.getItem('item_num');
                    // 创建分页
                    $("#Pagination").pagination(num_entries, {
                        num_edge_entries: 1, //边缘页数
                        num_display_entries: 4, //主体页数
                        callback: pageselectCallback,
                        items_per_page: 3 //每页显示3项
                    });
                }();

                function pageselectCallback(page_index, jq) {
                    //订单总数
                    var length = sessionStorage.getItem('item_num');
                    //获取将被添加的每页的最后一个商品的代号，以保证最后一页无论是否为每页所显示的书目，都不出现bug
                    var max_elem = Math.min((page_index + 1) * psize, length);
                    //每次点击页数时，清空之前页的内容。以假装翻页数据更新
                    $("#Searchresult").html("");
                    //循环向tobdy中添加当前页个数的商品
                    for (var i = page_index * psize; i < max_elem; i++) {
                        $("#Searchresult").append($('#hiddenresult .show:eq(' + i + ')').clone()); //装载对应分页的内容
                    }
                    return false;
                }
            } else {
                alert(data.resultInfo);
            }
        }
    });

    //  else {
    //     //设置表头的信息。若是游客身份，则不填加四个跳转事件
    //     //同时设置表头
    //     $("#u777 p span").empty();
    //     $("#u779 p span").empty();
    //     $("#u781 p span").empty();
    //     $("#u783 p span").html(' 登录 ');
    //     $("#u785 p span").html(' 注册 ');
    //
    //     $("#u783 p span").css('cursor', 'pointer').click(function () {
    //         location.href = '/onlineshopping/login2.html';
    //     });
    //     $("#u785 p span").css('cursor', 'pointer').click(function () {
    //         location.href = '/onlineshopping/register.html';
    //     });
    //     //导航栏未登录状态下点击
    //     $('#u687 p span').css('cursor', 'pointer').click(function (event) {
    //         /* Act on the event */
    //         if (window.confirm('登录状态才可以查看自己积分情况，是否登录？')) {
    //             location.href = webRoot + '/login2.html';
    //         } else {
    //             return false;
    //         }
    //     })
    //     $('#u691 p span').css('cursor', 'pointer').click(function (event) {
    //         /* Act on the event */
    //         if (window.confirm('登录状态才可以查看自己收藏夹情况，是否登录？')) {
    //             location.href = webRoot + '/login2.html';
    //         } else {
    //             return false;
    //         }
    //     });
    //     $('#u693 p span').css('cursor', 'pointer').click(function (event) {
    //         if (window.confirm('登录状态才可以查看自己购物车情况，是否登录？')) {
    //             location.href = webRoot + '/login2.html';
    //         } else {
    //             return false;
    //         }
    //     });
    //     $('#u695 p span').css('cursor', 'pointer').click(function (event) {
    //         if (window.confirm('登录状态才可以查看自己交易信息情况，是否登录？')) {
    //             location.href = webRoot + '/login2.html';
    //         } else {
    //             return false;
    //         }
    //     });
    //     if (window.confirm('您当前是未登录状态，请登录后再访问本页面,是否转到登录页面？')) {
    //         location.href = webRoot + '/login2.html';
    //     } else {
    //         return false;
    //     }
    // }
});
//点击order表单，为order_info页面进行url传参。通过info页面的get方法进入该订单的详细信息
function turnToOrderInfo(para) {
    location.href = '/onlineshopping/order_info.html?order_id=' + para + '';
}