package com.javaclasses.model.service;

/**
 * Custom exception for catching user authentication failures
 */
public class UserAuthenticationException extends Exception {

    public UserAuthenticationException(String message) {
        super(message);
    }
}