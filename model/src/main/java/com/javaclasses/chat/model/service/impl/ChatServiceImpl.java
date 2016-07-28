package com.javaclasses.chat.model.service.impl;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.service.ChatCreationException;
import com.javaclasses.chat.model.service.ChatJoiningException;
import com.javaclasses.chat.model.service.ChatLeavingException;
import com.javaclasses.chat.model.service.ChatService;

import java.util.Collection;

/**
 * Implementation of {@link ChatService} interface
 */
public class ChatServiceImpl implements ChatService {

    @Override
    public ChatId createChat(UserId userId, ChatName chatName)
            throws ChatCreationException {
        return null;
    }

    @Override
    public ChatDTO joinChat(UserId userId, ChatId chatId)
            throws ChatJoiningException {
        return null;
    }

    @Override
    public ChatId leaveChat(UserId userId, ChatId chatId)
            throws ChatLeavingException {
        return null;
    }

    @Override
    public Collection<ChatDTO> findAll() {
        return null;
    }

    @Override
    public ChatDTO findByName(ChatName chatName) {
        return null;
    }

    @Override
    public ChatDTO findById(ChatId chatId) {
        return null;
    }

    @Override
    public void delete(ChatId chatId) {

    }
}
