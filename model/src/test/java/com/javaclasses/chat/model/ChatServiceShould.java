package com.javaclasses.chat.model;

import com.javaclasses.chat.model.dto.ChatDTO;
import com.javaclasses.chat.model.entity.tinytype.ChatId;
import com.javaclasses.chat.model.entity.tinytype.ChatName;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.service.ChatCreationException;
import com.javaclasses.chat.model.service.ChatService;
import com.javaclasses.chat.model.service.impl.ChatServiceImpl;
import org.junit.Test;

import static com.javaclasses.chat.model.service.ErrorMessage.CHAT_ALREADY_EXISTS;
import static com.javaclasses.chat.model.service.ErrorMessage.CHAT_NAME_CANNOT_BE_EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChatServiceShould {

    private final ChatService chatService = ChatServiceImpl.getInstance();

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
}
