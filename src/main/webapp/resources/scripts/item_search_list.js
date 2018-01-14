var webRoot='/onlineshopping';
var searchStr;
var searchObj;
var page = 1;
var init_pagination = true;
if (sessionStorage.getItem('searchDatas') != null) {
    searchStr = sessionStorage.getItem('searchDatas');//按照四个关键字搜索
    searchObj=JSON.parse(searchStr);
}
$(document).ready(function() {
    if (sessionStorage.getItem('searchDatas') != null) {
        getData(true)
    }
});
    function getData(){
        // $.ajaxSetup({
        //     async : false
        // });
        $("#table1").empty();
        $.ajax({
            url: webRoot + "/special-goods/page-"+page,
            type: 'GET',
            data: {catalog_name:searchObj.catalog_name,small_price:searchObj.small_price,large_price:searchObj.large_price,keyword:searchObj.keyword},//可能需要更改格式
            success: function (data) {
                if(data.resultCode==0){
                    if (init_pagination) {
                        $("#pagination").pagination(data.content.totalGoodsNum, {
                            current_page: page - 1,
                            num_edge_entries: 2,
                            num_display_entries: 4, //主体页数
                            prev_text: "上一页",
                            next_text: "下一页",
                            items_per_page: 10,
                            callback: function (page_index) {
                                if(!init_pagination) {
                                    page = page_index + 1;
                                    getData(false);
                                }
                            }
                        });
                        init_pagination = false;
                    }
                    var goods_num = data.content.goodsNum;
                    var pages_num =page;
                        // data.content.pageNum;//当前页数
                    var total_pages_num = data.content.totalPagesNum;//获取每页的商品数和总页数

                    $.each(data.content.goodsIds, function (index) {
                        $('#table1').append($("<tr></tr>").attr("id", index));
                    })
                    $.each(data.content.goodsIds, function (index, val) {
                        /* iterate through array or object */

                        //根据goodsid来查看商品信息
                        $.get(webRoot + "/goods/" + val + "/info", function (data) {
                            $('#' + index)
                                .append("<td>" + (parseInt(index) + 1) + "</td>")
                                .append($("<td></td>")
                                    .append($("<a>"+data.content.name+"</a>")
                                        .attr("goodsid",val)
                                        .attr("resultValue",0)
                                        .click(getClickGoodsIds)
                                        .mouseenter(whenMouseOver)
                                        .mouseleave(whenMouseLeave)
                                    )
                                )
                                .append("<td>" + "<img width='100px' hight='100px' src=" + data.content.photo + ">" + "</td>" +
                                    "<td>" + data.content.price + "</td>")
                                .append($("<td align='center' goodsId='val.goodsId'></td>")
                                    .append($("<input type='button' value='加入购物车'>")
                                        .click(moveToCartList)
                                        .attr("goodsId", val)
                                    )
                                    .append("<br/>")
                                    .append($("<input type='button' value='加入收藏夹' >")
                                        .click(moveToFavorite)
                                        .attr("goodsId", val)
                                    )
                                )
                        });
                    });

                    var lu = '';
                    if (total_pages_num > 1) {
                        /*optional stuff to do after success */
                        lu = "<ul id='agePre'><li style='display: inline'><a onclick='preEvent()' style='cursor: pointer' id='pre' data-num='1'>上一页</a></li>";
                    } else {
                        lu = "<ul id='agePre'>";
                    }
                    for (var i = 0; i < total_pages_num; i++) {
                        lu += "<li style='display: inline'><a  onclick='getData("+(i + 1)+")' style='cursor: pointer' type='num'>"+(parseInt(i) + 1)+  "</a></li>"
                    }
                    //判定li中是否有上一页按钮
                    if (lu.indexOf('上一页') != -1) {
                        lu += "<li style='display: inline'><a onclick='nextEvent()' id='next' style='cursor: pointer' data-num='1'>下一页</a></li>" + "<listyle='display: inline'>   共<li id='pagesNum'style='display: inline'>"
                             + total_pages_num+ "</li>  页</li></ul>"
                    } else {
                        lu += "<span style='display: inline'>共<span id='pagesNum' style='display: inline'>" + total_pages_num + "</span>页</span></ul>";
                    }

                    $('#page').append(lu);

                    $('#pre').attr('data-num', pages_num);//设定页数以此
                    $('#next').attr('data-num', pages_num);
                }
                else{
                    alert('搜索的商品不存在');
                    location.href= webRoot+'/item_search.html';
                }
            }
        });
    }
    function preEvent() {
        var cur_page = $('#pre').attr('data-num');
        if (cur_page <= 1) {
            $(this).attr('disable', 'true');
        } else {
            getData(parseInt(cur_page) - 1);
        }
    }

    function nextEvent() {
        var cur_page = $('#next').attr('data-num');
        var pages_num = $('#pagesNum').html();
        if (cur_page < pages_num) {
            getData(parseInt(cur_page) + 1);
        } else {
            $(this).attr('disable', 'true');
        }
    }

    function moveToCartList() {
        //将数据转换成约定好形式传向后台
        if(sessionStorage.getItem("status")==0){
            var para=$(this).attr('goodsId');
            $.ajax({
                url: '/onlineshopping/user/shoppingcart',
                type: 'PUT',
                dataType: 'json',
                data: {goodsId:para},
                success: function (data) {
                    if (data.resultCode == -1) {
                        alert("该商品不存在存货");
                    } else if (data.resultCode == -2) {
                        alert('购物车中已添加过该类商品');
                    } else {
                        alert('成功添加至购物车！');
                    }
                }
            })
            .done(function () {
                console.log("request success");
            })
            .fail(function () {
                console.log("request error");
            })
            .always(function () {
                console.log("request complete");
            });
        }else{
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        }
    }

    //添加进入收藏夹
    function moveToFavorite() {
        //将数据转换成约定好形式传向后台
        if(sessionStorage.getItem("status"==0)){
            var para=$(this).attr('goodsId');
            $.ajax({
                url:  '/onlineshopping/user/favorite',
                type: 'PUT',
                dataType: 'json',
                data: {goodsId:para},
                success: function (data) {
                    if (data.resultCode == -1) {
                        alert("该商品不存在存货！！");
                    } else if (data.resultCode == -2) {
                        alert('。。收藏夹中已存在该商品！');
                    } else {
                        alert('成功添加至收藏夹！');
                    }
                }
            })
            .done(function () {
                console.log("moveToFavorite success");
            })
            .fail(function () {
                console.log("moveToFavorite error");
            })
            .always(function () {
                console.log("moveToFavorite complete");
            });
        }else{
            if(window.confirm("未登录，是否现在登录？")){
                location.href = "login.html";
            }
        }
    }
    function getClickGoodsIds() {
        location.href = "/onlineshopping/item_info.html?id=" + $(this).attr("goodsid") + "&&resultCode=" + 0;//
    }
    function whenMouseOver(){
        $(this).css("cursor","pointer");
        $(this).css("color","red");
    }
    function whenMouseLeave(){
        $(this).css("color","black");
    }
