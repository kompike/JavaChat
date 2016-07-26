package com.javaclasses.model.service;

import com.javaclasses.model.dto.LoginDTO;
import com.javaclasses.model.dto.RegistrationDTO;
import com.javaclasses.model.dto.UserDTO;
import com.javaclasses.model.entity.Token;
import com.javaclasses.model.entity.tinytype.UserId;

import java.util.Collection;

/**
 * Basic interface for user management
 */
public interface UserService {

    UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException;

    Token login(LoginDTO loginDTO)
            throws UserAuthenticationException;

    UserDTO findByName(String userName);

    UserDTO findById(UserId userId);

    UserDTO findByToken(Token token);

    Collection<UserDTO> findAll();
}
