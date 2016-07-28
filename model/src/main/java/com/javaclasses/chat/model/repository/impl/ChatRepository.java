package com.javaclasses.chat.model.repository.impl;

import com.javaclasses.chat.model.entity.Chat;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.repository.InMemoryRepository;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for chat entity
 */
public class ChatRepository extends InMemoryRepository<ChatId, Chat> {

    private static ChatRepository chatRepository;

    private AtomicLong idCounter = new AtomicLong(1);

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        if (chatRepository == null) {
            chatRepository = new ChatRepository();
        }

        return chatRepository;
    }

    public Chat findByName(String chatName) {
        final Collection<Chat> chats = findAll();
        Chat chat = null;

        for (Chat currentChat : chats) {
            if (currentChat.getChatName().getName().equals(chatName)) {
                chat = currentChat;
                break;
            }
        }

        return chat;
    }

    @Override
    protected ChatId generateId() {
        return new ChatId(idCounter.getAndIncrement());
    }
}
