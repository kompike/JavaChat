package com.javaclasses.model.entity.tynitype;

/**
 * Tiny type for username
 */
public class UserName {

    private final String name;

    public UserName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserName userName = (UserName) o;

        return name.equals(userName.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
