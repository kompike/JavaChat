package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.repository.InMemoryRepository;

import java.util.Collection;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRepository extends InMemoryRepository<UserId, User> {

    private static final Object ID_LOCK = new Object();

    private static UserRepository userRepository;

    private long idCounter = 1;

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
        synchronized (ID_LOCK) {
            return new UserId(idCounter++);
        }
    }
}
