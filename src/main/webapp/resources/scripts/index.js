var webRoot = '/onlineshopping';
$(document).ready(function () {
    // $.ajaxSetup({
    //     async : false
    // });
    //添加left导航栏中所有的目录分类
    $.ajax({
        url: webRoot + '/catalog',
        type: 'GET',
        success: function (data) {
            if (data.resultCode == 0) {
                //先添加每个目录的外框，保证顺序
                $.each(data.content, function (index, catalog) {
                    $("#right").append(
                        $("<div></div>")
                            .attr("id",catalog.catalogName.replace("/", ""))
                            .css('width', '800px')
                            .css("height", "330px")
                            .append($("<div></div>")
                                .append(catalog.catalogName + " >> ")
                                .append($("<a>查看全部</a>").attr("href", "item_list.html?catalog="+catalog.catalogName))
                            )
                    );
                });
                $.each(data.content, function (index, val) {
                    //显示主题
                    //导航栏中每个目录的分类具有点击事件，该事件的具体效果待定
                    loadPage(val.catalogName);
                    loadLeft(val.catalogName);
                    // $("#ul").children().children().eq(index).click(function () {

                        // $("#right").empty();//先清空right中内容，在添加。right中是div块通过直接append里添加选择块
                        // $.ajax({
                        // 	//获取catalogName并获取该种分类商品的总信息（包括所有商品的goodsid）
                        // 	url: webRoot+'/catalog/'+encodeURIComponent(encodeURIComponent(val.catalogName))+'/goods/page-1',
                        // 	type: 'GET',
                        // 	success:function(data){
                        // 		if(data.resultCode==0) {
                        // 			var cataTitle = '<div>' + val.catalogName + '</div>';
                        // 			catalogName = val.catalogName.replace("/", "")
                        // 			$("#right").append($("<div></div>")
                        // 				.attr('id', catalogName)
                        // 				.css('width', '800px')
                        // 				.css('height', '320px'));
                        // 			$('#' + catalogName).append(cataTitle);
                        //
                        // 			var dd = '';
                        // 			/* iterate through array or object */
                        // 			$.each(data.content.goodsIds, function (index, val) {
                        // 				/* iterate through array or object */
                        // 				//根据各个商品的id信息进行get，获取商品的所有信息
                        // 				$.get(webRoot + '/goods/' + val + '/info', function (data) {
                        // 					/*optional stuff to do after success */
                        // 					if(data.resultCode==0) {
                        //                        var resultCodeValue = val.resultCode;
                        //                        dd = '<dl style="float:left;align-content: center">' +
                        //                            '<dt>' + "<img src='" + data.content.photo + "' onclick='getClickGoodsIds(" + val + "," + resultCodeValue + ")'>" + '</dt>' +
                        //                            '<dd>' + '￥' + '<span>' + data.content.price + '</span>' + '</dd>' +
                        //                            "<dd  goodsIds='" + data.content.goodsId + "'>" +
                        //                            //url传参，传递goodsid
                        //                            "<input type='button' value='加入购物车' onclick='moveToCartList(" + val + ")'>" +
                        //                            '</dd>' +
                        //                            '</dl>';
                        //                        $('#' + catalogName).append(dd);
                        //                    }
                        // 				})
                        // 			});
                        // 		}
                        // 	}
                        // })
                    // });
                });
            } else {
                alert(data.resultInfo);
            }
        }
    })
        .done(function () {
            console.log("left success");
        })
        .fail(function () {
            console.log("left error");
        })
        .always(function () {
            console.log("left complete");
        });
});
//执行一种分类的的load函数，添加四个商品
function loadPage(catalogName) {
    $.ajax({
        url: webRoot + '/catalog/' + encodeURIComponent(encodeURIComponent(catalogName)) + '/goods/page-1',
        type: 'GET',
        success: function (data) {
            if (data.resultCode === 0) {
                //在右边主框中添加标题栏显示分类名称

                var dd = '';
                /* iterate through array or object */
                $.each(data.content.goodsIds, function (index, goodsId) {
                    if (index >= 4)
                        return false;
                    $("#" + catalogName.replace("/", "")).append($("<dl style='float:left;'></dl>").attr("id", goodsId.replace(".","。")));
                });
                $.each(data.content.goodsIds, function (index, goodsId) {
                    if (index >= 4)
                        return false;
                    $.get(webRoot + '/goods/' + goodsId + '/info', function (result) {
                        /*optional stuff to do after success */
                        var resultCodeValue = result.resultCode;
                        dd =
                            '<dt>' + "<img id='img" + goodsId + "' src='" + result.content.photo + "'style='cursor: pointer' onclick='getClickGoodsIds(" + goodsId + "," + resultCodeValue + ")'>" + '</dt>' +
                            '<dd>' + '￥' + '<span>' + result.content.price + '</span>' + '</dd>' +
                            '<dd style="width: 120px; white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">'+result.content.name+'</dd>'+
                            "<dd align='center' goodsIds='" + goodsId + "'>" +
                            "<input type='button' value='加入购物车' onclick='moveToCartList(" + goodsId + ")'>" +
                            '</dd>';
                        $("#" + goodsId.replace(".", "。")).append(dd);
                    })
                });
            }else{
                $("#" + catalogName.replace("/", "")).css("display", "none");
                $("#left" + catalogName.replace("/", "")).css("display", "none");
            }
        }
    })
        .done(function () {
            console.log("loadPage success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("loadPage complete");
        });

}
function loadLeft(catalogName) {
    $("#ul").append($("<li></li>").attr("id", "left"+catalogName.replace("/", ""))
        .append($("<a></a>")
            .attr("href", "#"+catalogName.replace("/", ""))
            .append(catalogName)
        )
    );
}
//添加进购物车的函数
function moveToCartList(para) {
    if (sessionStorage.getItem("status") != 0) {
        if (window.confirm("还未登录，是否现在登录？")) {
            sessionStorage.setItem('ISFROMCLICK',0);
            location.href = "login.html";
        }else{
            return false
        }
    }
    $.ajax({
        url: webRoot + '/user/shoppingcart',
        type: 'PUT',
        data: {goodsId: para},
        success: function (data) {
            if(data.resultCode == 0) {
                alert("已成功添加到购物车");
            }else{
                alert(data.content);
            }
        }
    });
}
//各个图片的点击跳转事件，需传参数goodsid
function getClickGoodsIds(para, resultCodeValue) {
    location.href = "/onlineshopping/item_info.html?id=" + para + "&&resultCode=" + resultCodeValue;//
}
