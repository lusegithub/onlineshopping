if (sessionStorage.getItem("status") != 0) {
    location.href = "login.html";
}
var webRoot = '/onlineshopping';


//获取购物车数据.因为在之前的两个页面search_list与item_info中
//已经做过添加到后台购物车的操作
//全局变量购物车中总数和页数
var item_num;
var pages_num;
var psize = 3;//每页显示条数
$.ajax({
    url: webRoot + "/user/shoppingcart",
    type: 'GET',
    success: function (data) {
        //添加getdiscount的函数，在ajax中的回调函数中设置局部变量使用
        $.get(webRoot + '/user/discount', function (data) {
            /*optional stuff to do after success */
            var discount = data.content;
            sessionStorage.setItem("discount", discount);
        });
        if (data.resultCode == 0) {
            var li = "";
            $.each(data.content, function (index, val) {
                /* iterate through array or object */
                var discount = sessionStorage.getItem('discount');
                //获取购物车的总数，将其存储在session中以便全局获取该变量
                sessionStorage.setItem('item_num', parseInt(index) + 1);
                li =
                    "<tr class='show' goodsId='" + val.goodsId + "'>" +
                    "<td>" + (parseInt(index) + 1) + "</td>" +
                    "<td>" + val.name + "</td>" +
                    "<td>" + "<img src=" + val.photo + " width='140px' height='140px'>" + "</td>" +
                    "<td width='60px'>" + "￥" + val.price + "</td>" +
                    // "<td width='60px'>" + "￥" + parseInt(discount) * val.price + "</td>" +
                    // "<td>" +
                    // "<input id='min" + val.goodsId + "' disabled='true' type='button' value='-' >" +
                    // "<input id='text_box" + val.goodsId + "' readonly='readonly' type='text' value='1' style='width: 30px;text-align: center; '/>" +
                    // "<input max='" + val.stock + "'  id='add" + val.goodsId + "' type='button' value='+' >" +
                    // "</td>" +
                    "<td align='center' goodsId=" + val.goodsId + ">" +
                    "<input type='button' goodsId='" + val.goodsId + "' class='moveToCart' value='移入收藏夹'>" +
                    "<input type='button' goodsId='" + val.goodsId + "' class='remove' value='删除' >" +
                    "</td>" +
                    "</tr>";
                $('#hiddenresult').append(li);
                // $('#hiddenresult').append($("<tr class='show'></tr>")
                // 	.append("<td>"+(parseInt(index)+1)+"</td>"+
                //        "<td>"+val.name+"</td>"+
                //        "<td>"+"<img src="+val.photo+" width='140px' height='140px'>"+"</td>"+
                //        "<td width='60px'>"+"￥"+val.price+"</td>"+
                //        "<td width='60px'>"+"￥"+parseInt(discount)*val.price+"</td>"
                // 	)
                // 	.append($("<td></td>")
                // 		.append($("<input id='min"+val.goodsId+"' disabled='true' type='button' value='-' >")
                //            .attr('goodsId',val.goodsId)
                // 			.click(minFunction)
                // 		)
                // 		.append("<input id='text_box"+val.goodsId+"' type='text' value='1' style='width: 30px;text-align: center '/>")
                // 		.append($("<input  id='add"+val.goodsId+"' type='button'  value='+' >")
                //            .attr('goodsId',val.goodsId)
                // 			.click(addFunction)
                // 		)
                // 	)
                // 	.append($("<td></td>")
                // 		.append($("<input type='button' value='移入收藏夹' >")
                // 			.attr('goodsId',val.goodsId)
                //            .click(moveTofavorite)
                // 		)
                // 		.append($("<input type='button' value='删除' >")
                //            .attr('goodsId',val.goodsId)
                //            .click(moveTofavorite)
                //        )
                // 	)
                // );
            });

            //each之后可获得item的数量。进行总页数得获取.若不能整除则加一
            item_num = sessionStorage.getItem('item_num');
            if (item_num / psize > parseInt(item_num / psize)) {
                page_num = Math.ceil(item_num / psize);
            } else {
                page_num = item_num / psize;
            }

            var initPagination = function () {
                var num_entries = sessionStorage.getItem('item_num');
                // 创建分页
                $("#Pagination").pagination(num_entries, {
                    num_edge_entries: 1, //边缘页数
                    num_display_entries: 4, //主体页数
                    callback: pageselectCallback,
                    items_per_page: 3 //每页显示3项
                });
            }();

            function pageselectCallback(page_index, jq) {
                //商品总数量
                var length = sessionStorage.getItem('item_num');
                //获取将被添加的每页的最后一个商品的代号，以保证最后一页无论是否为每页所显示的书目，都不出现bug
                var max_elem = Math.min((page_index + 1) * psize, length);
                //每次点击页数时，清空之前页的内容。以假装翻页数据更新
                $("#Searchresult").html("");
                //循环向tobdy中添加当前页个数的商品
                for (var i = page_index * psize; i < max_elem; i++) {
                    var tr = $('#hiddenresult .show:eq(' + i + ')').clone();
                    tr.children().eq(5).children().eq(0).click(minFunction);
                    tr.children().eq(5).children().eq(2).click(addFunction);
                    tr.children().eq(4).children().eq(0).click(moveTofavorite);
                    tr.children().eq(4).children().eq(1).click(deleteCartListItem);

                    $("#Searchresult").append(tr);
                }
                return false;
            }
        } else if (data.resultCode == -1) {
            alert('购物车为空')
            history.back();
        } else if (data.resultCode == -2) {
            alert(data.resultInfo)
        }
    }
});

