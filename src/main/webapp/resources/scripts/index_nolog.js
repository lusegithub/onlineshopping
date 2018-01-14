//首页的购物车添加栏没有实现
function getClickGoodsIds(id){
	location.href="http://localhost:81/onlineShopping/item_search_list?id="+id;//
}

$(document).ready(function() {
		/* Act on the event */
	$.ajax({
		url: '/catalog/小说/goods/page-1',//小说
		type: 'GET',
		success:function(data){
			var json=$.parseJSON(data);

			//获取单个商品的goodsids信息并添加src属性与click事件
			for(var i=0;i<8;i++){
				$.get('/goods/'+json.content.goodsIds[i]+'/info', function(data) {
					/*optional stuff to do after success */

					var perjson=$.parseJSON(data);

					var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
					$('div div div div img').eq(i).attr({
						'src': perjson.content.photo,
						'id': id
					});
					$('div div div div img').eq(i).click(function(event) {
						getClickGoodsIds(id);
					});
				});
			}
		}
	})
	$.ajax({
		url: '/catalog/历史/goods/page-1',
		type: 'default GET (Other values: POST)',
		success:function(){
			var json=$.parseJSON(data);

			//获取单个商品的goodsids信息并添加src属性与click事件
			for(var i=8;i<14;i++){
				$.get('/goods/'+json.content.goodsIds[parseInt(i)-8]+'/info', function(data) {
					/*optional stuff to do after success */
					var perjson=$.parseJSON(data);

					var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
					$('div div div div img').eq(i).attr({
						'src': perjson.content.photo,
						'id': id
					});
					$('div div div div img').eq(i).click(function(event) {
						getClickGoodsIds(id);
					});
				});
			}
			//特殊的一个商品展示框
			$.get('/goods/'+json.content.goodsIds[8]+'/info', function(data) {
				/*optional stuff to do after success */
				var perjson=$.parseJSON(data);

				var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
				$('div div div div img').eq(33).attr({
					'src': perjson.content.photo,
					'id': id
				});

				$('div div div div img').eq(33).click(function(event) {
					getClickGoodsIds(id);
				});
			});
		}
	})

	$.ajax({
		url: '/catalog/wenxue/goods/page-1',//文学
		type: 'default GET (Other values: POST)',
		success:function(){
			var json=$.parseJSON(data);

			//获取单个商品的goodsids信息并添加src属性与click事件
			for(var i=14;i<22;i++){
				$.get('/goods/'+json.content.goodsIds[parseInt(i)-14]+'/info', function(data) {
					/*optional stuff to do after success */
					var perjson=$.parseJSON(data);

					var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
					$('div div div div img').eq(i).attr({
						'src': perjson.content.photo,
						'id': id
					});
					$('div div div div img').eq(i).click(function(event) {
						getClickGoodsIds(id);
					});
				});
			}
		}
	})
	
	$.ajax({
		url: '/catalog/zhaunji/goods/page-1',
		type: 'default GET (Other values: POST)',
		success:function(){
			var json=$.parseJSON(data);

			//获取单个商品的goodsids信息并添加src属性与click事件
			for(var i=22;i<30;i++){
				$.get('/goods/'+json.content.goodsIds[parseInt(i)-22]+'/info', function(data) {
					/*optional stuff to do after success */
					var perjson=$.parseJSON(data);

					var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
					$('div div div div img').eq(i).attr({
						'src': perjson.content.photo,
						'id': id
					});
					$('div div div div img').eq(i).click(function(event) {
						getClickGoodsIds(id);
					});
				});
			}
		}
	})
	
	$.ajax({
		url: '/catalog/xinlixue/goods/page-1',
		type: 'default GET (Other values: POST)',
		success:function(){
			for(var i=30;i<33;i++){
				$.get('/goods/'+json.content.goodsIds[parseInt(i)-30]+'/info', function(data) {
					/*optional stuff to do after success */
					var perjson=$.parseJSON(data);

					var id=perjson.content.goodsIds;
					//将图片的源和id存储在img的属性中，方便获取id
					$('div div div div img').eq(i).attr({
						'src': perjson.content.photo,
						'id': id
					});
					$('div div div div img').eq(i).click(function(event) {
						getClickGoodsIds(id);
					});
				});
			}
		}
	})

});