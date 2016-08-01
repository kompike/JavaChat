define(function(require) {
		
	var Chat = require('./chat');
	
	var EventBus = require('./eventbus');
	
	var Events = require('./events');
	
	var ErrorMessages = require('./errormessages');
	
	var UserService = require('./service/clouduserservice');
	
	var ChatService = require('./service/cloudchatservice');
	
	var baseURL = 'http://localhost:8080/';
	
	var eventBus = new EventBus();

	var chat = new Chat("chatroom", eventBus, new UserService(eventBus, baseURL), new ChatService(eventBus, baseURL));
	
	chat.initChat();
});	