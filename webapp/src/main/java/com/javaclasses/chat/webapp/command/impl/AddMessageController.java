package com.javaclasses.chat.webapp.command.impl;


import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.MessageCreationException;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.command.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Implementation of {@link Handler} interface for adding message process
 */
public class AddMessageController implements Handler {

    private final Logger log = LoggerFactory.getLogger(AddMessageController.class);

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public JsonObject process(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
        }

        final String requestChatId = request.getParameter("chatId");
        final String requestTokenId = request.getParameter("tokenId");
        final String requestMessage = request.getParameter("message");

        final ChatId chatId = new ChatId(Long.valueOf(requestChatId));
        final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));
        final UserDTO user = userService.findByToken(tokenId);

        final JsonObject jsonObject = new JsonObject();

        try {
            chatService.addMessage(chatId, user.getUserId(), requestMessage);
            jsonObject.add("chatId", String.valueOf(chatId.getId()));

            final StringBuilder builder = new StringBuilder("[");

            final Collection<MessageDTO> chatMessages = chatService.getChatMessages(chatId);

            for (MessageDTO messageDTO : chatMessages) {
                final JsonObject chatJson = new JsonObject();
                chatJson.add("message", messageDTO.getMessage());
                final UserDTO author = userService.findById(messageDTO.getAuthor());
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
            return null;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("User request successfully processed.");
            }
        }
    }
}
