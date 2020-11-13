package com.cheminv.app.web.rest;

import com.cheminv.app.domain.InvUser;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.security.SecurityUtils;
import com.cheminv.app.service.InvUserService;
import com.cheminv.app.service.StorageService;
import com.cheminv.app.service.dto.InvUserDTO;
import com.cheminv.app.service.dto.PasswordChangeDTO;
import com.cheminv.app.web.rest.errors.EmailAlreadyUsedException;
import com.cheminv.app.web.rest.errors.InvalidPasswordException;
import com.cheminv.app.web.rest.errors.LoginAlreadyUsedException;
import com.cheminv.app.web.rest.vm.ManagedUserVM;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final InvUserService userService;

    private final StorageService storageService;

    private final InvUserRepository userRepository;

    public AccountResource(InvUserService userService, StorageService storageService, InvUserRepository userRepository) {
        this.userService = userService;
        this.storageService = storageService;
        this.userRepository = userRepository;
    }


    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public InvUserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(InvUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }


    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userService.registerUser(managedUserVM, managedUserVM.getPassword());
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody InvUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<InvUser> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getEmail().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<InvUser> user = userRepository.findOneByEmailIgnoreCase(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getPostTitle(),userDTO.getAvatarUrl());
    }

    @PostMapping(path = "/account/change-avatar")
    public ResponseEntity<String> changeAvatar(@RequestParam MultipartFile file) throws Exception {
        String fileName = storageService.saveUserAvatar(file);
        return ResponseEntity.status(HttpStatus.OK)
            .body(fileName);
    }

    @GetMapping(path = "/account/avatar/{fileName}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable String fileName) throws Exception {
        Resource resource =  storageService.loadUserAvatar(fileName);
        InputStream in = resource.getInputStream();
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+resource.getFilename())
            .header(HttpHeaders.CONTENT_TYPE,new MimetypesFileTypeMap().getContentType(resource.getFilename()))
            .body(IOUtils.toByteArray(in));
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }


    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
