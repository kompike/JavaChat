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
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given username already exists.", ex.getMessage());
        }
    }

    @Test
    public void checkForGapsInNickname() {
        final String nickname = "New user";
        final String password = "password";

        try {
            userService.register(new RegistrationDTO(nickname, password, password));
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
            userService.register(new RegistrationDTO(nickname, password, confirmPassword));
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

        try {
            userService.register(new RegistrationDTO(nickname, password, password));
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

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final UserDTO userDTO = userService.findById(userId);

        assertEquals("Actual nickname of registered user does not equal expected.",
                nickname, userDTO.getUserName());

        try {
            final String nicknameWithWhitespaces = "   UserWithWhitespaces   ";
            userService.register(
                    new RegistrationDTO(nicknameWithWhitespaces, password, password));
            fail("UserRegistrationException was not thrown.");
        } catch (UserRegistrationException ex) {
            assertEquals("Wrong message for already existing user.",
                    "User with given username already exists.", ex.getMessage());
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
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() {

        final String nickname = "Fedya";
        final String password = "password";

        try {
            userService.login(new LoginDTO(nickname, password));
            fail("UserAuthenticationException was not thrown.");
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
            fail("UserAuthenticationException was not thrown.");
        } catch (UserAuthenticationException ex) {
            assertEquals("Wrong message for incorrect password.",
                    "Incorrect login/password.", ex.getMessage());
        }
    }
}
