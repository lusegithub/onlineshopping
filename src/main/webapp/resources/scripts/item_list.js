var webRoot = "/onlineshopping";

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) {
        return decodeURI(r[2]);
    }
    return null; //返回参数值
}

var init_pagination = true;
$(function () {
    var catalog = getUrlParam("catalog");
    $("#catalog").html(catalog);
    var empty = $("#empty");
    var show = $("#i2");
    var goodsList = $("#goodsList");
    var page = 1;
    getGoods(init_pagination);

    function getGoods(init_pagination) {
        goodsList.empty();
        $.get(webRoot + '/catalog/' + encodeURIComponent(encodeURIComponent(catalog)) + '/goods/page-' + page, function (response) {
            if (response.resultCode === 0) {
                empty.css("display", "none");
                show.css("display", "block");
                createGoodsList(response.content.goodsIds);
                if (init_pagination) {
                    $("#pagination").pagination(response.content.totalGoodsNum, {
                        current_page: page - 1,
                        num_edge_entries: 2,
                        num_display_entries: 4, //主体页数
                        prev_text: "上一页",
                        next_text: "下一页",
                        items_per_page: 10,
                        callback: function (page_index) {
                            if(!init_pagination) {
                                page = page_index + 1;
                                getGoods(false);
                            }
                        }
                    });
                    init_pagination = false;
                }
            } else if (response.resultCode === -3) {
                empty.empty();
                empty.append("目录\"" + catalog + "\"为空！");
                empty.css("display", "block");
                show.css("display", "none");
            } else {
                alert(response.resultInfo);
                history.back();
            }
        }).error(function () {
            alert("请求失败！");
            history.back();
        })
    }

    function createGoodsList(goodsIds) {
        goodsList.empty();
        $.each(goodsIds, function (index) {
            goodsList.append($("<tr></tr>").attr("id", index));
        });
        $.each(goodsIds, function (index, goodsId) {
            $.get(webRoot + "/goods/" + goodsId + "/info", function (response) {
                if (response.resultCode === 0) {
                    $("#" + index)
                        .append($("<td></td>").append(index + 1))
                        .append($("<td></td>").append(response.content.name))
                        .append($("<td></td>").append($("<img width='100px' hight='100px'>").attr("src", response.content.photo)))
                        .append($("<td></td>").append(response.content.price))
                        .append($("<td></td>").append(response.content.stock))
                        .append($("<td align='center'></td>")
                            .append($("<input type='button' value='加入购物车'>")
                                .click(moveToCartList)
                                .attr("goodsId", goodsId)
                            )
                            .append("<br/>")
                            .append($("<input type='button' value='加入收藏夹' >")
                                .click(moveToFavorite)
                                .attr("goodsId", goodsId)
                            )
                        );
                } else {
                    alert(response.resultInfo);
                }
            });
        });
    }

    //编辑
    function editGoods() {
        var goodsId = $(this).parent().attr("id");
        location.href = "goods_edit.html?catalog=" + catalog + "&goodsId=" + goodsId;
    }

    //删除
    function deleteGoods() {
        var goodsId = $(this).parent().attr("id");
        if (window.confirm("确认删除商品" + goodsId + "吗？"))
            $.ajax({
                url: webRoot + "/catalog/" + encodeURIComponent(encodeURIComponent(catalog)) + "/goods/" + goodsId,
                type: "DELETE"
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0)
                    getGoods();
            }).error(function () {
                alert("删除商品失败！");
            })
    }

    var isbn = $("#ISBN");
    var goods_name = $("#goods_name");
    var photo = $("#photo");
    var describe = $("#describe");
    var price = $("#price");
    var stock = $("#stock");

    function moveToCartList() {
        if(sessionStorage.getItem("status") != 0){
            if(window.confirm("登陆后才能添加到购物车！\n是否现在登录？")){
                location.href = "login.html";
            }
        }
        //将数据转换成约定好形式传向后台
        var para = $(this).attr('goodsId');
        $.ajax({
            url: webRoot + '/user/shoppingcart',
            type: 'PUT',
            dataType: 'json',
            data: {goodsId: para},
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
    }

    //添加进入收藏夹
    function moveToFavorite() {
        if(sessionStorage.getItem("status") != 0){
            if(window.confirm("登陆后才能添加到收藏夹！\n是否现在登录？")){
                location.href = "login.html";
            }
        }
        //将数据转换成约定好形式传向后台
        var para = $(this).attr('goodsId');
        $.ajax({
            url: webRoot + '/user/favorite',
            type: 'PUT',
            dataType: 'json',
            data: {goodsId: para},
            success: function (data) {
                if (data.resultCode == -1) {
                    alert("该商品不存在存货！！");
                } else if (data.resultCode == -2) {
                    alert('收藏夹中已存在该商品！');
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
    }
});

