package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching user authentication failures
 */
public class UserAuthenticationException extends Exception {

    private final ErrorMessage errorMessage;

    public UserAuthenticationException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage.toString();
    }
}
