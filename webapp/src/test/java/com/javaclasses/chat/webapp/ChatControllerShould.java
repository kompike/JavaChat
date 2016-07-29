package com.javaclasses.chat.webapp;

import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static com.javaclasses.chat.webapp.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatControllerShould {

    @Test
    public void allowUserToCreateNewChat() throws IOException {

        final String nickname = "UserWithoutChat";
        final String password = "NewPassword";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        final HttpResponse response = createChat(tokenId, "myNewChat");

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain chatList field.",
                responseContent.contains("chatList"));
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws IOException {

        final String nickname = "AnotherUserWithoutChat";
        final String password = "NewPassword";
        final String chatName = "existingChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);
        final HttpResponse response = createChat(tokenId, chatName);

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void checkForEmptyChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "JustUser";
        final String password = "NewPassword";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        final HttpResponse response = createChat(tokenId, "");

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_NAME_CANNOT_BE_EMPTY + "'}", responseContent);
    }

    @Test
    public void trimChatNameWhileCreatingNewChat() throws IOException {

        final String nickname = "Me";
        final String password = "MyPass";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, "chatWithWhiteSpaces");

        final HttpResponse response = createChat(tokenId, "  chatWithWhiteSpaces   ");

        final String responseContent = getResponseContent(response);

        assertEquals("Already existing chat was created.",
                "{'errorMessage':'" + CHAT_ALREADY_EXISTS + "'}", responseContent);
    }

    @Test
    public void allowUserToJoinChat() throws IOException {

        final String nickname = "UserToJoin";
        final String password = "NewPassword";
        final String chatName = "myChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);

        final HttpResponse joinChatResponse = joinChat(tokenId, chatName);

        final String responseContent = getResponseContent(joinChatResponse);

        assertTrue("Result must contain messages field.",
                responseContent.contains("messages"));
    }

    @Test
    public void prohibitUserToJoinChatHeAlreadyJoined() throws IOException {

        final String nickname = "JoinedUser";
        final String password = "NewPassword";
        final String chatName = "chat123321";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);
        final HttpResponse response = joinChat(tokenId, chatName);

        final String responseContent = getResponseContent(response);

        assertEquals("Already joined user joined chat again.",
                "{'errorMessage':'" + USER_ALREADY_JOINED + "'}", responseContent);
    }

    @Test
    public void allowUserToLeaveChat() throws IOException {

        final String nickname = "UserToLeave";
        final String password = "NewPassword";
        final String chatName = "mySecondChat";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);

        final HttpResponse response = leaveChat(tokenId, chatName);

        final String responseContent = getResponseContent(response);

        assertTrue("Result must contain chatId field.",
                   responseContent.contains("chatId"));

    }

    @Test
    public void prohibitUserToLeaveChatHeAlreadyLeft() throws IOException {

        final String nickname = "LeftUser";
        final String password = "NewPassword";
        final String chatName = "chatToLeave";

        registerUser(nickname, password, password);

        final HttpResponse httpResponse = loginUser(nickname, password);

        final String tokenId = getParameterFromResponse(httpResponse, "tokenId");

        createChat(tokenId, chatName);
        joinChat(tokenId, chatName);
        leaveChat(tokenId, chatName);

        final HttpResponse response = leaveChat(tokenId, chatName);

        final String responseContent = getResponseContent(response);

        assertEquals("Already left user left chat again.",
                "{'errorMessage':'" + USER_ALREADY_LEFT + "'}", responseContent);
    }
}