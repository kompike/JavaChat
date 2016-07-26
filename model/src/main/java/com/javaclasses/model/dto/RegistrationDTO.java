package com.javaclasses.model.dto;

/**
 * Data transfer object of user registration information
 */
public class RegistrationDTO {

    private String userName;
    private String password;
    private String confirmPassword;

    public RegistrationDTO(String userName, String password, String confirmPassword) {
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
