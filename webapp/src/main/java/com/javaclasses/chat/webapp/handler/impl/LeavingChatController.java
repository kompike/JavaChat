package com.javaclasses.chat.webapp.handler.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatLeavingException;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
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

        final JsonObject jsonObject = new JsonObject();

        final String requestChatName = request.getParameter("chatName");
        final String requestTokenId = request.getParameter("tokenId");

        if (requestTokenId == null) {
            jsonObject.add("errorMessage", "User not authorized");
            jsonObject.setResponseStatusCode(403);
            return jsonObject;
        }

        final ChatName chatName = new ChatName(requestChatName);
        final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));
        final UserDTO user = userService.findByToken(tokenId);

        if (user == null) {
            jsonObject.add("errorMessage", "User not authorized");
            jsonObject.setResponseStatusCode(403);
            return jsonObject;
        }

        final ChatDTO chat = chatService.findByName(chatName);

        try {
            chatService.leaveChat(user.getUserId(), chat.getChatId());
            jsonObject.add("chatId", chat.getChatId().toString());
            jsonObject.setResponseStatusCode(200);
        } catch (ChatLeavingException e) {
            jsonObject.add("errorMessage", e.getMessage());
            jsonObject.setResponseStatusCode(500);
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
