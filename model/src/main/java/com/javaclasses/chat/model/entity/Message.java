package com.javaclasses.chat.model.entity;

import com.javaclasses.chat.model.entity.tinytype.TextColor;
import com.javaclasses.chat.model.entity.tinytype.UserId;

/**
 * Entity of chat message instance
 */
public class Message {

    private String message;
    private UserId author;
    private TextColor color;

    public Message(String message, UserId author, TextColor color) {
        this.message = message;
        this.author = author;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public UserId getAuthor() {
        return author;
    }

    public TextColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (!message.equals(message1.message)) return false;
        if (!author.equals(message1.author)) return false;
        return color.equals(message1.color);

    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}
