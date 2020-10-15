package com.cheminv.app.web.rest;

import com.cheminv.app.service.InvStorageService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.InvStorageDTO;

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
 * REST controller for managing {@link com.cheminv.app.domain.InvStorage}.
 */
@RestController
@RequestMapping("/api")
public class InvStorageResource {

    private final Logger log = LoggerFactory.getLogger(InvStorageResource.class);

    private static final String ENTITY_NAME = "invStorage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvStorageService invStorageService;

    public InvStorageResource(InvStorageService invStorageService) {
        this.invStorageService = invStorageService;
    }

    /**
     * {@code POST  /inv-storages} : Create a new invStorage.
     *
     * @param invStorageDTO the invStorageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invStorageDTO, or with status {@code 400 (Bad Request)} if the invStorage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inv-storages")
    public ResponseEntity<InvStorageDTO> createInvStorage(@RequestBody InvStorageDTO invStorageDTO) throws URISyntaxException {
        log.debug("REST request to save InvStorage : {}", invStorageDTO);
        if (invStorageDTO.getId() != null) {
            throw new BadRequestAlertException("A new invStorage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvStorageDTO result = invStorageService.save(invStorageDTO);
        return ResponseEntity.created(new URI("/api/inv-storages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inv-storages} : Updates an existing invStorage.
     *
     * @param invStorageDTO the invStorageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invStorageDTO,
     * or with status {@code 400 (Bad Request)} if the invStorageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invStorageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inv-storages")
    public ResponseEntity<InvStorageDTO> updateInvStorage(@RequestBody InvStorageDTO invStorageDTO) throws URISyntaxException {
        log.debug("REST request to update InvStorage : {}", invStorageDTO);
        if (invStorageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvStorageDTO result = invStorageService.save(invStorageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invStorageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inv-storages} : get all the invStorages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invStorages in body.
     */
    @GetMapping("/inv-storages")
    public List<InvStorageDTO> getAllInvStorages() {
        log.debug("REST request to get all InvStorages");
        return invStorageService.findAll();
    }

    /**
     * {@code GET  /inv-storages/:id} : get the "id" invStorage.
     *
     * @param id the id of the invStorageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invStorageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inv-storages/{id}")
    public ResponseEntity<InvStorageDTO> getInvStorage(@PathVariable Long id) {
        log.debug("REST request to get InvStorage : {}", id);
        Optional<InvStorageDTO> invStorageDTO = invStorageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invStorageDTO);
    }

    /**
     * {@code DELETE  /inv-storages/:id} : delete the "id" invStorage.
     *
     * @param id the id of the invStorageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inv-storages/{id}")
    public ResponseEntity<Void> deleteInvStorage(@PathVariable Long id) {
        log.debug("REST request to delete InvStorage : {}", id);
        invStorageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
