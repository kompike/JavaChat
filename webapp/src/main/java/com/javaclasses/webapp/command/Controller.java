package com.javaclasses.webapp.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for abstract command instance
 */
public interface Controller {

    String execute(HttpServletRequest request);
}
