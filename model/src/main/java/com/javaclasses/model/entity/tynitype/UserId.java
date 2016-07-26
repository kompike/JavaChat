package com.javaclasses.model.entity.tynitype;

/**
 * Tiny type for user's unique identifier
 */
public class UserId {

    private final long userId;

    public UserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId1 = (UserId) o;

        return userId == userId1.userId;

    }

    @Override
    public int hashCode() {
        return (int) (userId);
    }

    @Override
    public String toString() {
        return String.valueOf(userId);
    }
}
