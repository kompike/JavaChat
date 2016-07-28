package com.javaclasses.chat.model.entity.tinytype;

/**
 * Tiny type for chat name
 */
public class ChatName {

    private final String name;

    public ChatName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatName userName = (ChatName) o;

        return name.equals(userName.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
