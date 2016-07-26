package com.javaclasses.model.entity;

import com.javaclasses.model.entity.tinytype.Password;
import com.javaclasses.model.entity.tinytype.UserId;
import com.javaclasses.model.entity.tinytype.UserName;

/**
 * User entity implementation
 */
public class User implements Entity<UserId> {

    private UserId userId;
    private UserName userName;
    private Password password;

    public User(UserName userName, Password password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public UserId getId() {
        return userId;
    }

    @Override
    public void setId(UserId userId) {
        this.userId = userId;
    }

    public UserName getUserName() {
        return userName;
    }

    public Password getPassword() {
        return password;
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
