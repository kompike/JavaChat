package com.javaclasses.model.entity;

import com.javaclasses.model.entity.tynitype.UserId;

/**
 * User entity implementation
 */
public class User implements Entity<UserId> {

    private UserId userId;
    private String nickname;
    private String password;

    @Override
    public UserId getId() {
        return userId;
    }

    @Override
    public void setId(UserId userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!nickname.equals(user.nickname)) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
