package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.NOT_ALLOWED_TO_POST_EMPTY_MESSAGE;
import static com.javaclasses.chat.model.service.ErrorMessage.USER_IS_NOT_IN_CHAT;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageControllerShould {

    @Test
    public void allowUserToPostMessagesInChat() throws IOException {

        final String nickname = "MessagePoster";
        final String password = "NewPassword";
        final String chatName = "AnotherNewChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);

        joinChat(tokenId, chatName);

        final HttpResponse addMessageResponse = createMessage(tokenId, chatName, "Hello)", "#000");

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertTrue("Result must contain messages field.",
                addMessageResponseContent.contains("messages"));
        assertTrue("Result must contain message field.",
                addMessageResponseContent.contains("Hello"));
    }

    @Test
    public void prohibitUserToAddMessageWithoutJoiningChat() throws IOException {

        final String nickname = "NotJoinedMessagePoster";
        final String password = "NewPassword";
        final String chatName = "AnotherNewChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);

        final HttpResponse addMessageResponse = createMessage(tokenId, chatName, "Hello)", "#000");

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertEquals("Not joined user posted message.",
                "{'errorMessage':'" + USER_IS_NOT_IN_CHAT + "'}", addMessageResponseContent);
    }

    @Test
    public void prohibitUserToPostEmptyMessage() throws IOException {

        final String nickname = "UserWithEmptyMessage";
        final String password = "NewPassword";
        final String chatName = "AnotherNewChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);

        joinChat(tokenId, chatName);

        final HttpResponse addMessageResponse = createMessage(tokenId, chatName, "", "#000");

        final String addMessageResponseContent = getResponseContent(addMessageResponse);

        assertEquals("Empty message was posted.",
                "{'errorMessage':'" + NOT_ALLOWED_TO_POST_EMPTY_MESSAGE + "'}", addMessageResponseContent);
    }
}