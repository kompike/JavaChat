package com.javaclasses.chat.model;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.dto.MessageDTO;
import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.TextColor;
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
    private UserService userService = UserServiceImpl.getInstance(chatService);

    private final String chatName = "NewChat";
    private final UserId userId = new UserId(1);
    private final String nickname = "User";
    private final String password = "password";

    private final String messageText = "New message!!!";
    private final String color = "#000";

    @Test
    public void allowToCreateNewChat() throws ChatCreationException {

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        final ChatDTO chatDTO = chatService.findById(chatId);

        assertEquals("Actual name of created does not equal expected.",
                chatName, chatDTO.getChatName());

        chatService.delete(chatId);
    }

    @Test
    public void prohibitCreationOfAlreadyExistingChat() throws ChatCreationException {

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
    public void prohibitCreationOfChatWithEmptyName() {

        final String chatName = "";

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
            throws UserRegistrationException, ChatCreationException, ChatMembershipException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatService.getChatUsers(chatId).size());

        chatService.delete(chatId);
        userService.delete(userId);
    }

    @Test
    public void prohibitUserToJoinChatHeAlreadyJoined()
            throws ChatMembershipException, ChatCreationException, UserRegistrationException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatService.getChatUsers(chatId).size());

        try {
            chatService.joinChat(userId, chatId);
            fail("User joined the chat he already joined.");
        } catch (ChatMembershipException ex) {
            assertEquals("Wrong message for joining chat which user already joined.",
                    USER_ALREADY_JOINED.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }
    }

    @Test
    public void allowUserToLeaveChat()
            throws UserRegistrationException, ChatCreationException, ChatMembershipException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatService.getChatUsers(chatId).size());

        chatService.leaveChat(userId, chatId);

        assertEquals("User did not leave the chat.",
                0, chatService.getChatUsers(chatId).size());

        chatService.delete(chatId);
        userService.delete(userId);
    }

    @Test
    public void prohibitUserToLeaveChatHeAlreadyLeft()
            throws UserRegistrationException, ChatCreationException, ChatMembershipException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatService.getChatUsers(chatId).size());

        chatService.leaveChat(userId, chatId);

        assertEquals("User did not leave the chat.",
                0, chatService.getChatUsers(chatId).size());

        try {
            chatService.leaveChat(userId, chatId);
            fail("User left the chat he already left.");
        } catch (ChatMembershipException ex) {
            assertEquals("Wrong message for leaving chat which user already left.",
                    USER_ALREADY_LEFT.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }
    }

    @Test
    public void allowUserToPostNewMessage()
            throws UserRegistrationException, ChatCreationException,
            ChatMembershipException, MessageCreationException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));

        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        assertEquals("User did not join the chat.",
                1, chatService.getChatUsers(chatId).size());

        final MessageDTO messageDTO = new MessageDTO(messageText,
                userId, chatId, new TextColor(color));

        chatService.addMessage(messageDTO);

        assertEquals("Message was not added.",
                1, chatService.getChatMessages(chatId).size());

        chatService.delete(chatId);
        userService.delete(userId);
    }

    @Test
    public void prohibitUserToAddMessageWithoutJoiningChat()
            throws ChatMembershipException, ChatCreationException, UserRegistrationException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));

        try {
            final MessageDTO messageDTO = new MessageDTO(messageText,
                    userId, chatId, new TextColor(color));
            chatService.addMessage(messageDTO);
            fail("Message was added to chat.");
        } catch (MessageCreationException ex) {
            assertEquals("Wrong message for adding message without joining chat.",
                    USER_IS_NOT_IN_CHAT.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }
    }

    @Test
    public void prohibitUserToPostEmptyMessage()
            throws ChatMembershipException, ChatCreationException, UserRegistrationException {

        final UserId userId = userService.register(new RegistrationDTO(nickname, password, password));
        final ChatId chatId = chatService.createChat(userId, new ChatName(chatName));
        chatService.joinChat(userId, chatId);

        try {
            final MessageDTO messageDTO = new MessageDTO("",
                    userId, chatId, new TextColor("#000"));
            chatService.addMessage(messageDTO);
            fail("Message was added to chat.");
        } catch (MessageCreationException ex) {
            assertEquals("Wrong message for adding message without joining chat.",
                    NOT_ALLOWED_TO_POST_EMPTY_MESSAGE.toString(), ex.getMessage());

            chatService.delete(chatId);
            userService.delete(userId);
        }
    }
}