/**
 * Created by jacky on 17-7-3.
 */
$(function () {
    var content = sessionStorage.getItem("sales");
    var result = eval(content);

    var num_of_sales;
    var psize = 10;//每页显示条数
    $.each(result, function (index, val) {
        sessionStorage.setItem('num_of_sales', parseInt(index) + 1);
        var $trTemp = $("<tr></tr>");
        $trTemp.append("<td>" + val.goodsId + "</td>");
        $trTemp.append("<td>" + val.goodsName + "</td>");
        $trTemp.append("<td>" + "￥" + val.goodsPrice + "</td>");
        $trTemp.append("<td>" + val.goodsNumber + "</td>");
        $trTemp.append("<td>" + "￥" + val.revenue + "</td>");
        $trTemp.appendTo("#hiddenresult");
    });
    num_of_sales = sessionStorage.getItem('num_of_sales');
    $("#Pagination").pagination(num_of_sales, {
        num_edge_entries: 1, //边缘页数
        num_display_entries: 4, //主体页数
        prev_text: '上一页',
        next_text: '下一页',
        callback: pageselectCallback,
        items_per_page: psize //每页显示条数
    });

    function pageselectCallback(page_index, jq) {
        //商品总数量
        //获取将被添加的每页的最后一个商品的代号，以保证最后一页无论是否为每页所显示的书目，都不出现bug
        var max_elem = Math.min((page_index + 1) * psize, num_of_sales);
        //每次点击页数时，清空之前页的内容。以假装翻页数据更新
        $("#Searchresult").html("");
        //循环向tobdy中添加当前页个数的商品
        for (var i = page_index * psize; i < max_elem; i++) {
            $("#Searchresult").append($('#hiddenresult tr:eq(' + i + ')').clone()); //装载对应分页的内容
        }
        return false;
    }
});
