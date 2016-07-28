package com.javaclasses.chat.webapp;

import com.javaclasses.chat.webapp.command.Handler;
import com.javaclasses.chat.webapp.command.RequestContext;
import com.javaclasses.chat.webapp.command.impl.ChatCreationController;
import com.javaclasses.chat.webapp.command.impl.PageNotFoundController;
import com.javaclasses.chat.webapp.command.impl.RegistrationController;
import com.javaclasses.chat.webapp.command.impl.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all possible handlers
 */
public class HandlerRegistry  {

    private final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);

    private static HandlerRegistry handlerRegistry;

    private HandlerRegistry() {
    }

    public static HandlerRegistry getInstance() {
        if (handlerRegistry == null) {
            handlerRegistry = new HandlerRegistry();
        }

        return handlerRegistry;
    }

    private final Map<RequestContext, Handler> registry =
            new HashMap<RequestContext, Handler>(){{
        put(new RequestContext("/register", "post"), new RegistrationController());
        put(new RequestContext("/login", "post"), new LoginController());
        put(new RequestContext("/create-chat", "post"), new ChatCreationController());
    }};

    /**
     * Searches handler by given data
     * @param requestContext Request data
     * @return Handler by given data
     */
    public Handler getHandler(RequestContext requestContext) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for handler...");
        }

        final Handler handler = registry.get(requestContext);

        if (handler == null) {
            if (log.isWarnEnabled()) {
                log.warn("Handler by given request context not found.");
            }

            return new PageNotFoundController();
        }

        try {
            return handler;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Handler successfully found: " +
                        handler.getClass().getSimpleName());
            }
        }
    }
}
