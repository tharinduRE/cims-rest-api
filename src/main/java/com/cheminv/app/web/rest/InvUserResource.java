package com.cheminv.app.web.rest;

import com.cheminv.app.domain.InvUser;
import com.cheminv.app.repository.InvUserRepository;
import com.cheminv.app.security.AuthoritiesConstants;
import com.cheminv.app.service.InvUserService;
import com.cheminv.app.service.dto.InvUserDTO;

import com.cheminv.app.service.mapper.InvUserMapper;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.web.rest.errors.EmailAlreadyUsedException;
import com.cheminv.app.web.rest.errors.InvalidPasswordException;
import com.cheminv.app.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.InvUser}.
 */
@RestController
@RequestMapping("/api")
public class InvUserResource {

    private final Logger log = LoggerFactory.getLogger(InvUserResource.class);

    private static final String ENTITY_NAME = "invUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvUserService userService;

    private final InvUserMapper userMapper;

    private final InvUserRepository userRepository;

    public InvUserResource(InvUserService invUserService, InvUserMapper userMapper, InvUserRepository userRepository) {
        this.userService = invUserService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<InvUserDTO> createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }else {
            InvUser newUser = userService.createUser(managedUserVM,managedUserVM.getPassword());
            return ResponseEntity.created(new URI("/api/users/" + newUser.getEmail()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getEmail(), newUser.getEmail()))
                .body(userMapper.userToUserDTO(newUser));
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<InvUserDTO> updateUser(@Valid @RequestBody InvUserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<InvUser> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<InvUserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getEmail(), userDTO.getEmail()));
    }



    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<InvUserDTO>> getAllUsers(Pageable pageable) {
        final Page<InvUserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE /users/:email} : delete the "login" User.
     *
     * @param email the email of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{email}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        log.debug("REST request to delete User: {}", email);
        userService.deleteUser(email);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "A user is deleted with identifier " + email, email)).build();
    }


}
