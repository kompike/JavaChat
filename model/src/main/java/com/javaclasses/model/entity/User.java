package com.javaclasses.model.entity;

import com.javaclasses.model.entity.tynitype.Password;
import com.javaclasses.model.entity.tynitype.UserId;
import com.javaclasses.model.entity.tynitype.UserName;

/**
 * User entity implementation
 */
public class User {

    private UserId userId;
    private UserName userName;
    private Password password;

    public User(UserName userName, Password password) {
        this.userName = userName;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!userName.equals(user.userName)) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
