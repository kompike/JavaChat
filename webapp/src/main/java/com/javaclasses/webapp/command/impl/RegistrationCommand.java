package com.javaclasses.webapp.command.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import com.javaclasses.webapp.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link Command} interface for registration process
 */
public class RegistrationCommand implements Command {

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws UserRegistrationException {

        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDTO registrationDTO =
                new RegistrationDTO(nickname, password, confirmPassword);
        final UserId userId = userService.register(registrationDTO);
        final UserDTO registeredUser = userService.findById(userId);

        final Gson gson = new GsonBuilder().create();

        return gson.toJson(registeredUser);
    }
}
