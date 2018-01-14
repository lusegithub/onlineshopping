if(sessionStorage.getItem("status") != 0){
    location.href = "login.html";
}
var webRoot='/onlineshopping'
$(document).ready(function() {
//获取收藏夹数据.因为在之前的两个页面search_list与item_info中
//已经做过添加到后台收藏夹的操作
		//全局变量收藏总数和页数
	var item_num;
	var pages_num;
	var	psize=5;//每页显示条数
	$.ajax({
		url: webRoot+"/user/favorite",
		type: 'GET',
		success:function(data){
		//添加getdiscount的函数，在ajax中的回调函数中设置局部变量使用
			$.get(webRoot+'/user/discount', function(data) {
				/*optional stuff to do after success */
                var discount=data.content;
                sessionStorage.setItem('discount',parseInt(discount));
            });
			if(data.resultCode==0){
				$.each(data.content, function(index, val) {
				/* iterate through array or object */
				//获取收藏夹的总数，将其存储在session中以便全局获取该变量
					sessionStorage.setItem('item_num',parseInt(index)+1);
					var discount=sessionStorage.getItem('discount');
					var li=
					"<tr class='show'>"+
					"<td>"+(parseInt(index)+1)+"</td>"+
					"<td>"+val.name+"</td>"+
					"<td>"+"<img src="+val.photo+" width='140px' height='140px'>"+"</td>"+
					"<td>"+'￥'+val.price+"</td>"+
					"<td align='center' goodsId="+val.goodsId+">"+
						"<input type='button' id='moveToCart' value='加入购物车' onclick='moveToCartList("+val.goodsId+")'>"+
						"<input type='button' id='removeFromCart' value='删除' onclick='deleteFavoriteItem("+val.goodsId+")'>"+
					"</td>"+
					"</tr>";
					$('#hiddenresult').append(li);
					 // sessionStorage.setItem('favorite'+index+'',li);

				});
				//each之后可获得item的数量。进行总页数得获取.若不能整除则加一
				item_num=sessionStorage.getItem('item_num');
				if(item_num/psize>parseInt(item_num/psize)){
					page_num=Math.ceil(item_num/psize);
				}else{
					page_num=item_num/psize;
				}

				var initPagination = function() {
					var num_entries =sessionStorage.getItem('item_num');
					// 创建分页
					$("#Pagination").pagination(num_entries, {
						num_edge_entries: 1, //边缘页数
						num_display_entries: 4, //主体页数
						callback: pageselectCallback,
						items_per_page:5 //每页显示3项
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
			}else if(data.resultCode==-1){
				alert('商品不存在')
			}else if(data.resultCode==-2){
				alert('商品已添加')
			}
		}
	});

});
	//删除收藏夹中数据行
	function deleteFavoriteItem(para){
	//在ajax中进行对页面中的数据删除避免前后端数据不统一
		if(window.confirm('确认删除？')){
            $.ajax({
                url: webRoot+'/user/favorite',
                type: 'POST',
                datatype:'json',
                data: {goodsId:para},
                success:function(data){
                    //判定前端显示的商品是否合法或有其他问题
                    if(data.resultCode == 0){
                        // $(btn).parent().parent().remove();
                        alert('成功删除');
                        location.reload();
                    }
                }
            })
			.done(function() {
				console.log("delete success");
			})
			.fail(function() {
				console.log("delete error");
			})
			.always(function() {
				console.log("delete complete");
			});
		}else{
			return false
		}


	}
	//添加商品至购物车并弹出框显示成功与否
	function moveToCartList(para){
	$.ajax({
		url: webRoot+'/user/shoppingcart',
		type: 'PUT',
		dataType: 'json',
		data: {goodsId:para},
		success:function(data){
			if(data.resultCode==0){
                alert('已添加至购物车！');
                $.ajax({
                    url: webRoot+'/user/favorite',
                    type: 'POST',
                    datatype:'json',
                    data: {goodsId:para},
                    success:function(data){
                        //判定前端显示的商品是否合法或有其他问题
                        if(data.resultCode == 0){
                            // $(btn).parent().parent().remove();
                            if(window.confirm("是否转到购物车？")){
                                location.href=webRoot+'/cart_list.html'
                            }else
                            location.reload();
                        }
                    }
                })
				.done(function() {
					console.log("delete success");
				})
				.fail(function() {
					console.log("delete error");
				})
				.always(function() {
					console.log("delete complete");
				});
			}else if(data.resultCode==-1){
                alert("该商品不存在存货");
			}else{
                alert('该商品之前已存在于购物车');
			}
		}
		})
		.done(function() {
			console.log("moveToFavorite success");
		})
		.fail(function() {
			console.log("moveToFavorite error");
		})
		.always(function() {
			console.log("moveToFavorite complete");
		});

	}
