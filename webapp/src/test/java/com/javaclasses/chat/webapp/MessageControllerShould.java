package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.javaclasses.chat.model.service.ErrorMessage.NOT_ALLOWED_TO_POST_EMPTY_MESSAGE;
import static com.javaclasses.chat.model.service.ErrorMessage.USER_IS_NOT_IN_CHAT;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageControllerShould {

    private static final String URL = "http://localhost:8080/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";
    private static final String CHAT_CREATION_URL = URL + "create-chat";
    private static final String JOIN_CHAT_URL = URL + "join-chat";
    private static final String ADD_MESSAGE_URL = URL + "add-message";

    @Test
    public void allowUserToPostMessagesInChat() throws IOException {

        final String nickname = "MessagePoster";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> chatCreationUrlParameters = new ArrayList<>();
        chatCreationUrlParameters.add(new BasicNameValuePair("chatName", "AnotherNewChat"));
        chatCreationUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, chatCreationUrlParameters);

        final List<NameValuePair> chatJoiningUrlParameters = new ArrayList<>();
        chatJoiningUrlParameters.add(new BasicNameValuePair("chatName", "AnotherNewChat"));
        chatJoiningUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse joinChatResponse = generateResponse(JOIN_CHAT_URL, chatJoiningUrlParameters);

        final String responseContent = getResponseContent(joinChatResponse);

        assertTrue("Result must contain messages field.",
                responseContent.contains("messages"));

        final List<NameValuePair> addMessageUrlParameters = new ArrayList<>();
        addMessageUrlParameters.add(new BasicNameValuePair("chatName", "AnotherNewChat"));
        addMessageUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));
        addMessageUrlParameters.add(new BasicNameValuePair("message", "Hello)))"));

        final HttpResponse addMessageResponse = generateResponse(ADD_MESSAGE_URL, addMessageUrlParameters);

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertTrue("Result must contain messages field.",
                addMessageResponseContent.contains("messages"));
        assertTrue("Result must contain messages field.",
                addMessageResponseContent.contains("Hello"));
    }

    @Test
    public void prohibitUserToAddMessageWithoutJoiningChat() throws IOException {

        final String nickname = "NotJoinedMessagePoster";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> chatCreationUrlParameters = new ArrayList<>();
        chatCreationUrlParameters.add(new BasicNameValuePair("chatName", "Chat123"));
        chatCreationUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, chatCreationUrlParameters);

        final List<NameValuePair> addMessageUrlParameters = new ArrayList<>();
        addMessageUrlParameters.add(new BasicNameValuePair("chatName", "Chat123"));
        addMessageUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));
        addMessageUrlParameters.add(new BasicNameValuePair("message", "Hello)))"));

        final HttpResponse addMessageResponse = generateResponse(ADD_MESSAGE_URL, addMessageUrlParameters);

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertEquals("Not joined user posted message.",
                "{'errorMessage':'" + USER_IS_NOT_IN_CHAT + "'}", addMessageResponseContent);
    }

    @Test
    public void prohibitUserToPostEmptyMessage() throws IOException {

        final String nickname = "UserWithEmptyMessage";
        final String password = "NewPassword";

        final List<NameValuePair> registrationUrlParameters = getRegistrationUrlParameters(nickname, password, password);

        generateResponse(REGISTRATION_URL, registrationUrlParameters);

        final List<NameValuePair> loginUrlParameters = getLoginUrlParameters(nickname, password);

        final String loginResponse = getResponseContent(generateResponse(LOGIN_URL, loginUrlParameters));
        final String tokenId = loginResponse.split(",")[0].split(":")[1].replaceAll("'", "");

        final List<NameValuePair> chatCreationUrlParameters = new ArrayList<>();
        chatCreationUrlParameters.add(new BasicNameValuePair("chatName", "Chat321"));
        chatCreationUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        generateResponse(CHAT_CREATION_URL, chatCreationUrlParameters);

        final List<NameValuePair> chatJoiningUrlParameters = new ArrayList<>();
        chatJoiningUrlParameters.add(new BasicNameValuePair("chatName", "Chat321"));
        chatJoiningUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));

        final HttpResponse joinChatResponse = generateResponse(JOIN_CHAT_URL, chatJoiningUrlParameters);

        final String responseContent = getResponseContent(joinChatResponse);

        assertTrue("Result must contain messages field.",
                responseContent.contains("messages"));

        final List<NameValuePair> addMessageUrlParameters = new ArrayList<>();
        addMessageUrlParameters.add(new BasicNameValuePair("chatName", "Chat321"));
        addMessageUrlParameters.add(new BasicNameValuePair("tokenId", tokenId));
        addMessageUrlParameters.add(new BasicNameValuePair("message", ""));

        final HttpResponse addMessageResponse = generateResponse(ADD_MESSAGE_URL, addMessageUrlParameters);

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertEquals("Empty message was posted.",
                "{'errorMessage':'" + NOT_ALLOWED_TO_POST_EMPTY_MESSAGE + "'}", addMessageResponseContent);
    }
}
