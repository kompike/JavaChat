package com.javaclasses.chat.model.entity;

import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.entity.tinytype.UserId;

/**
 * Security token entity implementation
 */
public class Token implements Entity<TokenId> {

    private TokenId tokenId;
    private UserId userId;

    public Token(UserId userId) {
        this.userId = userId;
    }

    @Override
    public TokenId getId() {
        return tokenId;
    }

    @Override
    public void setId(TokenId id) {
        this.tokenId = id;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return tokenId.equals(token.tokenId) && userId.equals(token.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
