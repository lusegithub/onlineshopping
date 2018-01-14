/**
 * Created by jacky on 17-7-3.
 */
document.write("<script src='../files/all/global.js'></script>");
$(function () {
    var catalog = $("#catalog");
    $.get(server_host+"/catalog", function (response) {
        if(response.resultCode === 0) {
            $.each(response.content, function (index, item, arr) {
                catalog.append($("<option></option>").append(item.catalogName).val(item.catalogName));
            })
        }
    })
    $("#query").click(function () {
        var begin_time=$("#start_date").val();
        var end_time=$("#end_date").val();
        $.ajax({
            url: server_host+'/revenue',
            type: 'GET',
            data:{
                "catalog_name":catalog.val(),
                "begin_time":begin_time,
                "end_time":end_time
            },
            success:function (data) {
                if (data.resultCode==0){
                    sessionStorage.setItem("sales", JSON.stringify(data.content));
                    location.href = host + "/sale_manage/sales_list.html";
                }else if (data.resultCode==-2){
                    alert(data.resultInfo);
                }else {
                    location.href = host + "/sale_manage/query_fail.html";
                }
            },
            error:function(data){
            }
        })
    });
});