package com.javaclasses.webapp.command;

import com.javaclasses.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for abstract handler instance
 */
public interface Handler {

    /**
     * Process request data
     * @param request HttpServletRequest from user
     * @return Entity of {@link JsonEntity} with processed data
     */
    JsonEntity process(HttpServletRequest request);
}
