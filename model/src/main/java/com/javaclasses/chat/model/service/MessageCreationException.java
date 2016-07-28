package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching adding new message failures
 */
public class MessageCreationException extends Exception {

    public MessageCreationException(String message) {
        super(message);
    }
}
