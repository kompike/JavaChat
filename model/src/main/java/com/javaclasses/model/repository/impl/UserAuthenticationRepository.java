package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.repository.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link InMemoryRepository} implementation for authenticated users
 */
public class UserAuthenticationRepository extends InMemoryRepository<User,Token> {

    private final Logger log = LoggerFactory.getLogger(UserAuthenticationRepository.class);

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

        if (log.isInfoEnabled()) {
            log.info("Adding logged user to the storage...");
        }

        final Token token = new Token(user.getUserId().getUserId());
        add(user, token);

        try {
            return token;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Logged user added to the storage");
            }

        }
    }
}
