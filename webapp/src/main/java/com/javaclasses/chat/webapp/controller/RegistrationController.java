package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.service.UserRegistrationException;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.HandlerRegistry;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Realization of {@link Handler} interface for registration process
 */
public class RegistrationController {

    private final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService = UserServiceImpl.getInstance();

    private RegistrationController() {
        HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();
        handlerRegistry.registerHandler(new RequestContext("/register", "post"), request -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing registration request...");
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
                jsonObject.setResponseStatusCode(200);
            } catch (UserRegistrationException e) {
                jsonObject.add("errorMessage", e.getMessage());
                jsonObject.setResponseStatusCode(500);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Registration request successfully processed.");
                }
            }
        });
    }

    public static RegistrationController init() {
        return new RegistrationController();
    }
}
