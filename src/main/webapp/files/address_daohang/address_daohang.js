if (sessionStorage.getItem("status") != 0) {
    location.href = "login.html";
}

document.write("<script src='files/global.js'></script>");
var count_address;
function getParam(paramName) {
    paramValue = "", isFound = !1;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = decodeURI(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
    }
    return paramValue == "" && (paramValue = null), paramValue
}

function checkPhone(phone){
    for(var i=0; i<phone.length; ++i){
        if(isNaN(phone[i])){
            return false;
        }
    }
    return true;
}

//获取地址
function getAddress(){
    $.ajax({
        type: "GET",
        url: host + "/user/addresses",
        dataType: 'json',
        async: false,
        success: function (response) {
            var result_code = response.resultCode;
            if (result_code === 0) {
                var result_info = response.resultInfo;
                var content = response.content;
                count_address = content.length;
                $.each(content, function (i, item) {
                    localStorage.setItem("address_" + i, JSON.stringify(item));
                })
            } else {
                count_address = 0;
            }

        }
    })

    var style = 'top:5px;left:36px;position:absolute;z-index:1;';
    if (count_address === 0) {
        $('input#u2352_input').remove();
        $('div#u2317 p span').text("");
        $('input#u2354_input').remove();
        $('input#u2355_input').remove();
    }
    for (var i = 0; i < count_address; ++i) {
        if (i === 0) {
            var address_0 = localStorage.getItem('address_0');
            var content = JSON.parse(address_0);
            var name=content.name;
            var address = content.address;
            var phone=content.phoneNumber;
            $('div#u2314')
                .append("<tr style='top:5px;left:206px;position:absolute;z-index:1;visibility: visible;'>"+"<td>"+name+"</td>"+"</tr>")
                .append("<input value='0' type='radio' name='Radio_addr' style=" + style + " onclick='radioFunc()'>");
            $('div#u2317').css({"display": "", "visibility": "visible"});
            $('div#u2317').append($('<tr></tr>')
                .css('visibility','visible')
                .css('width','300px')
                .append('<td style="width:150px">'+address+'</td>')
                .append('<td style="width:150px;">'+phone+'</td>')
            );
            $('div#u2318').append('<input id="input_addr1_c" type="submit" value="修改" style="position:absolute;z-index:1">');
            $('div#u2318').append('<input id="input_addr1_d" type="submit" value="删除" style="position:absolute;z-index:1">');
        } else if (i === 1) {
            var address_1 = localStorage.getItem('address_1');
            var content = JSON.parse(address_1);
            var name=content.name;
            var address = content.address;
            var phone=content.phoneNumber;
            $('div#u2320')
                .append("<tr style='top:5px;left:206px;position:absolute;z-index:1;visibility: visible;'>"+"<td>"+name+"</td>"+"</tr>")
                .append("<input value='1' type='radio' name='Radio_addr' style=" + style + " onclick='radioFunc()'>");
            $('div#u2323').css({"display": "", "visibility": "visible"});
            $('div#u2323').append($('<tr></tr>')
                .css('visibility','visible')
                .css('width','300px')
                .append('<td style="width:150px">'+address+'</td>')
                .append('<td style="width:150px;">'+phone+'</td>')
            );
            $('div#u2324').append('<input id="input_addr1_c" type="submit" value="修改" style="position:absolute;z-index:1">');
            $('div#u2324').append('<input id="input_addr1_d" type="submit" value="删除" style="position:absolute;z-index:1">');
        } else if (i === 2) {
            var address_2 = localStorage.getItem('address_2');
            var content = JSON.parse(address_2);
            var name=content.name;
            var address = content.address;
            var phone=content.phoneNumber;
            $('div#u2326')
                .append("<tr style='top:5px;left:206px;position:absolute;z-index:1;visibility: visible;'>"+"<td>"+name+"</td>"+"</tr>")
                .append("<input value='2' type='radio' name='Radio_addr' style=" + style + " onclick='radioFunc()'>");
            $('div#u2329').css({"display": "", "visibility": "visible"});
            $('div#u2329').append($('<tr></tr>')
                .css('visibility','visible')
                .css('width','300px')
                .append('<td style="width:150px">'+address+'</td>')
                .append('<td style="width:150px;">'+phone+'</td>')
            );
            $('div#u2330').append('<input id="input_addr1_c" type="submit" value="修改" style="position:absolute;z-index:1">');
            $('div#u2330').append('<input id="input_addr1_d" type="submit" value="删除" style="position:absolute;z-index:1">');
        } else if (i === 3) {
            var address_3 = localStorage.getItem('address_3');
            var content = JSON.parse(address_3);
            var name=content.name;
            var address = content.address;
            var phone=content.phoneNumber;
            $('div#u2332')
                .append("<tr style='top:5px;left:206px;position:absolute;z-index:1;visibility: visible;'>"+"<td>"+name+"</td>"+"</tr>")
                .append("<input value='3' type='radio' name='Radio_addr' style=" + style + " onclick='radioFunc()'>");
            $('div#u2335').css({"display": "", "visibility": "visible"});
            $('div#u2335').append($('<tr></tr>')
                .css('visibility','visible')
                .css('width','300px')
                .append('<td style="width:150px">'+address+'</td>')
                .append('<td style="width:150px;">'+phone+'</td>')
            );
            $('div#u2336').append('<input id="input_addr1_c" type="submit" value="修改" style="position:absolute;z-index:1">');
            $('div#u2336').append('<input id="input_addr1_d" type="submit" value="删除" style="position:absolute;z-index:1">');
        }
        // else {
        //     var address_4 = localStorage.getItem('address_4');
        //     var content = JSON.parse(address_4);
        //     var address = content.address;
        //     $('div#u2338').append('<input value="4" type="radio" name="Radio_addr" style=' + style + '>');
        //     $('div#u2341').css({"display": "", "visibility": "visible"});
        //     $('div#u2341 p span').text(address);
        //     $('div#u2342').append('<input id="input_addr1_c" type="submit" value="修改" style="position:absolute;z-index:1">');
        //     $('div#u2342').append('<input id="input_addr1_d" type="submit" value="删除" style="position:absolute;z-index:1">');
        // }
    }

    if(location.search.substr(1) == "select"){

        $('div#u2342').append($("<input id='confirm' type='button' value='确定' style='width: 100px;position: absolute;z-index: 1'>").click(function () {
            var aid = $("input[name='Radio_addr']:checked").val();
            if(aid==null) {
                alert("请选择一个地址");
                $("#confirm").css(disabled,true);
            }
            location.href = "order_confirm.html?aid="+parseInt(aid);
        }));
    }
}

