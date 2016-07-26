package com.javaclasses.model.repository.impl;

import com.javaclasses.model.entity.Token;
import com.javaclasses.model.entity.tinytype.TokenId;
import com.javaclasses.model.repository.InMemoryRepository;

/**
 * {@link InMemoryRepository} implementation for authenticated users
 */
public class TokenRepository extends InMemoryRepository<TokenId, Token> {

    private static TokenRepository tokenRepository;

    private long idCounter = 1;

    private TokenRepository() {
    }

    public static TokenRepository getInstance() {
        if (tokenRepository == null) {
            tokenRepository = new TokenRepository();
        }

        return tokenRepository;
    }

    @Override
    protected TokenId generateId() {
        return new TokenId(idCounter++);
    }
}
