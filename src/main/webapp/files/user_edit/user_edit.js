document.write("<script src='files/global.js'></script>");
function checkASCII(obj) {
    var str = obj;
    for (var i = 0; i < str.length; i++) {
        var code = str.charCodeAt(i);
        if (code >= 0 && code < 128) {
            continue;
        } else {
            return false;
        }
    }
    return true;
}

function getParam(paramName) { 
    paramValue = "", isFound = !1; 
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) { 
        arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0; 
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++ 
    } 
    return paramValue == "" && (paramValue = null), paramValue 
}

$(document).ready(function(){
    document.getElementById("u2561_input").type = "password";
    document.getElementById("u2562_input").type = "password";
    document.getElementById("u2563_input").type = "password";
    $.ajax({
        type: "GET",
        url: host + '/user/info',
        dataType: 'json',
        success: function(results){
            var result_code = results.resultCode;
            var result_info = results.resultInfo;
            var content = results.content;
            if(result_code === 0){
                var userId = content.userId;
                var regdate = content.regdate;
                var points = content.points;
                var level = content.level;
                sessionStorage.setItem("nickname", content.nickname);
                $('div#u2582 p span').text(content.nickname + ' |');
                $('input#u2559_input').val(userId);
                $('input#u2559_input').css('text-align', 'center');
                $('input#u2560_input').val(content.nickname);
                $('input#u2560_input').css('text-align', 'center');
                $('input#u2560_input').blur(function(){
                    $('input#u2560_input').css('text-align', 'center');
                })
                $('input#u2561_input').blur(function(){
                    $('input#u2561_input').css('text-align', 'center');
                })
                $('input#u2562_input').blur(function(){
                    $('input#u2562_input').css('text-align', 'center');
                })
                $('input#u2563_input').blur(function(){
                    $('input#u2563_input').css('text-align', 'center');
                })
            }else{
                alert('Error');
            }
        }
    })

    $('input#u2564_input').click(function(){
        var nickname_tmp = $('input#u2560_input').val();
        var old_passwd = $('input#u2561_input').val();
        var new_passwd = $('input#u2562_input').val();
        var confirm_passwd = $('input#u2563_input').val();

        if(nickname_tmp != sessionStorage.getItem("nickname")){
            $.ajax({
                type: 'PUT',
                url: host + '/user/info/nickname',
                dataType: 'json',
                data: {nickname: nickname_tmp},
                success: function(results){
                    nickname = nickname_tmp;
                    var result_code = results.resultCode;
                    var result_info = results.resultInfo;
                    if(result_code === 0){
                        $('div#u2582 p span').text(nickname_tmp + ' |');
                        sessionStorage.setItem("nickname", nickname_tmp);
                    }else{
                        alert('Wrong');
                    }
                }
            })
        }

        if (new_passwd === '' || new_passwd === undefined) {
            alert("密码不能为空");
        }else if(new_passwd !== confirm_passwd){
            alert("两次密码不一致");
        }else if(new_passwd.length > 20 || new_passwd.length < 6) {
            alert("密码长度不合法");
        }else if (!checkASCII(new_passwd)) {
            alert("字符不合法");
        }else{
            $.ajax({
                    type: 'PUT',
                    url: host + '/user/info/passwd',
                    dataType: 'json',
                    data: {old_passwd: old_passwd, new_passwd: new_passwd},
                    success: function(results){
                        var result_code = results.resultCode;
                        var result_into = results.resultInfo;
                        var content = results.content;
                        if(result_code === 0){
                            location.href = host + '/login.html';
                        }
                    }
                })
        }
    })

    $('div#u2566').click(function(){
        location.href = host + '/index.html';
    })

    $('div#u2584 p span').click(function(){
        location.href = host + '/user_edit.html';
    })

    $('div#u2586 p span').click(function(){
        location.href = host + '/address_daohang.html';
    })

    $('div#u2588 p span').click(function(){
        $.ajax({
            type: 'PUT',
            url: host + '/user',
            dataType: 'json',
            success: function(results){
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                if(result_code === 0){
                    sessionStorage.setItem('status', -1);
                    location.href = host + "/index.html";
                }else{
                    alert('Error');
                }
            }
        })
    })

    $('div#u2484').click(function(){
        $(function(){
            location.href = host + '/credit_query.html';
        })
    })

    $('div#u2488').click(function(){
        $(function(){
            location.href = host + '/favorite.html';
        })
    })

    $('div#u2490').click(function(){
        $(function(){
            location.href = host + '/cart_list.html';
        })
    })

    $('div#u2492').click(function(){
        $(function(){
            location.href = host + '/tradequery.html'
        })
    })
})