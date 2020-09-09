package com.cheminv.app.web.rest;

import com.cheminv.app.service.ItemManufacturerService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.ItemManufacturerDTO;

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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cheminv.app.domain.ItemManufacturer}.
 */
@RestController
@RequestMapping("/api")
public class ItemManufacturerResource {

    private final Logger log = LoggerFactory.getLogger(ItemManufacturerResource.class);

    private static final String ENTITY_NAME = "itemManufacturer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemManufacturerService itemManufacturerService;

    public ItemManufacturerResource(ItemManufacturerService itemManufacturerService) {
        this.itemManufacturerService = itemManufacturerService;
    }

    /**
     * {@code POST  /item-manufacturers} : Create a new itemManufacturer.
     *
     * @param itemManufacturerDTO the itemManufacturerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemManufacturerDTO, or with status {@code 400 (Bad Request)} if the itemManufacturer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-manufacturers")
    public ResponseEntity<ItemManufacturerDTO> createItemManufacturer(@Valid @RequestBody ItemManufacturerDTO itemManufacturerDTO) throws URISyntaxException {
        log.debug("REST request to save ItemManufacturer : {}", itemManufacturerDTO);
        if (itemManufacturerDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemManufacturer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemManufacturerDTO result = itemManufacturerService.save(itemManufacturerDTO);
        return ResponseEntity.created(new URI("/api/item-manufacturers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-manufacturers} : Updates an existing itemManufacturer.
     *
     * @param itemManufacturerDTO the itemManufacturerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemManufacturerDTO,
     * or with status {@code 400 (Bad Request)} if the itemManufacturerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemManufacturerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-manufacturers")
    public ResponseEntity<ItemManufacturerDTO> updateItemManufacturer(@Valid @RequestBody ItemManufacturerDTO itemManufacturerDTO) throws URISyntaxException {
        log.debug("REST request to update ItemManufacturer : {}", itemManufacturerDTO);
        if (itemManufacturerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemManufacturerDTO result = itemManufacturerService.save(itemManufacturerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemManufacturerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-manufacturers} : get all the itemManufacturers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemManufacturers in body.
     */
    @GetMapping("/item-manufacturers")
    public List<ItemManufacturerDTO> getAllItemManufacturers() {
        log.debug("REST request to get all ItemManufacturers");
        return itemManufacturerService.findAll();
    }

    /**
     * {@code GET  /item-manufacturers/:id} : get the "id" itemManufacturer.
     *
     * @param id the id of the itemManufacturerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemManufacturerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-manufacturers/{id}")
    public ResponseEntity<ItemManufacturerDTO> getItemManufacturer(@PathVariable Long id) {
        log.debug("REST request to get ItemManufacturer : {}", id);
        Optional<ItemManufacturerDTO> itemManufacturerDTO = itemManufacturerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemManufacturerDTO);
    }

    /**
     * {@code DELETE  /item-manufacturers/:id} : delete the "id" itemManufacturer.
     *
     * @param id the id of the itemManufacturerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-manufacturers/{id}")
    public ResponseEntity<Void> deleteItemManufacturer(@PathVariable Long id) {
        log.debug("REST request to delete ItemManufacturer : {}", id);
        itemManufacturerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/item-manufacturers?query=:query} : search for the itemManufacturer corresponding
     * to the query.
     *
     * @param query the query of the itemManufacturer search.
     * @return the result of the search.
     */
    @GetMapping("/_search/item-manufacturers")
    public List<ItemManufacturerDTO> searchItemManufacturers(@RequestParam String query) {
        log.debug("REST request to search ItemManufacturers for query {}", query);
        return itemManufacturerService.search(query);
    }
}
