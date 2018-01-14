/**
 * Created by lsy
 */
$(function () {
    getCatalog();

    //按钮
    var resetCheckbox = $("#resetCheckbox");
    var del = $("#delete");
    var edit = $("#edit");

    //重置复选框
    resetCheckbox.click(function () {
        $.each($("[type='checkbox']"), function (index, checkbox, arr) {
            checkbox.checked = false;
        });
    });

    //点击删除
    del.click(function () {
        if (window.confirm("确认要删除勾选的目录吗？"))
            $.each(checkedCatalog(), function (index, catalog, arr) {
                $.ajax({
                    url: webRoot + '/catalog/' + encodeURIComponent(encodeURIComponent(catalog)),
                    type: 'DELETE',
                    success: function (response) {
                        alert(response.resultInfo);
                        if (response.resultCode === 0) {
                            getCatalog();
                        }
                    },
                    error: function (xhr) {
                        if(xhr.status === 401) {
                            alert("登录超时，请重新登录！");
                            $("#logout").click();
                        }else {
                            alert("请求失败！");
                        }
                    }
                });
            })
    });

    //点击编辑
    edit.click(function () {
        var catalogList = checkedCatalog();
        if (catalogList.length > 1) {
            alert("只能同时编辑一个目录！");
        } else if (catalogList.length === 1) {
            location.href = "catalog_edit.html?catalog=" + catalogList[0];
        }
    });

    //按钮
    var add = $("#add");
    var resetCatalog = $("#resetCatalog");
    var catalog = $("#catalog_name");

    //点击添加
    add.click(function () {
        if (catalog.val() === "") {
            alert("请输入目录名！");
            catalog.focus();
        } else {
            $.ajax({
                url: webRoot + "/catalog",
                type: 'post',
                data: {
                    catalog_name: catalog.val()
                }
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0) {
                    catalog.val("");
                    getCatalog();
                }
            }).error(function () {
                alert("请求失败！");
            });
        }
    });

    //点击重置
    resetCatalog.click(function () {
        catalog.val("");
    })
});

function getCatalog() {
    //复选框所在的table
    var table = $("table:first");
    //获取目录
    $.get(webRoot + "/catalog", function (response) {
        if (response.resultCode === 0) {
            table.empty();
            var tr;
            $.each(response.content, function (index, catalog) {
                if (index % 5 === 0) {
                    table.append(tr);
                    tr = $("<tr></tr>");
                }
                tr.append($("<td></td>")
                    .append($("<input type='checkbox'>")
                        .attr("value", catalog.catalogName))
                    .append($("<a></a>")
                        .append(catalog.catalogName)
                        .attr("href", pagePrefix + "/goods_manage/index.html?catalog=" + catalog.catalogName))
                );
            });
            table.append(tr);
        }
    }).error(function () {
        alert("请求失败！");
        history.back();
    })
}

function checkedCatalog() {
    var catalog = [];
    $.each($("[type='checkbox']"), function (index, checkbox, arr) {
        if (checkbox.checked === true) {
            catalog.push(checkbox.value);
        }
    });
    return catalog;
}