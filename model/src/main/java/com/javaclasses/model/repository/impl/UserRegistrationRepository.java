package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * {@link InMemoryRepository} implementation for user entity
 */
public class UserRegistrationRepository extends InMemoryRepository<User, UserId> {

    private final Logger log = LoggerFactory.getLogger(UserRegistrationRepository.class);

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

        if (log.isInfoEnabled()) {
            log.info("Adding new user to the storage...");
        }

        final UserId userId = new UserId(idCounter++);
        user.setUserId(userId);
        add(user, userId);

        try {
            return userId;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("New user added to the storage");
            }
        }
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
}
