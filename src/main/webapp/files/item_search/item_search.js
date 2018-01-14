$(document).ready(function() {
	var key_word=$('#input').val();
	var searchStr=
	{
		"catalog_name":$('#u942_input option:selected').val(),
		"small_price":$('#u949_input').attr('value'),
		"large_price":$('#950_input').attr('value'),
		"keyword":$('#953_input').attr('value')
	};
	var storageSStr=localStorage.setItem('searchDatas','searchStr');//按照四个关键字搜索
	var storageKeyword=localStorage.setItem('keyWord','key_word');//按照关键字搜索
	if(storageSStr||storageKeyword){
		$('#searchBtn').click(function(){
			$(location).attr({
				href: 'http://localhost:81/onlineShopping/item_search_list.html';
			});
		});	
	}
});

