package com.javaclasses.chat.model.repository.impl;

import com.javaclasses.chat.model.entity.User;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.repository.InMemoryRepository;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRepository extends InMemoryRepository<UserId, User> {

    private static UserRepository userRepository;

    private AtomicLong idCounter = new AtomicLong(1);

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }

        return userRepository;
    }

    public User findByNickname(String nickname) {
        final Collection<User> users = findAll();
        User user = null;

        for (User currentUser : users) {
            if (currentUser.getUserName().getName().equals(nickname)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }

    @Override
    protected UserId generateId() {
        return new UserId(idCounter.getAndIncrement());
    }
}
