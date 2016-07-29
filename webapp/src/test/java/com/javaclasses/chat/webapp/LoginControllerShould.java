package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.INCORRECT_CREDENTIALS;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginControllerShould {

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String nickname = "NewUser";
        final String password = "NewPassword";

        registerUser(nickname, password, password);

        final HttpResponse response = loginUser(nickname, password);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain tokenId field.",
                responseContent.contains("tokenId"));
        assertTrue("Result must contain userName field with '" + nickname + "' value.",
                responseContent.contains(nickname));
        assertTrue("Result must contain message field.",
                responseContent.contains("User successfully logged in"));
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() throws IOException {

        final String nickname = "Frank";
        final String password = "FrankPass";

        final HttpResponse response = loginUser(nickname, password);

        final String responseContent = getResponseContent(response);

        assertEquals("Not registered user logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void checkPasswordCorrectnessDuringLogin() throws IOException {

        final String nickname = "Misha";
        final String password = "MishaPass";

        registerUser(nickname, password, password);

        final HttpResponse response = loginUser(nickname, "password");

        final String responseContent = getResponseContent(response);

        assertEquals("User with incorrect password logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }
}