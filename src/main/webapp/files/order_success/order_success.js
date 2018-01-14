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
    var nickname = getParam('nickname');
    $('div#u1797 p span').text(nickname + ' |');
    $('div#u1797 p').attr('align', 'Right');

    var cost = getParam('cost');
    var text = '订单总金额: ￥' + cost;
    $('div#u1755 p span').text(text);

    var discount;
    var count = "1";
    var goodsId;
    var name;
    var price;
    var id;
    var dis_price;
    var discount2;
    var cost = 0;
    $.ajax({
        type: 'GET',
        url: host + '/user/discount',
        dataType: 'json',
        success: function(results){
            var result_code = results.resultCode;
            var result_info = results.resultInfo;
            if(result_code === 0){
                var content = results.content;
                discount = content.discount;
                localStorage.getItem('ordering');
                var content = JSON.parse(content);
                $.each(content, function(i, item){
                                    goodsId = item.goodsId;
                                    name = item.name;
                                    price = item.price;
                                    id = i+1;
                                    dis_price = price * discount;
                    
                                    cost += dis_price;
                                    discount2 = discount * 10 + "折";

                                    $("tbody").append('<tr><td align="center"><span>' 
                                     + id +'</span></td><td align="center"><a href="item_info.html?goodsId=' + goodsId
                                     + '">' + name + '</a></td><td align="center">' + price + '</td><td align="center">' 
                                     + discount2 + '</td><td align="center">' + dis_price + '</td><td align="center">' + count + '</td></tr>');
                            })

            }
        }
    })
    

    $('div#u1710').click(function(){
        $(function(){
            location.href = host + '/credit_query.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1714').click(function(){
        $(function(){
            location.href = host + '/favorite.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1716').click(function(){
        $(function(){
            location.href = host + '/cart_list.html?' +
            "nickname=" + nickname;
        })
    })

    $('div#u1718').click(function(){
        $(function(){
            location.href = host + '/tradequery.html?' +
            "nickname=" + nickname;
        })
    })

    $("div#u1803").click(function(){
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

    $("div#u1799").click(function(){
        $(function(){
                    location.href = host + "/user_edit.html?" +
                    "nickname=" + nickname;
                })
    })

    $("div#u1801").click(function(){
        $(function(){
                    location.href = host + "/address_daohang.html?" +
                    "nickname=" + nickname;
                })
    })

    $("div#u1795").click(function(){
        $(function(){
                    location.href = host + "/index.html?" +
                    "nickname=" + nickname;
                })
    })

})