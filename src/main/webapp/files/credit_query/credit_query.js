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
		$('#u779 p span').html(data.content.nickname+"  |");
		//若是用户登录则四个页面可以跳转。添加跳转事件
		$('#u687 p span').click(function(event){
          /* Act on the event */
          location.href='http://localhost:81/onlineShopping/credit_query.html';
        });
        $('#u691 p span').click(function(event){
          /* Act on the event */
          location.href='http://localhost:81/onlineShopping/favorite.html';
        });
        $('#u693 p span').click(function(event){
          location.href='http://localhost:81/onlineShopping/cart_list.html';
        });
        $('#u695 p span').click(function(event){
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
	$("#u777 p span").html();
	$("#u779 p span").html();
	$("#u781 p span").html();
	$("#u783 p span").html('【登录】 |');
	$("#u785 p span").html('【注册】');
}

	//获取积分数据.json中按照数组显示的订单信息。可一次性获取订单数量并在前端分页显示
	//全局变量订单总数和页数
	var item_num;
	var pages_num;
	var	psize=3;//每页显示条数
	$.ajax({
		url: "/user/points",
		type: 'GET',
		success:function(data){
		/* iterate through array or object */
		var discount;
		//添加getdiscount的函数，在ajax中的回调函数中设置局部变量使用
		$.get('/user/discount', function(data) {
			/*optional stuff to do after success */
			discount=data.content.discount;
		});
			var json=JSON.parse(data);
			//判定操作是否合法以及是否成功
			if(json.resultCode==0){
				var li="";
				$.each(json.content.pointsItem, function(index, val) {
			    /* iterate through array or object */
				//获取订单的总数，将其存储在session中以便全局获取该变量
					sessionStorage.setItem('item_num',parseInt(index)+1);
					li+=
					"<tr class='show'>"+
					"<td>"+"<a href='javascript:void(0);' onclick='turnToOrderInfo(this)'>"+val.order_id+"</a>"+"</td>"+
					"<td>"+val.orderTime+"</td>"+
					"<td>"+val.price+"</td>"+
					"<td>"+val.points+"</td>"+
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
			//订单总数
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
	//点击order表单，为order_info页面进行url传参。通过info页面的get方法进入该订单的详细信息
	function turnToOrderInfo(node){
		var order_id=$(node).html();
		location.href='http://localhost:81/onlineShopping/order_info.html?'+order_id+'';
	}
});