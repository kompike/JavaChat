package com.javaclasses.chat.webapp.command.impl;

import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatLeavingException;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.command.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link Handler} interface for leaving chat process
 */
public class LeavingChatController implements Handler {

    private final Logger log = LoggerFactory.getLogger(LeavingChatController.class);

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public JsonObject process(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
        }

        final String requestChatId = request.getParameter("chatId");
        final String requestTokenId = request.getParameter("tokenId");

        final ChatId chatId = new ChatId(Long.valueOf(requestChatId));
        final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));
        final UserDTO user = userService.findByToken(tokenId);

        final JsonObject jsonObject = new JsonObject();

        try {
            chatService.leaveChat(user.getUserId(), chatId);
            jsonObject.add("chatId", String.valueOf(chatId));
            jsonObject.setResponseStatusCode(200);
        } catch (ChatLeavingException e) {
            jsonObject.add("errorMessage", e.getMessage());
            jsonObject.setResponseStatusCode(500);
        }

        try {
            return null;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("User request successfully processed.");
            }
        }
    }
}
