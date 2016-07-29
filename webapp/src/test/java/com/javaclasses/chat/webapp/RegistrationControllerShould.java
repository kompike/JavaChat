package com.javaclasses.chat.webapp;

import com.javaclasses.chat.model.service.UserRegistrationException;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static com.javaclasses.chat.webapp.TestUtils.getResponseContent;
import static com.javaclasses.chat.webapp.TestUtils.registerUser;
import static org.junit.Assert.assertEquals;

public class RegistrationControllerShould {

    @Test
    public void allowNewUserToRegister() throws IOException {

        final String nickname = "User";
        final String password = "password";

        final HttpResponse response = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(response);

        assertEquals("Response content does not equal expected JSON.",
                "{'message':'User successfully registered'}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws IOException {

        final String nickname = "ExistingUser";
        final String password = "ExistingPassword";

        registerUser(nickname, password, password);
        final HttpResponse response = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForGapsInNickname() throws IOException {

        final String nickname = "New User";
        final String password = "passWithoutGaps";

        final HttpResponse response = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(response);

        assertEquals("User with gaps in nickname was registered.",
                "{'errorMessage':'" + NICKNAME_CANNOT_CONTAIN_GAPS + "'}", responseContent);
    }

    @Test
    public void checkPasswordEquality() throws IOException {

        final String nickname = "UserWithDifferentPasswords";
        final String password = "firstPassword";
        final String confirmPassword = "secondPassword";

        final HttpResponse response = registerUser(nickname, password, confirmPassword);

        final String responseContent = getResponseContent(response);

        assertEquals("User with different passwords was registered.",
                "{'errorMessage':'" + PASSWORDS_DOES_NOT_MATCH + "'}", responseContent);
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() throws IOException {

        final String nickname = "";
        final String password = "pass";

        final HttpResponse response = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(response);

        assertEquals("User with empty nickname was registered.",
                "{'errorMessage':'" + ALL_FIELDS_MUST_BE_FILLED + "'}", responseContent);
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException, IOException {

        final String nickname = "UserWithWhitespaces";
        final String password = "password";

        registerUser(nickname, password, password);

        final HttpResponse response = registerUser("  UserWithWhitespaces  ", password, password);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }
}