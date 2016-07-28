package com.javaclasses.chat.model.entity;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Chat entity implementation
 */
public class Chat implements Entity<ChatId> {

    private ChatId chatId;
    private ChatName chatName;
    private UserId owner;
    private Set<UserId> users;
    private List<Message> messages;

    @Override
    public ChatId getId() {
        return chatId;
    }

    @Override
    public void setId(ChatId id) {
        this.chatId = id;
    }

    public Chat(ChatName chatName, UserId owner) {
        this.chatName = chatName;
        this.owner = owner;
        this.users = new HashSet<>();
        this.messages = new ArrayList<>();
    }

    public ChatName getChatName() {
        return chatName;
    }

    public UserId getOwner() {
        return owner;
    }

    public Set<UserId> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean addUser(UserId userId) {
        return users.add(userId);
    }

    public boolean removeUser(UserId userId) {
        return users.remove(userId);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
