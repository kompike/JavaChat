package com.javaclasses.chat.webapp;

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

public class ChatControllerShould {

    private static final String URL = "http://localhost:8080/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";
    private static final String CHAT_CREATION_URL = URL + "create-chat";
    private static final String JOIN_CHAT_URL = URL + "join-chat";
    private static final String LEAVE_CHAT_URL = URL + "leave-chat";

    @Test
    public void allowUserToCreateNewChat() throws IOException {

        final String nickname = "UserWithoutChat";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "newChatName"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain chatList field.",
                responseContent.contains("chatList"));
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws IOException {

        final String nickname = "AnotherUserWithoutChat";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "existingChat"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);
        final HttpResponse response = generateResponse(CHAT_CREATION_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForEmptyChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "JustUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", ""));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_URL, urlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_NAME_CANNOT_BE_EMPTY + "'}", responseContent);
    }

    @Test
    public void trimChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "Me";
        final String password = "MyPass";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "chatWithWhiteSpaces"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);

        final List<NameValuePair> newChatUrlParameters = new ArrayList<>();
        newChatUrlParameters.add(new BasicNameValuePair("chatName", "   chatWithWhiteSpaces   "));
        newChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(CHAT_CREATION_URL, newChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void allowUserToJoinChat() throws IOException {

        final String nickname = "UserToJoin";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "myChat"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);

        final List<NameValuePair> joiningChatUrlParameters = new ArrayList<>();
        joiningChatUrlParameters.add(new BasicNameValuePair("chatName", "myChat"));
        joiningChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(JOIN_CHAT_URL, joiningChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain messages field.",
                responseContent.contains("messages"));
    }

    @Test
    public void prohibitUserToJoinChatHeAlreadyJoined() throws IOException {

        final String nickname = "JoinedUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "myChat123"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);

        final List<NameValuePair> joiningChatUrlParameters = new ArrayList<>();
        joiningChatUrlParameters.add(new BasicNameValuePair("chatName", "myChat123"));
        joiningChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(JOIN_CHAT_URL, joiningChatUrlParameters);
        final HttpResponse response = generateResponse(JOIN_CHAT_URL, joiningChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already joined user joined chat again.",
                "{'errorMessage':'" + USER_ALREADY_JOINED + "'}", responseContent);
    }

    @Test
    public void allowUserToLeaveChat() throws IOException {

        final String nickname = "UserToLeave";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "mySecondChat"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);

        final List<NameValuePair> joiningChatUrlParameters = new ArrayList<>();
        joiningChatUrlParameters.add(new BasicNameValuePair("chatName", "mySecondChat"));
        joiningChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(JOIN_CHAT_URL, joiningChatUrlParameters);

        final List<NameValuePair> leavingChatUrlParameters = new ArrayList<>();
        leavingChatUrlParameters.add(new BasicNameValuePair("chatName", "mySecondChat"));
        leavingChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse response = generateResponse(LEAVE_CHAT_URL, leavingChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain chatId field.",
                   responseContent.contains("chatId"));

    }

    @Test
    public void prohibitUserToLeaveChatHeAlreadyLeft() throws IOException {

        final String nickname = "LeftUser";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chatName", "ChatToLeave"));
        urlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, urlParameters);

        final List<NameValuePair> joiningChatUrlParameters = new ArrayList<>();
        joiningChatUrlParameters.add(new BasicNameValuePair("chatName", "ChatToLeave"));
        joiningChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(JOIN_CHAT_URL, joiningChatUrlParameters);

        final List<NameValuePair> leavingChatUrlParameters = new ArrayList<>();
        leavingChatUrlParameters.add(new BasicNameValuePair("chatName", "ChatToLeave"));
        leavingChatUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(LEAVE_CHAT_URL, leavingChatUrlParameters);
        final HttpResponse response = generateResponse(LEAVE_CHAT_URL, leavingChatUrlParameters);

        final String responseContent = getResponseContent(response);

        assertEquals("Already left user left chat again.",
                "{'errorMessage':'" + USER_ALREADY_LEFT + "'}", responseContent);
    }
}
