package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching chat creation failures
 */
public class ChatCreationException extends Exception {

    private final ErrorMessage errorMessage;

    public ChatCreationException(ErrorMessage message) {
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage.toString();
    }
}
