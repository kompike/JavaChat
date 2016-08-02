if (typeof define !== 'function') {
	var Events = require('../events');
}

var ChatService = function(eventBus, serverURL) {
	
	var _addChat = function(chatData) {
		var name = chatData.chatName;
		var tokenId = chatData.tokenId;
		$.post(
			serverURL + "api/chat/create",
			{
				chatName: name,
				tokenId: tokenId
			},
			function(xhr) {
				var data = eval("(" + xhr + ")");
				eventBus.post(Events.CHAT_CREATED, data.message)
				eventBus.post(Events.CHAT_LIST_UPDATED, data.chatList);
			}, 'text')
			.fail(function(xhr, status, error) {
				var err = eval("(" + xhr.responseText + ")");
				eventBus.post(Events.CHAT_CREATION_FAILED, err.errorMessage);
			});
	}
		
	var _onChatAdded = function(chat) {
		return _addChat(chat);
	}
		
	var _onUserJoined = function(chatData) {
		var name = chatData.chatName;
		var tokenId = chatData.tokenId;
		$.post(
			serverURL + "api/chat/join",
			{
				chatName: name,
				tokenId: tokenId
			},
			function(xhr) {
				var data = eval("(" + xhr + ")");
				eventBus.post(Events.USER_JOINED_CHAT, data);
			}, 'text')
			.fail(function(xhr, status, error) {
				var err = eval("(" + xhr.responseText + ")");
				eventBus.post(Events.CHAT_JOINING_FAILED, err.errorMessage);
			});
	}
		
	var _onUserLeft = function(chatData) {
		var name = chatData.chatName;
		var tokenId = chatData.tokenId;
		$.post(
			serverURL + "api/chat/leave",
			{
				chatName: name,
				tokenId: tokenId
			},
			function(xhr) {
				var data = eval("(" + xhr + ")");
				eventBus.post(Events.USER_LEFT_CHAT, data.chatId);
			}, 'text')
			.fail(function(xhr, status, error) {
				var err = eval("(" + xhr.responseText + ")");
				eventBus.post(Events.CHAT_LEAVING_FAILED, err.errorMessage);
			});
	}
		
	var _onMessageAdded = function(messageData) {
		var message = messageData.message;
		var tokenId = messageData.tokenId;
		var chatName = messageData.chatName;
		var color = messageData.color;
		$.post(
			serverURL + "api/message/add",
			{
				message: message,
				tokenId: tokenId,
				chatName: chatName,
				color: color
			},
			function(xhr) {
				var data = eval("(" + xhr + ")");
				eventBus.post(Events.MESSAGE_ADDED, data);
			}, 'text')
			.fail(function(xhr, status, error) {
				var err = eval("(" + xhr.responseText + ")");
				var errorMessage = {
					'message' : err.errorMessage,
					'chatId' : err.chatId
				};
				eventBus.post(Events.MESSAGE_ADDING_FAILED, errorMessage);
			});
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