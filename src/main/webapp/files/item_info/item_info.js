var url=location.search;
var str=url.substr(1);
var arr=str.split('&&');
var num=arr[0].indexOf('=');
//获取第一个参数
var goodsIds=arr[0].substr(0,num);
var goodsIdsValue=arr[0].substr(num+1);
//获取第二个参数
num=arr[1].indexOf('=');
var resultCode=arr[1].substr(0,num);
var resultCodeValue=arr[1].substr(num+1);

if(resultCodeValue!=-1){
	//点击某商品图片时进入详细信息,传输一个带有goodsid的参数
	
	
	$.ajax({
		url: '/goods/'+goodsIdsValue+'/info',
		type: 'GET',
		success:function(data){
			var jsonData=$.parseJSON('data');

			$('#u1223').html(jsonData.content.name);
			$('#u1224_img').attr('src'：jsonData.content.photo,
				'id':jsonData.content.goodsIds);
			$('#u1227 p span').html(jsonData.content.describe);
			$('#u1233 p span').html('市场价：'+jsonData.content.price);
			$('#u1235 p span').html('库存量：'+jsonData.content.stock);
			
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
	//为评论添加事件跳转到comment_list页面，并传递goodsids参数
	$('#1241 p span').click(function(event) {
		/* Act on the event */
	    location.href='http://localhost:81/onlineShopping/commnet_list.html?'+name+'='+value;
	});
	//为添加到购物车添加事件跳转到cart_list页面，并传递goodsids参数。同时向服务器put添加的商品id
	$('#u1229 p span').click(function(event) {
          /* Act on the event */
          $.ajax({
          	url: '/user/shoppingcart',
          	type: 'PUT',
          	data: {goodsIds: goodsIdsValue},
          	success:function(){
          		//此时有两个参数随页面传输，id和resultCode,根据接口文档resultCode可以判断是否已有
        		location.href='http://localhost:81/onlineShopping/cart_list.html?'+name+'='+value'&&resultCode'=data.resultCode;
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
          
    });
    //添加收藏按钮，向服务器中put到收藏夹。再根据url的传参，在收藏夹界面显示
    $("#u1231").click(function(event) {
    	/* Act on the event */
    	$.ajax({
    		url: '/user/favorite',
    		type: 'PUT',
    		data: {goodsIds: goodsIdsValue},
    		success:function(data){
    			//data.resultCode判定商品是否收藏过
    			alert()
    			location.href='http://localhost:81/onlineShopping/favorite.html?'+name+'='+value'&&resultCode'=data.resultCode;
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
    	
    	
    });