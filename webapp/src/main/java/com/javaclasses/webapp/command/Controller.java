package com.javaclasses.webapp.command;

import com.javaclasses.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for abstract command instance
 */
public interface Controller {

    JsonEntity execute(HttpServletRequest request);
}
