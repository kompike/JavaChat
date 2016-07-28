package com.javaclasses.chat.model;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.service.*;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import com.javaclasses.chat.model.service.impl.UserServiceImpl;
import org.junit.Test;

import static com.javaclasses.chat.model.service.ErrorMessage.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChatServiceShould {

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();

    @Test
    public void allowToCreateNewChat() throws ChatCreationException {

        final String chatName = "NewChat";
        final UserId userId = new UserId(1);

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.findById(chatId);

        assertEquals("Actual name of created does not equal expected.",
                chatName, chatDTO.getChatName());

        chatService.delete(chatId);
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws ChatCreationException {

        final String chatName = "NewChat";
        final UserId userId = new UserId(1);

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.findById(chatId);

        assertEquals("Actual name of created does not equal expected.",
                chatName, chatDTO.getChatName());

        try {
            chatService.createChat(userId, new ChatName(chatName));
            fail("Already existing chat was created.");
        } catch (ChatCreationException ex) {
            assertEquals("Wrong message for already existing chat.",
                    CHAT_ALREADY_EXISTS.toString(), ex.getMessage());

            chatService.delete(chatId);
        }
    }

    @Test
    public void checkForEmptyChatNameWhileCreatingNewChat() {

        final String chatName = "";
        final UserId userId = new UserId(1);

        try {
            chatService.createChat(userId, new ChatName(chatName));
            fail("Chat with empty name was created.");
        } catch (ChatCreationException ex) {
            assertEquals("Wrong message for empty chat name.",
                    CHAT_NAME_CANNOT_BE_EMPTY.toString(), ex.getMessage());
        }
    }

    @Test
    public void trimChatNameWhileCreatingNewChat() throws ChatCreationException {

        final String chatName = "ChatWithWhitespaces";
        final UserId userId = new UserId(1);

        final ChatName createdChatName = new ChatName(chatName);
        final ChatId chatId = chatService.createChat(userId, createdChatName);
        final ChatDTO chatDTO = chatService.findByName(createdChatName);

        assertEquals("Actual name of created does not equal expected.",
                chatName, chatDTO.getChatName());

        try {
            chatService.createChat(userId, new ChatName("   ChatWithWhitespaces   "));
            fail("Chat name was not trimmed.");
        } catch (ChatCreationException ex) {
            assertEquals("Wrong message for already existing chat.",
                    CHAT_ALREADY_EXISTS.toString(), ex.getMessage());

            chatService.delete(chatId);
        }
    }

    @Test
    public void allowUserToJoinChat()
            throws ChatJoiningException, ChatCreationException, UserRegistrationException {

        final String chatName = "NewChat";
        final String nickname = "User";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatDTO.getUserNameList().size());

        try {
            chatService.joinChat(userId, chatId);
            fail("User joined the chat he already joined.");
        } catch (ChatJoiningException ex) {
            assertEquals("Wrong message for joining chat which user already joined.",
                    USER_ALREADY_JOINED.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }

    }

    @Test
    public void prohibitUserToJoinChatHeAlreadyJoined()
            throws UserRegistrationException, ChatCreationException, ChatJoiningException {

        final String chatName = "NewChat";
        final String nickname = "User";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatDTO.getUserNameList().size());

        chatService.delete(chatId);
        userService.delete(userId);

    }

    @Test
    public void allowUserToLeaveChat()
            throws UserRegistrationException, ChatCreationException, ChatJoiningException, ChatLeavingException {

        final String chatName = "NewChat";
        final String nickname = "User";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatDTO.getUserNameList().size());

        final ChatDTO chatWithoutUsers = chatService.leaveChat(userId, chatId);

        assertEquals("User did not leave the chat.",
                0, chatWithoutUsers.getUserNameList().size());

        chatService.delete(chatId);
        userService.delete(userId);

    }

    @Test
    public void prohibitUserToLeaveChatHeAlreadyLeft()
            throws UserRegistrationException, ChatCreationException, ChatJoiningException, ChatLeavingException {

        final String chatName = "NewChat";
        final String nickname = "User";
        final String password = "password";

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatDTO.getUserNameList().size());

        final ChatDTO chatWithoutUsers = chatService.leaveChat(userId, chatId);

        assertEquals("User did not leave the chat.",
                0, chatWithoutUsers.getUserNameList().size());

        try {
            chatService.leaveChat(userId, chatId);
            fail("User left the chat he already left.");
        } catch (ChatLeavingException ex) {
            assertEquals("Wrong message for leaving chat which user already left.",
                    USER_ALREADY_LEFT.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }
    }
}
