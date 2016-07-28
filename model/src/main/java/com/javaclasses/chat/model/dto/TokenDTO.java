package com.javaclasses.chat.model.dto;

import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.entity.tinytype.UserId;

/**
 * Data transfer object for token entity
 */
public class TokenDTO {

    private TokenId tokenId;
    private UserId userId;

    public TokenDTO(TokenId tokenId, UserId userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public TokenId getTokenId() {
        return tokenId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenDTO tokenDTO = (TokenDTO) o;

        if (!tokenId.equals(tokenDTO.tokenId)) return false;
        return userId.equals(tokenDTO.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
