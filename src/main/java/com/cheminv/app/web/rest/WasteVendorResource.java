package com.cheminv.app.web.rest;

import com.cheminv.app.service.WasteVendorService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.WasteVendorDTO;

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
 * REST controller for managing {@link com.cheminv.app.domain.WasteVendor}.
 */
@RestController
@RequestMapping("/api")
public class WasteVendorResource {

    private final Logger log = LoggerFactory.getLogger(WasteVendorResource.class);

    private static final String ENTITY_NAME = "wasteVendor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WasteVendorService wasteVendorService;

    public WasteVendorResource(WasteVendorService wasteVendorService) {
        this.wasteVendorService = wasteVendorService;
    }

    /**
     * {@code POST  /waste-vendors} : Create a new wasteVendor.
     *
     * @param wasteVendorDTO the wasteVendorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wasteVendorDTO, or with status {@code 400 (Bad Request)} if the wasteVendor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waste-vendors")
    public ResponseEntity<WasteVendorDTO> createWasteVendor(@RequestBody WasteVendorDTO wasteVendorDTO) throws URISyntaxException {
        log.debug("REST request to save WasteVendor : {}", wasteVendorDTO);
        if (wasteVendorDTO.getId() != null) {
            throw new BadRequestAlertException("A new wasteVendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WasteVendorDTO result = wasteVendorService.save(wasteVendorDTO);
        return ResponseEntity.created(new URI("/api/waste-vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waste-vendors} : Updates an existing wasteVendor.
     *
     * @param wasteVendorDTO the wasteVendorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wasteVendorDTO,
     * or with status {@code 400 (Bad Request)} if the wasteVendorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wasteVendorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waste-vendors")
    public ResponseEntity<WasteVendorDTO> updateWasteVendor(@RequestBody WasteVendorDTO wasteVendorDTO) throws URISyntaxException {
        log.debug("REST request to update WasteVendor : {}", wasteVendorDTO);
        if (wasteVendorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WasteVendorDTO result = wasteVendorService.save(wasteVendorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wasteVendorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /waste-vendors} : get all the wasteVendors.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wasteVendors in body.
     */
    @GetMapping("/waste-vendors")
    public List<WasteVendorDTO> getAllWasteVendors(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all WasteVendors");
        return wasteVendorService.findAll();
    }

    /**
     * {@code GET  /waste-vendors/:id} : get the "id" wasteVendor.
     *
     * @param id the id of the wasteVendorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wasteVendorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waste-vendors/{id}")
    public ResponseEntity<WasteVendorDTO> getWasteVendor(@PathVariable Long id) {
        log.debug("REST request to get WasteVendor : {}", id);
        Optional<WasteVendorDTO> wasteVendorDTO = wasteVendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wasteVendorDTO);
    }

    /**
     * {@code DELETE  /waste-vendors/:id} : delete the "id" wasteVendor.
     *
     * @param id the id of the wasteVendorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waste-vendors/{id}")
    public ResponseEntity<Void> deleteWasteVendor(@PathVariable Long id) {
        log.debug("REST request to delete WasteVendor : {}", id);
        wasteVendorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
