package com.cheminv.app.web.rest;

import com.cheminv.app.service.ItemStockService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.ItemStockDTO;
import com.cheminv.app.service.dto.ItemStockCriteria;
import com.cheminv.app.service.ItemStockQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheminv.app.domain.ItemStock}.
 */
@RestController
@RequestMapping("/api")
public class ItemStockResource {

    private final Logger log = LoggerFactory.getLogger(ItemStockResource.class);

    private static final String ENTITY_NAME = "itemStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemStockService itemStockService;

    private final ItemStockQueryService itemStockQueryService;

    public ItemStockResource(ItemStockService itemStockService, ItemStockQueryService itemStockQueryService) {
        this.itemStockService = itemStockService;
        this.itemStockQueryService = itemStockQueryService;
    }

    /**
     * {@code POST  /item-stocks} : Create a new itemStock.
     *
     * @param itemStockDTO the itemStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemStockDTO, or with status {@code 400 (Bad Request)} if the itemStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-stocks")
    public ResponseEntity<ItemStockDTO> createItemStock(@Valid @RequestBody ItemStockDTO itemStockDTO) throws URISyntaxException {
        log.debug("REST request to save ItemStock : {}", itemStockDTO);
        if (itemStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemStockDTO result = itemStockService.save(itemStockDTO);
        return ResponseEntity.created(new URI("/api/item-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-stocks} : Updates an existing itemStock.
     *
     * @param itemStockDTO the itemStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemStockDTO,
     * or with status {@code 400 (Bad Request)} if the itemStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-stocks")
    public ResponseEntity<ItemStockDTO> updateItemStock(@Valid @RequestBody ItemStockDTO itemStockDTO) throws URISyntaxException {
        log.debug("REST request to update ItemStock : {}", itemStockDTO);
        if (itemStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemStockDTO result = itemStockService.save(itemStockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemStockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-stocks} : get all the itemStocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemStocks in body.
     */
    @GetMapping("/item-stocks")
    public ResponseEntity<List<ItemStockDTO>> getAllItemStocks(ItemStockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ItemStocks by criteria: {}", criteria);
        Page<ItemStockDTO> page = itemStockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-stocks/count} : count all the itemStocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/item-stocks/count")
    public ResponseEntity<Long> countItemStocks(ItemStockCriteria criteria) {
        log.debug("REST request to count ItemStocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemStockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /item-stocks/:id} : get the "id" itemStock.
     *
     * @param id the id of the itemStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-stocks/{id}")
    public ResponseEntity<ItemStockDTO> getItemStock(@PathVariable Long id) {
        log.debug("REST request to get ItemStock : {}", id);
        Optional<ItemStockDTO> itemStockDTO = itemStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemStockDTO);
    }

    /**
     * {@code DELETE  /item-stocks/:id} : delete the "id" itemStock.
     *
     * @param id the id of the itemStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-stocks/{id}")
    public ResponseEntity<Void> deleteItemStock(@PathVariable Long id) {
        log.debug("REST request to delete ItemStock : {}", id);
        itemStockService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
