$(function () {
    var msg = $("#msg");
    var warning = $("#warning");
    var goodsList = $("#goodsList");
    var pagination = $("#pagination");
    var page;
    var initPagination = true;
    var items_per_page = 10;
    var maodian = false;

    //搜索的两个输入
    var catalogSelected = $("#catalogSelected");
    var catalog;
    var keywordInput = $("#keyword");
    var keyword = "";
    //初始化目录选择框
    $.get(
        webRoot + "/catalog",
        function (response) {
            if (response.resultCode === 0) {
                $.each(response.content, function (index, catalog) {
                    catalogSelected.append($("<option></option>")
                        .val(catalog.catalogName)
                        .append(catalog.catalogName)
                    );
                    $("#catalog_name").append($("<option></option>")
                        .val(catalog.catalogName)
                        .append(catalog.catalogName)
                    )
                });
                //从目录管理跳转过来
                catalog = getUrlParam("catalog");
                if (catalog != null) {
                    msg.css("display", "none");
                    catalogSelected.val(catalog);
                    getGoods();
                    // getGoodsByCatalog();
                }
            }
        }
    );

    $("#search").click(function () {
        catalog = catalogSelected.val();
        keyword = keywordInput.val();

        initPagination = true;
        getGoods();
    });

    function getGoods() {
        goodsList.empty();
        if(initPagination) {
            page = 1;
            pagination.empty();
            warning.css("display", "none");
            if (catalog === "" && keyword === "") {
                msg.css("display", "block");
                return;
            }else {
                msg.css("display", "none");
            }
        }
        $.ajax({
            url: webRoot + "/special-goods/page-" + page,
            type: "GET",
            data: {
                catalog_name: catalog,
                keyword: keyword,
                small_price: "",
                large_price: ""
            }
        }).done(function (response) {
            if (response.resultCode === 0) {
                createGoodsList(response.content.goodsIds);
                if (initPagination) {
                    createPagination(response.content.totalGoodsNum, getGoods);
                }
            } else {
                warn(response.resultInfo);
            }
        }).error(function () {
            alert("请求失败！");
        })
    }

    function createGoodsList(goodsIds) {
        $.each(goodsIds, function (index, goodsId) {
            $.get(webRoot + "/goods/" + goodsId + "/info", function (response) {
                if (response.resultCode === 0) {
                    goodsList.append($("<div></div>")
                        .attr("id", goodsId)
                        .attr("catalog_name",response.content.catalogName)
                        .append($("<img>").attr("src", response.content.photo))
                        .append($("<p></p>").append(response.content.name))
                        .append($("<p>ISBN：</p>").append(goodsId))
                        .append($("<p>价格：</p>").append(response.content.price))
                        .append($("<input type='button' value='修改'>").click(editGoods))
                        .append($("<input type='button' value='删除'>").click(deleteGoods))
                    )
                }
            });
        });
    }

    function createPagination(totalNum, fun) {
        pagination.pagination(totalNum, {
            current_page: page - 1,
            num_edge_entries: 2,
            num_display_entries: 4, //主体页数
            prev_text: "上一页",
            next_text: "下一页",
            items_per_page: items_per_page,
            callback: function (page_index) {
                if (initPagination) {
                    initPagination = false;
                } else {
                    maodian = true;
                    page = page_index + 1;
                    goodsList.empty();
                    fun();
                }
            }
        });
    }

    function warn(message) {
        msg.css("display", "block");
        warning.html(message);
        warning.css("display", "block");
    }

    //编辑
    function editGoods() {
        var goodsId = $(this).parent().attr("id");
        location.href = "goods_edit.html?catalog=" + $(this).parent().attr("catalog_name") + "&goodsId=" + goodsId;
    }

    //删除
    function deleteGoods() {
        var goodsId = $(this).parent().attr("id");
        if (window.confirm("确认删除商品" + goodsId + "吗？"))
            $.ajax({
                url: webRoot + "/catalog/" + encodeURIComponent(encodeURIComponent($(this).parent().attr("catalog_name"))) + "/goods/" + goodsId,
                type: "DELETE"
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0){
                    initPagination = true;
                    getGoods();
                }
                    // pagination.trigger("setPage", [page - 1])
                // getGoodsByCatalog();
            }).error(function () {
                alert("删除商品失败！");
            })
    }

    var catalog_name = $("#catalog_name");
    var isbn = $("#ISBN");
    var goods_name = $("#goods_name");
    var photo = $("#photo");
    var describe = $("#describe");
    var price = $("#price");
    var stock = $("#stock");

    //增加
    $("#add").click(function () {
        if (checkInfo()) {
            var formData = new FormData();
            formData.append("ISBN", isbn.val());
            formData.append("goods_name", goods_name.val());
            formData.append("file", photo.prop('files')[0]);
            formData.append("describe", describe.val());
            formData.append("goods_price", price.val());
            formData.append("stock", stock.val());
            $.ajax({
                url: webRoot + "/catalog/" + encodeURIComponent(encodeURIComponent(catalog_name.val())) + "/goods",
                type: "POST",
                processData: false,
                contentType: false,
                data: formData
            }).done(function (response) {
                alert(response.resultInfo);
                if (response.resultCode === 0){
                    initPagination = true;
                    getGoods();
                    reset();
                }
            }).error(function () {
                alert("请求失败！");
            });
        }
    });

    function checkInfo() {
        if (goods_name.val() === "") {
            alert("请输入商品名称！");
            goods_name.focus();
            return false;
        }
        if (isbn.val() === "") {
            alert("请输入ISBN！");
            isbn.focus();
            return false;
        }
        if (photo.val() === "") {
            alert("请上传商品图片！");
            photo.focus();
            return false;
        }
        if (photo.prop('files')[0].size > 5 * 1024 * 1024) {
            alert("图片超过5M！");
            photo.focus();
            return false;
        }
        if (price.val() === "") {
            alert("请输入市场价！");
            price.focus();
            return false;
        }
        if (stock.val() === "") {
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
        catalog_name.children().eq(0).prop("selected", true);
        isbn.val("");
        goods_name.val("");
        photo.val("");
        describe.val("");
        price.val("");
        stock.val("");
    }

    $("#back").click(function () {
        if(maodian) {
            history.back();
        }
        history.back();
    });
});

