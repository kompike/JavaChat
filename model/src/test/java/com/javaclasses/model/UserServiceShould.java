package com.javaclasses.model;

import com.javaclasses.model.service.impl.UserServiceImpl;
import org.junit.Test;

public class UserServiceShould {

    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    @Test
    public void allowToCreateNewUser(){
    }

    @Test
    public void prohibitRegistrationOfAlreadyExistingUser() {
    }

    @Test
    public void checkForGapesInNickname() {
    }

    @Test
    public void checkPasswordEquality() {
    }

    @Test
    public void checkForEmptyFieldsWhileRegisteringNewUser() {
    }

    @Test
    public void trimNicknameWhileRegisteringNewUser() {
    }

    @Test
    public void allowRegisteredUserToLogin() {
    }

    @Test
    public void prohibitLoginOfNotRegisteredUser() {
    }

    @Test
    public void checkPasswordCorrectness() {
    }
}
