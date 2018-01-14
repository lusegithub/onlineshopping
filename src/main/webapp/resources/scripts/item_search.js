$(document).ready(function () {
    var webRoot = '/onlineshopping'
    if (sessionStorage.getItem('status') == 0) {
        var nickname=sessionStorage.getItem('nickname');
        $('#u958 p span').html(nickname);
        $('#u960 p span').empty();
        $('#u960 p span').html('个人信息');
        $('#u960').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = webRoot + '/user_edit.html'
        });
        $("#u962 p span").empty();
        $('#u962 p span').html('地址信息');
        $('#u962 p span').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = webRoot + '/address_daohang.html'
        });
        $('#u964 p span').empty();
        $('#u964 p span').html('退出');
        $('#u964 p span').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            $.ajax({
                url: webRoot + '/user',
                type: 'PUT',
                success: function () {
                    sessionStorage.setItem('status', -1)//退出登录时清除cookie防止登录状态不准确
                    alert('已成功退出');
                    location.reload();
                },
            })
        });
        //若是用户登录则四个页面可以跳转。添加跳转事件
        $('#u902 p span').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = '/onlineshopping/credit_query.html';
        });
        $('#u906 p span').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            location.href = '/onlineshopping/favorite.html';
        });
        $('#u908 p span').css('cursor', 'pointer').click(function (event) {
            location.href = '/onlineshopping/cart_list.html';
        });
        $('#u910 p span').css('cursor', 'pointer').click(function (event) {
            location.href = '/onlineshopping/tradequery.html';
        });
    } else {
        //设置表头的信息。若是游客身份，则不填加四个跳转事件
        $('#u902 p span').css('cursor', 'pointer').click(function (event) {
            if(window.confirm("未登录，是否现在登录？")){
                location.href =webRoot + "/login.html";
            }
        });
        $('#u906 p span').css('cursor', 'pointer').click(function (event) {
            /* Act on the event */
            if(window.confirm("未登录，是否现在登录？")){
                location.href = webRoot +"/login.html";
            }
        });
        $('#u908 p span').css('cursor', 'pointer').click(function (event) {
            if(window.confirm("未登录，是否现在登录？")){
                location.href = webRoot +"/login.html";
            }
        });
        $('#u910 p span').css('cursor', 'pointer').click(function (event) {
            if(window.confirm("未登录，是否现在登录？")){
                location.href = webRoot +"/login.html";
            }
        });
        $("#u956 p span").empty();
        $("#u958 p span").empty();
        $("#u960 p span").empty();
        $("#u962 p span").empty();
        $("#u964 p span").empty();
        $("#u962 p span").html('【登录】');
        $("#u964 p span").html('【注册】');
        $("#u962 p span").css('cursor', 'pointer').click(function () {
            location.href = webRoot + '/login.html'
        });
        $("#u964 p span").css('cursor', 'pointer').click(function () {
            location.href = webRoot + 'register.html'
        });
    }


    $.ajax({
        url: webRoot + '/catalog',
        type: 'GET',
        success: function (data) {
            var li = '';
            if (data.resultCode == 0) {
                $.each(data.content, function (index, val) {
                    /* iterate through array or object */
                    li += "<option value=" + val.catalogName + ">" + val.catalogName + "</option>"
                });
                $("#u940_input").append(li);
            } else {
                alert('目录为空')
            }
        }
    })
        .done(function () {
            console.log("option_select success");
        })
        .fail(function () {
            console.log("option_select error");
        })
        .always(function () {
            console.log("option_select complete");
        });


    $('#u952_input').click(function () {
        var small = $('#u947_input');
        var large = $('#u948_input');
        var searchObj =
            {
                catalog_name: $('#u940_input option:selected').val(),
                small_price: small.val(),
                large_price: large.val(),
                keyword: $('#u951_input').val()
            };

        var allNull = true;
        $.each(searchObj, function (index, item) {
            if (item !== "") {
                allNull = false;
                return false;
            }
        });
        if(allNull) {
            alert("不能全为空！");
            return;
        }
        if(small.val() === "" && large.val()!==""){
            alert("请输入价格下限！")
            small.focus();
            return;
        }
        if(small.val() !== "" && large.val()===""){
            alert("请输入价格上限！");
            large.focus();
            return;
        }
        if(small.val()>large.val()){
            alert('价格区间不合法');
            small.focus();
            return;
        }

        var storageSStr = JSON.stringify(searchObj)
        sessionStorage.setItem('searchDatas', storageSStr);//按照四个关键字搜索
        $(location).attr({
            href: webRoot + '/item_search_list.html'
        });
    });

});

