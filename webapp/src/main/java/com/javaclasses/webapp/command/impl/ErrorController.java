package com.javaclasses.webapp.command.impl;

import com.javaclasses.webapp.JsonEntity;
import com.javaclasses.webapp.command.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Controller} interface for handling error requests
 */
public class ErrorController implements Controller {

    @Override
    public JsonEntity execute(HttpServletRequest request) {

        final String errorMessage = "Page not found";

        final JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.add("errorMessage", errorMessage);
        jsonEntity.add("responseStatus", "404");

        return jsonEntity;
    }
}
