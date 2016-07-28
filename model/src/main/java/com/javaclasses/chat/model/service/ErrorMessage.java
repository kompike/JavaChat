package com.javaclasses.chat.model.service;

/**
 * Enum of error messages
 */
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with given username already exists."),
    NICKNAME_CANNOT_CONTAIN_GAPS("Nickname cannot contain gaps."),
    ALL_FIELDS_MUST_BE_FILLED("All fields must be filled."),
    PASSWORDS_DOES_NOT_MATCH("Passwords does not match."),
    INCORRECT_CREDENTIALS("Incorrect login/password.");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
