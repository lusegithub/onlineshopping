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
    var nickname = getParam("nickname");
    console.log(nickname);

    $("div#u668 p span").eq(0).text(nickname);

    $('div#u668').click(function(){
        location.href = host + "/user_edit.html?" +
        "nickname=" + nickname;
    })

    $("div#u670").click(function(){
        location.href = host + "/address_daohang.html?" +
        "nickname=" + nickname;        
    })
})