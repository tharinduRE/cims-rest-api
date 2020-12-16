package com.cheminv.app.web.rest;

import com.cheminv.app.domain.Store;
import com.cheminv.app.repository.StoreRepository;
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
 * REST controller for managing {@link Store}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "invStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreRepository storeRepository;

    public StoreResource(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * {@code POST  /stores} : Create a new store.
     *
     * @param store the store to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new store, or with status {@code 400 (Bad Request)} if the store has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stores")
    public ResponseEntity<Store> createInvStore(@RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Store result = storeRepository.save(store);
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stores} : Updates an existing store.
     *
     * @param store the store to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated store,
     * or with status {@code 400 (Bad Request)} if the store is not valid,
     * or with status {@code 500 (Internal Server Error)} if the store couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stores")
    public ResponseEntity<Store> updateInvStore(@RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Store result = storeRepository.save(store);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, store.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stores} : get all the invStores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invStores in body.
     */
    @GetMapping("/stores")
    public List<Store> getAllInvStores() {
        log.debug("REST request to get all InvStores");
        return storeRepository.findAll();
    }

    /**
     * {@code GET  /stores/:id} : get the "id" invStore.
     *
     * @param id the id of the invStore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invStore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stores/{id}")
    public ResponseEntity<Store> getInvStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Optional<Store> invStore = storeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invStore);
    }

    /**
     * {@code DELETE  /stores/:id} : delete the "id" invStore.
     *
     * @param id the id of the invStore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteInvStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
