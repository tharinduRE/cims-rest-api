package com.cheminv.app.web.rest;

import com.cheminv.app.domain.InvStore;
import com.cheminv.app.repository.InvStoreRepository;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.InvStore}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvStoreResource {

    private final Logger log = LoggerFactory.getLogger(InvStoreResource.class);

    private static final String ENTITY_NAME = "invStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvStoreRepository invStoreRepository;

    public InvStoreResource(InvStoreRepository invStoreRepository) {
        this.invStoreRepository = invStoreRepository;
    }

    /**
     * {@code POST  /inv-stores} : Create a new invStore.
     *
     * @param invStore the invStore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invStore, or with status {@code 400 (Bad Request)} if the invStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inv-stores")
    public ResponseEntity<InvStore> createInvStore(@RequestBody InvStore invStore) throws URISyntaxException {
        log.debug("REST request to save InvStore : {}", invStore);
        if (invStore.getId() != null) {
            throw new BadRequestAlertException("A new invStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvStore result = invStoreRepository.save(invStore);
        return ResponseEntity.created(new URI("/api/inv-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inv-stores} : Updates an existing invStore.
     *
     * @param invStore the invStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invStore,
     * or with status {@code 400 (Bad Request)} if the invStore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inv-stores")
    public ResponseEntity<InvStore> updateInvStore(@RequestBody InvStore invStore) throws URISyntaxException {
        log.debug("REST request to update InvStore : {}", invStore);
        if (invStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvStore result = invStoreRepository.save(invStore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invStore.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inv-stores} : get all the invStores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invStores in body.
     */
    @GetMapping("/inv-stores")
    public List<InvStore> getAllInvStores() {
        log.debug("REST request to get all InvStores");
        return invStoreRepository.findAll();
    }

    /**
     * {@code GET  /inv-stores/:id} : get the "id" invStore.
     *
     * @param id the id of the invStore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invStore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inv-stores/{id}")
    public ResponseEntity<InvStore> getInvStore(@PathVariable Long id) {
        log.debug("REST request to get InvStore : {}", id);
        Optional<InvStore> invStore = invStoreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invStore);
    }

    /**
     * {@code DELETE  /inv-stores/:id} : delete the "id" invStore.
     *
     * @param id the id of the invStore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inv-stores/{id}")
    public ResponseEntity<Void> deleteInvStore(@PathVariable Long id) {
        log.debug("REST request to delete InvStore : {}", id);
        invStoreRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
