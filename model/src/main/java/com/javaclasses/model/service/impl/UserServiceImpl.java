package com.javaclasses.model.service.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.Password;
import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.entity.tynitype.UserName;
import com.javaclasses.model.repository.impl.UserAuthenticationRepository;
import com.javaclasses.model.repository.impl.UserRegistrationRepository;
import com.javaclasses.model.service.UserAuthenticationException;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl userService;

    private final UserRegistrationRepository userRegistrationRepository =
            UserRegistrationRepository.getInstance();
    private final UserAuthenticationRepository userAuthenticationRepository =
            UserAuthenticationRepository.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    @Override
    public UserId register(String nickname, String password, String confirmPassword)
            throws UserRegistrationException {

        if (log.isInfoEnabled()) {
            log.info("Start registering new user...");
        }

        checkNotNull(nickname, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");
        checkNotNull(confirmPassword, "Confirmed password cannot be null");

        final String userName = nickname.trim();

        if (userRegistrationRepository.findByNickname(userName) != null) {

            if (log.isWarnEnabled()) {
                log.warn("User with given nickname already exists.");
            }

            throw new UserRegistrationException("User with given nickname already exists.");
        }
        if (userName.contains(" ")) {

            if (log.isWarnEnabled()) {
                log.warn("Nickname cannot contain gaps.");
            }

            throw new UserRegistrationException("Nickname cannot contain gaps.");
        }
        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

            if (log.isWarnEnabled()) {
                log.warn("All fields must be filled.");
            }

            throw new UserRegistrationException("All fields must be filled.");
        }
        if (!password.equals(confirmPassword)){

            if (log.isWarnEnabled()) {
                log.warn("Passwords does not match.");
            }

            throw new UserRegistrationException("Passwords does not match.");
        }

        final User user = new User(new UserName(userName), new Password(password));

        try {
            return userRegistrationRepository.register(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully registered.");
            }

        }
    }

    @Override
    public Token login(String nickname, String password)
            throws UserAuthenticationException {

        if (log.isInfoEnabled()) {
            log.info("Start login user...");
        }

        checkNotNull(nickname, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");

        final User user = userRegistrationRepository.findByNickname(nickname);

        if (user == null) {

            if (log.isWarnEnabled()) {
                log.warn("Incorrect login/password.");
            }

            throw new UserAuthenticationException("Incorrect login/password.");
        }
        if (!user.getPassword().getPassword().equals(password)) {

            if (log.isWarnEnabled()) {
                log.warn("Incorrect login/password.");
            }

            throw new UserAuthenticationException("Incorrect login/password.");
        }

        try {
            return userAuthenticationRepository.login(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully logged in.");
            }
        }
    }
}
