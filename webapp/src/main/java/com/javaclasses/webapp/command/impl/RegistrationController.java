package com.javaclasses.webapp.command.impl;

import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import com.javaclasses.webapp.JsonObject;
import com.javaclasses.webapp.command.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Handler} interface for registration process
 */
public class RegistrationController implements Handler {

    private final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public JsonObject process(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
        }

        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDTO registrationDTO =
                new RegistrationDTO(nickname, password, confirmPassword);

        final JsonObject jsonObject = new JsonObject();
        try {
            userService.register(registrationDTO);
            jsonObject.add("message", "User successfully registered");
            jsonObject.add("responseStatus", "200");
        } catch (UserRegistrationException e) {
            jsonObject.add("errorMessage", e.getMessage());
            jsonObject.add("responseStatus", "404");
        }

        try {
            return jsonObject;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("User request successfully processed.");
            }
        }
    }
}
