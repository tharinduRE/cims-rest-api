package com.cheminv.app.service;

import com.cheminv.app.config.Constants;
import com.cheminv.app.domain.Authority;
import com.cheminv.app.domain.User;
import com.cheminv.app.repository.AuthorityRepository;
import com.cheminv.app.repository.UserRepository;
import com.cheminv.app.security.AuthoritiesConstants;
import com.cheminv.app.security.SecurityUtils;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.exception.EmailAlreadyUsedException;
import com.cheminv.app.service.exception.InvalidPasswordException;
import com.cheminv.app.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
    }

    public User registerUser(InvUserDTO userDTO, String password) {
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(s->
        {
            throw new EmailAlreadyUsedException();
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setPostTitle(userDTO.getPostTitle());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setInvStores(userDTO.getAuthStores());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(InvUserDTO userDTO, String password) {
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(s->
        {
            throw new EmailAlreadyUsedException();
        });
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPostTitle(userDTO.getPostTitle());
        user.setInvStores(userDTO.getAuthStores());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setPassword(passwordEncoder.encode(password));
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<InvUserDTO> updateUser(InvUserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setPostTitle(userDTO.getPostTitle());
                user.setInvStores(userDTO.getAuthStores());
                user.setAvatarUrl(userDTO.getAvatarUrl());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(InvUserDTO::new);
    }


    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     */
    public void updateUser(String firstName, String lastName, String email, String postTitle,String avatarUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByEmailIgnoreCase)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setPostTitle(postTitle);
                if (avatarUrl != null) {
                    user.setAvatarUrl(avatarUrl);
                }
                log.debug("Changed Information for User: {}", user);
            });
    }


    public void deleteUser(String email) {
        userRepository.findOneByEmailIgnoreCase(email).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByEmailIgnoreCase)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }


    @Transactional(readOnly = true)
    public List<InvUserDTO> getAllManagedUsers() {
        List<User> managedUserList = userRepository.findAllByEmailNot(Constants.SYSTEM_ACCOUNT);
        return userMapper.usersToUserDTOs(managedUserList);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    /**
     * Delete the invUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}
