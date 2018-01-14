/**
 * Created by lsy
 */

$(function () {
    var catalog = getUrlParam("catalog");
    var goodsId = getUrlParam("goodsId");

    var goods_name = $("#goods_name");
    var ISBN = $("#ISBN");
    var photo = $("#photo");
    var file = $("#file");
    var describe = $("#describe");
    var price = $("#price");
    var stock = $("#stock");

    //获取旧的数据
    $.get(webRoot + "/goods/" + goodsId + "/info", function (response) {
        if (response.resultCode === 0) {
            goods_name.val(response.content.name);
            ISBN.val(response.content.goodsId);
            photo.attr("src", response.content.photo);
            describe.val(response.content.describe);
            price.val(response.content.price);
            stock.val(response.content.stock);
        }else{
            alert(response.resultInfo);
            history.back();
        }
    }).error(function () {
        alert("请求失败！");
        history.back();
    });

    //修改
    $("#edit").click(function () {
        if(checkInfo()) {
            var formData = new FormData();
            formData.append("goods_name", goods_name.val());

            if (file.val() === ""){
                formData.append("file", "");
                console.log("未上传文件");
            }else {
                if(file.prop('files')[0].size > 5*1024*1024){
                    alert("图片超过5M！");
                    return;
                }
                formData.append("file", file.prop('files')[0]);
            }
            formData.append("describe", describe.val());
            formData.append("goods_price", price.val());
            formData.append("stock", stock.val());
            $.ajax({
                url: webRoot+"/catalog/"+encodeURIComponent(encodeURIComponent(catalog))+"/goods/"+goodsId,
                type: "POST",
                processData: false,
                contentType: false,
                data: formData
            }).done(function (response) {
                alert(response.resultInfo);
            }).error(function () {
                alert("请求失败！");
            })
        }
    });

    function checkInfo() {
        if(goods_name.val() === ""){
            alert("请输入商品名称！");
            goods_name.focus();
            return false;
        }
        if(price.val() === "") {
            alert("请输入市场价！");
            price.focus();
            return false;
        }
        if(stock.val() === ""){
            alert("请输入库存量！");
            stock.focus();
            return false;
        }
        return true;
    }

    $("#reset").click(function () {
        reset();
    });

    function reset() {
        goods_name.val("");
        photo.val("");
        file.val("");
        describe.val("");
        price.val("");
        stock.val("");
    }

    $("#back").click(function () {
        history.back();
    });
});