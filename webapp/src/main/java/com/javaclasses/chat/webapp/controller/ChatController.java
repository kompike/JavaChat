package com.javaclasses.chat.webapp.controller;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TextColor;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.service.*;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import com.javaclasses.chat.webapp.HandlerRegistry;
import com.javaclasses.chat.webapp.JsonObject;
import com.javaclasses.chat.webapp.handler.Handler;
import com.javaclasses.chat.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.*;
import static com.javaclasses.chat.webapp.controller.Constants.*;

/**
 * Realization of {@link Handler} interface for chat management
 */
public class ChatController {

    private final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance(chatService);

    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private ChatController() {
        createChat();
        joinChat();
        leaveChat();
        addMessage();
    }

    private void createChat() {
        handlerRegistry.registerHandler(new RequestContext(CREATE_CHAT_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing creating chat request...");
            }

            final JsonObject jsonObject = new JsonObject();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonObject);
            }

            final String chatName = request.getParameter(CHAT_NAME_PARAMETER);

            try {
                chatService.createChat(user.getUserId(), new ChatName(chatName));
                final String chats = getChatList();

                jsonObject.add(CHAT_LIST_PARAMETER, chats);
                jsonObject.setResponseStatusCode(SC_OK);

            } catch (ChatCreationException e) {
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
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

    private void joinChat() {
        handlerRegistry.registerHandler(new RequestContext(JOIN_CHAT_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing joining chat request...");
            }

            final JsonObject jsonObject = new JsonObject();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonObject);
            }

            final String requestChatName = request.getParameter(CHAT_NAME_PARAMETER);
            final ChatDTO chatDTO = chatService.findByName(new ChatName(requestChatName));

            try {
                final ChatId chatId = chatDTO.getChatId();
                chatService.joinChat(user.getUserId(), chatId);
                final String messages = getChatMessages(chatId);
                jsonObject.add(CHAT_ID_PARAMETER, String.valueOf(chatId.getId()));
                jsonObject.add(CHAT_NAME_PARAMETER, chatDTO.getChatName());
                jsonObject.add(MESSAGES_PARAMETER, messages);
                jsonObject.setResponseStatusCode(SC_OK);
            } catch (ChatMembershipException e) {
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Joining chat request successfully processed.");
                }
            }
        });
    }

    private void leaveChat() {
        handlerRegistry.registerHandler(new RequestContext(LEAVE_CHAT_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing leaving chat request...");
            }

            final JsonObject jsonObject = new JsonObject();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonObject);
            }

            final String requestChatName = request.getParameter(CHAT_NAME_PARAMETER);
            final ChatDTO chat = chatService.findByName(new ChatName(requestChatName));

            try {
                chatService.leaveChat(user.getUserId(), chat.getChatId());
                jsonObject.add(CHAT_ID_PARAMETER, chat.getChatId().toString());
                jsonObject.setResponseStatusCode(SC_OK);
            } catch (ChatMembershipException e) {
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
            }

            try {
                return jsonObject;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Leaving chat request successfully processed.");
                }
            }
        });
    }

    private void addMessage() {
        handlerRegistry.registerHandler(new RequestContext(ADD_MESSAGE_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing adding message request...");
            }

            final JsonObject jsonObject = new JsonObject();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonObject);
            }

            final String requestChatName = request.getParameter(CHAT_NAME_PARAMETER);
            final String requestMessage = request.getParameter(MESSAGE_PARAMETER);
            final String messageColor = request.getParameter(COLOR_PARAMETER);
            final ChatDTO chat = chatService.findByName(new ChatName(requestChatName));

            try {
                final ChatId chatId = chat.getChatId();
                final MessageDTO messageDTO =
                        new MessageDTO(requestMessage, user.getUserId(),
                                chatId, new TextColor(messageColor));
                chatService.addMessage(messageDTO);
                final String messages = getChatMessages(chatId);
                jsonObject.add(CHAT_ID_PARAMETER, chatId.toString());
                jsonObject.add(MESSAGES_PARAMETER, messages);
                jsonObject.setResponseStatusCode(SC_OK);
            } catch (MessageCreationException e) {
                jsonObject.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonObject.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
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

    private UserDTO getUserByToken(HttpServletRequest request) {

        final String requestTokenId = request.getParameter(TOKEN_ID_PARAMETER);
        final TokenId tokenId = new TokenId(Long.valueOf(requestTokenId));

        return userService.findByToken(tokenId);
    }

    private JsonObject getUserNotAuthorizedJson(JsonObject jsonObject) {

        jsonObject.add(ERROR_MESSAGE_PARAMETER, "User not authorized");
        jsonObject.setResponseStatusCode(SC_FORBIDDEN);

        return jsonObject;
    }

    private String getChatList() {

        final StringBuilder builder = new StringBuilder("[");

        final Collection<ChatDTO> chatList = chatService.findAll();

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

    private String getChatMessages(ChatId chatId) {

        final StringBuilder builder = new StringBuilder("[");

        final Collection<MessageDTO> messages = chatService.getChatMessages(chatId);

        for (MessageDTO messageDTO : messages) {
            final JsonObject chatJson = new JsonObject();
            final String author = userService.findById(messageDTO.getAuthor()).getUserName();
            chatJson.add(MESSAGE_PARAMETER, messageDTO.getMessage());
            chatJson.add(AUTHOR_PARAMETER, author);
            builder.append(chatJson.generateJson()).append(",");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }

    public static ChatController init() {
        return new ChatController();
    }
}