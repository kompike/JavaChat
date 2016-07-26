package com.javaclasses.model;

import com.javaclasses.model.entity.User;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.repository.impl.UserAuthenticationRepository;
import com.javaclasses.model.repository.impl.UserRegistrationRepository;
import com.javaclasses.model.service.UserRegistrationException;
import com.javaclasses.model.service.impl.UserServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private UserServiceImpl userService = UserServiceImpl.getInstance();
    private UserRegistrationRepository registrationRepository =
            UserRegistrationRepository.getInstance();
    private UserAuthenticationRepository authenticationRepository =
            UserAuthenticationRepository.getInstance();

    @Test
    public void allowToCreateNewUser() throws UserRegistrationException {

        final String nickname = "User";
        final String password = "password";
        final String confirmPassword = "password";

        final UserId userId = userService.register(nickname, password, confirmPassword);
        final User user = registrationRepository.find(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, user.getNickname());
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws UserRegistrationException {
        final String nickname = "ExistingUser";
        final String password = "password";
        final String confirmPassword = "password";

        final UserId userId = userService.register(nickname, password, confirmPassword);
        final User user = registrationRepository.find(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, user.getNickname());

        try {
            userService.register(nickname, password, confirmPassword);
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given nickname already exists.", ex.getMessage());
        }
    }

    @Test
    public void checkForGapesInNickname() {
        final String nickname = "New user";
        final String password = "password";
        final String confirmPassword = "password";

        try {
            userService.register(nickname, password, confirmPassword);
            fail("UserRegistrationException was not thrown.");
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
            userService.register(nickname, password, confirmPassword);
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for not equal passwords.",
                    "Passwords does not match.", ex.getMessage());
        }
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() {
        final String nickname = "";
        final String password = "password";
        final String confirmPassword = "password";

        try {
            userService.register(nickname, password, confirmPassword);
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for empty fields during registration.",
                    "All fields must be filled.", ex.getMessage());
        }
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException {
        final String nickname = "UserWithWhitespaces";
        final String password = "password";
        final String confirmPassword = "password";

        final UserId userId = userService.register(nickname, password, confirmPassword);
        final User user = registrationRepository.find(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, user.getNickname());

        try {
            final String nicknameWithWhitespaces = "   UserWithWhitespaces   ";
            userService.register(nicknameWithWhitespaces, password, confirmPassword);
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given nickname already exists.", ex.getMessage());
        }
    }

    @Test
    public void allowRegisteredUserToLogin() {
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() {
    }

    @Test
    public void checkPasswordCorrectness() {
    }
}