$(document).ready(function () {
    $('div#u2359').hover(function () {
        $('div#u2359').css('cursor', 'pointer');
    })
    $('div#u2361').hover(function () {
        $('div#u2361').css('cursor', 'pointer');
    })
    $('div#u2363').hover(function () {
        $('div#u2363').css('cursor', 'pointer');
    })
    var nickname;

    $.ajax({
        type: 'GET',
        url: host + '/user/info',
        dataType: 'json',
        success: function (results) {
            var result_code = results.resultCode;
            if (result_code === 0) {
                var content = results.content;
                nickname = content.nickname;
                $("div#u2357 p span").text(nickname + "  |");

            }
        }
    })
    getAddress();

    //修改
    for(var i=0; i<count_address; ++i){
        // var json=JSON.parse(localStorage.getItem('address_'+i));
        (function(val){
            $('input#input_addr1_c').eq(val).click(function(){
            location.href = host + "/address_edit.html?select" + "&id="+val;
            })
        })(i)

    }


    //删除
    for(var i=0; i<count_address; ++i){
        (function(val){
            $('input#input_addr1_d').eq(val).click(function(){
            var address = localStorage.getItem('address_' + (val));
            var content = JSON.parse(address);
            var addressId = content.addressId;
            $.ajax({
                type: 'DELETE',
                dataType: 'json',
                url: host + '/user/address/' + addressId,
                success: function(results){
                    console.log(addressId);
                    var result_code = results.resultCode;
                    var result_info = results.resultInfo;
                    if(result_code === 0){
                        location.reload();
                        location.href = host + "/address_daohang.html?" +
                        "nickname=" + nickname;
                    }else{
                    alert('Error');
                }
                }
            })
        })
    })(i)
    }

    //提交
    $("input#u2305_input").click(function () {
        console.log('startSubmit');
        var consignee = $("input#u2300_input").val();
        var addr = $("input#u2303_input").val();
        var zip = $("input#u2301_input").val();
        var phone = $("input#u2302_input").val();

        if(addr === "" || consignee === "" || phone === ""){
            alert("请填写完整");
        // }else if(phone.length != 11 || !checkPhone(phone)){
        //     alert("请填写正确的手机号码");
        }else{
            $.ajax({
            type: 'POST',
            url: host + '/user/addresses',
            dataType: 'json',
            data: {address:addr, consignee:consignee,
            zip: zip, phone: phone},
            success: function(results){
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                if(result_code === 0){
                    location.reload();
                }else if(result_code === -2){
                    alert("地址不合法");
                }else if(result_code === -3){
                    alert("邮编不合法");
                }else if(result_code === -4){
                    alert("电话不合法");
                }else if(result_code === -1){
                    alert("姓名不合法");
                }else if(result_code === -6){
                    alert("地址数量超出限制");
                }else if(result_code === -5){
                    alert("用户未登录");
                }
            }
        })
        }

    })

    //重填
    $('input#u2306_input').click(function () {
        $('input#u2300_input').val('');
        $('input#u2301_input').val('');
        $('input#u2302_input').val('');
        $('input#u2303_input').val('');
    })

    //注销
    $("div#u2363").click(function () {
        $.ajax({
            type: 'PUT',
            url: host + '/user',
            dataType: 'json',
            success: function (results) {
                var result_code = results.resultCode;
                var result_info = results.resultInfo;
                console.log(result_code);
                if (result_code === 0) {
                    sessionStorage.setItem('status', -1);
                    location.href = host + "/index.html";
                } else {
                    alert('Error');
                }
            }
        })
    })

    $('div#u2359').click(function () {
        location.href = host + '/user_edit.html?nickname='
            + nickname;
    })

    $('div#u2230').click(function () {
        $(function () {
            location.href = host + '/credit_query.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u2234').click(function () {
        $(function () {
            location.href = host + '/favorite.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u2236').click(function () {
        $(function () {
            location.href = host + '/cart_list.html?' +
                "nickname=" + nickname;
        })
    })

    $('div#u2238').click(function () {
        $(function () {
            location.href = host + '/tradequery.html?' +
                "nickname=" + nickname;
        })
    })

})
function radioFunc(){
    $("#confirm").css(disabled,false);
}
