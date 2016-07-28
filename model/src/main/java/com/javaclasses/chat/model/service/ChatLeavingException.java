package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching chat leaving failures
 */
public class ChatLeavingException extends Exception {

    public ChatLeavingException(String message) {
        super(message);
    }
}
