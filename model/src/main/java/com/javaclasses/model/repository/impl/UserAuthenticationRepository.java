package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.repository.InMemoryRepository;

/**
 * {@link InMemoryRepository} implementation for authenticated users
 */
public class UserAuthenticationRepository extends InMemoryRepository<User,Token> {

    private static UserAuthenticationRepository userRepository;

    private UserAuthenticationRepository() {
    }

    public static UserAuthenticationRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserAuthenticationRepository();
        }

        return userRepository;
    }

    public Token login(User user) {
        final Token token = new Token(user.getUserId().getUserId());
        add(user, token);
        return token;
    }
}
