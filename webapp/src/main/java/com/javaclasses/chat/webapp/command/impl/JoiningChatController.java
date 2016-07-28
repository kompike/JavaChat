package com.javaclasses.chat.webapp.command.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatJoiningException;
import com.javaclasses.chat.model.service.ChatService;
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
 * Implementation of {@link Handler} interface for joining chat process
 */
public class JoiningChatController implements Handler {

    private final Logger log = LoggerFactory.getLogger(JoiningChatController.class);

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
        final ChatDTO chatDTO = chatService.findById(chatId);

        final JsonObject jsonObject = new JsonObject();

        try {
            chatService.joinChat(user.getUserId(), chatId);
            jsonObject.add("chatId", String.valueOf(chatDTO.getChatId().getId()));
            jsonObject.add("chatName", chatDTO.getChatName());

            final StringBuilder builder = new StringBuilder("[");

            final Collection<MessageDTO> messages = chatService.getChatMessages(chatId);
            for (MessageDTO messageDTO : messages) {
                final JsonObject chatJson = new JsonObject();
                chatJson.add("message", String.valueOf(chatDTO.getChatId().getId()));
                final String author = userService.findById(messageDTO.getAuthor()).getUserName();
                chatJson.add("author", author);
                builder.append(chatJson.generateJson()).append(",");
            }

            if (builder.length() > 1) {
                builder.setLength(builder.length() - 1);
            }
            builder.append("]");

            jsonObject.add("messageList", builder.toString());
            jsonObject.setResponseStatusCode(200);
        } catch (ChatJoiningException e) {
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
