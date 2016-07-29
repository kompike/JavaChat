package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.LoginDTO;
import com.javaclasses.chat.model.dto.TokenDTO;
import com.javaclasses.chat.model.service.UserAuthenticationException;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.HandlerRegistry;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Realization of {@link Handler} interface for login process
 */
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService = UserServiceImpl.getInstance();

    private LoginController() {
        HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();
        handlerRegistry.registerHandler(new RequestContext("/login", "post"), request -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing login request...");
            }

            final String nickname = request.getParameter("nickname");
            final String password = request.getParameter("password");

            final LoginDTO loginDTO =
                    new LoginDTO(nickname, password);

            final JsonObject jsonObject = new JsonObject();
            try {
                final TokenDTO tokenDTO = userService.login(loginDTO);
                jsonObject.add("tokenId", String.valueOf(tokenDTO.getTokenId().getId()));
                jsonObject.add("userName", nickname);
                jsonObject.add("message", "User successfully logged in");
                jsonObject.setResponseStatusCode(200);
            } catch (UserAuthenticationException e) {
                jsonObject.add("errorMessage", e.getMessage());
                jsonObject.setResponseStatusCode(500);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Login request successfully processed.");
                }
            }
        });
    }

    public static LoginController init() {
        return new LoginController();
    }
}
