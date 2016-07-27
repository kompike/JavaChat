package com.javaclasses.webapp;

import com.javaclasses.webapp.command.Controller;
import com.javaclasses.webapp.command.impl.ErrorController;
import com.javaclasses.webapp.command.impl.RegistrationController;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry  {

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

    public Controller getController(String uri, String method) {
        final HashMap<String, Controller> controllerHashMap = registry.get(uri);

        if (controllerHashMap == null) {
            return new ErrorController();
        }

        return controllerHashMap.get(method);
    }
}
