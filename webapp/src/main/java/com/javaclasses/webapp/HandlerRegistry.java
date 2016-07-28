package com.javaclasses.webapp;

import com.javaclasses.webapp.command.Handler;
import com.javaclasses.webapp.command.impl.ErrorController;
import com.javaclasses.webapp.command.impl.LoginController;
import com.javaclasses.webapp.command.impl.RegistrationController;
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

    private final Map<String, HashMap<String, Handler>> registry =
            new HashMap<String, HashMap<String, Handler>>(){{
        put("/register", new HashMap<String, Handler>(){{
            put("post", new RegistrationController());
        }});
        put("/login", new HashMap<String, Handler>(){{
            put("post", new LoginController());
        }});
    }};

    /**
     * Searches handler by given data
     * @param uri Request uri
     * @param method Request method
     * @return Handler by given data
     */
    public Handler getHandler(String uri, String method) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for handler by uri: " + uri + " and method: " + method);
        }

        final HashMap<String, Handler> handlerHashMap = registry.get(uri);

        if (handlerHashMap == null) {
            if (log.isWarnEnabled()) {
                log.warn("Handler by given uri and method not found.");
            }

            return new ErrorController();
        }

        try {
            return handlerHashMap.get(method);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Handler successfully found: " +
                        handlerHashMap.get(method).getClass().getSimpleName());
            }
        }
    }
}
