package com.javaclasses.webapp.command;

import com.javaclasses.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for abstract command instance
 */
public interface Controller {

    /**
     * Process request data
     * @param request HttpServletRequest from user
     * @return Entity of {@link JsonEntity} with processed data
     */
    JsonEntity execute(HttpServletRequest request);
}
