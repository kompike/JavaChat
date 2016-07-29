package com.javaclasses.chat.webapp.command.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
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

        final ChatDTO chatDTO = chatService.findByName(chatName);

        try {
            chatService.joinChat(user.getUserId(), chatDTO.getChatId());
            jsonObject.add("chatId", String.valueOf(chatDTO.getChatId().getId()));
            jsonObject.add("chatName", chatDTO.getChatName());

            final StringBuilder builder = new StringBuilder("[");

            final Collection<MessageDTO> messages = chatService.getChatMessages(chatDTO.getChatId());
            for (MessageDTO messageDTO : messages) {
                final JsonObject chatJson = new JsonObject();
                chatJson.add("message", messageDTO.getMessage());
                final String author = userService.findById(messageDTO.getAuthor()).getUserName();
                chatJson.add("author", author);
                builder.append(chatJson.generateJson()).append(",");
            }

            if (builder.length() > 1) {
                builder.setLength(builder.length() - 1);
            }
            builder.append("]");

            jsonObject.add("messages", builder.toString());
            jsonObject.setResponseStatusCode(200);
        } catch (ChatJoiningException e) {
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
