package com.cheminv.app.web.rest;

import com.cheminv.app.service.WasteItemService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.WasteItemDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.WasteItem}.
 */
@RestController
@RequestMapping("/api")
public class WasteItemResource {

    private final Logger log = LoggerFactory.getLogger(WasteItemResource.class);

    private static final String ENTITY_NAME = "wasteItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WasteItemService wasteItemService;

    public WasteItemResource(WasteItemService wasteItemService) {
        this.wasteItemService = wasteItemService;
    }

    /**
     * {@code POST  /waste-items} : Create a new wasteItem.
     *
     * @param wasteItemDTO the wasteItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wasteItemDTO, or with status {@code 400 (Bad Request)} if the wasteItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waste-items")
    public ResponseEntity<WasteItemDTO> createWasteItem(@Valid @RequestBody WasteItemDTO wasteItemDTO) throws URISyntaxException {
        log.debug("REST request to save WasteItem : {}", wasteItemDTO);
        if (wasteItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new wasteItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WasteItemDTO result = wasteItemService.save(wasteItemDTO);
        return ResponseEntity.created(new URI("/api/waste-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waste-items} : Updates an existing wasteItem.
     *
     * @param wasteItemDTO the wasteItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wasteItemDTO,
     * or with status {@code 400 (Bad Request)} if the wasteItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wasteItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waste-items")
    public ResponseEntity<WasteItemDTO> updateWasteItem(@Valid @RequestBody WasteItemDTO wasteItemDTO) throws URISyntaxException {
        log.debug("REST request to update WasteItem : {}", wasteItemDTO);
        if (wasteItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WasteItemDTO result = wasteItemService.save(wasteItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wasteItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /waste-items} : get all the wasteItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wasteItems in body.
     */
    @GetMapping("/waste-items")
    public List<WasteItemDTO> getAllWasteItems() {
        log.debug("REST request to get all WasteItems");
        return wasteItemService.findAll();
    }

    /**
     * {@code GET  /waste-items/:id} : get the "id" wasteItem.
     *
     * @param id the id of the wasteItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wasteItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waste-items/{id}")
    public ResponseEntity<WasteItemDTO> getWasteItem(@PathVariable Long id) {
        log.debug("REST request to get WasteItem : {}", id);
        Optional<WasteItemDTO> wasteItemDTO = wasteItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wasteItemDTO);
    }

    /**
     * {@code DELETE  /waste-items/:id} : delete the "id" wasteItem.
     *
     * @param id the id of the wasteItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waste-items/{id}")
    public ResponseEntity<Void> deleteWasteItem(@PathVariable Long id) {
        log.debug("REST request to delete WasteItem : {}", id);
        wasteItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
