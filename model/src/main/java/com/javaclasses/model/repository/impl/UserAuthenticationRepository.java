package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.repository.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Repository} implementation for authenticated users
 */
public class UserAuthenticationRepository implements Repository<User,Token> {

    private Map<Token, User> authenticatedUsers = new HashMap<>();

    private static UserAuthenticationRepository userRepository;

    private UserAuthenticationRepository() {
    }

    public static UserAuthenticationRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserAuthenticationRepository();
        }

        return userRepository;
    }

    @Override
    public Token add(User user) {
        final Token token = new Token(user.getId().getId());
        authenticatedUsers.put(token, user);
        return token;
    }

    @Override
    public User find(Token token) {
        return authenticatedUsers.get(token);
    }

    @Override
    public Collection<User> findAll() {
        return authenticatedUsers.values();
    }
}
