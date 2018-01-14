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
    for (var i = 1990; i<=2017; i++) {
        $("#u1855_input").append($("<option></option>").attr("value", i).append(i));
        $("#u1864_input").append($("<option></option>").attr("value", i).append(i));
    }
    var nickname = sessionStorage.getItem("nickname");
    $('div#u1876').text(nickname + ' |');
    $('div#u1876').attr('align', 'Right');
    $("input#u1874_input").click(function(){
        var status = $("select#u1873_input").val();
        var begin_time = $('input#date1').val();
        var end_time = $('input#date2').val();
        var orderData={
            s:status,
            b:begin_time,
            e:end_time
        }
        sessionStorage.setItem("orderData",JSON.stringify(orderData));
        location.href = host + '/order_list.html?nickname=' +nickname;
        // $.ajax({
        //     type: 'GET',
        //     url: host + '/user/orders',
        //     dataType: 'json',
        //     data: {begin_time: begin_time, end_time: end_time,
        //         status: status},
        //     success: function(results){
        //         var result_code = results.resultCode;
        //         console.log(result_code);
        //         if (result_code === 0) {
        //             console.log(results.content);
        //             localStorage.setItem('orders', JSON.stringify(results.content));
        //             location.href = host + '/order_list.html?nickname=' +
        //                 nickname;
        //         }else if(result_code === -1){
        //             alert("时间参数不合法")
        //         }else if(result_code === -2){
        //             alert("状态参数不合法")
        //         }else if(result_code === -3){
        //             alert("开始时间不能大于结束时间")
        //         }else if(result_code === -4){
        //             alert("订单数量为零")
        //         }
        //     }
        // })

    })

    $('div#u1814').click(function(){
        $(function(){
            location.href = host + '/credit_query.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1818').click(function(){
        $(function(){
            location.href = host + '/favorite.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1820').click(function(){
        $(function(){
            location.href = host + '/cart_list.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1822').click(function(){
        $(function(){
            location.href = host + '/tradequery.html?' +
            "nickname=" + nickname;
        })
    })

    $("div#u1882").click(function(){
        if(window.confirm("是否退出登录？")){
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
                        location.href = host + "/login.html";
                    }else{
                        alert('Error');
                    }
                }
            })
        }else{
            return false;
        }

    });

    $("div#u1878").click(function(){
        $(function(){
                    location.href = host + "/user_edit.html?" +
                    "nickname=" + nickname;
                })
    })

    $("div#u1880").click(function(){
        $(function(){
                    location.href = host + "/address_daohang.html?" +
                    "nickname=" + nickname;
                })
    })

})