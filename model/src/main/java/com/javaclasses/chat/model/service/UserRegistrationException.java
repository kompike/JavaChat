package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching user registration failures
 */
public class UserRegistrationException extends Exception {

    private final ErrorMessage errorMessage;

    public UserRegistrationException(ErrorMessage message) {
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage.toString();
    }
}