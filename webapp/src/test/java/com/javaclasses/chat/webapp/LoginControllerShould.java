package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.javaclasses.chat.model.service.ErrorMessage.INCORRECT_CREDENTIALS;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static com.javaclasses.chat.webapp.TestUtils.generateResponse;
import static com.javaclasses.chat.webapp.TestUtils.getResponseContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginControllerShould {

    private static final String URL = "http://localhost:8080/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String nickname = "NewUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final HttpResponse response = generateResponse(LOGIN_URL, loginUrlParameters);

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

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final HttpResponse response = generateResponse(LOGIN_URL, loginUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Not registered user logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void checkPasswordCorrectnessDuringLogin() throws IOException {

        final String nickname = "Misha";
        final String password = "MishaPass";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, "pass");

        final HttpResponse response = generateResponse(LOGIN_URL, loginUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("User with incorrect password logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }
}
