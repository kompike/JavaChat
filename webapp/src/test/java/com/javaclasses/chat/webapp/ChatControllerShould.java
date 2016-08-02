package com.javaclasses.chat.webapp;

import org.apache.http.HttpEntity;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatControllerShould {

    private static final String PARAMETER_NAME = "tokenId";

    private final String message = "Hello)";
    private final String color = "#000";

    @Test
    public void allowUserToCreateNewChat() throws IOException {

        final String nickname = "UserWithoutChat";
        final String password = "NewPassword";
        final String chatName = "myNewChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        final HttpEntity httpEntity = createChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Result must contain chatList field.",
                responseContent.contains("chatList"));
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws IOException {

        final String nickname = "AnotherUserWithoutChat";
        final String password = "NewPassword";
        final String chatName = "existingChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);
        final HttpEntity httpEntity = createChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForEmptyChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "JustUser";
        final String password = "NewPassword";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        final HttpEntity httpEntity = createChat(tokenId, "");

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_NAME_CANNOT_BE_EMPTY + "'}", responseContent);
    }

    @Test
    public void trimChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "Me";
        final String password = "MyPass";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, "chatWithWhiteSpaces");

        final HttpEntity httpEntity = createChat(tokenId, "  chatWithWhiteSpaces   ");

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void allowUserToJoinChat() throws IOException {

        final String nickname = "UserToJoin";
        final String password = "NewPassword";
        final String chatName = "myChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);

        final HttpEntity httpEntity = joinChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Result must contain messages field.",
                responseContent.contains("messages"));
    }

    @Test
    public void prohibitUserToJoinChatHeAlreadyJoined() throws IOException {

        final String nickname = "JoinedUser";
        final String password = "NewPassword";
        final String chatName = "chat123321";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);
        final HttpEntity httpEntity = joinChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already joined user joined chat again.",
                "{'errorMessage':'" + USER_ALREADY_JOINED + "'}", responseContent);
    }

    @Test
    public void allowUserToLeaveChat() throws IOException {

        final String nickname = "UserToLeave";
        final String password = "NewPassword";
        final String chatName = "mySecondChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);

        final HttpEntity httpEntity = leaveChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertTrue("Result must contain chatId field.",
                   responseContent.contains("chatId"));

    }

    @Test
    public void prohibitUserToLeaveChatHeAlreadyLeft() throws IOException {

        final String nickname = "LeftUser";
        final String password = "NewPassword";
        final String chatName = "chatToLeave";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);
        leaveChat(tokenId, chatName);

        final HttpEntity httpEntity = leaveChat(tokenId, chatName);

        final String responseContent = getResponseContent(httpEntity);

        assertEquals("Already left user left chat again.",
                "{'errorMessage':'" + USER_ALREADY_LEFT + "'}", responseContent);
    }

    @Test
    public void allowUserToPostMessagesInChat() throws IOException {

        final String nickname = "MessagePoster";
        final String password = "NewPassword";
        final String chatName = "AnotherNewChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);

        joinChat(tokenId, chatName);

        final HttpEntity httpEntity = createMessage(tokenId, chatName, message, color);

        final String addMessageResponseContent = getResponseContent(httpEntity);

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

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);

        final HttpEntity httpEntity = createMessage(tokenId, chatName, message, color);

        final String addMessageResponseContent = getResponseContent(httpEntity);

        assertTrue("Not joined user posted message.",
                addMessageResponseContent.contains(USER_IS_NOT_IN_CHAT.toString()));
    }

    @Test
    public void prohibitUserToPostEmptyMessage() throws IOException {

        final String nickname = "UserWithEmptyMessage";
        final String password = "NewPassword";
        final String chatName = "AnotherNewChat";

        registerUser(nickname, password, password);

        final HttpEntity loginHttpEntity = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(loginHttpEntity, PARAMETER_NAME);

        createChat(tokenId, chatName);

        joinChat(tokenId, chatName);

        final HttpEntity httpEntity = createMessage(tokenId, chatName, "", color);

        final String addMessageResponseContent = getResponseContent(httpEntity);

        assertTrue("Not joined user posted message.",
                addMessageResponseContent.contains(NOT_ALLOWED_TO_POST_EMPTY_MESSAGE.toString()));
    }
}