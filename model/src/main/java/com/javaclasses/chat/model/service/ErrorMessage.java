package com.javaclasses.chat.model.service;

/**
 * Enum of error messages
 */
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with given username already exists."),
    NICKNAME_CANNOT_CONTAIN_GAPS("Nickname cannot contain gaps."),
    ALL_FIELDS_MUST_BE_FILLED("All fields must be filled."),
    PASSWORDS_DOES_NOT_MATCH("Passwords does not match."),
    INCORRECT_CREDENTIALS("Incorrect login/password."),

    CHAT_ALREADY_EXISTS("Chat with given name already exists."),
    CHAT_NAME_CANNOT_BE_EMPTY("Chat name cannot be empty."),

    USER_ALREADY_JOINED("You already joined this chat."),
    USER_ALREADY_LEFT("You already left this chat."),

    USER_IS_NOT_IN_CHAT("You must join the chat to post messages."),
    NOT_ALLOWED_TO_POST_EMPTY_MESSAGE("You cannot post empty message.");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
