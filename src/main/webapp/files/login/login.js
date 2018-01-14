document.write("<script src='files/global.js'></script>");
function ischeckemail(email){
    if (email != "") {
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        isok= reg.test(email);
        if (!isok) {
             return false;
        }
        return true;
     }
}

function checkASCII(obj){
  var str=obj;
  for(var i=0 ; i<str.length; i++){
    var code=str.charCodeAt(i);
    if (code >= 0 && code < 128) {
      continue;
    }else{
      return false;
    }
  }
  return true;
 }

$(document).ready(function () {
    $("#login").click(function () {
        var userid = $("#userInput").val();
        var passwd = $("#passwordInput").val();

        if(userid == "" | passwd == ""){
                alert("请填写完整");
        }else if(!ischeckemail(userid)){
                alert("用户名不合法");
        }else if (!checkASCII(passwd) || !checkASCII(userid)) {
                alert("字符不合法");
        }else if(passwd.length > 20 || passwd.length < 6){
                alert("密码长度不合法");
        }else{

        $.ajax({
            type: "POST",
            url: host + "/user/login",
            dataType: 'json',
            data: {user_id: userid, password: passwd},
            success: function (results) {
                console.log(results);
                console.log("success");
                var result_code = results.resultCode;
                console.log(result_code);
                if (result_code === 0) {
                    var result_info = results.resultInfo;
                    var content = results.content;
                    user_id = content.userId;
                    nickname = content.nickname;
                    level = content.level;
                    register = content.regdate;
                    points = content.points;

                    if(nickname == "undefined")
                        sessionStorage.setItem("nickname", userid)
                    else
                        sessionStorage.setItem("nickname", nickname)

                    sessionStorage.setItem('status', 0);
                    if(sessionStorage.getItem('ISFROMCLICK')==0){
                        sessionStorage.setItem('ISFROMCLICK',-1);
                    }else{
                        history.back();
                    }
                    // $(function () {
                    //     location.href = host + "/index.html?" +
                    //         "nickname=" + nickname;
                    //
                    // })

                } else {
                    alert("用户名或者密码不正确");
                }
            }
        })
}
    // $("#u2146 p span").click(function () {
    //     if(window.confirm("请先登录！")){
    //         return;
    //     }
    // })
    $("input#reWrite").click(function () {
        $("input#userInput").val("");
        $("input#passwordInput").val("");
    })
})

})