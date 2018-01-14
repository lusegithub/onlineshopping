/**
 * Created by lsy
 */

var webRoot = "/onlineshopping";
var pagePrefix = webRoot + "/backmanage";

$(function () {
    if($.cookie("manager_id") == null){
        // alert("还未登录，请登录！");
        //未登录，跳到登录页面
        location.href = pagePrefix + "/login.html";
    }

    //退出
    $("#logout").click(function () {
        $.ajax({
            url: webRoot + '/manager',
            type: 'PUT'
        });
        //给cookie设置已过期的时间
        $.cookie("manager_id", "", {expires:-1});
        location.href = pagePrefix + "/login.html?logout=";
    });

    //首页
    $("#home").click(function () {
        location.href = pagePrefix + "/";
    });

    //目录管理
    $("#catalogManage").click(function () {
        location.href = pagePrefix + "/catalog_manage";
    });

    //商品管理
    $("#goodsManage").click(function () {
        location.href = pagePrefix + "/goods_manage";
    });

    //订单管理
    $("#orderManage").click(function () {
        location.href =  pagePrefix + "/order_manage";
    });

    //销售量统计
    $("#saleManage").click(function () {
        location.href = pagePrefix + "/sale_manage";
    });

    //会员管理
    $("#userManage").click(function () {
        location.href = pagePrefix + "/user_manage";
    });
});
