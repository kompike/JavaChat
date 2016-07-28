package com.javaclasses.chat.model.service;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;

import java.util.Collection;

/**
 * Basic interface for chat management
 */
public interface ChatService {

    ChatId createChat(UserId userId, ChatName chatName)
            throws ChatCreationException;

    ChatDTO joinChat(UserId userId, ChatId chatId)
            throws ChatJoiningException;

    ChatDTO leaveChat(UserId userId, ChatId chatId) throws ChatLeavingException;

    Collection<ChatDTO> findAll();

    ChatDTO findByName(ChatName chatName);

    ChatDTO findById(ChatId chatId);

    void delete(ChatId chatId);
}
