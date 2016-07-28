package com.javaclasses.webapp;

import com.javaclasses.webapp.command.Controller;
import com.javaclasses.webapp.command.impl.ErrorController;
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

    private final Map<String, HashMap<String, Controller>> registry =
            new HashMap<String, HashMap<String, Controller>>(){{
        put("/register", new HashMap<String, Controller>(){{
            put("get", new RegistrationController());
            put("post", new RegistrationController());
        }});
    }};

    /**
     * Searches controller by given data
     * @param uri Request uri
     * @param method Request method
     * @return Controller by given data
     */
    public Controller getController(String uri, String method) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for controller by uri: " + uri + " and method: " + method);
        }

        final HashMap<String, Controller> controllerHashMap = registry.get(uri);

        if (controllerHashMap == null) {
            if (log.isWarnEnabled()) {
                log.warn("Controller by given uri and method not found.");
            }

            return new ErrorController();
        }

        try {
            return controllerHashMap.get(method);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Controller successfully found: " +
                        controllerHashMap.get(method).getClass().getSimpleName());
            }
        }
    }
}
