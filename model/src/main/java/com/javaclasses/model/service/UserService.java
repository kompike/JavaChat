package com.javaclasses.model.service;

import com.javaclasses.model.dto.LoginDTO;
import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.TokenDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.tinytype.TokenId;
import com.javaclasses.model.entity.tinytype.UserId;

import java.util.Collection;

/**
 * Basic interface for user management
 */
public interface UserService {

    UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException;

    TokenDTO login(LoginDTO loginDTO)
            throws UserAuthenticationException;

    UserDTO findByName(String userName);

    UserDTO findById(UserId userId);

    UserDTO findByToken(TokenId tokenId);

    Collection<UserDTO> findAll();

    void delete(UserId userId);
}
