if(sessionStorage.getItem("status") != 0){
    location.href = "login.html";
}
$(document).ready(function() {
	var webRoot='/onlineshopping'
	//判定是否已经登录，来决定所显示的游客或用户信息
	//获取当前url并对可能的参数进行存储，使用时要判定是否为空。避免通过ip直接进行的访问
	var url=location.search;
	var order_id;
	//获取连同？在内的之后的参数
	if(url.indexOf('?')!=-1){
		order_id=url.substr(10);
	}

		$.ajax({
			url: webRoot+'/user/info',
			type: 'GET',
			success:function(data){
				// var token=1;
				//在本地前端设置一个token令牌，通过查取token是否存在与值来判定是否在登陆。
				$('#u1797 p span').empty();
				$('#u1797 p span').html(data.content.nickname);

				$('#u1799 p span').css('cursor','pointer').click(function(){
					location.href=webRoot+'/user_edit.html'
				});
				$('#u1801 p span').css('cursor','pointer').click(function(){
					location.href=webRoot+'/address_daohang.html'
				});
				$('#u1803 p span').css('cursor','pointer').click(function(){
					sessionStorage.setItem('status',-1);
					location.href=webRoot+'/index.html'
				});

				//若是用户登录则四个页面可以跳转。添加跳转事件
				$('#u1710 p span').css('cursor','pointer').click(function(event){
				  /* Act on the event */
					  location.href='/onlineshopping/credit_query.html';
				});
				$('#u1714 p span').css('cursor','pointer').click(function(event){
				  /* Act on the event */
				  location.href='/onlineshopping/favorite.html';
				});
				$('#u1716 p span').css('cursor','pointer').click(function(event){
				  location.href='/onlineshopping/cart_list.html';
				});
				$('#u1718 p span').css('cursor','pointer').click(function(event){
				  location.href='/onlineshopping/tradequery.html';
				});
			}
		});
        $.ajax({
            url: webRoot+'/user/order/'+order_id,
            type: 'GET',
            success:function(data){
            	if(data.resultCode==0){
                    $('#orderId').html(data.content.orderId);
                    $('#orderTime').html(data.content.orderTime);
                    $('#discount').html(data.content.discount);
                    $('#price').html(data.content.price);
                    $('#pay').html('货到付款');
                    $('#status').html(data.content.status);
                    $('#name').html(data.content.consignee);
                    $('#address').html(data.content.address);
                    $("#phone").html(data.content.phone);
				}else
					alert(data.resultInfo)
            }
        })
		.done(function() {
			console.log("order load by id success");
		})
		.fail(function() {
			console.log("order load by id error");
		})
		.always(function() {
			console.log("complete");
		});

});