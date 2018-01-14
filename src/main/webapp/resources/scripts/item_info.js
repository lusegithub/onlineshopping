var webRoot = '/onlineshopping'

var url = location.search;//带？的之后字符串
if(url.indexOf("?") == -1){
    location.href=webRoot+'/index.html'
}
var str = url.substr(1);
var arr = str.split('&&');
var num = arr[0].indexOf('=');

var goodsIds = arr[0].substr(0, num);
var goodsIdsValue = arr[0].substr(num+1);//获取第一个参数goodsid

num = arr[1].indexOf('=');
var resultCode = arr[1].substr(0, num);
var resultCodeValue = arr[1].substr(num+1);//获取第二个参数resultCode


//点击某商品图片时进入详细信息,传输一个带有goodsid的参数。//可判定非登陆用户是否为输入页面进来。

$(document).ready(function () {
    //获取url参数
    if (sessionStorage.getItem("status") == 0) {

        $('#u2 a').html(sessionStorage.getItem("nickname"));
        $('#u3 a').html("个人信息");
        $('#u4 a').html('地址信息');
        $('#u5 a').html("退出");
        $('#u3 a').css('cursor','pointer').click(function(){
            location.href=webRoot+'/user_edit.html';
        })
        $('#u4 a').css('cursor','pointer').click(function(event) {
            /* Act on the event */
            location.href=webRoot+'/address_daohang.html'
        });
        $('#u5 a').css('cursor','pointer').click(function(event) {
            /* Act on the event */
            $.ajax({
                url: webRoot+'/user',
                type: 'PUT',
                success:function(data){
                    if(data.resultCode==0){
                        sessionStorage.setItem('status',-1);
                        alert('已成功退出');
                        location.reload();
                    }else{
                        alert(data.resultInfo);
                    }
                }
            })
                .done(function(){
                    console.log('exit success')
                })
                .fail(function () {
                    console.log('exit fail')
                })
                .always(function(){
                    console.log('complete')
                });

        });
        //添加跳转事件
        $('#home').css('cursor','pointer').click(function(){
            /* Act on the event */
            location.href=webRoot+'/index.html';
        });
        $('#credit_query').css('cursor','pointer').click(function(){
            /* Act on the event */
            location.href=webRoot+'/credit_query.html';
        });
        $('#item_search').css('cursor','pointer').click(function () {
            location.href=webRoot+'/item_search.html';
        });
        $('#favorite').css('cursor','pointer').click(function(){
            /* Act on the event */
            location.href=webRoot+'/favorite.html';
        });
        $('#cart_list').css('cursor','pointer').click(function(){
            location.href=webRoot+'/cart_list.html';
        });
        $('#trade_query').css('cursor','pointer').click(function(){
            location.href=webRoot+'/tradequery.html';
        });
        $('#goods_send').css('cursor','pointer').click(function(){
            location.href=webRoot+'/send_note.html';
        });

        //添加至购物车
        $('#u1229 p span').click(function (event) {
            /* Act on the event */
            moveToCartList();
        });
        //添加收藏按钮
        $("#u1231").click(function (event) {
            /* Act on the event */
            moveTofavorite()
        });
        $('#partComment').click(function(){
            location.href = '/onlineshopping/part_comment.html?name=' + goodsIdsValue;
        })
    } else {
        //设置表头的信息。若是游客身份，则不填加四个跳转事件
        $('#u1 a').empty();
        $('#u2 a').empty();
        $('#u3 a').empty();
        $('#u4 a').html('注册');
        $('#u5 a').html("登录");

        $('#u4 a').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = webRoot + '/register.html'
        });
        $('#u5 a').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = webRoot + '/login.html'
        });
        $('#home').css('cursor', 'pointer').click(function () {
            /* Act on the event */
            location.href = webRoot + '/index.html';
        });
        $('#credit_query').css('cursor', 'pointer').click(function () {
            if (window.confirm("未登录，是否现在登录？")) {
                location.href = "login.html";
            } else {
                return false;
            }
        });
        $('#item_search').css('cursor', 'pointer').click(function () {
            location.href = webRoot + '/item_search.html';
        });
        $('#favorite').css('cursor', 'pointer').click(function () {
            if (window.confirm("未登录，是否现在登录？")) {
                location.href = "login.html";
            } else {
                return false;
            }
        });
        $('#cart_list').css('cursor', 'pointer').click(function () {
            if (window.confirm("未登录，是否现在登录？")) {
                location.href = "login.html";
            } else {
                return false;
            }
        });
        $('#trade_query').css('cursor', 'pointer').click(function () {
            if (window.confirm("未登录，是否现在登录？")) {
                location.href = "login.html";
            } else {
                return false;
            }
        });
        $('#goods_send').css('cursor', 'pointer').click(function () {
            location.href = webRoot + '/send_note';
        });
        $('#partComment').click(function(){
            if(window.confirm('未登录，是否现在登录？')){
                location.href = "login.html";
            } else {
                return false;
            }
        });
        $('#u1229 p span').click(function (event) {
            /* Act on the event */
            if(window.confirm('未登录，是否现在登录？')){
                location.href = "login.html";
            } else {
                return false;
            }
        });
        //添加收藏按钮
        $("#u1231").click(function (event) {
            /* Act on the event */
            if(window.confirm('未登录，是否现在登录？')){
                location.href = "login.html";
            } else {
                return false;
            }
        });
    }
    //是否登录均相同的操作
    $.ajax({
        url: webRoot + '/goods/' + goodsIdsValue + '/info',
        type: 'GET',
        success: function (data) {
            //在无边框的块中添加信息。
            //为主页的商品进行的请求跳转，当出现不可预知的异常，则弹出错误信息。（不可预测的异常）
            if (data.resultCode == 0) {
                $('#u1223').html(data.content.name);
                $('#u1224_img').attr({'src': data.content.photo, 'id': data.content.goodsIds});
                $('#u1227 textarea').html(data.content.describe);
                $('#u1233 p span').html('市场价:￥' + data.content.price);
                $('#u1235 p span').html('库存量：' + data.content.stock);
            } else {
                alert(data.resultInfo);
                history.back();
            }
        }
    })
        .done(function () {
            console.log("goodsRequest success");
        })
        .fail(function () {
            console.log("goodsRequest error");
        })
        .always(function () {
            console.log("goodsRequest complete");
        });
    //为评论添加事件跳转到comment_list页面，并传递goodsids参数
    $('#comment').click(function (event) {
        /* Act on the event */
        location.href = '/onlineshopping/comment_list.html?name=' + goodsIdsValue;
    });
})
function moveToCartList() {
    //将数据转换成约定好形式传向后台
    $.ajax({
        url: webRoot + '/user/shoppingcart',
        type: 'PUT',
        dataType: 'json',
        data: {goodsId: goodsIdsValue},
        success: function (data) {
            if (data.resultCode == -1) {
                alert("该商品不存在存货");
            } else if (data.resultCode == -2) {
                alert('该商品已在购物车内');
            } else {
                alert('成功已添加至购物车！');
            }
        }
    })
        .done(function () {
            console.log("success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
}
function moveTofavorite() {
    //将数据转换成约定好形式传向后台
    $.ajax({
        url: webRoot + '/user/favorite',
        type: 'PUT',
        dataType: 'json',
        data: {goodsId: goodsIdsValue},
        success: function (data) {
            if (data.resultCode == -1) {
                alert("该商品不存在存货");
            } else if (data.resultCode == -2) {
                alert('该商品已在收藏夹内');
            } else {
                alert('成功已添加至收藏夹！');
            }
        }
    })
        .done(function () {
            console.log("success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
}