/**
 * Created by lsy
 */
$(function () {
    var old_catalog_name = getUrlParam("catalog");
    var new_catalog = $("#new_catalog");
    new_catalog.val(old_catalog_name);
    var edit = $("#edit");
    var reset = $("#reset");

    edit.click(function () {
        if(new_catalog.val() === "") {
            alert("请输入新的目录名！");
            new_catalog.focus();
        }else {
            $.ajax({
                url: webRoot + "/catalog",
                type: 'put',
                data: {
                    old_catalog_name: old_catalog_name,
                    new_catalog_name: new_catalog.val()
                }
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0) {
                    history.back();
                }
            }).error(function () {
                alert("请求失败！");
            });
        }
    });

    reset.click(function () {
        new_catalog.val("");
    });
});