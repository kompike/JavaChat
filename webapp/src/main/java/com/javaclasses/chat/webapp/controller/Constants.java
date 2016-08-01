package com.javaclasses.chat.webapp.controller;

/**
 * Constants for controllers
 */
public final class Constants {

    private Constants() {
    }

    // Possible URLs
    /*package*/ static final String LOGIN_URL = "/login";
    /*package*/ static final String USER_REGISTRATION_URL = "/register";
    /*package*/ static final String CREATE_CHAT_URL = "/chat/create";
    /*package*/ static final String LEAVE_CHAT_URL = "/chat/leave";
    /*package*/ static final String JOIN_CHAT_URL = "/chat/join";
    /*package*/ static final String ADD_MESSAGE_URL = "/message/add";

    // Possible request methods
    /*package*/ static final String POST_METHOD = "post";

    // Request parameter names
    /*package*/ static final String NICKNAME_PARAMETER = "nickname";
    /*package*/ static final String PASSWORD_PARAMETER = "password";
    /*package*/ static final String CONFIRM_PASSWORD_PARAMETER = "confirmPassword";
    /*package*/ static final String USER_NAME_PARAMETER = "userName";
    /*package*/ static final String CHAT_NAME_PARAMETER = "chatName";
    /*package*/ static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    /*package*/ static final String CHAT_LIST_PARAMETER = "chatList";
    /*package*/ static final String CHAT_ID_PARAMETER = "chatId";
    /*package*/ static final String MESSAGES_PARAMETER = "messages";
    /*package*/ static final String MESSAGE_PARAMETER = "message";
    /*package*/ static final String COLOR_PARAMETER = "color";
    /*package*/ static final String TOKEN_ID_PARAMETER = "tokenId";
    /*package*/ static final String AUTHOR_PARAMETER = "author";
}
