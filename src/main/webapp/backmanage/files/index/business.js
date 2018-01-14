/**
 * Created by lsy
 */

$(function () {
    $("#welcome").html("您好，" + $.cookie("manager_id") + "！欢迎登录后台管理系统！");
});