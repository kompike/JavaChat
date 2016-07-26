package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Repository} implementation for user entity
 */
public class UserRegistrationRepository implements Repository<User, UserId> {

    private Map<UserId, User> users = new HashMap<>();

    private long idCounter = 1;

    private static UserRegistrationRepository userRegistrationRepository;

    private UserRegistrationRepository() {
    }

    public static UserRegistrationRepository getInstance() {
        if (userRegistrationRepository == null) {
            userRegistrationRepository = new UserRegistrationRepository();
        }

        return userRegistrationRepository;
    }

    @Override
    public UserId add(User user) {
        final UserId userId = new UserId(idCounter++);
        user.setId(userId);
        users.put(userId, user);
        return userId;
    }

    @Override
    public User find(UserId userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    public User findByNickname(String nickname) {
        final Collection<User> users = findAll();
        User user = null;

        for (User currentUser : users) {
            if (currentUser.getNickname().equals(nickname)) {
                user = currentUser;
                break;
            }
        }

        return user;
    }
}
