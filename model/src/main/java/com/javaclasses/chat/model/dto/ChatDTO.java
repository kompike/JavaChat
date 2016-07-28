package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.entity.tinytype.UserName;

import java.util.Set;

/**
 * Data transfer object for chat entity
 */
public class ChatDTO {

    private ChatId chatId;
    private UserId owner;
    private ChatName chatName;
    private Set<UserName> users;

    public ChatDTO(ChatId chatId, UserId owner, ChatName chatName, Set<UserName> users) {
        this.chatId = chatId;
        this.owner = owner;
        this.chatName = chatName;
        this.users = users;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public UserId getOwner() {
        return owner;
    }

    public ChatName getChatName() {
        return chatName;
    }

    public Set<UserName> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDTO chatDTO = (ChatDTO) o;

        if (!chatId.equals(chatDTO.chatId)) return false;
        if (!owner.equals(chatDTO.owner)) return false;
        return chatName.equals(chatDTO.chatName);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
