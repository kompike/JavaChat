package com.javaclasses.chat.model.entity;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.entity.tinytype.UserName;

import java.util.Set;

/**
 * Chat entity implementation
 */
public class Chat implements Entity<ChatId> {

    private ChatId chatId;
    private ChatName chatName;
    private UserId owner;
    private Set<UserName> users;

    @Override
    public ChatId getId() {
        return chatId;
    }

    @Override
    public void setId(ChatId id) {
        this.chatId = id;
    }

    public Chat(ChatName chatName, UserId owner, Set<UserName> users) {
        this.chatName = chatName;
        this.owner = owner;
        this.users = users;
    }

    public ChatName getChatName() {
        return chatName;
    }

    public void setChatName(ChatName chatName) {
        this.chatName = chatName;
    }

    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }

    public Set<UserName> getUsers() {
        return users;
    }

    public void setUsers(Set<UserName> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (!chatId.equals(chat.chatId)) return false;
        if (!chatName.equals(chat.chatName)) return false;
        return owner.equals(chat.owner);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
