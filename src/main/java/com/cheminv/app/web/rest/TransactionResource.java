package com.cheminv.app.web.rest;

import com.cheminv.app.domain.Transaction;
import com.cheminv.app.service.TransactionService;
import com.cheminv.app.web.rest.errors.BadRequestAlertException;
import com.cheminv.app.service.dto.ItemTransactionDTO;
import com.cheminv.app.service.dto.ItemTransactionCriteria;
import com.cheminv.app.service.TransactionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "itemTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionQueryService transactionQueryService;

    public TransactionResource(TransactionService transactionService, TransactionQueryService transactionQueryService) {
        this.transactionService = transactionService;
        this.transactionQueryService = transactionQueryService;
    }

    /**
     * {@code POST  /transactions} : Create a new itemTransaction.
     *
     * @param itemTransactionDTO the itemTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTransactionDTO, or with status {@code 400 (Bad Request)} if the itemTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<ItemTransactionDTO> createItemTransaction(@Valid @RequestBody ItemTransactionDTO itemTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", itemTransactionDTO);
        if (itemTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemTransactionDTO result = transactionService.save(itemTransactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions} : Updates an existing itemTransaction.
     *
     * @param itemTransactionDTO the itemTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the itemTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions")
    public ResponseEntity<ItemTransactionDTO> updateItemTransaction(@Valid @RequestBody ItemTransactionDTO itemTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", itemTransactionDTO);
        if (itemTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemTransactionDTO result = transactionService.save(itemTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transactions} : get all the itemTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTransactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<ItemTransactionDTO>> getAllItemTransactions(ItemTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ItemTransactions by criteria: {}", criteria);
        Page<ItemTransactionDTO> page = transactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions/count} : count all the itemTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transactions/count")
    public ResponseEntity<Long> countItemTransactions(ItemTransactionCriteria criteria) {
        log.debug("REST request to count ItemTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" itemTransaction.
     *
     * @param id the id of the itemTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<ItemTransactionDTO> getItemTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<ItemTransactionDTO> itemTransactionDTO = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTransactionDTO);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" itemTransaction.
     *
     * @param id the id of the itemTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteItemTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
