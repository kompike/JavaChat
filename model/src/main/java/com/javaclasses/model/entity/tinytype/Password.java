package com.javaclasses.model.entity.tinytype;

/**
 * Tiny type for user password
 */
public class Password {

    private final String password;

    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password1 = (Password) o;

        return password.equals(password1.password);

    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}
