package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching chat joining failures
 */
public class ChatMembershipException extends Exception {

    private final ErrorMessage errorMessage;

    public ChatMembershipException(ErrorMessage message) {
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage.toString();
    }
}
