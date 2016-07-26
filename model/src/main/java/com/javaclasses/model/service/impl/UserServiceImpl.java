package com.javaclasses.model.service.impl;

import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.service.UserAuthenticationException;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

    private static UserServiceImpl userService;

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    @Override
    public UserId register(String nickname, String pssword, String confirmPassword)
            throws UserRegistrationException {
        return null;
    }

    @Override
    public Token login(String nickname, String password)
            throws UserAuthenticationException {
        return null;
    }
}
