package com.javaclasses.chat.model.service;

/**
 * Custom exception for catching chat joining failures
 */
public class ChatJoiningException extends Exception {

    public ChatJoiningException(String message) {
        super(message);
    }
}
