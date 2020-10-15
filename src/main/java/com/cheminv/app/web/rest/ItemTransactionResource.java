package com.cheminv.app.web.rest;

import com.cheminv.app.service.ItemTransactionService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.dto.ItemTransactionCriteria;
import com.cheminv.app.service.ItemTransactionQueryService;

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
 * REST controller for managing {@link com.cheminv.app.domain.ItemTransaction}.
 */
@RestController
@RequestMapping("/api")
public class ItemTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ItemTransactionResource.class);

    private static final String ENTITY_NAME = "itemTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTransactionService itemTransactionService;

    private final ItemTransactionQueryService itemTransactionQueryService;

    public ItemTransactionResource(ItemTransactionService itemTransactionService, ItemTransactionQueryService itemTransactionQueryService) {
        this.itemTransactionService = itemTransactionService;
        this.itemTransactionQueryService = itemTransactionQueryService;
    }

    /**
     * {@code POST  /item-transactions} : Create a new itemTransaction.
     *
     * @param itemTransactionDTO the itemTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTransactionDTO, or with status {@code 400 (Bad Request)} if the itemTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-transactions")
    public ResponseEntity<ItemTransactionDTO> createItemTransaction(@Valid @RequestBody ItemTransactionDTO itemTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save ItemTransaction : {}", itemTransactionDTO);
        if (itemTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemTransactionDTO result = itemTransactionService.save(itemTransactionDTO);
        return ResponseEntity.created(new URI("/api/item-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-transactions} : Updates an existing itemTransaction.
     *
     * @param itemTransactionDTO the itemTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the itemTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-transactions")
    public ResponseEntity<ItemTransactionDTO> updateItemTransaction(@Valid @RequestBody ItemTransactionDTO itemTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update ItemTransaction : {}", itemTransactionDTO);
        if (itemTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemTransactionDTO result = itemTransactionService.save(itemTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-transactions} : get all the itemTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTransactions in body.
     */
    @GetMapping("/item-transactions")
    public ResponseEntity<List<ItemTransactionDTO>> getAllItemTransactions(ItemTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ItemTransactions by criteria: {}", criteria);
        Page<ItemTransactionDTO> page = itemTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-transactions/count} : count all the itemTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/item-transactions/count")
    public ResponseEntity<Long> countItemTransactions(ItemTransactionCriteria criteria) {
        log.debug("REST request to count ItemTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(itemTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /item-transactions/:id} : get the "id" itemTransaction.
     *
     * @param id the id of the itemTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-transactions/{id}")
    public ResponseEntity<ItemTransactionDTO> getItemTransaction(@PathVariable Long id) {
        log.debug("REST request to get ItemTransaction : {}", id);
        Optional<ItemTransactionDTO> itemTransactionDTO = itemTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTransactionDTO);
    }

    /**
     * {@code DELETE  /item-transactions/:id} : delete the "id" itemTransaction.
     *
     * @param id the id of the itemTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-transactions/{id}")
    public ResponseEntity<Void> deleteItemTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ItemTransaction : {}", id);
        itemTransactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
