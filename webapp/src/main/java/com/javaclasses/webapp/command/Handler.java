package com.javaclasses.webapp.command;

import com.javaclasses.webapp.JsonObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for abstract handler instance
 */
public interface Handler {

    /**
     * Process request data
     * @param request HttpServletRequest from user
     * @return Entity of {@link JsonObject} with processed data
     */
    JsonObject process(HttpServletRequest request);
}
