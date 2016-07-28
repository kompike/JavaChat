package com.javaclasses.chat.webapp.command.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
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
 * Implementation of {@link Handler} interface for chat creation process
 */
public class ChatCreationController implements Handler {

    private final Logger log = LoggerFactory.getLogger(ChatCreationController.class);

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public JsonObject process(HttpServletRequest request) {

        if (log.isInfoEnabled()) {
            log.info("Start processing user request...");
        }

        final String chatName = request.getParameter("chatName");
        final String tokenId = request.getParameter("tokenId");

        final TokenId id = new TokenId(Long.valueOf(tokenId));
        final UserDTO user = userService.findByToken(id);

        final JsonObject jsonObject = new JsonObject();
        try {
            chatService.createChat(user.getUserId(), new ChatName(chatName));

            final StringBuilder builder = new StringBuilder("[");

            final Collection<ChatDTO> chatList = chatService.findAll();
            for (ChatDTO chatDTO : chatList) {
                builder.append(chatDTO.getChatName()).append(",");
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
                log.info("User request successfully processed.");
            }
        }
    }
}
