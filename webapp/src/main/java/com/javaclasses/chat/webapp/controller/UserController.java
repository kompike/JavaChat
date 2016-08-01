package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.LoginDTO;
import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.dto.TokenDTO;
import com.javaclasses.chat.model.service.UserAuthenticationException;
import com.javaclasses.chat.model.service.UserRegistrationException;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.HandlerRegistry;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static com.javaclasses.chat.webapp.controller.Constants.*;

/**
 * Realization of {@link Handler} interface for user management
 */
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService = UserServiceImpl.getInstance(ChatServiceImpl.getInstance());
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController() {
        loginUser();
        registerUser();
    }

    private void loginUser() {
        handlerRegistry.registerHandler(new RequestContext(LOGIN_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing login request...");
            }

            final String nickname = request.getParameter(NICKNAME_PARAMETER);
            final String password = request.getParameter(PASSWORD_PARAMETER);

            final LoginDTO loginDTO =
                    new LoginDTO(nickname, password);

            final JsonObject jsonObject = new JsonObject();
            try {
                final TokenDTO tokenDTO = userService.login(loginDTO);
                jsonObject.add(TOKEN_ID_PARAMETER, tokenDTO.getTokenId().toString());
                jsonObject.add(USER_NAME_PARAMETER, nickname);
                jsonObject.add(MESSAGE_PARAMETER, "User successfully logged in");
                jsonObject.setResponseStatusCode(SC_OK);
            } catch (UserAuthenticationException e) {
                System.out.println(e.getMessage());
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
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

    private void registerUser() {
        handlerRegistry.registerHandler(new RequestContext(USER_REGISTRATION_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing registration request...");
            }

            final String nickname = request.getParameter(NICKNAME_PARAMETER);
            final String password = request.getParameter(PASSWORD_PARAMETER);
            final String confirmPassword = request.getParameter(CONFIRM_PASSWORD_PARAMETER);

            final RegistrationDTO registrationDTO =
                    new RegistrationDTO(nickname, password, confirmPassword);

            final JsonObject jsonObject = new JsonObject();
            try {
                userService.register(registrationDTO);
                jsonObject.add(MESSAGE_PARAMETER, "User successfully registered");
                jsonObject.setResponseStatusCode(SC_OK);
            } catch (UserRegistrationException e) {
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
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

    public static UserController init() {
        return new UserController();
    }
}
