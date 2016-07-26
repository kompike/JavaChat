package com.javaclasses.model.service.impl;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.Token;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.impl.UserAuthenticationRepository;
import com.javaclasses.model.repository.impl.UserRegistrationRepository;
import com.javaclasses.model.service.UserAuthenticationException;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

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

        checkNotNull(nickname, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");
        checkNotNull(confirmPassword, "Confirmed password cannot be null");

        if (userRegistrationRepository.findByNickname(nickname) != null) {
            throw new UserRegistrationException("User with given nickname already exists.");
        }
        if (nickname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            throw new UserRegistrationException("All fields must be filled.");
        }
        if (!password.equals(confirmPassword)){
            throw new UserRegistrationException("Passwords does not match.");
        }

        final User user = new User(nickname, password);

        return userRegistrationRepository.add(user);
    }

    @Override
    public Token login(String nickname, String password)
            throws UserAuthenticationException {

        checkNotNull(nickname, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");

        final User user = userRegistrationRepository.findByNickname(nickname);

        if (user == null) {
            throw new UserAuthenticationException("Incorrect login/password");
        }
        if (!user.getPassword().equals(password)) {
            throw new UserAuthenticationException("Incorrect login/password");
        }

        return userAuthenticationRepository.add(user);
    }
}
