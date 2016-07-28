package com.javaclasses.chat.model.service.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.entity.Chat;
import com.javaclasses.chat.model.entity.User;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.entity.tinytype.UserName;
import com.javaclasses.chat.model.repository.impl.ChatRepository;
import com.javaclasses.chat.model.repository.impl.UserRepository;
import com.javaclasses.chat.model.service.ChatCreationException;
import com.javaclasses.chat.model.service.ChatJoiningException;
import com.javaclasses.chat.model.service.ChatLeavingException;
import com.javaclasses.chat.model.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.javaclasses.chat.model.service.ErrorMessage.*;

/**
 * Implementation of {@link ChatService} interface
 */
public class ChatServiceImpl implements ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private static ChatServiceImpl chatService;

    private final ChatRepository chatRepository =
            ChatRepository.getInstance();
    private final UserRepository userRepository =
            UserRepository.getInstance();

    private ChatServiceImpl() {
    }

    public static ChatServiceImpl getInstance() {
        if (chatService == null) {
            chatService = new ChatServiceImpl();
        }

        return chatService;
    }

    @Override
    public ChatId createChat(UserId userId, ChatName chatName)
            throws ChatCreationException {

        if (log.isInfoEnabled()) {
            log.info("Start creating new chat...");
        }

        final String createdChatName = chatName.getName().trim();

        checkNotNull(userId, "Chat id cannot be null");
        checkNotNull(createdChatName, "Chat name cannot be null");

        if (chatRepository.findByName(createdChatName) != null) {

            if (log.isWarnEnabled()) {
                log.warn(CHAT_ALREADY_EXISTS.toString());
            }

            throw new ChatCreationException(CHAT_ALREADY_EXISTS.toString());
        }

        if (createdChatName.isEmpty()) {

            if (log.isWarnEnabled()) {
                log.warn(CHAT_NAME_CANNOT_BE_EMPTY.toString());
            }

            throw new ChatCreationException(CHAT_NAME_CANNOT_BE_EMPTY.toString());
        }

        final Chat chat = new Chat(chatName, userId, new HashSet<>());

        try {
            return chatRepository.add(chat);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Chat successfully created.");
            }
        }
    }

    @Override
    public ChatDTO joinChat(UserId userId, ChatId chatId)
            throws ChatJoiningException {

        final Chat chat = chatRepository.findById(chatId);
        final User user = userRepository.findById(userId);

        if (!chat.getUsers().add(user.getUserName())) {
            throw new ChatJoiningException(USER_ALREADY_JOINED.toString());
        }

        return createChatDTOFromChat(chat);
    }

    @Override
    public ChatDTO leaveChat(UserId userId, ChatId chatId)
            throws ChatLeavingException {

        final Chat chat = chatRepository.findById(chatId);
        final User user = userRepository.findById(userId);

        if (!chat.getUsers().remove(user.getUserName())) {
            throw new ChatLeavingException(USER_ALREADY_LEFT.toString());
        }

        return createChatDTOFromChat(chat);
    }

    @Override
    public Collection<ChatDTO> findAll() {

        if (log.isInfoEnabled()) {
            log.info("Start looking for all created chats...");
        }

        final Collection<Chat> chats = chatRepository.findAll();

        final Collection<ChatDTO> chatDTOList = new ArrayList<>();

        for (Chat chat : chats) {
            chatDTOList.add(createChatDTOFromChat(chat));
        }

        try {
            return chatDTOList;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Found " + chatDTOList.size() + " chats.");
            }
        }
    }

    @Override
    public ChatDTO findByName(ChatName chatName) {

        final String name = chatName.getName();

        if (log.isInfoEnabled()) {
            log.info("Start looking for chat with name: " + name);
        }

        final Chat chat = chatRepository.findByName(name);

        try {
            return createChatDTOFromChat(chat);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Chat successfully found.");
            }
        }
    }

    @Override
    public ChatDTO findById(ChatId chatId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for chat with id: " + chatId);
        }

        final Chat chat = chatRepository.findById(chatId);

        try {
            return createChatDTOFromChat(chat);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Chat successfully found.");
            }
        }
    }

    @Override
    public void delete(ChatId chatId) {

        if (log.isInfoEnabled()) {
            log.info("Start deleting chat with id: " + chatId.getId());
        }

        chatRepository.delete(chatId);

        if (log.isInfoEnabled()) {
            log.info("Chat successfully deleted.");
        }
    }

    private ChatDTO createChatDTOFromChat(Chat chat) {

        final Set<String> userNameList = new HashSet<>();

        for (UserName userName : chat.getUsers()) {
            userNameList.add(userName.getName());
        }

        return new ChatDTO(chat.getId(),
                chat.getOwner(), chat.getChatName().getName(), userNameList);
    }
}
