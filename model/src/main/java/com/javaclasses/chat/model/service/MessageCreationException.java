package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching adding new message failures
 */
public class MessageCreationException extends Exception {

    private final ErrorMessage errorMessage;

    public MessageCreationException(ErrorMessage message) {
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage.toString();
    }
}
