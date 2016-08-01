package com.javaclasses.chat.webapp.handler;

import com.javaclasses.chat.webapp.JsonObject;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Implementation of {@link Handler} interface for handling error requests
 */
public class PageNotFoundHandler implements Handler {

    @Override
    public JsonObject process(HttpServletRequest request) {

        final String errorMessage = "Page not found";

        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("errorMessage", errorMessage);
        jsonObject.setResponseStatusCode(SC_NOT_FOUND);

        return jsonObject;
    }
}
