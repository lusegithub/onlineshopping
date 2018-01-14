/**
 * Created by jacky on 17-6-30.
 */
document.write("<script src='../files/all/global.js'></script>");
$(function () {
    $("#query").click(function () {
        var order_id=$("#orderid").val();
        var reg = /^[0-9]*$/;
        if (!reg.test(order_id)){
            alert("订单号应为数字");
            return;
        }
        var user_id=$("#userid").val();
        var begin_time=$("#start_date").val();
        var end_time=$("#end_date").val();
        $.ajax({
            url: server_host+'/orders',
            type: 'GET',
            data:{
                "order_id":order_id,
                "user_id":user_id,
                "begin_time":begin_time,
                "end_time":end_time
            },
            success:function (data) {
                if (data.resultCode==-1||data.resultCode==-2||data.resultCode==-4){
                    location.href=host+"/order_manage/query_fail.html";
                }else if (data.resultCode==-3){
                    alert(data.resultInfo);
                }else {
                    sessionStorage.setItem("orders", JSON.stringify(data.content));
                    location.href = host + "/order_manage/order_list.html";
                }
            },
            error:function(data){
            }
        })
    });
    $("#reset").click(function () {
        $("#orderid").val("");
        $("#userid").val("");
        $("#start_date").val("");
        $("#end_date").val("");
    })
});