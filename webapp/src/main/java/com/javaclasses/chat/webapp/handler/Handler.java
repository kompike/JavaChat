package com.javaclasses.chat.webapp.handler;

import com.javaclasses.chat.webapp.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for abstract handler instance
 */
public interface Handler {

    /**
     * Process request data
     * @param request HttpServletRequest from user
     * @param response HttpServletResponse with processed data
     * @return Entity of {@link JsonObject} with processed data
     */
    JsonObject process(HttpServletRequest request, HttpServletResponse response);
}
