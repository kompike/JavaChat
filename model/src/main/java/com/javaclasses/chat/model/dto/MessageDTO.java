package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.UserId;

/**
 * Data transfer object for message entity
 */
public class MessageDTO {

    private String message;
    private UserId author;
    private ChatId chatId;

    public MessageDTO(String message, UserId author, ChatId chatId) {
        this.message = message;
        this.author = author;
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public UserId getAuthor() {
        return author;
    }

    public ChatId getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        if (!message.equals(that.message)) return false;
        if (!author.equals(that.author)) return false;
        return chatId.equals(that.chatId);

    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + chatId.hashCode();
        return result;
    }
}
