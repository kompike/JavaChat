package com.javaclasses.chat.model.repository.impl;

import com.javaclasses.chat.model.entity.Token;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.repository.InMemoryRepository;

import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link InMemoryRepository} implementation for authenticated users
 */
public class TokenRepository extends InMemoryRepository<TokenId, Token> {

    private static TokenRepository tokenRepository;

    private AtomicLong idCounter = new AtomicLong(1);

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
        return new TokenId(idCounter.getAndIncrement());
    }
}
