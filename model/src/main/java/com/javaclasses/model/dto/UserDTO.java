package com.javaclasses.model.dto;

/**
 * Data transfer object for user entity
 */
public class UserDTO {

    private long userId;
    private String userName;

    public UserDTO(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
