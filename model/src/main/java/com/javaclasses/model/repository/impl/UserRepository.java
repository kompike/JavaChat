package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.InMemoryRepository;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRepository extends InMemoryRepository<User, UserId> {

    private long idCounter = 1;

    private static UserRepository userRepository;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }

        return userRepository;
    }

    @Override
    public UserId nextId() {
        return new UserId(idCounter++);
    }
}
