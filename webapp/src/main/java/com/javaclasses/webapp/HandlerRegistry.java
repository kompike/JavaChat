package com.javaclasses.webapp;

import com.javaclasses.webapp.command.Command;
import com.javaclasses.webapp.command.impl.RegistrationCommand;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private static HandlerRegistry handlerRegistry;

    private HandlerRegistry() {
    }

    public static HandlerRegistry getInstance() {
        if (handlerRegistry == null) {
            handlerRegistry = new HandlerRegistry();
        }

        return handlerRegistry;
    }

    private final Map<String, Command> registry = new HashMap<String, Command>(){{
    }};

    public Command getCommand(String commandName) {
        return registry.get(commandName);
    }
}
