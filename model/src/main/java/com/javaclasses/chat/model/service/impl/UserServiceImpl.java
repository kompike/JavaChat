package com.javaclasses.chat.model.service.impl;

import com.javaclasses.chat.model.dto.LoginDTO;
import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.dto.TokenDTO;
import com.javaclasses.chat.model.dto.UserDTO;
import com.javaclasses.chat.model.entity.Token;
import com.javaclasses.chat.model.entity.User;
import com.javaclasses.chat.model.entity.tinytype.Password;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.entity.tinytype.UserName;
import com.javaclasses.chat.model.repository.impl.TokenRepository;
import com.javaclasses.chat.model.repository.impl.UserRepository;
import com.javaclasses.chat.model.service.UserAuthenticationException;
import com.javaclasses.chat.model.service.UserRegistrationException;
import com.javaclasses.chat.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.javaclasses.chat.model.service.ErrorMessage.*;

/**
 * Implementation of {@link UserService} interface
 */
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl userService;

    private final UserRepository userRepository =
            UserRepository.getInstance();
    private final TokenRepository tokenRepository =
            TokenRepository.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    @Override
    public UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException {

        if (log.isInfoEnabled()) {
            log.info("Start registering new user...");
        }

        final String userName = registrationDTO.getUserName().trim();
        final String password = registrationDTO.getPassword();
        final String confirmPassword = registrationDTO.getConfirmPassword();

        checkNotNull(userName, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");
        checkNotNull(confirmPassword, "Confirmed password cannot be null");

        if (userRepository.findByNickname(userName) != null) {

            if (log.isWarnEnabled()) {
                log.warn(USER_ALREADY_EXISTS.toString());
            }

            throw new UserRegistrationException(USER_ALREADY_EXISTS.toString());
        }
        if (userName.contains(" ")) {

            if (log.isWarnEnabled()) {
                log.warn(NICKNAME_CANNOT_CONTAIN_GAPS.toString());
            }

            throw new UserRegistrationException(NICKNAME_CANNOT_CONTAIN_GAPS.toString());
        }
        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

            if (log.isWarnEnabled()) {
                log.warn(ALL_FIELDS_MUST_BE_FILLED.toString());
            }

            throw new UserRegistrationException(ALL_FIELDS_MUST_BE_FILLED.toString());
        }
        if (!password.equals(confirmPassword)){

            if (log.isWarnEnabled()) {
                log.warn(PASSWORDS_DOES_NOT_MATCH.toString());
            }

            throw new UserRegistrationException(PASSWORDS_DOES_NOT_MATCH.toString());
        }

        final User user = new User(new UserName(userName), new Password(password));

        try {
            return userRepository.add(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully registered.");
            }

        }
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO)
            throws UserAuthenticationException {

        if (log.isInfoEnabled()) {
            log.info("Start login user...");
        }

        final String userName = loginDTO.getUserName();
        final String password = loginDTO.getPassword();

        checkNotNull(userName, "Nickname cannot be null");
        checkNotNull(password, "Password cannot be null");

        final User user = userRepository.findByNickname(userName);

        if (user == null) {

            if (log.isWarnEnabled()) {
                log.warn(INCORRECT_CREDENTIALS.toString());
            }

            throw new UserAuthenticationException(INCORRECT_CREDENTIALS.toString());
        }
        if (!user.getPassword().getPassword().equals(password)) {

            if (log.isWarnEnabled()) {
                log.warn(INCORRECT_CREDENTIALS.toString());
            }

            throw new UserAuthenticationException(INCORRECT_CREDENTIALS.toString());
        }

        final Token token = new Token(user.getId());
        final TokenId tokenId = tokenRepository.add(token);
        final Token tokenById = tokenRepository.findById(tokenId);

        final TokenDTO tokenDTO = new TokenDTO(tokenById.getId(), tokenById.getUserId());

        try {
            return tokenDTO;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully logged in.");
            }
        }
    }

    @Override
    public UserDTO findByName(UserName userName) {

        final String nickname = userName.getName();

        if (log.isInfoEnabled()) {
            log.info("Start looking for user with username: " + nickname);
        }

        final User user = userRepository.findByNickname(nickname);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public UserDTO findById(UserId userId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for user with id: " + userId.getId());
        }

        final User user = userRepository.findById(userId);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public UserDTO findByToken(TokenId tokenId) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for user by security token...");
        }

        final Token token = tokenRepository.findById(tokenId);

        final UserId userId = token.getUserId();

        final User user = userRepository.findById(userId);

        try {
            return createUserDTOFromUser(user);
        } finally {

            if (log.isInfoEnabled()) {
                log.info("User successfully found.");
            }
        }
    }

    @Override
    public Collection<UserDTO> findAll() {

        if (log.isInfoEnabled()) {
            log.info("Start looking for all registered users...");
        }

        final Collection<User> users = userRepository.findAll();

        final Collection<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {
            userDTOList.add(createUserDTOFromUser(user));
        }

        try {
            return userDTOList;
        } finally {

            if (log.isInfoEnabled()) {
                log.info("Found " + userDTOList.size() + " users.");
            }

        }
    }

    @Override
    public void delete(UserId userId) {

        if (log.isInfoEnabled()) {
            log.info("Start deleting user with id: " + userId.getId());
        }

        userRepository.delete(userId);

        if (log.isInfoEnabled()) {
            log.info("User successfully deleted.");
        }
    }

    private UserDTO createUserDTOFromUser(User user) {

        return new UserDTO(user.getId(), user.getUserName().getName());
    }
}
