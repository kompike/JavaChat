package com.javaclasses.model.dto;

/**
 * Data transfer object of user login information
 */
public class LoginDTO {

    private String userName;
    private String password;

    public LoginDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}