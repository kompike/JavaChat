package com.javaclasses.webapp.command.impl;

import com.javaclasses.webapp.JsonObject;
import com.javaclasses.webapp.command.Handler;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Handler} interface for handling error requests
 */
public class ErrorController implements Handler {

    @Override
    public JsonObject process(HttpServletRequest request) {

        final String errorMessage = "Page not found";

        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("errorMessage", errorMessage);
        jsonObject.add("responseStatus", "404");

        return jsonObject;
    }
}
