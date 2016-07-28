package com.javaclasses.chat.webapp;

import com.javaclasses.chat.model.service.UserRegistrationException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DispatcherServletShould {

    private static final String URL = "http://localhost:8080/";
    private static final String REGISTRATION_PAGE_URL = URL + "register";
    private static final String LOGIN_PAGE_URL = URL + "login";
    private static final String CHAT_CREATION_PAGE_URL = URL + "create-chat";

    @Test
    public void allowNewUserToRegister() throws IOException {

        final String nickname = "User";
        final String password = "password";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, password);

        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Response content does not equal expected JSON.",
                "{'message':'User successfully registered'}", responseContent);
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() throws IOException {

        final String nickname = "ExistingUser";
        final String password = "ExistingPassword";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, urlParameters);
        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForGapsInNickname() throws IOException {

        final String nickname = "New User";
        final String password = "passWithoutGaps";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, password);

        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("User with gaps in nickname was registered.",
                "{'errorMessage':'" + NICKNAME_CANNOT_CONTAIN_GAPS + "'}", responseContent);
    }

    @Test
    public void checkPasswordEquality() throws IOException {

        final String nickname = "UserWithDifferentPasswords";
        final String password = "firstPassword";
        final String confirmPassword = "secondPassword";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, confirmPassword);

        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("User with different passwords was registered.",
                "{'errorMessage':'" + PASSWORDS_DOES_NOT_MATCH + "'}", responseContent);
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() throws IOException {

        final String nickname = "";
        final String password = "pass";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, password);

        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("User with empty nickname was registered.",
                "{'errorMessage':'" + ALL_FIELDS_MUST_BE_FILLED + "'}", responseContent);
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() throws UserRegistrationException, IOException {

        final String nickname = "UserWithWhitespaces";
        final String password = "password";

        final List<NameValuePair> urlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, urlParameters);

        final List<NameValuePair> urlParametersWithNewNickname =
                getRegistrationUrlParameters("   UserWithWhitespaces  ", password, password);

        final HttpResponse response = generateResponse(REGISTRATION_PAGE_URL, urlParametersWithNewNickname);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing user was registered.",
                "{'errorMessage':'" + USER_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void allowExistingUserToLogin() throws IOException {

        final String nickname = "NewUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final HttpResponse response = generateResponse(LOGIN_PAGE_URL, loginUrlParameters);

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

        final HttpResponse response = generateResponse(LOGIN_PAGE_URL, loginUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Not registered user logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void checkPasswordCorrectnessDuringLogin() throws IOException {

        final String nickname = "Misha";
        final String password = "MishaPass";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, "pass");

        final HttpResponse response = generateResponse(LOGIN_PAGE_URL, loginUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("User with incorrect password logged in.",
                "{'errorMessage':'" + INCORRECT_CREDENTIALS + "'}", responseContent);
    }

    @Test
    public void allowUserToCreateNewChat() throws IOException {

        final String nickname = "UserWithoutChat";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_PAGE_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "newChatName"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain chatList field.",
                responseContent.contains("chatList"));
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws IOException {

        final String nickname = "AnotherUserWithoutChat";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_PAGE_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "existingChat"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_PAGE_URL, urlParameters);
        final HttpResponse response = generateResponse(CHAT_CREATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForEmptyChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "JustUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_PAGE_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", ""));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_PAGE_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_NAME_CANNOT_BE_EMPTY + "'}", responseContent);
    }

    @Test
    public void trimChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "Me";
        final String password = "MyPass";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_PAGE_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_PAGE_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "chatWithWhiteSpaces"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_PAGE_URL, urlParameters);

        final List<NameValuePair> newChatUrlParameters = new ArrayList<>();
        newChatUrlParameters.add(new BasicNameValuePair("chatName", "   chatWithWhiteSpaces   "));
        newChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_PAGE_URL, newChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }
}