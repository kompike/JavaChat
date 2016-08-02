package com.javaclasses.chat.webapp;

import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.PageNotFoundHandler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.HttpMethod.POST;

/**
 * Registry of all possible handlers
 */
public class HandlerRegistry  {

    private final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);

    // Possible URLs
    public static final String LOGIN_URL = "/api/login";
    public static final String USER_REGISTRATION_URL = "/api/register";
    public static final String CREATE_CHAT_URL = "/api/chats";
    public static final String JOIN_CHAT_URL = "/api/membership/join";
    public static final String LEAVE_CHAT_URL = "/api/membership/leave";
    public static final String ADD_MESSAGE_URL = "/api/messages";

    // Possible request methods
    public static final String POST_METHOD = POST;

    // Request parameter names
    public static final String NICKNAME_PARAMETER = "nickname";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String CONFIRM_PASSWORD_PARAMETER = "confirmPassword";
    public static final String USER_NAME_PARAMETER = "userName";
    public static final String CHAT_NAME_PARAMETER = "chatName";
    public static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    public static final String CHAT_LIST_PARAMETER = "chatList";
    public static final String USER_CHATS_PARAMETER = "userChats";
    public static final String CHAT_ID_PARAMETER = "chatId";
    public static final String MESSAGES_PARAMETER = "messages";
    public static final String MESSAGE_PARAMETER = "message";
    public static final String COLOR_PARAMETER = "color";
    public static final String TOKEN_ID_PARAMETER = "tokenId";
    public static final String AUTHOR_PARAMETER = "author";

    private static HandlerRegistry handlerRegistry;

    private HandlerRegistry() {
    }

    public static HandlerRegistry getInstance() {
        if (handlerRegistry == null) {
            handlerRegistry = new HandlerRegistry();
        }

        return handlerRegistry;
    }

    private final Map<RequestContext, Handler> registry = new HashMap<>();

    /**
     * Add new handler to registry
     * @param context Request data
     * @param handler Handler instance to be executed by given context
     */
    public void registerHandler(RequestContext context, Handler handler) {

        if (log.isInfoEnabled()) {
            log.info("Start adding handler...");
        }

        try {
            registry.put(context, handler);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("New handler successfully added.");
            }
        }
    }

    /**
     * Searches handler by given data
     * @param requestContext Request data
     * @return Handler by given data
     */
    public Handler getHandler(RequestContext requestContext) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for handler..." + requestContext.toString());
        }

        final Handler handler = registry.get(requestContext);

        if (handler == null) {
            if (log.isWarnEnabled()) {
                log.warn("Handler by given request context not found: " + requestContext.toString());
            }

            return new PageNotFoundHandler();
        }

        try {
            return handler;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Handler successfully found. " + requestContext.toString());
            }
        }
    }
}
