package com.javaclasses.chat.model.service;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
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
            throws ChatMembershipException;

    ChatDTO leaveChat(UserId userId, ChatId chatId)
            throws ChatMembershipException;

    Collection<ChatDTO> findAll();

    ChatDTO findByName(ChatName chatName);

    ChatDTO findById(ChatId chatId);

    Collection<ChatDTO> findChatsByUser(UserId userId);

    Collection<UserId> getChatUsers(ChatId chatId);

    void addMessage(MessageDTO messageDTO)
            throws MessageCreationException;

    Collection<MessageDTO> getChatMessages(ChatId chatId);

    void delete(ChatId chatId);
}
