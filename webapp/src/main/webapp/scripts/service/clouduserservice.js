if (typeof define !== 'function') {
	var Events = require('../events');
}

var UserService = function(eventBus, serverURL) {
	
	var _addUser = function(user) {
		var name = user.nickname;
		var pass = user.password;
		var confirmPass = user.repeatPassword;
        $.ajax({
			url: serverURL + "api/register",
			method: 'POST',
			data: {
				nickname: name,
				password: pass,
				confirmPassword: confirmPass
			},
			statusCode: {
				200: function(data) {
					eventBus.post(Events.USER_REGISTERED, data);
				},
				500: function(xhr, status, error) {
                    var err = eval("(" + xhr.responseText + ")");
					eventBus.post(Events.REGISTRATION_FAILED, err.errorMessage);
				}
			}
		});
	};
		
	var _onUserAdded = function(user) {
		return _addUser(user);
	};
	
	var _loginUser = function(user) {
		var name = user.nickname;
		var pass = user.password;
		$.ajax({
			url: serverURL + "api/login",
			method: 'POST',
			data: {
				nickname: name,
				password: pass
			},
			dataType: 'json',
			statusCode: {
				200: function(data) {
					eventBus.post(Events.LOGIN_SUCCESSFULL, data);
				},
                500: function(xhr, status, error) {
                    var err = eval("(" + xhr.responseText + ")");
					eventBus.post(Events.LOGIN_FAILED, err.errorMessage);
				}
			}
		});		
	};
		
	var _onUserLogin = function(user) {
		_loginUser(user);
	};
	
	return {
		'onUserAdded' : _onUserAdded, 
		'onUserLogin' : _onUserLogin
	};	
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return UserService;
});