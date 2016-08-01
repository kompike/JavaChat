package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.UserId;

import java.util.List;

/**
 * Data transfer object for chat entity
 */
public class ChatDTO {

    private ChatId chatId;
    private UserId owner;
    private String chatName;
    private List<MessageDTO> messages;

    public ChatDTO(ChatId chatId, UserId owner, String chatName, List<MessageDTO> messages) {
        this.chatId = chatId;
        this.owner = owner;
        this.chatName = chatName;
        this.messages = messages;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDTO chatDTO = (ChatDTO) o;

        if (!chatId.equals(chatDTO.chatId)) return false;
        if (!owner.equals(chatDTO.owner)) return false;
        if (!chatName.equals(chatDTO.chatName)) return false;
        return messages.equals(chatDTO.messages);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
