package com.javaclasses.chat.model.service;

import com.javaclasses.chat.model.dto.RegistrationDTO;
import com.javaclasses.chat.model.entity.tinytype.TokenId;
import com.javaclasses.chat.model.entity.tinytype.UserId;
import com.javaclasses.chat.model.dto.LoginDTO;
import com.javaclasses.chat.model.dto.TokenDTO;
import com.javaclasses.chat.model.dto.UserDTO;

import java.util.Collection;

/**
 * Basic interface for user management
 */
public interface UserService {

    /**
     * Register new user
     * @param registrationDTO DTO with registration information
     * @return Id of registered user
     * @throws UserRegistrationException In case of error during registration
     */
    UserId register(RegistrationDTO registrationDTO)
            throws UserRegistrationException;

    /**
     * Login user into the system
     * @param loginDTO DTO with login information
     * @return Security token DTO for current user
     * @throws UserAuthenticationException In case of error during login
     */
    TokenDTO login(LoginDTO loginDTO)
            throws UserAuthenticationException;

    /**
     * Search for user by username
     * @param userName Username of user to be found
     * @return DTO with user information
     */
    UserDTO findByName(String userName);

    /**
     * Search for user by id
     * @param userId Id of user to be found
     * @return DTO with user information
     */
    UserDTO findById(UserId userId);

    /**
     * Search for user by security token id
     * @param tokenId Security token id of user to be found
     * @return DTO with user information
     */
    UserDTO findByToken(TokenId tokenId);

    /**
     * Get all registered users
     * @return Collection of DTOs with user information
     */
    Collection<UserDTO> findAll();

    /**
     * Delete user from repository by user id
     * @param userId Id of user to be deleted
     */
    void delete(UserId userId);
}
