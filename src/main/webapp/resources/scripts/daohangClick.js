$(document).ready(function(){
    /**
     * Created by vincent on 2017/7/11.
     */
    var webRoot='/onlineshopping';
//判定是否已经登录，来决定所显示的游客或用户信息
    if(sessionStorage.getItem("status")==0){
        var nickname = sessionStorage.getItem("nickname");

        // $('#u1').html("注册");
        $('#u2').html(nickname);
        $('#u3').html("个人信息");
        $('#u4').html('地址信息');
        $('#u5').html("退出");
        $('#u1').css('cursor','pointer').click(function(){
            location.href=webRoot+'/register.html';
        });
        $('#u3').css('cursor','pointer').click(function(){
            location.href=webRoot+'/user_edit.html';
        })
        $('#u4').css('cursor','pointer').click(function(event) {
            /* Act on the event */
            location.href=webRoot+'/address_daohang.html'
        });
        $('#u5').css('cursor','pointer').click(function(event) {
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
    }else{
        //设置表头的信息。若是游客身份，则不填加四个跳转事件
        $('#u1').empty();
        $('#u2').empty();
        $('#u3').empty();
        $('#u4').html('注册');
        $('#u5').html("登录");

        $('#u4').css('cursor','pointer').click(function(event) {
            /* Act on the event */
            location.href=webRoot+'/register.html'
        });
        $('#u5').css('cursor','pointer').click(function(event) {
            /* Act on the event */
            location.href=webRoot+'/login.html'
        });
        $('#home').css('cursor','pointer').click(function(){
            /* Act on the event */
            location.href=webRoot+'/index.html';
        });
        $('#credit_query').css('cursor','pointer').click(function(){
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        });
        $('#item_search').css('cursor','pointer').click(function () {
            location.href = webRoot + '/item_search.html';
        });
        $('#favorite').css('cursor','pointer').click(function(){
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        });
        $('#cart_list').css('cursor','pointer').click(function(){
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        });
        $('#trade_query').css('cursor','pointer').click(function(){
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        });
        $('#goods_send').css('cursor','pointer').click(function(){
            location.href=webRoot+'/send_note.html';
        });
    }
})