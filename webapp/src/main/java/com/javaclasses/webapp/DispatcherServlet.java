package com.javaclasses.webapp;

import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.webapp.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private final HandlerRegistry registry = HandlerRegistry.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String commandName = request.getParameter("command");

        Command command = registry.getCommand(commandName);

        String responseData;

        try {
            System.out.println("responseData = command.execute(request, response)");
            responseData = command.execute(request, response);
        } catch (UserRegistrationException e) {
            responseData = e.getMessage();
        }

        request.getRequestDispatcher(responseData).forward(request, response);
    }
}
