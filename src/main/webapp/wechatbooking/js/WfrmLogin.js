function login() {
	if ($("#shopid").val() == "") {

        $('#my-alert').removeClass().addClass("am-alert am-alert-warning");

        $('#my-msg').text("请填写用户名");

        $('#my-alert').on('closed.alert.amui', function () {

            $('#my-alert').removeClass().addClass("am-alert am-alert-warning am-hide");

            $('#my-alert').alert('close');
        });

        return false;
    }

    if ($("#password").val() == "") {

        $('#my-alert').removeClass().addClass("am-alert am-alert-warning");

        $('#my-msg').text("请填写密码");

        $('#my-alert').on('closed.alert.amui', function () {

            $('#my-alert').removeClass().addClass("am-alert am-alert-warning am-hide");

            $('#my-alert').alert('close');
        });
        return false;
    }

    $("#btnLogin").html("登录中 ... <i class='am-icon-spinner am-icon-spin'></i>");
    //TODO
   	console.info("点击确定按钮");
}
document.onkeydown = function (event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    
    if (e && e.keyCode == 13) { // enter 键
        login();
    }
}; 
