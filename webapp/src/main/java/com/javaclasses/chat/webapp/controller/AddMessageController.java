package com.javaclasses.chat.webapp.controller;


import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TextColor;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.MessageCreationException;
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
 * Realization of {@link Handler} interface for adding message process
 */
public class AddMessageController {

    private final Logger log = LoggerFactory.getLogger(AddMessageController.class);

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private AddMessageController() {
        HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();
        handlerRegistry.registerHandler(new RequestContext("/add-message", "post"), request -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing adding message request...");
            }

            final JsonObject jsonObject = new JsonObject();

            final String requestChatName = request.getParameter("chatName");
            final String requestTokenId = request.getParameter("tokenId");
            final String requestMessage = request.getParameter("message");
            final String messageColor = request.getParameter("color");

            if (requestTokenId == null) {
                jsonObject.add("errorMessage", "User not authorized");
                jsonObject.setResponseStatusCode(403);
                return jsonObject;
            }

            final ChatName chatName = new ChatName(requestChatName);
            final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));
            final UserDTO user = userService.findByToken(tokenId);
            final ChatDTO chat = chatService.findByName(chatName);

            if (user == null) {
                jsonObject.add("errorMessage", "User not authorized");
                jsonObject.setResponseStatusCode(403);
                return jsonObject;
            }

            try {
                final MessageDTO messageDTO =
                        new MessageDTO(requestMessage, user.getUserId(),
                                chat.getChatId(), new TextColor(messageColor));

                chatService.addMessage(messageDTO);

                jsonObject.add("chatId", chat.getChatId().toString());

                final StringBuilder builder = new StringBuilder("[");

                final Collection<MessageDTO> chatMessages = chatService.getChatMessages(chat.getChatId());

                for (MessageDTO message : chatMessages) {
                    final JsonObject chatJson = new JsonObject();
                    chatJson.add("message", message.getMessage());
                    final UserDTO author = userService.findById(message.getAuthor());
                    chatJson.add("author", author.getUserName());
                    builder.append(chatJson.generateJson()).append(",");
                }

                if (builder.length() > 1) {
                    builder.setLength(builder.length() - 1);
                }
                builder.append("]");

                jsonObject.add("messages", builder.toString());
                jsonObject.setResponseStatusCode(200);
            } catch (MessageCreationException e) {
                jsonObject.add("errorMessage", e.getMessage());
                jsonObject.setResponseStatusCode(500);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Adding message request successfully processed.");
                }
            }
        });
    }

    public static AddMessageController init() {
        return new AddMessageController();
    }
}
