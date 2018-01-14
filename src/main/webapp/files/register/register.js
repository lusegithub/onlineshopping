document.write("<script src='files/global.js'></script>");

function ischeckemail(email) {
    if (email != "") {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        isok = reg.test(email);
        if (!isok) {
            return false;
        }
        return true;
    }
}

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

$(document).ready(function () {
    $("#u5").click(function () {
        location.href = 'login.html';
    })
    $("input#u2089_input").click(function () {
        var email = $("input#u2107_input").val();
        var nickname = $("input#u2115_input").val();
        var passwd = $("input#u2123_input").val();
        var passwd_repeat = $("input#u2131_input").val();

        if (email == "" | passwd == "" | passwd_repeat == "") {
            alert("请填写完整");
        } else if (passwd != passwd_repeat) {
            alert("密码不一致");
        } else if (passwd.length > 20 || passwd.length < 6) {
            alert("密码长度不合法");
        } else if (!ischeckemail(email)) {
            alert("邮箱格式不正确，请重新输入！");
        } else if (!checkASCII(passwd) || !checkASCII(email)) {
            alert('字符不合法');
        }
        else {
            $.ajax({
                type: "POST",
                url: host + "/user",
                dataType: "json",
                data: {user_id: email, password: passwd, nickname: nickname},
                success: function (results) {
                    var result_code = results.resultCode;
                    if (result_code === 0) {
                        var result_info = results.resultInfo;
                        var content = results.content;
                        user_id = content.userId;
                        nickname = content.nickname;
                        level = content.level;
                        register = content.regdate;
                        points = content.points;

                        $.ajax({
                            type: "POST",
                            url: host + "/user/login",
                            dataType: 'json',
                            data: {
                                user_id: user_id,
                                password: passwd
                            },
                            success: function (response) {
                                if(nickname == "")
                                    sessionStorage.setItem("nickname", userid)
                                else
                                    sessionStorage.setItem("nickname", nickname)
                                if (response.resultCode === 0) {
                                    sessionStorage.setItem("status", 0);
                                    location.href = host + "/index.html";
                                }
                            }
                        });
                    } else if (result_code === -1) {
                        alert("用户名已存在！");
                    } else if (result_code === -2) {
                        alert("密码不合法！");
                    } else if (result_code === -3) {
                        alert("昵称不合法！");
                    } else if (result_code === -4) {
                        alert("用户名不合法!");
                    } else {
                        alert("注册失败！");
                    }
                }
            });
        }

    });
});