package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.HandlerRegistry;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Realization of {@link Handler} interface for chat creation process
 */
public class ChatCreationController {

    private final Logger log = LoggerFactory.getLogger(ChatCreationController.class);

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private ChatCreationController() {
        HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();
        handlerRegistry.registerHandler(new RequestContext("/create-chat", "post"), request -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing creating chat request...");
            }
            final JsonObject jsonObject = new JsonObject();

            final String chatName = request.getParameter("chatName");
            final String tokenId = request.getParameter("tokenId");

            if (tokenId == null) {
                jsonObject.add("errorMessage", "User not authorized");
                jsonObject.setResponseStatusCode(403);
                return jsonObject;
            }

            final TokenId id = new TokenId(Long.valueOf(tokenId));
            final UserDTO user = userService.findByToken(id);

            if (user == null) {
                jsonObject.add("errorMessage", "User not authorized");
                jsonObject.setResponseStatusCode(403);
                return jsonObject;
            }

            try {
                chatService.createChat(user.getUserId(), new ChatName(chatName));

                final StringBuilder builder = new StringBuilder("[");

                final Collection<ChatDTO> chatList = chatService.findAll();

                for (ChatDTO chatDTO : chatList) {
                    final JsonObject chatJson = new JsonObject();
                    chatJson.add("chatId", String.valueOf(chatDTO.getChatId().getId()));
                    chatJson.add("chatName", chatDTO.getChatName());
                    builder.append(chatJson.generateJson()).append(",");
                }

                if (builder.length() > 1) {
                    builder.setLength(builder.length() - 1);
                }
                builder.append("]");

                jsonObject.add("chatList", builder.toString());
                jsonObject.setResponseStatusCode(200);

            } catch (Exception e) {
                jsonObject.add("errorMessage", e.getMessage());
                jsonObject.setResponseStatusCode(500);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Chat creation request successfully processed.");
                }
            }
        });
    }

    public static ChatCreationController init() {
        return new ChatCreationController();
    }
}
