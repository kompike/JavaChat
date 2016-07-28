package com.javaclasses.chat.model.dto;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationDTO that = (RegistrationDTO) o;

        if (!userName.equals(that.userName)) return false;
        if (!password.equals(that.password)) return false;
        return confirmPassword.equals(that.confirmPassword);

    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + confirmPassword.hashCode();
        return result;
    }
}
