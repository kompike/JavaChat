package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching chat creation failures
 */
public class ChatCreationException extends Exception {

    public ChatCreationException(String message) {
        super(message);
    }
}
