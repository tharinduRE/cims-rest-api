package com.cheminv.app.web.rest;

import com.cheminv.app.service.ItemTransactionService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.ItemTransactionDTO;

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

    public ItemTransactionResource(ItemTransactionService itemTransactionService) {
        this.itemTransactionService = itemTransactionService;
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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTransactions in body.
     */
    @GetMapping("/item-transactions")
    public List<ItemTransactionDTO> getAllItemTransactions() {
        log.debug("REST request to get all ItemTransactions");
        return itemTransactionService.findAll();
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

    /**
     * {@code SEARCH  /_search/item-transactions?query=:query} : search for the itemTransaction corresponding
     * to the query.
     *
     * @param query the query of the itemTransaction search.
     * @return the result of the search.
     */
    @GetMapping("/_search/item-transactions")
    public List<ItemTransactionDTO> searchItemTransactions(@RequestParam String query) {
        log.debug("REST request to search ItemTransactions for query {}", query);
        return itemTransactionService.search(query);
    }
}
