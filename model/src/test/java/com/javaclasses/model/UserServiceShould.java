package com.javaclasses.model;

import com.javaclasses.model.dto.LoginDTO;
import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.Token;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.service.UserAuthenticationException;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.UserService;
import com.javaclasses.model.service.impl.UserServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private UserService userService = UserServiceImpl.getInstance();

    @Test
    public void allowToCreateNewUser() throws UserRegistrationException {

        final String nickname = "User";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        userService.delete(userId);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws UserRegistrationException {
        final String nickname = "ExistingUser";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        try {
            userService.register(new RegistrationDTO(nickname, password, password));
            fail("Already existing user was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given username already exists.", ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void checkForGapsInNickname() {
        final String nickname = "New  user";
        final String password = "password";

        try {
            userService.register(new RegistrationDTO(nickname, password, password));
            fail("User with gaps in username was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for gaps in nickname.",
                    "Nickname cannot contain gaps.", ex.getMessage());
        }
    }

    @Test
    public void checkPasswordEquality() {
        final String nickname = "UserWithWrongPass";
        final String password = "password";
        final String confirmPassword = "pass";

        try {
            userService.register(new RegistrationDTO(nickname, password, confirmPassword));
            fail("User with different passwords was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for not equal passwords.",
                    "Passwords does not match.", ex.getMessage());
        }
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() {
        final String nickname = "";
        final String password = "password";

        try {
            userService.register(new RegistrationDTO(nickname, password, password));
            fail("User with empty username was registered.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for empty fields during registration.",
                    "All fields must be filled.", ex.getMessage());
        }
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException {
        final String nickname = "UserWithWhitespaces";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        try {
            final String nicknameWithWhitespaces = "   UserWithWhitespaces   ";
            userService.register(
                    new RegistrationDTO(nicknameWithWhitespaces, password, password));
            fail("Username was not trimmed.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given username already exists.", ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void allowRegisteredUserToLogin() throws UserRegistrationException, UserAuthenticationException {

        final String nickname = "Vasya";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        final Token token = userService.login(new LoginDTO(nickname, password));
        final UserDTO loggedUser = userService.findByToken(token);

        assertEquals("Actual nickname of logged user does not equal expected.",
                nickname, loggedUser.getUserName());

        userService.delete(userId);
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() {

        final String nickname = "Fedya";
        final String password = "password";

        try {
            userService.login(new LoginDTO(nickname, password));
            fail("Not registered user logged in.");
        } catch (UserAuthenticationException ex) {
            assertEquals("Wrong message for not registered user.",
                    "Incorrect login/password.", ex.getMessage());
        }
    }

    @Test
    public void checkPasswordCorrectness() throws UserRegistrationException {

        final String nickname = "Misha";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        try {
            userService.login(new LoginDTO(nickname, "pass"));
            fail("User with incorrect password logged in.");
        } catch (UserAuthenticationException ex) {
            assertEquals("Wrong message for incorrect password.",
                    "Incorrect login/password.", ex.getMessage());

            userService.delete(userId);
        }
    }

    @Test
    public void workCorrectlyInMultipleThreads() throws Exception {

        final int threadPoolSize = 100;

        final CountDownLatch startLatch =
                new CountDownLatch(threadPoolSize);

        final ExecutorService executorService =
                Executors.newFixedThreadPool(threadPoolSize);

        final Set<Long> uniqueUserIds = new HashSet<>();

        final Set<UserDTO> loggedUsers = new HashSet<>();

        final List<Future<UserDTO>> futureList = new ArrayList<>();

        for (int i = 0; i < threadPoolSize; i++) {

            final int currentIndex = i;

            final Future<UserDTO> future = executorService.submit(() -> {
                startLatch.countDown();
                startLatch.await();

                final String nickname = "User_" + currentIndex;
                final String password = "password_" + currentIndex;

                final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
                final UserDTO userDTO = userService.findById(userId);

                uniqueUserIds.add(userDTO.getUserId());

                assertEquals("Actual nickname of registered user does not equal expected.",
                        nickname, userDTO.getUserName());

                final Token token = userService.login(new LoginDTO(nickname, password));
                final UserDTO loggedUserDTO = userService.findByToken(token);

                assertEquals("Actual nickname of logged user does not equal expected.",
                        nickname, loggedUserDTO.getUserName());

                loggedUsers.add(loggedUserDTO);

                return userDTO;
            });

            futureList.add(future);
        }

        for (Future future: futureList) {

            future.get();
        }

        assertEquals("Users number must be " + threadPoolSize, threadPoolSize,
                userService.findAll().size());

        assertEquals("Logged users number must be " + threadPoolSize, threadPoolSize,
                loggedUsers.size());

        assertEquals("Ids are not unique", threadPoolSize,
                uniqueUserIds.size());

        for (UserDTO userDTO : userService.findAll()) {
            userService.delete(new UserId(userDTO.getUserId()));
        }
    }

    @Test
    public void failWhileRegisteringExistingUserInMultipleThreads() throws Exception {

        final int threadPoolSize = 100;

        final CountDownLatch startLatch =
                new CountDownLatch(threadPoolSize);

        final ExecutorService executorService =
                Executors.newFixedThreadPool(threadPoolSize);

        final Set<Long> uniqueUserIds = new HashSet<>();

        final List<Future<UserDTO>> futureList = new ArrayList<>();

        for (int i = 0; i < threadPoolSize; i++) {

            final int currentIndex = i;

            final Future<UserDTO> future = executorService.submit(() -> {
                startLatch.countDown();
                startLatch.await();

                UserDTO userDTO = null;

                if (currentIndex == threadPoolSize / 2) {

                    final String nickname = "User_" + 0;
                    final String password = "password_" + 0;

                    try {
                        userService.register(new RegistrationDTO(nickname, password, password));
                        fail("UserRegistrationException was not thrown.");
                    } catch (UserRegistrationException ex) {
                        assertEquals("Wrong message for already existing user.",
                                "User with given username already exists.", ex.getMessage());
                    }
                } else {

                    final String nickname = "User_" + currentIndex;
                    final String password = "password_" + currentIndex;

                    final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
                    userDTO = userService.findById(userId);

                    uniqueUserIds.add(userDTO.getUserId());

                    assertEquals("Actual nickname of registered user does not equal expected.",
                            nickname, userDTO.getUserName());
                }
                return userDTO;
            });

            futureList.add(future);
        }

        for (Future future: futureList) {

            future.get();
        }

        assertEquals("Users number must be " + (threadPoolSize - 1), threadPoolSize - 1,
                userService.findAll().size());

        assertEquals("Ids are not unique", threadPoolSize - 1,
                uniqueUserIds.size());

        for (UserDTO userDTO : userService.findAll()) {
            userService.delete(new UserId(userDTO.getUserId()));
        }
    }
}
