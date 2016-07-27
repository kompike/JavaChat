package com.javaclasses.webapp.command.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import com.javaclasses.webapp.command.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Controller} interface for registration execute
 */
public class RegistrationController implements Controller {

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) {

        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDTO registrationDTO =
                new RegistrationDTO(nickname, password, confirmPassword);

        final Gson gson = new GsonBuilder().create();
        String jsonString;
        try {
            UserId userId = userService.register(registrationDTO);
            UserDTO registeredUser = userService.findById(userId);
            jsonString = gson.toJson(registeredUser);
        } catch (UserRegistrationException e) {
            jsonString = e.getMessage();
        }

        return jsonString;
    }
}
