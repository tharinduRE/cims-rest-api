package com.cheminv.app.web.rest;

import com.cheminv.app.service.InvUserService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.InvUserDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final InvUserService invUserService;

    public InvUserResource(InvUserService invUserService) {
        this.invUserService = invUserService;
    }

    /**
     * {@code POST  /inv-users} : Create a new invUser.
     *
     * @param invUserDTO the invUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invUserDTO, or with status {@code 400 (Bad Request)} if the invUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inv-users")
    public ResponseEntity<InvUserDTO> createInvUser(@RequestBody InvUserDTO invUserDTO) throws URISyntaxException {
        log.debug("REST request to save InvUser : {}", invUserDTO);
        if (invUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new invUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvUserDTO result = invUserService.save(invUserDTO);
        return ResponseEntity.created(new URI("/api/inv-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inv-users} : Updates an existing invUser.
     *
     * @param invUserDTO the invUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invUserDTO,
     * or with status {@code 400 (Bad Request)} if the invUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inv-users")
    public ResponseEntity<InvUserDTO> updateInvUser(@RequestBody InvUserDTO invUserDTO) throws URISyntaxException {
        log.debug("REST request to update InvUser : {}", invUserDTO);
        if (invUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvUserDTO result = invUserService.save(invUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inv-users} : get all the invUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invUsers in body.
     */
    @GetMapping("/inv-users")
    public List<InvUserDTO> getAllInvUsers() {
        log.debug("REST request to get all InvUsers");
        return invUserService.findAll();
    }

    /**
     * {@code GET  /inv-users/:id} : get the "id" invUser.
     *
     * @param id the id of the invUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inv-users/{id}")
    public ResponseEntity<InvUserDTO> getInvUser(@PathVariable Long id) {
        log.debug("REST request to get InvUser : {}", id);
        Optional<InvUserDTO> invUserDTO = invUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invUserDTO);
    }

    /**
     * {@code DELETE  /inv-users/:id} : delete the "id" invUser.
     *
     * @param id the id of the invUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inv-users/{id}")
    public ResponseEntity<Void> deleteInvUser(@PathVariable Long id) {
        log.debug("REST request to delete InvUser : {}", id);
        invUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
