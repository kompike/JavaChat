package com.javaclasses.chat.webapp.handler.impl;

import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Handler} interface for handling error requests
 */
public class PageNotFoundController implements Handler {

    @Override
    public JsonObject process(HttpServletRequest request) {

        final String errorMessage = "Page not found";

        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("errorMessage", errorMessage);
        jsonObject.setResponseStatusCode(404);

        return jsonObject;
    }
}
