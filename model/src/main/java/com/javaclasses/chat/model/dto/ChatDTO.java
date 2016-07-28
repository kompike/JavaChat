package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.entity.tinytype.UserName;

import java.util.Set;

/**
 * Data transfer object for chat entity
 */
public class ChatDTO {

    private ChatId chatId;
    private UserId owner;
    private String chatName;
    private Set<String> userNameList;

    public ChatDTO(ChatId chatId, UserId owner, String chatName, Set<String> userNameList) {
        this.chatId = chatId;
        this.owner = owner;
        this.chatName = chatName;
        this.userNameList = userNameList;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public UserId getOwner() {
        return owner;
    }

    public String getChatName() {
        return chatName;
    }

    public Set<String> getUserNameList() {
        return userNameList;
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
