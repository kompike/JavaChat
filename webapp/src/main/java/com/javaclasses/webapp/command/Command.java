package com.javaclasses.webapp.command;

import com.javaclasses.model.service.UserRegistrationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for abstract command instance
 */
public interface Command {

    String execute(HttpServletRequest request, HttpServletResponse response)
            throws UserRegistrationException;
}
