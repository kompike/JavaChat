package com.javaclasses.model.entity.tynitype;

/**
 * Tiny type for security token
 */
public class Token {

    private final long token;

    public Token(long token) {
        this.token = token;
    }

    public long getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        return token == token1.token;

    }

    @Override
    public int hashCode() {
        return (int) (token);
    }

    @Override
    public String toString() {
        return String.valueOf(token);
    }
}
