if (typeof define !== 'function') {
	var Events = require('../events');
}

var ChatService = function(eventBus, serverURL) {
	
	var _addChat = function(chat) {
		
	}
		
	var _onChatAdded = function(chat) {
		return _addChat(chat);
	}
		
	var _onUserJoined = function(chatData) {
		
	}
		
	var _onUserLeft = function(chatData) {
		
	}
		
	var _onMessageAdded = function(messageData) {	
		
	}
	
	return {
		'onChatAdded' : _onChatAdded,
		'onMessageAdded' : _onMessageAdded,
		'onUserJoined' : _onUserJoined, 
		'onUserLeft' : _onUserLeft		
	};	
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return ChatService;
});