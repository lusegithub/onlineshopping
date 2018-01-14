/**
 * Created by jacky on 17-6-30.
 */
document.write("<script src='../files/all/global.js'></script>");
$(function () {
    var content=sessionStorage.getItem("orders");
    var result=eval(content);

    var num_of_order;
    var	psize=10;//每页显示条数
    $.each(result,function (index,val) {
        sessionStorage.setItem('num_of_order',parseInt(index)+1);
        var $trTemp = $("<tr></tr>");
        $trTemp.append("<td>"+ val.orderId +"</td>");
        $trTemp.append("<td>"+ val.user.userId +"</td>");
        $trTemp.append("<td>"+ val.orderTime +"</td>");
        if (val.status=='待审核'){
            $trTemp.append("<td class='bigRed'>"+ val.status +"</td>");
        }else if(val.status=='审核通过'){
            $trTemp.append("<td class='bigGreen'>"+ val.status +"</td>");
        }else if (val.status=='已取消'){
            $trTemp.append("<td class='bigHui'>"+ val.status +"</td>");
        }else {
            $trTemp.append("<td class='bigBlue'>"+ val.status +"</td>");
        }
        $trTemp.append("<td><input type='button' onclick='getOrderDetail(this)' id='"+val.orderId+"' value='详情'></td>");
        $trTemp.appendTo("#hiddenresult");
    });
    num_of_order=sessionStorage.getItem('num_of_order');
    $("#Pagination").pagination(num_of_order, {
        num_edge_entries: 1, //边缘页数
        num_display_entries: 4, //主体页数
        prev_text: '上一页',
        next_text: '下一页',
        callback: pageselectCallback,
        items_per_page:psize //每页显示条数
    });

    function pageselectCallback(page_index, jq){
        //商品总数量
        //获取将被添加的每页的最后一个商品的代号，以保证最后一页无论是否为每页所显示的书目，都不出现bug
        var max_elem=Math.min((page_index+1)*psize,num_of_order);
        //每次点击页数时，清空之前页的内容。以假装翻页数据更新
        $("#Searchresult").html("");
        //循环向tobdy中添加当前页个数的商品
        for(var i=page_index*psize;i<max_elem;i++){
            $("#Searchresult").append($('#hiddenresult tr:eq('+i+')').clone()); //装载对应分页的内容
        }
        return false;
    }
});

function getOrderDetail(btn) {
    location.href=host+"/order_manage/order_info.html?orderId="+$(btn).attr('id');
}

