if (typeof define !== 'function') {
	var Events = require('../events');
}

var UserService = function(eventBus, serverURL) {
	
	var _addUser = function(user) {
		var name = user.nickname;
		var pass = user.password;
		var confirmPass = user.repeatPassword;
        $.post(serverURL + "api/register",{
                nickname: name,
                password: pass,
                confirmPassword: confirmPass
            }, function(xhr) {
                var data = eval("(" + xhr + ")");
                eventBus.post(Events.USER_REGISTERED, data.message);
            }, 'text')
            .fail(function(xhr, status, error) {
                var err = eval("(" + xhr.responseText + ")");
                eventBus.post(Events.REGISTRATION_FAILED, err.errorMessage);
            });
	};
		
	var _onUserAdded = function(user) {
		return _addUser(user);
	};
	
	var _loginUser = function(user) {
		var name = user.nickname;
		var pass = user.password;
        $.post(
            serverURL + "api/login",
            {
                nickname: name,
                password: pass
            },
            function(xhr) {
                var data = eval("(" + xhr + ")");
                localStorage.setItem('tokenId', data.tokenId);
                localStorage.setItem('currentUser', data.userName);
                eventBus.post(Events.LOGIN_SUCCESSFULL, data);
            }, 'text')
            .fail(function(xhr, status, error) {
                var err = eval("(" + xhr.responseText + ")");
                eventBus.post(Events.LOGIN_FAILED, err.errorMessage);
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