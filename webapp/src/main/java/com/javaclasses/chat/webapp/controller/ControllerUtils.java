package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.UserService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static com.javaclasses.chat.webapp.controller.Constants.*;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * Util class for controllers
 */
/*package*/ final class ControllerUtils {

    private final static ChatService CHAT_SERVICE = ChatServiceImpl.getInstance();
    private final static UserService USER_SERVICE = UserServiceImpl.getInstance(CHAT_SERVICE);

    private ControllerUtils(){
    }

    /*package*/ static UserDTO getUserByToken(HttpServletRequest request) {

        final String requestTokenId = request.getParameter(TOKEN_ID_PARAMETER);
        final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));

        return USER_SERVICE.findByToken(tokenId);
    }

    /*package*/ static JsonObject getUserNotAuthorizedJson(JsonObject jsonObject) {

        jsonObject.add(ERROR_MESSAGE_PARAMETER, "User not authorized");
        jsonObject.setResponseStatusCode(SC_FORBIDDEN);

        return jsonObject;
    }

    /*package*/ static String getChatList() {

        final StringBuilder builder = new StringBuilder("[");

        final Collection<ChatDTO> chatList = CHAT_SERVICE.findAll();

        for (ChatDTO chatDTO : chatList) {
            final JsonObject chatJson = new JsonObject();
            chatJson.add(CHAT_ID_PARAMETER, String.valueOf(chatDTO.getChatId().getId()));
            chatJson.add(CHAT_NAME_PARAMETER, chatDTO.getChatName());
            builder.append(chatJson.generateJson()).append(",");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }

    /*package*/ static String getChatMessages(ChatId chatId) {

        final StringBuilder builder = new StringBuilder("[");

        final Collection<MessageDTO> messages = CHAT_SERVICE.getChatMessages(chatId);

        for (MessageDTO messageDTO : messages) {
            final JsonObject chatJson = new JsonObject();
            final String author = USER_SERVICE.findById(messageDTO.getAuthor()).getUserName();
            chatJson.add(MESSAGE_PARAMETER, messageDTO.getMessage());
            chatJson.add(COLOR_PARAMETER, messageDTO.getColor().toString());
            chatJson.add(AUTHOR_PARAMETER, author);
            builder.append(chatJson.generateJson()).append(",");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }
}
