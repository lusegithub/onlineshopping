$(document).ready(function() {
//判定是否已经登录，来决定所显示的游客或用户信息
var getToken=sessionStorage.getItem('token');
if(getToken==1){
	$.ajax({
	url: '/user/info',
	type: 'GET',
	success:function(data){
		// var token=1;
		//在本地前端设置一个token令牌，通过查取token是否存在与值来判定是否在登陆。
		// sessionStorage.setItem("token",token);
		var json=$.parseJSON(data);
		$('#u1401 p span').html(data.content.nickname+"  |");
		//若是用户登录则四个页面可以跳转。添加跳转事件
		$('#u1358 p span').click(function(event){
          /* Act on the event */
          location.href='http://localhost:81/onlineShopping/credit_query.html';
        });
        $('#u1362 p span').click(function(event){
          /* Act on the event */
          location.href='http://localhost:81/onlineShopping/favorite.html';
        });
        $('#u1364 p span').click(function(event){
          location.href='http://localhost:81/onlineShopping/cart_list.html';
        });
        $('#u1366 p span').click(function(event){
          location.href='http://localhost:81/onlineShopping/tradequery.html';
        });
	}
})
.done(function() {
	console.log("success");
})
.fail(function() {
	console.log("error");
	//若获取失败则设置token为0，表示未登录
	// sessionStorage.setItem('token',0);
})
.always(function() {
	console.log("complete");
});
}else{
	//设置表头的信息。若是游客身份，则不填加四个跳转事件
	//同时设置表头
	$("#u1399 p span").html();
	$("#u1401 p span").html();
	$("#u1403 p span").html();
	$("#u1405 p span").html('【登录】 |');
	$("#u1407 p span").html('【注册】');
}

//获取购物车数据.因为在之前的两个页面search_list与item_info中
//已经做过添加到后台购物车的操作
		//全局变量购物车中总数和页数
	var item_num;
	var pages_num;
	var	psize=3;//每页显示条数
	$.ajax({
		url: "http://localhost:81/jqueryTest/favorite.txt",
		type: 'GET',
		success:function(data){
		/* iterate through array or object */
		var discount;
		//添加getdiscount的函数，在ajax中的回调函数中设置局部变量使用
		$.get('/path/to/file', function(data) {
			/*optional stuff to do after success */
		});
			var json=JSON.parse(data);
			if(json.resultCode!=-1&&json.resultCode!=-2){
				var li="";
				$.each(json.content, function(index, val) {
			    /* iterate through array or object */
				//获取购物车的总数，将其存储在session中以便全局获取该变量
					sessionStorage.setItem('item_num',parseInt(index)+1);
					li+=
					"<tr class='show'>"+
					"<td>"+(parseInt(index)+1)+"</td>"+
					"<td>"+val.name+"</td>"+
					"<td>"+"<img src="+val.photo+" width=50px height(70px)>"+"</td>"+
					"<td>"+val.price+"</td>"+
					"<td>"+val.price+"</td>"+
				 	"<td align='center' goodsId="+val.goodsId+">"+
				 		"<input type='button' id='moveToCart' value='移入收藏夹' onclick='moveTofavorite(this)'>"+
				 		"<input type='button' id='remove' value='删除' onclick='deleteCartListItem(this)'>"+
				 	"</td>"+
				 	"</tr>";
				 	 // sessionStorage.setItem('favorite'+index+'',li);
				});
				$('#hiddenresult').html(li);
				//each之后可获得item的数量。进行总页数得获取.若不能整除则加一
				item_num=sessionStorage.getItem('item_num');
				if(item_num/psize>parseInt(item_num/psize)){
				 	page_num=Math.ceil(item_num/psize);
				}else{
				 	page_num=item_num/psize;
				}
			}else if(json.resultCode==-1){
				alert('商品不存在')
			}else if(json.resultCode==-2){
				alert('商品已添加')
			}
		}
	});
		//画每页的表格.
	$(function(){
	//回调函数的作用是显示对应分页的列表项内容
	//回调函数在用户每次点击分页链接的时候执行
	//参数page_index{int整型}表示当前的索引页

		var initPagination = function() {
		var num_entries =sessionStorage.getItem('item_num');
		// 创建分页
		$("#Pagination").pagination(num_entries, {
			num_edge_entries: 1, //边缘页数
			num_display_entries: 4, //主体页数
			callback: pageselectCallback,
			items_per_page:3 //每页显示3项
		});
	 	}();
	 
		function pageselectCallback(page_index, jq){
			//商品总数量
			var length=sessionStorage.getItem('item_num');
			//获取将被添加的每页的最后一个商品的代号，以保证最后一页无论是否为每页所显示的书目，都不出现bug
			var max_elem=Math.min((page_index+1)*psize,length);
			//每次点击页数时，清空之前页的内容。以假装翻页数据更新
			$("#Searchresult").html("");
			//循环向tobdy中添加当前页个数的商品
			for(var i=page_index*psize;i<max_elem;i++){
				$("#Searchresult").append($('#hiddenresult .show:eq('+i+')').clone()); //装载对应分页的内容
			}
			return false;
		}
	});
	//删除购物车中数据行，并在后台数据中进行删除
	function delelteCartListItem(btn){
		//获取在input父标签中设置的属性goodid以便删除时获取到
		var getGoodsId=$('btn').parent().attr('goodsId');
		var goodsIdArr={
			goodsId:[getGoodsId]
		}
		var str=JSON.stringify(goodsIdArr);
		//在ajax中进行对页面中的数据删除避免前后端数据不统一
		$.ajax({
			url: '/user/shoppingcart',
			type: 'DELETE',
			dataType: 'json',
			data: str,
			success:function(data){
				//判定前端显示的商品是否合法或有其他问题
				if(data.resultCode!=-1){
					$(btn).parent().parent().remove();
				}
			}
		})
		.done(function() {
			console.log("success");
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});	
	}
	//添加商品至收藏夹并弹出框显示成功与否
	function moveTofavorite(btn){
		var getGoodsId=$(btn).parent().attr('goodsId');
		var goodsIdArr={
			goodsId:[getGoodsId]
		}
		//将数据转换成约定好形式传向后台
		var str=JSON.stringify(goodsIdArr);
		$.ajax({
			url: '/user/favorite',
			type: 'PUT',
			dataType: 'json',
			data: str,
			success:function(data){
				if(data.resultCode=-1){
					alert("该商品不存在存货");
				}else if(data.resultCode=-2){
					alert('该商品已在收藏夹内');
				}else{
					alert('已添加至收藏夹！');
				}
			}
		})
		.done(function() {
			console.log("success");
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
	}
	function balance(){
		$.ajax({
			url: '/user/shoppingcart',
			type: 'GET',
			success:function(data){
				var json=JSON.parse(data);
				var arrayOfGoodsid=new Array();
				$.each(json.content, function(index, val) {
					 /* iterate through array or object */
                    arrayOfGoodsid.push(val.goodsId);
				});
				var cartListGoodsId={
					goodsId:arrayOfGoodsid
				}
				//将goodsid以json对象的形式存入本地，以便获取
				localStorage.setItem('cartListGoodsId',cartListGoodsId);
				//进行跳转
				location.href='order_confirm.html';
			}
		})
		.done(function() {
			console.log("success");
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
	}
});