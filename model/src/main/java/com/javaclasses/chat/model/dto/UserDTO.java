package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.UserId;

/**
 * Data transfer object for user entity
 */
public class UserDTO {

    private UserId userId;
    private String userName;

    public UserDTO(UserId userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (!userId.equals(userDTO.userId)) return false;
        return userName.equals(userDTO.userName);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
