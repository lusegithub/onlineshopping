$(document).ready(function() {
	

var searchStr=localStorage.getItem('searchDatas');//按照四个关键字搜索
var searchObj=$.parseJSON('searchStr');

var searchStrKeyword=localStorage.getItem('keyWord');

if(searchStr){
	function getData(page){
	$.ajax({
		url: "/special-goods/page-"+page+"",
		type: 'GET',
		data: {catalog_name:'searchObj.catalog_name'},//可能需要更改格式
		success:function(data){
				 /* iterate through array or object */
				 var json=JSON.parse(data);
				 
				 var li="";
				 var goods_num=json.content.goodsNum;
				 var pages_num=json.content.pagesNum;//当前页数
				 var total_pages_num=json.content.totalPagesNum;//获取每页的商品数和总页数

				 $.each(json.content, function(index, val) {
				 	 /* iterate through array or object */
				 	  $.get("/goods/"+val+"/info", function(data) {
				 	  	
				 	  	var jsonPerInfo=JSON.parse(data);

				 	  	li +=
				 		"<tr>"+
				 		"<td>"+(parseInt(index)+1)+"<td>"+
				 		"<td>"+"<img src="data.content.photo">"+"<td>"+
				 		"<td>"+data.content.nickname+"</tdr>"+
				 		"<td>"+data.content.price+"</td>"+
				 		"<td>"+parseInt(data.content.price)*0.8+"</td>"+
				 		"<td align='center'>"+
				 			"<input type="button" id='removeToCart' value='加入购物车'>"+
				 			"<input type="button" id='removeToSaved' value='加入收藏夹'>"+
				 		"<td>"+
				 		"</tr>";

						});
				 });
				 	 
				 
				 $('#table1').append(li);

				 li='';
				if(total_pages_num>1){
				 	/*optional stuff to do after success */
				 	li="<ul><li><a href='javascript:viod(0);onclick=preEvent();' id='pre' data-num='1'>上一页</a></li>";			
				 }else{
				 	li="<ul>";
				 }
				 for(var i=0;i<total_pages_num;i++){
				 	li+="<li><a href='javascript:viod(0);oclick=getData("+(parseInt(i)+1)+");' type='num'>"+(parse(i)+1)+"</a></li>"
				 }
				 //判定li中是否有上一页按钮
				 if(li.indexof('上一页')!=-1){
				 	li+="<li><a href='javascript:viod(0);onclick=nextEvent();' id='next' data-num='1'>下一页</a></li></ul>"+"<span>共<span id='pagesNum'>"
				 	+pages_num;+"</span>页</span></ul>"
				 }else{
				 	li+="<span>共<span id='pagesNum'>"pages_num"</span>页</span></ul>";
				 }

				 $('.turn_page').html(li);

				 $('#pre').attr('data-num', 'pages_num');//设定页数以此
				 $('#next').attr('data-num','pages_num');

				 
		}
	});
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

	function preEvent(){
		var cur_page=$('#pre').attr('data-num');
		if(cur_page<=1){
			$(this).attr('disable','true');
		}else{
			getData(parseInt(pages_num)-1);
		}
	}

	function nextEvent(){
		var cur_page=$('#next').attr('data-num');
		var pages_num=$('#pagesNum').html();
		if(cur_page<pages_num){
			getData(parseInt(cur_page)+1);
		}else{
			$(this).attr('disable','true');
		}
	}
}
// }else if(searchStrKeyword){
// 	$('searchBtn').click(function(){
// 		$.ajax({
// 			url: '/goods/like-'+searchStrKeyword+'',
// 			type: 'GET',
// 			success:function(data){
//            		var json=JSON.parse(data);
				 
// 				 var li="";
// 				 var goods_num=json.content.goodsNum;
// 				 var pages_num=json.content.pagesNum;//当前页数
// 				 var total_pages_num=json.content.totalPagesNum;//获取每页的商品数和总页数

// 				 for(var i=0;i<goods_num;i++){
// 				 	 /* iterate through array or object */
// 				 	  $.get("/goods/"+json.content[i]+"/info", function(data) {
				 	  	
// 				 	  	var jsonPerInfo=JSON.parse(data);

// 				 	  	var li +=
// 				 		"<tr>"+
// 				 		"<td>"+(parseInt(i)+1)+"<td>"+
// 				 		"<td>"+"<img src="data.content.photo">"+"<td>"+
// 				 		"<td>"+data.content.nickname+"</tdr>"+
// 				 		"<td>"+data.content.price+"</td>"+
// 				 		"<td>"+parseInt(data.content.price)*0.8+"</td>"+
// 				 		"<td align='center'>"+
// 				 			"<input type="button" id='removeToCart' value='加入购物车'>"+
// 				 			"<input type="button" id='removeToSaved' value='加入收藏夹'>"+
// 				 		"<td>"+
// 				 		"</tr>";

// 						});
// 				 }
// 				 $('#table').html(li);

// 				 li='';
// 				if(total_pages_num>1){
// 				 	/*optional stuff to do after success */
// 				 	li="<ul><li><a href='javascript:viod(0);onclick=preEvent();' id='pre' data-num='1'>上一页</a></li>";			
// 				 }else{
// 				 	li="<ul>";
// 				 }
// 				 for(var i=0;i<total_pages_num;i++){
// 				 	li+="<li><a href='javascript:viod(0);oclick=getData("+(parseInt(i)+1)+");' type='num'>"+(parse(i)+1)+"</a></li>"
// 				 }
// 				 //判定li中是否有上一页按钮
// 				 if(li.indexof('上一页')!=-1){
// 				 	li+="<li><a href='javascript:viod(0);onclick=nextEvent();' id='next' data-num='1'>下一页</a></li></ul>"+"<span>共<span id='pagesNum'>"
// 				 	+pages_num;+"</span>页</span></ul>"
// 				 }else{
// 				 	li+="<span>共<span id='pagesNum'>"pages_num"</span>页</span></ul>";
// 				 }

// 				 $('.turn_page').html(li);

// 				 $('#pre').attr('data-num', 'pages_num');//设定页数以此
// 				 $('#next').attr('data-num','pages_num');

				 
// 		}
// 	});
// 	.done(function() {
// 		console.log("success");
// 	})
// 	.fail(function() {
// 		console.log("error");
// 	})
// 	.always(function() {
// 		console.log("complete");
// 	});
// });

// 	function preEvent(){
// 		var cur_page=$('#pre').attr('data-num');
// 		if(cur_page<=1){
// 			$(this).attr('disable','true');
// 		}else{
// 			getData(parseInt(pages_num)-1);
// 		}
// 	}

// 	function nextEvent(){
// 		var cur_page=$('#next').attr('data-num');
// 		var pages_num=$('#pagesNum').html();
// 		if(cur_page<pages_num){
// 			getData(parseInt(cur_page)+1);
// 		}else{
// 			$(this).attr('disable','true');
// 		}
// 	}
// }


});