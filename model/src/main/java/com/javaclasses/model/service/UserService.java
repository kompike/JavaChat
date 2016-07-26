package com.javaclasses.model.service;

import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.entity.tynitype.UserId;

/**
 * Basic interface for user management
 */
public interface UserService {

    UserId register(String nickname, String pssword, String confirmPassword)
            throws UserRegistrationException;

    Token login(String nickname, String password)
            throws UserAuthenticationException;
}
