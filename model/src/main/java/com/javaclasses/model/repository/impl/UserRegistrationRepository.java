package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.InMemoryRepository;

import java.util.Collection;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRegistrationRepository extends InMemoryRepository<User, UserId> {

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

    public UserId register(User user) {
        final UserId userId = new UserId(idCounter++);
        user.setUserId(userId);
        add(user, userId);
        return userId;
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