//删除购物车中数据行，并在后台数据中进行删除
function deleteCartListItem() {
    if (window.confirm("确定删除该商品吗？")) {
        //获取在input父标签中设置的属性goodid以便删除时获取到
        //在ajax中进行对页面中的数据删除避免前后端数据不统一
        var para = $(this).attr('goodsId');
        // alert(para)
        $.ajax({
            url: webRoot + '/user/shoppingcart',
            type: 'POST',
            data: {goodsId: para},
            success: function (data) {
                //判定前端显示的商品是否合法或有其他问题
                if (data.resultCode == 0) {
                    alert('成功删除');
                    window.location.reload();
                }
            }
        })
            .done(function () {
                console.log("success");
            })
            .fail(function () {
                console.log("error");
            })
            .always(function () {
                console.log("complete");
            });
    }
}
//添加商品至收藏夹并弹出框显示成功与否
function moveTofavorite() {
    var para = $(this).attr('goodsId');
    //将数据转换成约定好形式传向后台
    $.ajax({
        url: webRoot + '/user/favorite',
        type: 'PUT',
        dataType: 'json',
        data: {goodsId: para},
        success: function (data) {
            if (data.resultCode == -1) {
                alert("该商品不存在存货");
            } else if (data.resultCode == -2) {
                alert('该商品已在收藏夹内');
            } else {
                alert('已添加至收藏夹！');
            }
        }
    })
    .done(function () {
        console.log("success");
    })
    .fail(function () {
        console.log("error");
    })
    .always(function () {
        console.log("complete");
    });
}
function balance() {
    // var s = JSON.stringify(str);
    // $.ajax({
    //     url: webRoot + '/user/orders',
    //     type: 'POST',
    //     data: s,
    //     contentType: "application/json; charset=utf-8",
    //     success: function (data) {
    //         //进行跳转
    //         if (data.resultCode == 0) {
    //             location.href = webRoot + '/order_confirm.html';
    //             $(".remove").each(function (index, button) {
    //                 button.click();
    //             })
    //         } else {
    //             alert(data.resultInfo);
    //         }
    //     }
    // })
    location.href=webRoot+'/order_confirm.html';
}
function minFunction() {
    var t = $(this).next();
    t.val(Math.abs(parseInt(t.val())) - 1);
    if (parseInt(t.val()) == 1) {
        $(this).attr('disabled', true)
    }
}
function addFunction() {
    var t = $(this).prev();
    if (parseInt(t.val()) == $(this).attr("max")) {
        alert("不能超过库存量！");
    } else {
        t.val(Math.abs(parseInt(t.val())) + 1);
    }
    if (parseInt(t.val()) != 1) {
        $(this).prev().prev().attr('disabled', false)
    }
}