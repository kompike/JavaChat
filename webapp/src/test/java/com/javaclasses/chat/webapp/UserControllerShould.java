package com.javaclasses.chat.webapp;

import com.javaclasses.chat.model.service.UserRegistrationException;
import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserControllerShould {

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String nickname = "NewUser";
        final String password = "NewPassword";

        registerUser(nickname, password, password);

        final HttpEntity httpEntity = loginUser(nickname, password);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Result must contain tokenId field.",
                responseContent.contains("tokenId"));
        assertTrue("Result must contain userName field with '" + nickname + "' value.",
                responseContent.contains(nickname));
        assertTrue("Result must contain chatList field.",
                responseContent.contains("chatList"));
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() throws IOException {

        final String nickname = "Frank";
        final String password = "FrankPass";

        final HttpEntity httpEntity = loginUser(nickname, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Not registered user logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void checkPasswordCorrectnessDuringLogin() throws IOException {

        final String nickname = "Misha";
        final String password = "MishaPass";

        registerUser(nickname, password, password);

        final HttpEntity httpEntity = loginUser(nickname, "password");

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with incorrect password logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void allowNewUserToRegister() throws IOException {

        final String nickname = "User";
        final String password = "password";

        final HttpEntity httpEntity = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Response content does not equal expected JSON.",
                "{'message':'User successfully registered'}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws IOException {

        final String nickname = "ExistingUser";
        final String password = "ExistingPassword";

        registerUser(nickname, password, password);
        final HttpEntity httpEntity = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForGapsInNickname() throws IOException {

        final String nickname = "New User";
        final String password = "passWithoutGaps";

        final HttpEntity httpEntity = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with gaps in nickname was registered.",
                "{'errorMessage':'" + NICKNAME_CANNOT_CONTAIN_GAPS + "'}", responseContent);
    }

    @Test
    public void checkPasswordEquality() throws IOException {

        final String nickname = "UserWithDifferentPasswords";
        final String password = "firstPassword";
        final String confirmPassword = "secondPassword";

        final HttpEntity httpEntity = registerUser(nickname, password, confirmPassword);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with different passwords was registered.",
                "{'errorMessage':'" + PASSWORDS_DOES_NOT_MATCH + "'}", responseContent);
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() throws IOException {

        final String nickname = "";
        final String password = "pass";

        final HttpEntity httpEntity = registerUser(nickname, password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("User with empty nickname was registered.",
                "{'errorMessage':'" + ALL_FIELDS_MUST_BE_FILLED + "'}", responseContent);
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException, IOException {

        final String nickname = "UserWithWhitespaces";
        final String password = "password";

        registerUser(nickname, password, password);

        final HttpEntity httpEntity = registerUser("  UserWithWhitespaces  ", password, password);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }
}