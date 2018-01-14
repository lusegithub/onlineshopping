if(sessionStorage.getItem("status") != 0){
    location.href = "login.html";
}

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
    var nickname = sessionStorage.getItem("nickname");
    var id = getParam('id');
    var contents = localStorage.getItem('address_' + id);
    var content = JSON.parse(contents);
    var name = content.name;
    var address = content.address;
    var zip = content.zip;
    var phoneNumber = content.phoneNumber;
    var addressid=content.addressId;

    $('div#u2467 p span').text(nickname + ' |');
    $('div#u2467 p').attr('align', 'Right');

    $('input#u2449_input').val(name);
    $('input#u2450_input').val(address);
    $('input#u2451_input').val(zip);
    $('input#u2452_input').val(phoneNumber);
    
    //提交
    $("input#u2453_input").click(function(){
        console.log('startSubmit');
        var addr = $("input#u2450_input").val();
        var consignee = $("input#u2449_input").val();
        var zip = $("input#u2451_input").val();
        var phone = $("input#u2452_input").val();

        if(addr !== "" && consignee !== "" && phone !== ""){
            $.ajax({
            type: 'PUT',
            url: host + '/user/address/'+addressid,
            dataType: 'json',
            data: {address:addr, consignee:consignee,
            zip: zip, phone: phone},
            success: function(results){
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                if(result_code === 0){
                    console.log("OK");
                    if(location.search.indexOf("select") == -1)
                        location.href = host + '/address_daohang.html'
                    else
                        location.href = host + '/address_daohang.html?select'
                }else{
                    alert(results.resultInfo);
                }
            }
        })
        }else{
            alert("请填写完整。");
        }
        
    })

    //重填
    $('input#u2454_input').click(function(){
        $('input#u2449_input').val(name);
        $('input#u2450_input').val(address);
        $('input#u2451_input').val(zip);
        $('input#u2452_input').val(phoneNumber);
    })

    //注销
    $("div#u2473").click(function(){
        $.ajax({
            type: 'PUT',
            url: host + '/user',
            dataType: 'json',
            success: function(results){
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                console.log(result_code);
                if(result_code === 0){
                    sessionStorage.setItem('status', -1);
                    location.href = host + "/index.html";
                }else{
                    alert('Error');
                }
            }
        })
        })

    $('input#u2455_input').click(function(){
        location.href = host + '/address_daohang.html?nickname='
        + nickname;
    })

    $('div#u2469').click(function(){
        location.href = host + '/user_edit.html?nickname='
        + nickname;
    })

    $('div#u2471').click(function(){
        location.href = host + '/address_daohang.html?nickname='
        + nickname;
    })

    $('div#u2374').click(function(){
        $(function(){
            location.href = host + '/credit_query.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u2378').click(function(){
        $(function(){
            location.href = host + '/favorite.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u2380').click(function(){
        $(function(){
            location.href = host + '/cart_list.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u2382').click(function(){
        $(function(){
            location.href = host + '/tradequery.html?' +
            "nickname=" + nickname;
        })
    })
 

})