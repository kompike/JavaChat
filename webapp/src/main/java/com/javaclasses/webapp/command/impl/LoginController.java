package com.javaclasses.webapp.command.impl;

import com.javaclasses.model.dto.LoginDTO;
import com.javaclasses.model.dto.TokenDTO;
import com.javaclasses.model.service.UserAuthenticationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import com.javaclasses.webapp.JsonObject;
import com.javaclasses.webapp.command.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Handler} interface for login process
 */
public class LoginController implements Handler {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public JsonObject process(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
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
            jsonObject.add("responseStatus", "200");
        } catch (UserAuthenticationException e) {
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
