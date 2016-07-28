package com.javaclasses.webapp.command.impl;

import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import com.javaclasses.webapp.JsonEntity;
import com.javaclasses.webapp.command.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Controller} interface for registration process
 */
public class RegistrationController implements Controller {

    private final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public JsonEntity execute(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
        }

        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDTO registrationDTO =
                new RegistrationDTO(nickname, password, confirmPassword);

        final JsonEntity jsonEntity = new JsonEntity();
        try {
            final UserId userId = userService.register(registrationDTO);
            final UserDTO registeredUser = userService.findById(userId);
            jsonEntity.add("userId", String.valueOf(registeredUser.getUserId().getId()));
            jsonEntity.add("userName", registeredUser.getUserName());
            jsonEntity.add("message", "User successfully registered");
            jsonEntity.add("responseStatus", "200");
        } catch (UserRegistrationException e) {
            jsonEntity.add("errorMessage", e.getMessage());
            jsonEntity.add("responseStatus", "404");
        }

        try {
            return jsonEntity;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("User request successfully processed.");
            }
        }
    }
}
