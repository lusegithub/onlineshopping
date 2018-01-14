$(function(){
    if(getUrlParam("logout") != null) {
        $("#logout").css("display", "block");
    }
	var manager_id = $("#manager_id");
	var password = $("#password");
	var login = $("#login");
	var reset = $("#reset");

	login.click(function() {
		if(manager_id.val() === ""){
			alert("请输入用户名！");
			manager_id.focus();
		}else if(password.val() === ""){
			alert("请输入密码！");
			password.focus();
		}else{
			$.ajax({
				url: '/onlineshopping/manager',
				type: 'GET',
				data: {
					manager_id: manager_id.val(),
					password: password.val()
				}
			})
			.done(function(response) {
				if (response.resultCode === 0) {
					var date = new Date();
					date.setTime(date.getTime() + 30*60*1000);
					$.cookie("manager_id", manager_id.val(), {expires:date});
					location.href = "/onlineshopping/backmanage";
				}else{
					alert(response.resultInfo);
				}
				
			})
			.error(function() {
				alert("请求失败！");
			});
			
		}
	});

	reset.click(function() {
		manager_id.val("");
		password.val("");
	});
});